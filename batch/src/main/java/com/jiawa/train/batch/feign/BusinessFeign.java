package com.jiawa.train.batch.feign;

import com.jiawa.train.common.resp.AxiosResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

//name为服务名
//@FeignClient(name = "business", url = "http://localhost:8002/business")
@FeignClient(name = "business")
public interface BusinessFeign {
    @GetMapping("/business/admin/train/queryList")
    AxiosResult queryList();

    @GetMapping("/admin/daily-train/gen-daily/{date}")
    AxiosResult genDaily(@PathVariable
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
