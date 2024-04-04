package com.jiawa.train.gateway.config;

import com.jiawa.train.gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginMemberFilter implements Ordered, GlobalFilter {
    private static final Logger Log = LoggerFactory.getLogger(TestFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

//        排除不需要拦截的请求
        if (path.contains("/admin")
                || path.contains("/hello")
                || path.contains("/redis")
                || path.contains("/member/member/login")
                || path.contains("/member/member/sendCode")
                || path.contains("/business/kaptcha")
        ) {
            Log.info("不需要登录验证：{}", path);
            return chain.filter(exchange);
        }

//        获取token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        Log.info("会员登录验证开始：{}", token);
        if (token == null || token.isEmpty()) {
            Log.info("token为空");
//            发送返回码401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            中断请求
            return exchange.getResponse().setComplete();
        }
//        检验token是否可用
        boolean validate = JwtUtil.validate(token);
        if(validate){
            Log.info("token有效，放行请求");
            return chain.filter(exchange);
        }else{
            Log.info("token无效，拦截请求");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
