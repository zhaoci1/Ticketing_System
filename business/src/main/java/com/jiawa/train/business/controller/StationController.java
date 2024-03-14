package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.StationQuery;
import com.jiawa.train.business.req.StationReq;
import com.jiawa.train.business.service.StationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/station")
public class StationController {
    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody StationReq req) {
        System.out.println(req);
        int save = stationService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid StationQuery req) {
        req.setMemberId(LoginMemberContext.getId());
        return AxiosResult.success(stationService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteStation(@PathVariable Long id) {

        return AxiosResult.success(stationService.delete(id)>=1);
    }
}
