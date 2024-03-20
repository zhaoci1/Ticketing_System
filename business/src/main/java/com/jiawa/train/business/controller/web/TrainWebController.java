package com.jiawa.train.business.controller.web;

import com.jiawa.train.business.req.TrainQuery;
import com.jiawa.train.business.req.TrainReq;
import com.jiawa.train.business.service.TrainSeatService;
import com.jiawa.train.business.service.TrainService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/train")
public class TrainWebController {
    @Resource
    private TrainService trainService;


    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid TrainQuery req) {
        return AxiosResult.success(trainService.queryList(req));
    }

    @GetMapping("/queryAll")
    public AxiosResult queryList() {
        return AxiosResult.success(trainService.queryAll());
    }


}
