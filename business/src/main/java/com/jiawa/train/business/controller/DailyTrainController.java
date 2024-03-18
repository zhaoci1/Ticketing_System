package com.jiawa.train.business.controller;

import com.jiawa.train.business.req.DailyTrainQuery;
import com.jiawa.train.business.req.DailyTrainReq;
import com.jiawa.train.business.service.DailyTrainService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/daily-train")
public class DailyTrainController {
    @Resource
    private DailyTrainService dailyTrainService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody DailyTrainReq req) {
        System.out.println(req);
        int save = dailyTrainService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid DailyTrainQuery req) {
        System.out.println(req);
        return AxiosResult.success(dailyTrainService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteDailyTrain(@PathVariable Long id) {

        return AxiosResult.success(dailyTrainService.delete(id)>=1);
    }
}
