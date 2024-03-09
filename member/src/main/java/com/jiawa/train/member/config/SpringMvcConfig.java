package com.jiawa.train.member.config;

import com.jiawa.train.common.interceptor.Loginterceptor;
import com.jiawa.train.common.interceptor.MemberIntercceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Resource
    MemberIntercceptor memberIntercceptor;
    @Resource
    Loginterceptor loginterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginterceptor);

        registry.addInterceptor(memberIntercceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/member/hello",
                        "/member/member/sendCode",
                        "/member/member/login"
                );
    }
}
