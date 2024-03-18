package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.DailyTrainStationQuery;
import com.jiawa.train.business.req.DailyTrainStationReq;
import com.jiawa.train.business.service.DailyTrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/daily-train-station")
public class DailyTrainStationController {
    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody DailyTrainStationReq req) {
        System.out.println(req);
        int save = dailyTrainStationService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid DailyTrainStationQuery req) {
        return AxiosResult.success(dailyTrainStationService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteDailyTrainStation(@PathVariable Long id) {

        return AxiosResult.success(dailyTrainStationService.delete(id)>=1);
    }
}
