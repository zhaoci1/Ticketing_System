package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.TrainStationQuery;
import com.jiawa.train.business.req.TrainStationReq;
import com.jiawa.train.business.service.TrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationController {
    @Resource
    private TrainStationService trainStationService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody TrainStationReq req) {
        System.out.println(req);
        int save = trainStationService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid TrainStationQuery req) {
        return AxiosResult.success(trainStationService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteTrainStation(@PathVariable Long id) {

        return AxiosResult.success(trainStationService.delete(id)>=1);
    }
}
