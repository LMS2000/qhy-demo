package com.security.verfiyTokenFilter;


import com.jwtAutoConfiguration.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 负责拦截除登录外的请求,校验token合法性,在token合法的情况下,生成Authentication<br>
 * OncePerRequestFilter是在一次外部请求中只过滤一次。对于服务器内部之间的forward等请求，不会再次执行过滤方法。
 * @author 大忽悠
 * @create 2022/10/7 18:53
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractTokenAuthenticationFilter extends OncePerRequestFilter {
    /**
     * 无需token验证的请求前缀
     */
    public static final String NO_TOKEN_AUTH_REQUEST_PREFIX="/noTokenAuth/";
    /**
     * 无需token校验的请求集合
     */
    protected final Set<String> ignore_request_patterns =new HashSet<>();
    /**
     * ant分隔路径匹配器
     */
    protected final AntPathMatcher antPathMatcher=new AntPathMatcher();
    /**
     * 请求路径处理器
     */
    protected final UrlPathHelper urlPathHelper=new UrlPathHelper();
    /**
     * jwt
     */
    @Resource
    protected Jwt jwt;

    /**
     * 1.放行无需token验证的相关请求
     * @throws ServletException
     */
    @Override
    protected void initFilterBean() throws ServletException {
        ignore_request_patterns.add("/**/login");
        ignore_request_patterns.add("/**"+NO_TOKEN_AUTH_REQUEST_PREFIX+"**");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
            return;
        }

        if (!attemptAuthentication(request,response)) {
            //默认为匿名访问--但是如果对应的资源需要权限才能访问,那么会403
            authenticationFailure(request,response,chain);
            SecurityContextHolder.clearContext();
            return;
        }

        beforeSuccessAuthInvoke(request,response);
        chain.doFilter(request,response);
        afterSuccessAuthInvoke(request,response);
        SecurityContextHolder.clearContext();
    }

    /**
     * 认证成功,当前请求还未被处理,进行前置处理
     * @param request
     * @param response
     */
    protected void beforeSuccessAuthInvoke(HttpServletRequest request, HttpServletResponse response){};

    /**
     * 认证成功,并且当前请求处理完后,进行后置处理
     * @param request
     * @param response
     */
    protected void afterSuccessAuthInvoke(HttpServletRequest request, HttpServletResponse response){};


    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String url = urlPathHelper.getPathWithinApplication(request);
        for (String ignoreReqPattern : ignore_request_patterns) {
                if(antPathMatcher.match(ignoreReqPattern,url)){
                    return false;
                }
        }
        return true;
    }

    /**
     * 如果认证成功,则设置认证成功的标记,否则不设置
     * @return false表示认证失败,true表示认证成功
     */
    protected boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(jwt.getTokenOfRequestHeaderName());
        if(token==null){
            //TODO: 方便测试
            token=request.getParameter("token");
            if(token==null){
                return false;
            }
        }
        UserDetails userDetails = verifyToken(token);
        if(userDetails==null || !userStatusCheck(userDetails)){
            return false;
        }
        authenticationSuccess(request,response,userDetails,token);
        return true;
    }

    /**
     * 对当前用户状态进行检查,如果状态异常,返回false,否则返回true
     * @param userDetails
     * @return
     */
    protected abstract boolean userStatusCheck(UserDetails userDetails);

    /**
     * @param token 待验证令牌
     * @return 通过解析token来获取当前用户详细信息
     */
    protected abstract UserDetails verifyToken(String token);

    /**
     * 认证失败,子类可以重写,决定是否采取何种措施,默认过滤器链继续往后执行,此时为匿名访问
     * @param request
     * @param response
     * @param chain
     */
    private void authenticationFailure(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(request,response);
    }

    protected void authenticationSuccess(HttpServletRequest request, HttpServletResponse response,UserDetails userDetails,String token) {
        //给使用该JWT令牌的用户进行授权
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        //放入spring security的上下文环境中，表示认证通过
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //刷新token
        response.setHeader(jwt.getRefreshTokenOfResponseHeaderName(),jwt.refreshToken(token));
    }

    /**
     * 新增无需token验证的请求
     * @param request
     */
    public void addIgnoreRequest(String request){
        ignore_request_patterns.add(request);
    }
}
