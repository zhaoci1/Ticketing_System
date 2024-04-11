package com.jiawa.train.business.controller;

import com.jiawa.train.business.req.DailyTrainCarriageQuery;
import com.jiawa.train.business.req.DailyTrainCarriageReq;
import com.jiawa.train.business.service.DailyTrainCarriageService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/daily-train-carriage")
public class DailyTrainCarriageController {
    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody DailyTrainCarriageReq req) {
        int save = dailyTrainCarriageService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid DailyTrainCarriageQuery req) {
        return AxiosResult.success(dailyTrainCarriageService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteDailyTrainCarriage(@PathVariable Long id) {
        return AxiosResult.success(dailyTrainCarriageService.delete(id)>=1);
    }
}
