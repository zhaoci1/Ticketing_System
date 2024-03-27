package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.mapper.DailyTrainSeatMapper;
import com.jiawa.train.business.req.DailyTrainSeatQuery;
import com.jiawa.train.business.req.DailyTrainSeatReq;
import com.jiawa.train.business.resp.DailyTrainSeatQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainSeatService {

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;
    @Resource
    private TrainSeatService trainSeatService;
    @Resource
    private TrainStationService trainStationService;

    private static final Logger Log = LoggerFactory.getLogger(DailyTrainSeatService.class);

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(DailyTrainSeatReq req) {
        DateTime now = DateTime.now();
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        int state;

        if (ObjectUtil.isNull(dailyTrainSeat.getId())) {
            //        获取当前登录的会员id
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            state = dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdateTime(now);
            state = dailyTrainSeatMapper.updateByPrimaryKey(dailyTrainSeat);
        }

        return state;
    }

    public PageResp<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQuery req) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("date desc,train_code asc,carriage_index asc,carriage_seat_index asc");
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);
        List<DailyTrainSeatQueryResp> list = BeanUtil.copyToList(dailyTrainSeats, DailyTrainSeatQueryResp.class);
        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeats);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }

    public void genDaily(Date date, String trainCode) {
        Log.info("开始生成日期：{}车次{}的车站信息", date, trainCode);

        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        dailyTrainSeatMapper.deleteByExample(dailyTrainSeatExample);

//        获取车次的车站信息
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);
//        获取车站个数
        String sell = StrUtil.fillBefore("", '0', trainStations.size());

//        查出某车次的所有的车厢信息信息
        List<TrainSeat> trainSeats = trainSeatService.selectByTrainCode(trainCode);

        if (CollUtil.isEmpty(trainSeats)) {
            Log.info("该车次没有车站基础数据，生成该车次车站信息结束");
            return;
        }

        for (TrainSeat station : trainSeats) {
            //            生成该车次的数据
            DateTime now = DateTime.now();
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(station, DailyTrainSeat.class);
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setSell(sell);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        }
    }

    //    计算座位有多少

    /**
     * @param date      车次时间
     * @param trainCode 车次编号
     * @param seatType  座位类型
     */
    public int countSeat(Date date, String trainCode, String seatType) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode)
                .andSeatTypeEqualTo(seatType);
        long l = dailyTrainSeatMapper.countByExample(dailyTrainSeatExample);
        if (l == 0L) {
            return -1;
        }
        return (int) l;
    }

    /**
     * 根据条件获取座位
     * @param date
     * @param trainCode
     * @param carriageIndex
     * @return
     */
    public List<DailyTrainSeat> selectByCarriage(Date date, String trainCode,Integer carriageIndex){
        DailyTrainSeatExample example = new DailyTrainSeatExample();
        example.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andCarriageIndexEqualTo(carriageIndex);
        return dailyTrainSeatMapper.selectByExample(example);
    }
}
