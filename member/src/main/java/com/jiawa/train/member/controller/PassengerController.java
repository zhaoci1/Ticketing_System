package com.jiawa.train.member.controller;

import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.member.req.PassengerReq;
import com.jiawa.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody PassengerReq req) {
        passengerService.save(req);
        return AxiosResult.success("ok");
    }
}
