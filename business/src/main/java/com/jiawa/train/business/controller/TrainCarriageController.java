package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.TrainCarriageQuery;
import com.jiawa.train.business.req.TrainCarriageReq;
import com.jiawa.train.business.service.TrainCarriageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageController {
    @Resource
    private TrainCarriageService trainCarriageService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody TrainCarriageReq req) {
        System.out.println(req);
        int save = trainCarriageService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid TrainCarriageQuery req) {
        return AxiosResult.success(trainCarriageService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteTrainCarriage(@PathVariable Long id) {

        return AxiosResult.success(trainCarriageService.delete(id)>=1);
    }
}
