package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.TrainQuery;
import com.jiawa.train.business.req.TrainReq;
import com.jiawa.train.business.service.TrainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train")
public class TrainController {
    @Resource
    private TrainService trainService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody TrainReq req) {
        System.out.println(req);
        int save = trainService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid TrainQuery req) {
        return AxiosResult.success(trainService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteTrain(@PathVariable Long id) {
        return AxiosResult.success(trainService.delete(id) >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList() {
        return AxiosResult.success(trainService.queryAll());
    }
}
