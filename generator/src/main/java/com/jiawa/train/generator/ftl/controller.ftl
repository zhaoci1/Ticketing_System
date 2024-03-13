package com.jiawa.train.${module}.controller;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.${module}.req.${Domain}Query;
import com.jiawa.train.${module}.req.${Domain}Req;
import com.jiawa.train.${module}.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${do_main}")
public class ${Domain}Controller {
    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public AxiosResult save(@Valid @RequestBody ${Domain}Req req) {
        System.out.println(req);
        int save = ${domain}Service.save(req);
        return AxiosResult.success(save >= 1);
    }

    @GetMapping("/queryList")
    public AxiosResult queryList(@Valid ${Domain}Query req) {
        req.setMemberId(LoginMemberContext.getId());
        return AxiosResult.success(${domain}Service.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    public AxiosResult delete${Domain}(@PathVariable Long id) {

        return AxiosResult.success(${domain}Service.delete(id)>=1);
    }
}
