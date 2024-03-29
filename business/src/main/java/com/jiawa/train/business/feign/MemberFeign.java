package com.jiawa.train.business.feign;

import com.jiawa.train.common.req.TicketReq;
import com.jiawa.train.common.resp.AxiosResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member", url = "http://127.0.0.1:8001")
public interface MemberFeign {

    @GetMapping("/member/feign/ticket/save")
    AxiosResult save(@RequestBody TicketReq req);
}
