package com.security.loginFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtAutoConfiguration.Jwt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 拦截登录请求,完成登录认证,认证成功,返回token
 * @author 大忽悠
 * @create 2023/2/9 13:08
 */
@RequiredArgsConstructor
@Slf4j
public abstract class TokenLoginAuthFilter extends GenericFilterBean {
    private final RequestMatcher requestMatcher=new AntPathRequestMatcher("/**/login");
    private final ObjectMapper objectMapper=new ObjectMapper();
    protected final AuthenticationManager authenticationManager;
    /**
     * jwt
     */
    @Resource
    protected Jwt jwt;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
          //1.只拦截登录请求
          if(!requiresAuthentication(request, response)){
              chain.doFilter(request,response);
              return;
          }
         //2.进行验证
        try {
            Authentication authenticationResult = attemptAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
            successfulAuthentication((HttpServletRequest) request, (HttpServletResponse) response, chain, authenticationResult);
        } catch (Exception failed) {
            this.logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication((HttpServletRequest) request, (HttpServletResponse) response, failed);
        }
    }

    /**
     * 告诉用户登录失败
     */
    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        writeRes(response,500,"登录认证失败: "+ ex.getMessage());
    }

    private void writeRes(HttpServletResponse response,Integer code, String msg) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(objectMapper.writeValueAsString(new Res(msg)));
    }

    /**
     * 验证成功,利用用户信息生成token,然后返回给用户
     */
    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse, FilterChain chain, Authentication authenticationResult) throws IOException {
        try {
            UserDetails details = (UserDetails) authenticationResult.getPrincipal();
            if(details.getUsername()==null){
                writeRes(httpServletResponse,500,"登录认证失败");
            }
            writeRes(httpServletResponse,200,jwt.generateToken(details.getUsername()));
        }catch (Exception e){
              writeRes(httpServletResponse,500,"登录认证失败");
              e.printStackTrace();
        }
    }

    /**
     * 具体是采用用户名密码登录,还是微信认证登录,还是其他登录方式由子类决定
     */
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse){
        return authenticationManager.authenticate(getAuthentication(request,httpServletResponse));
    }

    protected abstract Authentication getAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse);

    private boolean requiresAuthentication(ServletRequest request, ServletResponse response) {
       return requestMatcher.matches((HttpServletRequest) request);
    }

    @AllArgsConstructor
    @Data
    public static class Res implements Serializable {
        private String data;
    }
}
