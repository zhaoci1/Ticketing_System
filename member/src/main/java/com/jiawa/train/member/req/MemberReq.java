package com.jiawa.train.member.req;

public class MemberReq {
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
