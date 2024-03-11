package com.jiawa.train.member.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.member.req.PassengerQuery;
import com.jiawa.train.member.req.PassengerReq;
import com.jiawa.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody PassengerReq req) {
        int save = passengerService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid PassengerQuery req) {
        req.setMemberId(LoginMemberContext.getId());
        return AxiosResult.success(passengerService.queryList(req));
    }
}
