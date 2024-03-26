package com.jiawa.train.business.controller.admin;

import com.jiawa.train.business.req.ConfirmOrderDoReq;
import com.jiawa.train.business.req.ConfirmOrderQuery;
import com.jiawa.train.business.service.ConfirmOrderService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/confirm-order")
public class ConfirmOrderAdminController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody ConfirmOrderDoReq req) {
        int save = confirmOrderService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid ConfirmOrderQuery req) {
        return AxiosResult.success(confirmOrderService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteConfirmOrder(@PathVariable Long id) {

        return AxiosResult.success(confirmOrderService.delete(id)>=1);
    }
}
