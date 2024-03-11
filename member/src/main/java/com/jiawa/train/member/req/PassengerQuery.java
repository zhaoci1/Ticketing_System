package com.jiawa.train.member.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jiawa.train.common.req.PageReq;

public class PassengerQuery extends PageReq {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "PassengerQuery{" +
                "memberId=" + memberId +
                '}';
    }
}