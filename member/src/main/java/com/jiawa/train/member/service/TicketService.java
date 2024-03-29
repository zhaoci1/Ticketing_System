package com.jiawa.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.member.domain.Ticket;
import com.jiawa.train.member.domain.TicketExample;
import com.jiawa.train.member.mapper.TicketMapper;
import com.jiawa.train.member.req.TicketReq;
import com.jiawa.train.member.resp.TicketQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Resource
    private TicketMapper ticketMapper;

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(TicketReq req) {
        DateTime now = DateTime.now();
        Ticket ticket = BeanUtil.copyProperties(req, Ticket.class);
        ticket.setId(SnowUtil.getSnowflakeNextId());
        ticket.setCreateTime(now);
        ticket.setUpdateTime(now);


        return ticketMapper.insert(ticket);
    }

    public PageResp<TicketQueryResp> queryList(TicketQueryResp req) {
        TicketExample ticketExample = new TicketExample();
        ticketExample.setOrderByClause("id desc");
        TicketExample.Criteria criteria = ticketExample.createCriteria();

        if(ObjectUtil.isNotNull(req.getMemberId())){
            criteria.andMemberIdEqualTo(req.getMemberId());
        }

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<Ticket> tickets = ticketMapper.selectByExample(ticketExample);
        List<TicketQueryResp> list = BeanUtil.copyToList(tickets, TicketQueryResp.class);
        PageInfo<Ticket> pageInfo = new PageInfo<>(tickets);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return ticketMapper.deleteByPrimaryKey(id);
    }
}
