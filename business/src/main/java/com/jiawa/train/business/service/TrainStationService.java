package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.mapper.TrainStationMapper;
import com.jiawa.train.business.req.TrainStationQuery;
import com.jiawa.train.business.req.TrainStationReq;
import com.jiawa.train.business.resp.TrainStationQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStationService {

    @Resource
    private TrainStationMapper trainStationMapper;

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(TrainStationReq req) {
        DateTime now = DateTime.now();
        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        int state;

        if (ObjectUtil.isNull(trainStation.getId())) {
            TrainStation trainStationDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
//                进入条件则说明重复了，需要抛异常
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }
            trainStationDB = selectByUnique(req.getTrainCode(), req.getName());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
//                进入条件则说明重复了，需要抛异常
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR);
            }
            //        获取当前登录的会员id
            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            state = trainStationMapper.insert(trainStation);
        } else {
            trainStation.setUpdateTime(now);
            state = trainStationMapper.updateByPrimaryKey(trainStation);
        }

        return state;
    }

    public PageResp<TrainStationQueryResp> queryList(TrainStationQuery req) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("train_code asc,`index` asc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<TrainStation> trainStations = trainStationMapper.selectByExample(trainStationExample);

        List<TrainStationQueryResp> list = BeanUtil.copyToList(trainStations, TrainStationQueryResp.class);
        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStations);

        PageResp<TrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return trainStationMapper.deleteByPrimaryKey(id);
    }

    private TrainStation selectByUnique(String trainCode, Integer index) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria().
                andTrainCodeEqualTo(trainCode).
                andIndexEqualTo(index);
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
//                进入条件则说明重复了，需要抛异常
            return list.get(0);
        } else {
            return null;
        }
    }

    private TrainStation selectByUnique(String trainCode, String name) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria().
                andTrainCodeEqualTo(trainCode).
                andNameEqualTo(name);
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
//                进入条件则说明重复了，需要抛异常
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<TrainStation> selectByTrainCode(String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("`index` asc");
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainStationMapper.selectByExample(trainStationExample);
    }
}
