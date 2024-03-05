package com.jiawa.train.common.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final Logger Log = LoggerFactory.getLogger(JwtUtil.class);

    //    盐值
    private static final String key = "Jiawa12306";

    public static String createToken(Long id, String mobile) {
        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.HOUR, 24);
        Map<String, Object> payload = new HashMap<>();
//        签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
//        过期时间
        payload.put(JWTPayload.EXPIRES_AT, expTime);
//        生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);

//        内容
        payload.put("id", id);
        payload.put("mobile", mobile);
        String token = JWTUtil.createToken(payload, key.getBytes());
        Log.info("生成Token：{}", token);
        return token;
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public static boolean validate(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        boolean validate = jwt.validate(0);
        Log.info("JWT token校验结果：{}", validate);
        return validate;
    }

    /**
     * 反编译获取token的信息
     * @param token
     * @return
     */
    public static JSONObject getJSONObject(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();
//        移除时间信息
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);
        Log.info("根据token获取原始内容：{}", payloads);
        return payloads;
    }
}
