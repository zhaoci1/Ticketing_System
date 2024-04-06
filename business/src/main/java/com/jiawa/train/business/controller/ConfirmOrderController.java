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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private BeforeConfirmOrderService beforeConfirmOrderService;

    private static final Logger Log = LoggerFactory.getLogger(ConfirmOrderController.class);


    @PostMapping("/do")
    public AxiosResult doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        String imageCodeToken = req.getImageCodeToken();
        String imageCode = req.getImageCode();
        String imageCodeRedis = redisTemplate.opsForValue().get(imageCodeToken);
        Log.info("从redis中获取到验证码：{}",imageCodeRedis);

        if(ObjectUtil.isEmpty(imageCodeRedis)){
            return AxiosResult.error("验证码已过期");
        }

        if(!imageCodeRedis.equalsIgnoreCase(imageCode)){
            return AxiosResult.error("验证码不正确");
        }else{
            redisTemplate.delete(imageCodeToken);
        }
        beforeConfirmOrderService.beforeDoConfirm(req);
        return AxiosResult.success("");
    }
}
