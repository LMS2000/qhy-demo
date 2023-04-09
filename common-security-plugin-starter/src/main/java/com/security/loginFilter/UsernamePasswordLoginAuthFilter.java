package com.security.loginFilter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户名密码登录,登录成功后,返回用户信息
 * @author 大忽悠
 * @create 2023/2/9 13:16
 */
public class UsernamePasswordLoginAuthFilter extends TokenLoginAuthFilter {
    private final String USERNAME_PARAM="username";
    private final String PASSWORD_PARAM ="password";

    /**
     * 拿到的是全局的AuthenticationManager
     */
    public UsernamePasswordLoginAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter(USERNAME_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        username = (username != null) ? username : "";
        username = username.trim();
        password = (password != null) ? password : "";
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
