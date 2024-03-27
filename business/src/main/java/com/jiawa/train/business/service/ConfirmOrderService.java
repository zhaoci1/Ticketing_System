package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.business.enums.ConfirmOrderStatusEnum;
import com.jiawa.train.business.enums.SeatColEnum;
import com.jiawa.train.business.enums.SeatTypeEnum;
import com.jiawa.train.business.req.ConfirmOrderTicketReq;
import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.mapper.ConfirmOrderMapper;
import com.jiawa.train.business.req.ConfirmOrderQuery;
import com.jiawa.train.business.req.ConfirmOrderDoReq;
import com.jiawa.train.business.resp.ConfirmOrderQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * @param req
     */
    public void doConfirm(ConfirmOrderDoReq req) {
//        保存确认订单表，状态初始
        DateTime now = DateTime.now();
        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        List<ConfirmOrderTicketReq> tickets = req.getTickets();

        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrder.setMemberId(LoginMemberContext.getId());
        confirmOrder.setDate(date);
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setStart(start);
        confirmOrder.setEnd(end);
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setTickets(JSON.toJSONString(tickets));
        confirmOrderMapper.insert(confirmOrder);


//        查处余票记录
        DailyTrainTicket dailyTrainTicket =
                dailyTrainTicketService.selectByUnique(trainCode, end, date, start);

//        预扣减余票数量,并判断余票是否足够
        reduceickets(req, dailyTrainTicket);


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

            getSaat(req.getDate(), req.getTrainCode(),
                    ticketReq0.getSeatTypeCode(),
                    ticketReq0.getSeat().split("")[0],
                    offsetList);
        } else {
            Log.info("本次购票没有选座");
            for (ConfirmOrderTicketReq ticket : tickets) {
                System.out.println(ticket);
                getSaat(date, trainCode, ticket.getSeatTypeCode(), null, null);
            }
        }
//        选座
//                根据车厢获取座位
    }

    private void getSaat(Date date, String trainCode,
                         String seatType, String column, List<Integer> indexList) {
        List<DailyTrainCarriage> carriageList =
                dailyTrainCarriageService.selectBySeatType(date, trainCode, seatType);

        Log.info("共查出{}个符合条件的车厢", carriageList.size());
//        对车厢进行遍历
        for (DailyTrainCarriage dailyTrainCarriage : carriageList) {
            Log.info("开始从车厢{}选座", dailyTrainCarriage.getIndex());
            List<DailyTrainSeat> seatList = dailyTrainSeatService
                    .selectByCarriage(date, trainCode, dailyTrainCarriage.getIndex());
            Log.info("车厢{}的座位数{}", dailyTrainCarriage.getIndex(),seatList.size());
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
