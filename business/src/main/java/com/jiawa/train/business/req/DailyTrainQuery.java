package com.jiawa.train.business.req;

import com.jiawa.train.common.req.PageReq;

public class DailyTrainQuery extends PageReq {
 private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "DailyTrainQuery{"+
                '}'+super.toString();
    }
}