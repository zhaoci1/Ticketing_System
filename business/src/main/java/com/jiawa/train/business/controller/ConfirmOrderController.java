package com.jiawa.train.business.controller;

import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.ConfirmOrderQuery;
import com.jiawa.train.business.req.ConfirmOrderDoReq;
import com.jiawa.train.business.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/do")
    public AxiosResult doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        confirmOrderService.doConfirm(req);
        return AxiosResult.success("");
    }
}
