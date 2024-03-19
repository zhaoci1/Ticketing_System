package com.jiawa.train.business.controller;

import com.jiawa.train.business.req.DailyTrainQuery;
import com.jiawa.train.business.req.DailyTrainReq;
import com.jiawa.train.business.service.DailyTrainService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

        return AxiosResult.success(dailyTrainService.delete(id) >= 1);
    }

    @GetMapping("/gen-daily/{date}")
    public AxiosResult deleteDailyTrain
            (
                    @PathVariable
                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        dailyTrainService.genDaily(date);
        return AxiosResult.success("");
    }
}
