package com.jiawa.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.member.domain.Passenger;
import com.jiawa.train.member.domain.PassengerExample;
import com.jiawa.train.member.mapper.PassengerMapper;
import com.jiawa.train.member.req.PassengerQuery;
import com.jiawa.train.member.req.PassengerReq;
import com.jiawa.train.member.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(PassengerReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        int state;

        if (ObjectUtil.isNull(passenger.getId())) {
            //        获取当前登录的会员id
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            state = passengerMapper.insert(passenger);
        } else {
            passenger.setUpdateTime(now);
            state = passengerMapper.updateByPrimaryKey(passenger);
        }

        return state;
    }

    public PageResp<PassengerQueryResp> queryList(PassengerQuery req) {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("id desc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            criteria.andMemberIdEqualTo(req.getMemberId());
        }
//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<Passenger> passengers = passengerMapper.selectByExample(passengerExample);
        PageInfo<Passenger> pageInfo = new PageInfo<>(passengers);
        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(pageInfo.getList());
        return pageResp;
    }
}
