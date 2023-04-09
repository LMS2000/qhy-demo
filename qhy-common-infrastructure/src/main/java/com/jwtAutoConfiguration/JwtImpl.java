package com.jwtAutoConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.digester.Digester;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Slf4j
public class JwtImpl implements Jwt {
    private String secret;
    private Long expiration;
    private String tokenOfRequestHeaderName;
    private String refreshTokenOfResponseHeaderName;
    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    static {
        sdf.applyPattern("yyyy年MM月dd日 HH时mm分ss秒");
    }

    public JwtImpl(JwtProperties jwtProperties) {
        this.secret = jwtProperties.getSecret();
        this.expiration = jwtProperties.getExpiration();
        this.tokenOfRequestHeaderName = jwtProperties.getTokenOfRequestHeaderName();
        this.refreshTokenOfResponseHeaderName = jwtProperties.getRefreshTokenOfResponseHeaderName();

        if (secret == null || "".equals(secret.trim())) {
            secret = UUID.randomUUID().toString();
        }

        if (expiration == null || expiration <= 0L) {
            expiration = 30L;
        }

        if (tokenOfRequestHeaderName == null || "".equals(tokenOfRequestHeaderName.trim())) {
            tokenOfRequestHeaderName = "token";
        }

        if (refreshTokenOfResponseHeaderName == null || "".equals(refreshTokenOfResponseHeaderName.trim())) {
            refreshTokenOfResponseHeaderName = "refreshToken";
        }
        log.info("jwt已启用,秘钥为: {}, 过期时间为: {}分钟, token在请求头中的名字为: {}, refreshToken在响应头中的名字为: {}",
                secret, expiration, tokenOfRequestHeaderName, refreshTokenOfResponseHeaderName);
    }


    @Override
    public String generateToken(String name) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", name);
        claims.put("created", new Date());
        return generateToken(claims);
    }

    @Override
    public String generateToken(Map<String, Object> claims) {
        Date expireDate = new Date(System.currentTimeMillis() + Duration.ofMinutes(expiration).toMillis());
        return Jwts.builder().setClaims(claims)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 根据token获取username
     *
     * @param token 令牌
     * @return
     */
    @Override
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断token是否过期
     *
     * @param token 令牌
     * @return
     */
    @Override
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            log.info("令牌过期时间为：{}", sdf.format(expiration));
            log.info("令牌过期时间剩余为：{}", (expiration.getTime() - now.getTime()) / 6000 + "分钟");
            return expiration.before(now);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新token
     * @param token 原令牌
     * @return 新令牌
     */
    @Override
    public String refreshToken(String token) {
        String newToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.setExpiration(new Date());
            newToken = generateToken(claims);
            return newToken;
        } catch (Exception e) {
            newToken = null;
        }
        return newToken;
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    @Override
    public String validateToken(String token) {
        if (isTokenExpired(token)) {
            return null;
        }
        return getUsernameFromToken(token);
    }

    /**
     * 从token中获取数据声明
     * @param token 令牌
     * @return
     */
    @Override
    public Claims getClaimsFromToken(String token) {
        Claims claims=null;
        try{
           claims =  Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
        }catch (Exception e){
          log.error("解析token失败：{}",token);
        }
        return claims;
    }

    /**
     * token请求头的名字
     * @return
     */
    @Override
    public String getTokenOfRequestHeaderName() {
        return tokenOfRequestHeaderName;
    }

    /**
     *
     * @return
     */
    @Override
    public String getRefreshTokenOfResponseHeaderName() {
        return refreshTokenOfResponseHeaderName;
    }
}
