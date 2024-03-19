package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.DailyTrainSeat;
import com.jiawa.train.business.domain.DailyTrainSeatExample;
import com.jiawa.train.business.mapper.DailyTrainSeatMapper;
import com.jiawa.train.business.req.DailyTrainSeatQuery;
import com.jiawa.train.business.req.DailyTrainSeatReq;
import com.jiawa.train.business.resp.DailyTrainSeatQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyTrainSeatService {

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

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
        dailyTrainSeatExample.setOrderByClause("id desc");
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);
        List<DailyTrainSeatQueryResp> list = BeanUtil.copyToList(dailyTrainSeats, DailyTrainSeatQueryResp.class);
        PageInfo<DailyTrainSeatQueryResp> pageInfo = new PageInfo<>(list);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(pageInfo.getList());
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }
}
