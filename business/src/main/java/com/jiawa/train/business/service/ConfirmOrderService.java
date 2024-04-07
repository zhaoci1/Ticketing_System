package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.dto.ConfirmOrderMQDto;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.business.enums.ConfirmOrderStatusEnum;
import com.jiawa.train.business.enums.LockKeyPreEnum;
import com.jiawa.train.business.enums.SeatColEnum;
import com.jiawa.train.business.enums.SeatTypeEnum;
import com.jiawa.train.business.mapper.ConfirmOrderMapper;
import com.jiawa.train.business.req.ConfirmOrderDoReq;
import com.jiawa.train.business.req.ConfirmOrderQuery;
import com.jiawa.train.business.req.ConfirmOrderTicketReq;
import com.jiawa.train.business.resp.ConfirmOrderQueryResp;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ConfirmOrderService {

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private AfterConfirmOrderService afterConfirmOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Logger Log = LoggerFactory.getLogger(ConfirmOrderService.class);

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(ConfirmOrderDoReq req) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        int state;

        if (ObjectUtil.isNull(confirmOrder.getId())) {
            //        获取当前登录的会员id
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            state = confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            state = confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }

        return state;
    }

    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQuery req) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<ConfirmOrder> confirmOrders = confirmOrderMapper.selectByExample(confirmOrderExample);
        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrders, ConfirmOrderQueryResp.class);
        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrders);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return confirmOrderMapper.deleteByPrimaryKey(id);
    }


    /**
     * 保存车票
     *
     * @param dto
     */
    public void doConfirm(ConfirmOrderMQDto dto) {
        //        获取车次锁
        String lockKey = LockKeyPreEnum.CONFIRM_ORDER + "-" + DateUtil.formatDate(dto.getDate()) + "-" + dto.getTrainCode();
        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockKey, 10, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(setIfAbsent)) {
            Log.info("恭喜，抢到锁了!");
        } else {
            Log.info("很遗憾，有其他消费线程正在出票，不做任何处理");
            return;
        }

        try {
            while (true) {
                ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
                confirmOrderExample.setOrderByClause("id asc");
                ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
                criteria.andDateEqualTo(dto.getDate())
                        .andTrainCodeEqualTo(dto.getTrainCode())
                        .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
                PageHelper.startPage(1, 5);
                List<ConfirmOrder> list = confirmOrderMapper.selectByExampleWithBLOBs(confirmOrderExample);
                if (CollUtil.isEmpty(list)) {
                    Log.info("没有需要处理的订单，结束循环");
                    break;
                } else {
                    Log.info("本次处理{}条订单", list.size());
                }
//                一条一条的卖
                list.forEach(confirmOrder -> {
                    try {
                        sell(confirmOrder);
                    } catch (BusinessException e) {
                        if (e.getE().equals(BusinessExceptionEnum.BUSINESS_ORDER_TICKET_COUNT_ERROR)) {
                            Log.info("本订单余票不足，继续售卖下一个订单");
                            confirmOrder.setStatus(ConfirmOrderStatusEnum.EMPTY.getCode());
                            updateStatus(confirmOrder);
                        } else {
                            throw e;
                        }
                    }
                });
            }
        } finally {
            Log.error("购票流程结束，释放锁{}", lockKey);
            redisTemplate.delete(lockKey);
        }
    }

    //        保存确认订单表，状态初始
    private void sell(ConfirmOrder confirmOrder) {
        ConfirmOrderDoReq req = new ConfirmOrderDoReq();
        req.setMemberId(confirmOrder.getMemberId());
        req.setDate(confirmOrder.getDate());
        req.setTrainCode(confirmOrder.getTrainCode());
        req.setStart(confirmOrder.getStart());
        req.setEnd(confirmOrder.getEnd());
        req.setDailyTrainTicketId(confirmOrder.getDailyTrainTicketId());
        req.setTickets(JSON.parseArray(confirmOrder.getTickets(), ConfirmOrderTicketReq.class));
        req.setImageCode("");
        req.setImageCodeToken("");
        req.setLogId("");


        confirmOrder.setStatus(ConfirmOrderStatusEnum.PENDING.getCode());
        updateStatus(confirmOrder);

        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        List<ConfirmOrderTicketReq> tickets = req.getTickets();

//        查处余票记录
        DailyTrainTicket dailyTrainTicket =
                dailyTrainTicketService.selectByUnique(trainCode, end, date, start);

//        预扣减余票数量,并判断余票是否足够
        reduceickets(req, dailyTrainTicket);

//        最终的选座结果
        List<DailyTrainSeat> finalSeatList = new ArrayList<>();


//        计算座位的偏移值
        ConfirmOrderTicketReq ticketReq0 = tickets.get(0);
        if (StrUtil.isNotBlank(ticketReq0.getSeat())) {
            Log.info("本次购票有选座");
//            先查出本次选座的座位有哪些列,用于计算所选座位和第一个座位的偏移值
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(ticketReq0.getSeatTypeCode());
            Log.info("本次选座的座位类型,包含的列:{}", colEnumList);
//            组成和前端两排选座一样的列表
            List<String> referSeatList = new ArrayList<>();

            for (int i = 1; i <= 2; i++) {
                for (SeatColEnum seatColEnum : colEnumList) {
                    referSeatList.add(seatColEnum.getCode() + i);
                }
            }
            Log.info("用于做参照的两排座位：{}", referSeatList);
//            计算偏移值
//            先计算全部都绝对偏移值,也就是座位的索引值
//            然后把它们都减去第一个座位的索引值,得出来就是相对的偏移值了
            ArrayList<Integer> offsetList = new ArrayList<>();
            List<Integer> indexList = new ArrayList<>();
//            获取索引
            for (ConfirmOrderTicketReq ticket : tickets) {
                int index = referSeatList.indexOf(ticket.getSeat());
                indexList.add(index);
            }
            Log.info("计算得到所有座位的绝对偏移值:{}", indexList);
//            获取相对偏移值
            for (Integer i : indexList) {
                int offset = i - indexList.get(0);
                offsetList.add(offset);
            }
            Log.info("计算得到所有座位的相对偏移值:{}", offsetList);

            System.out.println(dailyTrainTicket);
            getSaat(finalSeatList,
                    req.getDate(), req.getTrainCode(),
                    ticketReq0.getSeatTypeCode(),
                    ticketReq0.getSeat().split("")[0],
                    offsetList, dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());
        } else {
            Log.info("本次购票没有选座");
//            遍历乘车人
            for (ConfirmOrderTicketReq ticket : tickets) {
                getSaat(finalSeatList,
                        date,
                        trainCode,
                        ticket.getSeatTypeCode(),
                        null,
                        null,
                        dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());
            }
        }
        Log.info("最终选座:{}", finalSeatList);

        try {
            //        选座后的事务处理
            afterConfirmOrderService.afterDoConfirm(dailyTrainTicket, finalSeatList, tickets, confirmOrder);
        } catch (Exception e) {
            Log.error("购买失败");
            throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_EXCEPTION);
        }
    }

    /**
     * 更新状态，直接更新到库
     *
     * @param confirmOrder
     */
    public void updateStatus(ConfirmOrder confirmOrder) {
        ConfirmOrder confirmOrderForUpdate = new ConfirmOrder();
        confirmOrderForUpdate.setId(confirmOrder.getId());
        confirmOrderForUpdate.setUpdateTime(new Date());
        confirmOrderForUpdate.setStatus(confirmOrder.getStatus());
        confirmOrderMapper.updateByPrimaryKeySelective(confirmOrderForUpdate);
    }

    /**
     * 选座, 根据车厢获取座位
     *
     * @param date       时间
     * @param trainCode  车次
     * @param seatType   座位类型
     * @param column     列名
     * @param offsetList 座位区间
     * @param startIndex 起始站
     * @param endIndex   终点站
     */
    private void getSaat(List<DailyTrainSeat> finalSeatList, Date date, String trainCode,
                         String seatType, String column,
                         ArrayList<Integer> offsetList,
                         Integer startIndex, Integer endIndex) {
        List<DailyTrainSeat> getlSeatList = new ArrayList<>();
        List<DailyTrainCarriage> carriageList =
                dailyTrainCarriageService.selectBySeatType(date, trainCode, seatType);

        Log.info("共查出{}个符合条件的车厢", carriageList.size());
//        对车厢进行遍历
        for (DailyTrainCarriage dailyTrainCarriage : carriageList) {
            Log.info("开始从车厢{}选座", dailyTrainCarriage.getIndex());
            getlSeatList = new ArrayList<>();
            List<DailyTrainSeat> seatList = dailyTrainSeatService
                    .selectByCarriage(date, trainCode, dailyTrainCarriage.getIndex());
            Log.info("车厢{}的座位数{}", dailyTrainCarriage.getIndex(), seatList.size());
//            遍历当前车厢里面的每一个座位
            for (DailyTrainSeat dailyTrainSeat : seatList) {
//                判断column,有值的话要比对列号
                String col = dailyTrainSeat.getCol();
                Integer seatIndex = dailyTrainSeat.getCarriageSeatIndex();

//                判断当前座位是否被选座过
                boolean alreadyChooseFlag = false;
                for (DailyTrainSeat trainSeat : finalSeatList) {
                    if (trainSeat.getId().equals(dailyTrainSeat.getId())) {
                        alreadyChooseFlag = true;
                        break;
                    }
                }

                if (alreadyChooseFlag) {
                    Log.info("座位{}被选中过,不能重复选择,将判断下一个座位", seatIndex);
                    continue;
                }

//                对不支持自助选座的座位进行选座
                if (StrUtil.isBlank(column)) {
                    Log.info("无选座");
                } else {
                    if (!column.equals(col)) {
                        Log.info("座位{}列值不对,继续下一个座位,当前列:{},目标列:{}", seatIndex, col, column);
                        continue;
                    }
                }
//                选出第一个座位
                boolean isChoose = calSell(dailyTrainSeat, startIndex, endIndex);
                if (isChoose) {
                    Log.info("选中座位");
                    getlSeatList.add(dailyTrainSeat);
                } else {
                    continue;
                }


//                选出第一个座位之后区间里面的座位
//                对支持选座的区间进行选座
//                偏移值的座位是否都选中了
                boolean isGetAllOffsetSeat = true;
                if (CollUtil.isNotEmpty(offsetList)) {
                    Log.info("有偏移值:{},校验偏移的座位是否可选", offsetList);
//                    查找偏移后的座位索引
                    for (int i = 1; i < offsetList.size(); i++) {
                        Integer offset = offsetList.get(i);
                        int nextIndex = seatIndex + offset - 1;
//                        有选座时,肯定是在同一个车厢
                        DailyTrainSeat nextSeat = seatList.get(nextIndex);

                        if (nextIndex >= seatList.size()) {
                            Log.info("座位{}不可选,偏移后的超出了这个车厢的座位数", nextSeat.getCarriageSeatIndex());
                            isGetAllOffsetSeat = false;
                            break;
                        }

                        boolean isChooseNext = calSell(nextSeat, startIndex, endIndex);
                        if (isChooseNext) {
                            Log.info("座位{}被选中", nextSeat.getCarriageSeatIndex());
                            getlSeatList.add(nextSeat);
                        } else {
                            Log.info("座位{}不可选", nextSeat.getCarriageSeatIndex());
                            isGetAllOffsetSeat = false;
                            break;
                        }
                    }
                }
//                如果为true,则表示所有的座位都选中了,
//                只要有一个没选中,就要跳出循环
                if (!isGetAllOffsetSeat) {
                    getlSeatList = new ArrayList<>();
                    continue;
                }
//                走到这里来,表示座位都选中了
                finalSeatList.addAll(getlSeatList);
                return;
            }
        }
    }

    /**
     * 计算某座位在车站区间内是否可卖
     *
     * @param dailyTrainSeat 当日的座位
     * @param startIndex     起始站
     * @param endIndex       终点站
     */
    private boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        String sell = dailyTrainSeat.getSell();
