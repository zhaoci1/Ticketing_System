package com.jiawa.train.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.jiawa.train.business.req.ConfirmOrderDoReq;
import com.jiawa.train.business.service.BeforeConfirmOrderService;
import com.jiawa.train.business.service.ConfirmOrderService;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private BeforeConfirmOrderService beforeConfirmOrderService;

    @Value("${spring.profiles.active}")
    private String env;

    private static final Logger Log = LoggerFactory.getLogger(ConfirmOrderController.class);


    @PostMapping("/do")
    public AxiosResult doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        if (!env.equals("dev")) {
            String imageCodeToken = req.getImageCodeToken();
            String imageCode = req.getImageCode();
            String imageCodeRedis = redisTemplate.opsForValue().get(imageCodeToken);
            Log.info("从redis中获取到验证码：{}", imageCodeRedis);

            if (ObjectUtil.isEmpty(imageCodeRedis)) {
                return AxiosResult.error("验证码已过期");
            }

            if (!imageCodeRedis.equalsIgnoreCase(imageCode)) {
                return AxiosResult.error("验证码不正确");
            } else {
                redisTemplate.delete(imageCodeToken);
            }
        }
        Long id = beforeConfirmOrderService.beforeDoConfirm(req);
        return AxiosResult.success(String.valueOf(id));
    }
    @GetMapping("/query-line-count/{id}")
    public AxiosResult queryLineCount(@PathVariable Long id){
        Integer count = confirmOrderService.queryLineCount(id);
        return AxiosResult.success(count);
    }
}
