package com.jiawa.train.member.controller.feign;

import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.member.req.TicketReq;
import com.jiawa.train.member.resp.TicketQueryResp;
import com.jiawa.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feign/ticket")
public class TicketFeignController {
    @Resource
    private TicketService ticketService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody TicketReq req) {
        System.out.println(req);
        int save = ticketService.save(req);
        return AxiosResult.success(save >= 1);
    }
}
