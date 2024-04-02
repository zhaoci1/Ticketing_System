package com.jiawa.train.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.MemberLoginResp;
import com.jiawa.train.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class MemberIntercceptor implements HandlerInterceptor {
    private static final Logger Log = LoggerFactory.getLogger(MemberIntercceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Log.info("MemberIntercceptor开始");
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            Log.info("获取会员登录：{}", token);
            JSONObject jsonObject = JwtUtil.getJSONObject(token);
            Log.info("当前登录会员：{}", jsonObject);
//            将获取的token转成member
            MemberLoginResp member = JSONUtil.toBean(jsonObject, MemberLoginResp.class);
//            将member写入线程本地变量
            LoginMemberContext.setMember(member);
        }
        Log.info("MemberIntercceptor结束");
        return true;
    }
}
