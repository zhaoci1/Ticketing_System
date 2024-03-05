package com.jiawa.train.member.controller;

import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.member.domain.Person;
import com.jiawa.train.member.req.MemberLoginReq;
import com.jiawa.train.member.req.MemberReq;
import com.jiawa.train.member.req.MemberSendCodeReq;
import com.jiawa.train.member.resp.MemberLoginResp;
import com.jiawa.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public AxiosResult<Integer> count() {
        return AxiosResult.success(memberService.count());
    }

    @PostMapping("/register")
    public AxiosResult register(@Valid MemberReq memberReq) {
        return AxiosResult.success(memberService.register(memberReq));
    }

    @PostMapping("/sendCode")
    public AxiosResult sendCode(@Valid @RequestBody MemberSendCodeReq memberReq) {
        String code = memberService.sendCode(memberReq);
        return AxiosResult.success(code);
    }

    @PostMapping("/login")
    public AxiosResult login(@Valid @RequestBody MemberLoginReq memberReq) {
        System.out.println(memberReq);
        return AxiosResult.success(memberService.login(memberReq));
    }
}
