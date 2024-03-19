package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.Train;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.DailyTrain;
import com.jiawa.train.business.domain.DailyTrainExample;
import com.jiawa.train.business.mapper.DailyTrainMapper;
import com.jiawa.train.business.req.DailyTrainQuery;
import com.jiawa.train.business.req.DailyTrainReq;
import com.jiawa.train.business.resp.DailyTrainQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainService {

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Resource
    private TrainService trainService;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    private static final Logger Log = LoggerFactory.getLogger(DailyTrainService.class);

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(DailyTrainReq req) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        int state;

        if (ObjectUtil.isNull(dailyTrain.getId())) {
            //        获取当前登录的会员id
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            state = dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdateTime(now);
            state = dailyTrainMapper.updateByPrimaryKey(dailyTrain);
        }

        return state;
    }

    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQuery req) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("date desc,code asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();

        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }

        if (ObjectUtil.isNotEmpty(req.getCode())) {
            criteria.andCodeEqualTo(req.getCode());
        }


//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<DailyTrain> dailyTrains = dailyTrainMapper.selectByExample(dailyTrainExample);
        List<DailyTrainQueryResp> list = BeanUtil.copyToList(dailyTrains, DailyTrainQueryResp.class);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrains);
        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return dailyTrainMapper.deleteByPrimaryKey(id);
    }

    //    生成某日所有车次信息，包括车次，车站，车厢，座位
    public void genDaily(Date date) {
//        获取所有车次信息
        List<Train> trainList = trainService.selectAll();
//        做空判断，防止空指针异常
        if (CollUtil.isEmpty(trainList)) {
            Log.info("没有车次基础数据，任务结束");
            return;
        }
        for (Train train : trainList) {
            genDailyTrain(date, train);
        }
    }

    public void genDailyTrain(Date date, Train train) {
        //            删除该车次已有的数据
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.createCriteria().andDateEqualTo(date).andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);

//            生成该车次的数据
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        dailyTrainMapper.insert(dailyTrain);

//        生成车次途径的车站信息(每日车站信息)
        dailyTrainStationService.genDaily(date, train.getCode());

//        生成车次的车厢信息
        dailyTrainCarriageService.genDaily(date, train.getCode());

    }
}
