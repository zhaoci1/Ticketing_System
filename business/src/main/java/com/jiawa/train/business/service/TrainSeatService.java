package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.business.enums.SeatColEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.mapper.TrainSeatMapper;
import com.jiawa.train.business.req.TrainSeatQuery;
import com.jiawa.train.business.req.TrainSeatReq;
import com.jiawa.train.business.resp.TrainSeatQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainSeatService {

    @Resource
    private TrainSeatMapper trainSeatMapper;

    @Resource
    private TrainCarriageService trainCarriageService;

    private static final Logger Log = LoggerFactory.getLogger(TrainSeatService.class);


    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(TrainSeatReq req) {
        DateTime now = DateTime.now();
        TrainSeat trainSeat = BeanUtil.copyProperties(req, TrainSeat.class);
        int state;

        if (ObjectUtil.isNull(trainSeat.getId())) {
            //        获取当前登录的会员id
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            state = trainSeatMapper.insert(trainSeat);
        } else {
            trainSeat.setUpdateTime(now);
            state = trainSeatMapper.updateByPrimaryKey(trainSeat);
        }

        return state;
    }

    public PageResp<TrainSeatQueryResp> queryList(TrainSeatQuery req) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("train_code asc,carriage_index asc,carriage_seat_index asc");
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<TrainSeat> trainSeats = trainSeatMapper.selectByExample(trainSeatExample);
        List<TrainSeatQueryResp> list = BeanUtil.copyToList(trainSeats, TrainSeatQueryResp.class);

        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeats);

        PageResp<TrainSeatQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return trainSeatMapper.deleteByPrimaryKey(id);
    }

    public void genTrainSeat(String trainCode) {
        DateTime now = DateTime.now();
//        情况当前车次下的所有的座位记录
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        trainSeatMapper.deleteByExample(trainSeatExample);

//        查找当前车次下的所有的车厢
        List<TrainCarriage> trainCarriages = trainCarriageService.selectByTrainCode(trainCode);

        Log.info("当前车次下的车厢数：{}",trainCarriages.size());
        //        循环生成每个车厢的座位
        for (TrainCarriage trainCarriage : trainCarriages) {
//        拿到车厢数据，包括，行数，座位类型(得到列数)
//            得到行，几排的意思
            Integer rowCount = trainCarriage.getRowCount();
            System.out.println(rowCount);
//            得到类型
            String seatType = trainCarriage.getSeatType();
//        根据车厢的座位类型，筛选出所有的列，比如车厢类型是一等座的，则筛选出车厢号为{ACDF}的车厢
            List<SeatColEnum> colsByType = SeatColEnum.getColsByType(seatType);

            Log.info("根据车厢座位的类型，筛选出所有的列：{}",colsByType);

//            获取车厢对应的索引
            int seatIndex = 1;

//        循环行数
            for (int i = 1; i <= rowCount; i++) {
//                循环列数
                for (SeatColEnum seatColEnum : colsByType) {
                    //                构造座位数据并保存数据库
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(SnowUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(i), '0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);
                    trainSeatMapper.insert(trainSeat);
                }

            }
            System.out.println(trainCarriage);
        }
    }

    public List<TrainSeat> selectByTrainCode(String trainCode) {
        TrainSeatExample trainStationExample = new TrainSeatExample();
        trainStationExample.setOrderByClause("`id` asc");
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainSeatMapper.selectByExample(trainStationExample);
    }
}
