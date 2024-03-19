package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.business.enums.SeatColEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.mapper.DailyTrainCarriageMapper;
import com.jiawa.train.business.req.DailyTrainCarriageQuery;
import com.jiawa.train.business.req.DailyTrainCarriageReq;
import com.jiawa.train.business.resp.DailyTrainCarriageQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainCarriageService {

    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    @Resource
    private TrainCarriageService trainCarriageService;

    private static final Logger Log = LoggerFactory.getLogger(DailyTrainCarriageService.class);


    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(DailyTrainCarriageReq req) {
        DateTime now = DateTime.now();
        List<SeatColEnum> colsByType = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(colsByType.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());

        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        int state;

        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) {
            //        获取当前登录的会员id
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            state = dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        } else {
            dailyTrainCarriage.setUpdateTime(now);
            state = dailyTrainCarriageMapper.updateByPrimaryKey(dailyTrainCarriage);
        }

        return state;
    }

    public PageResp<DailyTrainCarriageQueryResp> queryList(DailyTrainCarriageQuery req) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("date desc,train_code asc,`index` asc");
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();

        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);
        List<DailyTrainCarriageQueryResp> list = BeanUtil.copyToList(dailyTrainCarriages, DailyTrainCarriageQueryResp.class);
        PageInfo<DailyTrainCarriageQueryResp> pageInfo = new PageInfo<>(list);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(pageInfo.getList());
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return dailyTrainCarriageMapper.deleteByPrimaryKey(id);
    }


    public void genDaily(Date date, String trainCode) {
        Log.info("开始生成日期：{}车次{}的车站信息", date, trainCode);

//        删除某日某车次的车站信息
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        dailyTrainCarriageMapper.deleteByExample(dailyTrainCarriageExample);

//        查出某车次的所有的车厢信息信息
        List<TrainCarriage> trainCarriages = trainCarriageService.selectByTrainCode(trainCode);

        if (CollUtil.isEmpty(trainCarriages)) {
            Log.info("该车次没有车站基础数据，生成该车次车站信息结束");
            return;
        }

        for (TrainCarriage station : trainCarriages) {
            //            生成该车次的数据
            DateTime now = DateTime.now();
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(station, DailyTrainCarriage.class);
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriage.setDate(date);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        }
    }
}
