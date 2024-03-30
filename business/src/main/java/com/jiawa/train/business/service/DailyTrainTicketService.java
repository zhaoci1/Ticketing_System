package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.business.enums.SeatTypeEnum;
import com.jiawa.train.business.enums.TrainTypeEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.mapper.DailyTrainTicketMapper;
import com.jiawa.train.business.req.DailyTrainTicketQuery;
import com.jiawa.train.business.req.DailyTrainTicketReq;
import com.jiawa.train.business.resp.DailyTrainTicketQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class DailyTrainTicketService {

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Resource
    private TrainStationService trainStationService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    private static final Logger Log = LoggerFactory.getLogger(DailyTrainTicketService.class);

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(DailyTrainTicketReq req) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        int state;

        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            //        获取当前登录的会员id
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            state = dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            state = dailyTrainTicketMapper.updateByPrimaryKey(dailyTrainTicket);
        }

        return state;
    }

    @Cacheable("DailyTrainTicketService.queryList")
    public PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQuery req) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();

        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotNull(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }
        if (ObjectUtil.isNotNull(req.getStart())) {
            criteria.andStartEqualTo(req.getStart());
        }
        if (ObjectUtil.isNotNull(req.getEnd())) {
            criteria.andEndEqualTo(req.getEnd());
        }

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);

        List<DailyTrainTicketQueryResp> list = BeanUtil.copyToList(dailyTrainTickets, DailyTrainTicketQueryResp.class);
        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTickets);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public int delete(Long id) {
        return dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }


    @Transactional
    public void genDaily(DailyTrain dailyTrain, Date date, String trainCode) {
        Log.info("开始生成日期：{}车次{}的余票信息", date, trainCode);

        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);

//        查询途径的车站信息
        //        查出某车次的所有信息
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);

        if (CollUtil.isEmpty(trainStations)) {
            Log.info("该车次没有车站基础数据，生成该车次余票信息结束");
            return;
        }
        DateTime now = DateTime.now();
//        穷举所有的车票
        for (int i = 0; i < trainStations.size(); i++) {
//            获取出发站
            TrainStation trainStationStart = trainStations.get(i);
            BigDecimal sumKm = BigDecimal.ZERO;
            for (int j = i + 1; j < trainStations.size(); j++) {
                sumKm = sumKm.add(trainStations.get(j).getKm());
                TrainStation trainStationEnd = trainStations.get(j);
                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(trainStationStart.getName());
                dailyTrainTicket.setStartPinyin(trainStationStart.getNamePinyin());
                //                车站的出站时间
                dailyTrainTicket.setStartTime(trainStationStart.getOutTime());
                dailyTrainTicket.setStartIndex(trainStationStart.getIndex());
                dailyTrainTicket.setEnd(trainStationEnd.getName());
                dailyTrainTicket.setEndPinyin(trainStationEnd.getNamePinyin());
//                车站的进站时间
                dailyTrainTicket.setEndTime(trainStationEnd.getInTime());
                dailyTrainTicket.setEndIndex(trainStationEnd.getIndex());

//                获取当前车次的余票信息
                int Ydz = dailyTrainSeatService
                        .countSeat(date, trainStationStart.getTrainCode(), SeatTypeEnum.YDZ.getCode());
                int Edz = dailyTrainSeatService
                        .countSeat(date, trainStationStart.getTrainCode(), SeatTypeEnum.EDZ.getCode());
                int Rw = dailyTrainSeatService
                        .countSeat(date, trainStationStart.getTrainCode(), SeatTypeEnum.RW.getCode());
                int Yw = dailyTrainSeatService
                        .countSeat(date, trainStationStart.getTrainCode(), SeatTypeEnum.YW.getCode());
//                票价=里程之和*座位单价*车次类型
                String trainType = dailyTrain.getType();
//                计算票价系数
                BigDecimal fieldBy =
                        EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);

                BigDecimal ydzPrice = sumKm.multiply(SeatTypeEnum.YDZ.getPrice())
                        .multiply(fieldBy).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = sumKm.multiply(SeatTypeEnum.EDZ.getPrice())
                        .multiply(fieldBy).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = sumKm.multiply(SeatTypeEnum.RW.getPrice())
                        .multiply(fieldBy).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = sumKm.multiply(SeatTypeEnum.YW.getPrice())
                        .multiply(fieldBy).setScale(2, RoundingMode.HALF_UP);


                dailyTrainTicket.setYdz(Ydz);
                dailyTrainTicket.setYdzPrice(ydzPrice);
                dailyTrainTicket.setEdz(Edz);
                dailyTrainTicket.setEdzPrice(edzPrice);
                dailyTrainTicket.setRw(Rw);
                dailyTrainTicket.setRwPrice(rwPrice);
                dailyTrainTicket.setYw(Yw);
                dailyTrainTicket.setYwPrice(ywPrice);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);

                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }
    }

    public DailyTrainTicket selectByUnique(String trainCode, String end, Date date, String start) {
        DailyTrainTicketExample trainStationExample = new DailyTrainTicketExample();
        trainStationExample.createCriteria().
                andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date)
                .andStartEqualTo(start)
                .andEndEqualTo(end);
        List<DailyTrainTicket> list = dailyTrainTicketMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
//                进入条件则说明重复了，需要抛异常
            return list.get(0);
        } else {
            return null;
        }
    }
}
