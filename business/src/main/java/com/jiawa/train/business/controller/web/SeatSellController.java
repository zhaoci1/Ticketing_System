package com.jiawa.train.business.controller.web;


import com.jiawa.train.business.req.SeatSellReq;
import com.jiawa.train.business.service.DailyTrainSeatService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seat_sell")
public class SeatSellController {
    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    @GetMapping("query")
    public AxiosResult query(@Valid SeatSellReq req) {
        return AxiosResult.success(dailyTrainSeatService.querySeatSell(req));
    }
}
