package com.jwtAutoConfiguration;

import io.jsonwebtoken.Claims;

import java.util.Map;

/**
 * @author lms2000
 * @create 2022/9/23 20:48
 */
public interface Jwt {
    /**
     * 根据用户名生成令牌
     * @param name 用户名
     * @return 令牌
     */
    public String generateToken(String name);

    /**
     * 从claims生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    String generateToken(Map<String, Object> claims);

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    String getUsernameFromToken(String token);

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    Boolean isTokenExpired(String token);

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    String refreshToken(String token);

    /**
     * 验证令牌
     * @return 用户名
     */
    String validateToken(String token);


    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
     Claims getClaimsFromToken(String token);

    /**
     * @return token在请求头中的名字
     */
     String getTokenOfRequestHeaderName();

    /**
     * @return refreshToken在响应头中的名字
     */
     String getRefreshTokenOfResponseHeaderName();
}
