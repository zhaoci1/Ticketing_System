package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.DailyTrainTicketQuery;
import com.jiawa.train.business.req.DailyTrainTicketReq;
import com.jiawa.train.business.service.DailyTrainTicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/daily-train-ticket")
public class DailyTrainTicketController {
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody DailyTrainTicketReq req) {
        System.out.println(req);
        int save = dailyTrainTicketService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid DailyTrainTicketQuery req) {
        return AxiosResult.success(dailyTrainTicketService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteDailyTrainTicket(@PathVariable Long id) {

        return AxiosResult.success(dailyTrainTicketService.delete(id)>=1);
    }
}
