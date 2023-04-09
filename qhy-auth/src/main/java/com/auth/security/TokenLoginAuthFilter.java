package com.auth.security;

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
 * 登录拦截器父类
 */
@RequiredArgsConstructor
@Slf4j
public abstract class TokenLoginAuthFilter extends GenericFilterBean {

    private final RequestMatcher requestMatcher=new AntPathRequestMatcher("/login");
    private final ObjectMapper objectMapper=new ObjectMapper();
    protected final AuthenticationManager authenticationManager;

    /**
     * jwt
     */
    @Resource
    protected Jwt jwt;

    /**
     * 处理请求
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //只拦截登录的请求
     if(!requiresAuthentication(request,response)){
         chain.doFilter(request,response);
         return;
     }
      //然后校验登录
       try{
           Authentication authentication = attemptAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
           successfulAuthentication((HttpServletRequest) request, (HttpServletResponse) response,chain,authentication);
       }catch (Exception e){
           logger.error("An internal error occurred while trying to authenticate the user.",e);
          unsuccessfulAuthentication((HttpServletRequest) request, (HttpServletResponse) response,e);
       }

    }


    /**
     * 处理认证
     * @param request
     * @param httpServletResponse
     * @return
     */
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) {
          return authenticationManager.authenticate(getAuthentication(request,httpServletResponse));
    }

    protected abstract Authentication getAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse);

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        writeRes(response,500,"登录认证失败: "+ ex.getMessage());
    }

    /**
     * 登录成功之后返回token
     * @param request
     * @param httpServletResponse
     * @param chain
     * @param authenticationResult
     * @throws IOException
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
         * 判断是否拦截
         * @param request
         * @param response
         * @return
         */
    public boolean requiresAuthentication(ServletRequest request, ServletResponse response){
        return requestMatcher.matches((HttpServletRequest) request);
    }

    /**
     * 写入响应数据
     * @param response
     * @param code
     * @param msg
     * @throws IOException
     */
    private void writeRes(HttpServletResponse response,Integer code, String msg) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(objectMapper.writeValueAsString(new Res(msg)));
    }
        /**
         * 校验结果的响应数据
         */
    @AllArgsConstructor
    @Data
    public static class Res implements Serializable {
        private String data;
    }
}
