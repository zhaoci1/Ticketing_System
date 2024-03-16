package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.TrainSeat;
import com.jiawa.train.business.domain.TrainSeatExample;
import com.jiawa.train.business.mapper.TrainSeatMapper;
import com.jiawa.train.business.req.TrainSeatQuery;
import com.jiawa.train.business.req.TrainSeatReq;
import com.jiawa.train.business.resp.TrainSeatQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainSeatService {

    @Resource
    private TrainSeatMapper trainSeatMapper;

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
}
