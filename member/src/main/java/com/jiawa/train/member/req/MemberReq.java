package com.jiawa.train.member.req;

import jakarta.validation.constraints.NotBlank;

public class MemberReq {
    @NotBlank(message = "手机号不能为空!")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberReq{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
