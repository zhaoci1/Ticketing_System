package com.jiawa.train.member.controller;

import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.member.domain.Person;
import com.jiawa.train.member.req.MemberReq;
import com.jiawa.train.member.req.MemberSendCodeReq;
import com.jiawa.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public AxiosResult sendCode(@Valid MemberSendCodeReq memberReq) {
        memberService.sendCode(memberReq);
        return AxiosResult.success("OK");
    }
}
