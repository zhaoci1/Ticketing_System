package com.jiawa.train.business.controller.web;

import com.jiawa.train.business.req.DailyTrainTicketQuery;
import com.jiawa.train.business.service.DailyTrainTicketService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/daily-train-ticket")
public class DailyTrainTicketWebController {
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid DailyTrainTicketQuery req) {
        return AxiosResult.success(dailyTrainTicketService.queryList(req));
    }

}
