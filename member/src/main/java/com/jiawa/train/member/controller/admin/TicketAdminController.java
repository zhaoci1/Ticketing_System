package com.jiawa.train.member.controller.admin;

import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.member.resp.TicketQueryResp;
import com.jiawa.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {
    @Resource
    private TicketService ticketService;

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid TicketQueryResp req) {
        return AxiosResult.success(ticketService.queryList(req));
    }

}
