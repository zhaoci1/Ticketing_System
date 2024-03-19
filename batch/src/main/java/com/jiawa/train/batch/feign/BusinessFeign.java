package com.jiawa.train.batch.feign;

import com.jiawa.train.common.resp.AxiosResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//name为服务名
@FeignClient(name = "business", url = "http://localhost:8002/business")
public interface BusinessFeign {
    @GetMapping("/admin/train/queryList")
    AxiosResult test();
}
