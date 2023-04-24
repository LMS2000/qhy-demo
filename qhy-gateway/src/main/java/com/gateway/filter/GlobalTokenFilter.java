package com.gateway.filter;

import com.jwtAutoConfiguration.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 全局token认证过滤器
 *
 * @author 大忽悠
 * @create 2023/2/15 9:14
 */
@Component
@Order(-1)
@RequiredArgsConstructor
public class GlobalTokenFilter implements GlobalFilter {
    private final Jwt jwt;
    /**
     * 放行swagger相关请求
     */
    public static final List<String> SWAGGER_REQUEST_IGNORE = List.of("/**/css/**", "/**/fonts/**", "/**/images/**",
            "/**/js/**", "/**/webjars/**", "/**/doc.html#/**", "/**/swagger-resources", "/**/v3/**", "/**/doc.html", "/**/swagger-ui.html");
    private AntPathMatcher antPathMatcher=new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(ignoreRequest(exchange.getRequest())){
            return chain.filter(exchange);
        }
        //token验证
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        List<String> tokens = httpHeaders.get("token");
        //token认证成功
        if (tokens != null && tokens.size() == 1 && jwt.validateToken(tokens.get(0)) != null) {
            return chain.filter(exchange);
        }
        //拦截请求
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        //结束请求
        return exchange.getResponse().setComplete();
    }

    /**
     * 放行登录请求和swagger相关请求
     */
    private boolean ignoreRequest(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        for (String ignoreRequest : SWAGGER_REQUEST_IGNORE) {
                if(antPathMatcher.match(ignoreRequest,path)){
                    return true;
                }
        }
        return antPathMatcher.match("/**/login/**",path);
    }
}
