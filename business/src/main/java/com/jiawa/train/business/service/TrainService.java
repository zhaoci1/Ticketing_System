package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.TrainCarriage;
import com.jiawa.train.business.domain.TrainCarriageExample;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.Train;
import com.jiawa.train.business.domain.TrainExample;
import com.jiawa.train.business.mapper.TrainMapper;
import com.jiawa.train.business.req.TrainQuery;
import com.jiawa.train.business.req.TrainReq;
import com.jiawa.train.business.resp.TrainQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {


    @Resource
    private TrainMapper trainMapper;

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(TrainReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        int state;

        if (ObjectUtil.isNull(train.getId())) {

            Train trainDB = selectByUnique(req.getCode());
            if (ObjectUtil.isNotEmpty(trainDB)) {
//                进入条件则说明重复了，需要抛异常
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }
            //        获取当前登录的会员id
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            state = trainMapper.insert(train);
        } else {
            train.setUpdateTime(now);
            state = trainMapper.updateByPrimaryKey(train);
        }

        return state;
    }

    public PageResp<TrainQueryResp> queryList(TrainQuery req) {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code desc");
        TrainExample.Criteria criteria = trainExample.createCriteria();
//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<Train> trains = trainMapper.selectByExample(trainExample);
//        将train转为特定的返回类
        List<TrainQueryResp> list = BeanUtil.copyToList(trains, TrainQueryResp.class);

        PageInfo<Train> pageInfo = new PageInfo<>(trains);

        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return trainMapper.deleteByPrimaryKey(id);
    }

    public List<TrainQueryResp> queryAll() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code desc");
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<Train> trains = trainMapper.selectByExample(trainExample);
//        将train转为特定的返回类

        return BeanUtil.copyToList(trains, TrainQueryResp.class);
    }

    private Train selectByUnique(String code) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria().andCodeEqualTo(code);
        List<Train> list = trainMapper.selectByExample(trainExample);
        if (CollUtil.isNotEmpty(list)) {
//                进入条件则说明重复了，需要抛异常
            return list.get(0);
        } else {
            return null;
        }
    }
}
