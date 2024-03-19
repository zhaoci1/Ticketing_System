package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.DailyTrainSeatQuery;
import com.jiawa.train.business.req.DailyTrainSeatReq;
import com.jiawa.train.business.service.DailyTrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/daily-train-seat")
public class DailyTrainSeatController {
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody DailyTrainSeatReq req) {
        System.out.println(req);
        int save = dailyTrainSeatService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid DailyTrainSeatQuery req) {
        return AxiosResult.success(dailyTrainSeatService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteDailyTrainSeat(@PathVariable Long id) {

        return AxiosResult.success(dailyTrainSeatService.delete(id)>=1);
    }
}
