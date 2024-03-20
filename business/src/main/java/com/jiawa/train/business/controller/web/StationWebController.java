package com.jiawa.train.business.controller.web;

import com.jiawa.train.business.req.StationQuery;
import com.jiawa.train.business.req.StationReq;
import com.jiawa.train.business.service.StationService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/station")
public class StationWebController {
    @Resource
    private StationService stationService;

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid StationQuery req) {
        return AxiosResult.success(stationService.queryList(req));
    }


    @GetMapping("/queryAll")
    public AxiosResult queryList() {
        return AxiosResult.success(stationService.queryAll());
    }
}
