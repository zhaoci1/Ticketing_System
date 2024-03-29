package com.jiawa.train.member.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.member.req.TicketQuery;
import com.jiawa.train.member.req.TicketReq;
import com.jiawa.train.member.resp.TicketQueryResp;
import com.jiawa.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Resource
    private TicketService ticketService;

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid TicketQueryResp req) {
        req.setMemberId(LoginMemberContext.getId());
        return AxiosResult.success(ticketService.queryList(req));
    }

}
