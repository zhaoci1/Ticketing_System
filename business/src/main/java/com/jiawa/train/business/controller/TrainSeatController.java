package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.TrainSeatQuery;
import com.jiawa.train.business.req.TrainSeatReq;
import com.jiawa.train.business.service.TrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatController {
    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody TrainSeatReq req) {
        System.out.println(req);
        int save = trainSeatService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid TrainSeatQuery req) {
        return AxiosResult.success(trainSeatService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteTrainSeat(@PathVariable Long id) {

        return AxiosResult.success(trainSeatService.delete(id)>=1);
    }
}
