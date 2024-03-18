package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.DailyTrainStation;
import com.jiawa.train.business.domain.DailyTrainStationExample;
import com.jiawa.train.business.mapper.DailyTrainStationMapper;
import com.jiawa.train.business.req.DailyTrainStationQuery;
import com.jiawa.train.business.req.DailyTrainStationReq;
import com.jiawa.train.business.resp.DailyTrainStationQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyTrainStationService {

    @Resource
    private DailyTrainStationMapper dailyTrainStationMapper;

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(DailyTrainStationReq req) {
        DateTime now = DateTime.now();
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(req, DailyTrainStation.class);
        int state;

        if (ObjectUtil.isNull(dailyTrainStation.getId())) {
            //        获取当前登录的会员id
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            state = dailyTrainStationMapper.insert(dailyTrainStation);
        } else {
            dailyTrainStation.setUpdateTime(now);
            state = dailyTrainStationMapper.updateByPrimaryKey(dailyTrainStation);
        }

        return state;
    }

    public PageResp<DailyTrainStationQueryResp> queryList(DailyTrainStationQuery req) {
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.setOrderByClause("id desc");
        DailyTrainStationExample.Criteria criteria = dailyTrainStationExample.createCriteria();

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<DailyTrainStation> dailyTrainStations = dailyTrainStationMapper.selectByExample(dailyTrainStationExample);
        List<DailyTrainStationQueryResp> list = BeanUtil.copyToList(dailyTrainStations, DailyTrainStationQueryResp.class);
        PageInfo<DailyTrainStationQueryResp> pageInfo = new PageInfo<>(list);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(pageInfo.getList());
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return dailyTrainStationMapper.deleteByPrimaryKey(id);
    }
}
