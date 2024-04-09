package com.jiawa.train.business.controller.web;

import com.jiawa.train.business.req.DailyTrainStationQueryAllReq;
import com.jiawa.train.business.resp.DailyTrainStationQueryResp;
import com.jiawa.train.business.service.DailyTrainStationService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("daily_train_station_member")
public class DailyTrainStationMemberController {
    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @GetMapping("query-by-train-code")
    public AxiosResult queryByTrain(@Valid DailyTrainStationQueryAllReq req){
        List<DailyTrainStationQueryResp> list = dailyTrainStationService.queryByTrain(req.getDate(), req.getTrainCode());
        return AxiosResult.success(list);
    }
}
