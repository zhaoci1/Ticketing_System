package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.TrainStation;
import com.jiawa.train.business.domain.TrainStationExample;
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
        trainStationExample.setOrderByClause("id desc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<TrainStation> trainStations = trainStationMapper.selectByExample(trainStationExample);
        List<TrainStationQueryResp> list = BeanUtil.copyToList(trainStations, TrainStationQueryResp.class);
        PageInfo<TrainStationQueryResp> pageInfo = new PageInfo<>(list);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(pageInfo.getList());
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return trainStationMapper.deleteByPrimaryKey(id);
    }
}
