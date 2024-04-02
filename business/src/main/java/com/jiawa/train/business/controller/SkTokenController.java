package com.jiawa.train.business.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.business.req.SkTokenQuery;
import com.jiawa.train.business.req.SkTokenReq;
import com.jiawa.train.business.service.SkTokenService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/sk-token")
public class SkTokenController {
    @Resource
    private SkTokenService skTokenService;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody SkTokenReq req) {
        System.out.println(req);
        int save = skTokenService.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid SkTokenQuery req) {
        return AxiosResult.success(skTokenService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult deleteSkToken(@PathVariable Long id) {

        return AxiosResult.success(skTokenService.delete(id)>=1);
    }
}
