package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.business.enums.SeatColEnum;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.TrainCarriageExample;
import com.jiawa.train.business.mapper.TrainCarriageMapper;
import com.jiawa.train.business.req.TrainCarriageQuery;
import com.jiawa.train.business.req.TrainCarriageReq;
import com.jiawa.train.business.resp.TrainCarriageQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainCarriageService {

    @Resource
    private TrainCarriageMapper trainCarriageMapper;
    private static final Logger LOG = LoggerFactory.getLogger(TrainCarriageService.class);

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(TrainCarriageReq req) {
        DateTime now = DateTime.now();
        List<SeatColEnum> colsByType = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(colsByType.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());
        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        int state;

        if (ObjectUtil.isNull(trainCarriage.getId())) {
            TrainCarriage trainCarriageDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainCarriageDB)) {
//                进入条件则说明重复了，需要抛异常
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }
            //        获取当前登录的会员id
            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            state = trainCarriageMapper.insert(trainCarriage);
        } else {
            trainCarriage.setUpdateTime(now);
            state = trainCarriageMapper.updateByPrimaryKey(trainCarriage);
        }

        return state;
    }

    public PageResp<TrainCarriageQueryResp> queryList(TrainCarriageQuery req) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("train_code asc,`index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectByExample(trainCarriageExample);

        List<TrainCarriageQueryResp> list = BeanUtil.copyToList(trainCarriages, TrainCarriageQueryResp.class);
        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriages);
        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return trainCarriageMapper.deleteByPrimaryKey(id);
    }

    /**
     * @param trainCode
     */
    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("`index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        return trainCarriageMapper.selectByExample(trainCarriageExample);
    }

    private TrainCarriage selectByUnique(String trainCode, Integer index) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.createCriteria().
                andTrainCodeEqualTo(trainCode).
                andIndexEqualTo(index);
        List<TrainCarriage> list = trainCarriageMapper.selectByExample(trainCarriageExample);
        if (CollUtil.isNotEmpty(list)) {
//                进入条件则说明重复了，需要抛异常
            return list.get(0);
        } else {
            return null;
        }
    }

}
