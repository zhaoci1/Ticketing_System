package com.jiawa.train.${module}.req;

import com.jiawa.train.common.req.PageReq;

public class ${Domain}Query extends PageReq {
 private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "${Domain}Query{"+
                '}'+super.toString();
    }
}