//        System.out.println("长度为："+);
//        售卖信息
        String sellPart = "0";
        if (sell.length() != 1) {
            sellPart = sell.substring(startIndex, endIndex);
        }

        if (Integer.parseInt(sellPart) > 0) {
            Log.info("座位{}在本次车站区间内{}-{}已售过票,该座位不可选",
                    dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        } else {
            Log.info("座位{}在本次车站区间内{}-{}未售过票,可选中该座位",
                    dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
//            将字符串的0替换为1
//            111
            String curSell = sellPart.replace('0', '1');
//            0111
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);
//            01110
            curSell = StrUtil.fillAfter(curSell, '0', sell.length());

//            当前区间的售票信息与库里面已售信息按位与计算,即可获得卖出此票后的售票详情
//            获得的结果是十进制的数
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(sell);

//            转为二进制的字符串
//            11111
            String newSell = NumberUtil.getBinaryStr(newSellInt);
//            因为上面的表达式不会向最高位补0,所以需要继续补0操作
            newSell = StrUtil.fillBefore(newSell, '0', sell.length());
            Log.info("座位{}被选中,原售票信息:{},车站区间:{}-{},即:{},最终售票信息:{}",
                    dailyTrainSeat.getCarriageSeatIndex(), sell, startIndex, endIndex,
                    curSell, newSell);
            dailyTrainSeat.setSell(newSell);
            return true;
        }
    }

    /**
     * 根据条件去判断余票是否足够
     *
     * @param req
     * @param dailyTrainTicket
     */
    private static void reduceickets(ConfirmOrderDoReq req, DailyTrainTicket dailyTrainTicket) {
        for (ConfirmOrderTicketReq ticketReq : req.getTickets()) {
            String seatTypeCode = ticketReq.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);
            switch (seatTypeEnum) {
                case YDZ -> {
                    int countLeft = dailyTrainTicket.getYdz() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.BUSINESS_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(countLeft);
                }
                case EDZ -> {
                    int countLeft = dailyTrainTicket.getEdz() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.BUSINESS_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setEdz(countLeft);
                }
                case RW -> {
                    int countLeft = dailyTrainTicket.getRw() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.BUSINESS_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setRw(countLeft);
                }
                case YW -> {
                    int countLeft = dailyTrainTicket.getYw() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.BUSINESS_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(countLeft);
                }
            }
        }
    }

}
