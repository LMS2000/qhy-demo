package com.security.verfiyTokenFilter;

import com.security.client.AuthClient;
import com.security.client.UserAuthorityDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

/**
 * 向认证中心发送请求获取权限
 * @author 大忽悠
 * @create 2023/2/14 12:34
 */
@Slf4j
public class RpcTokenAuthenticationFilter extends AbstractTokenAuthenticationFilter{
    private final UserDetailsService userDetailsService;
    private final AuthClient authClient;
    public static final String SEPARATOR ="_";

    public RpcTokenAuthenticationFilter(UserDetailsService userDetailsService, AuthClient authClient) {
        this.userDetailsService = userDetailsService;
        this.authClient = authClient;
    }

    @Override
    protected boolean userStatusCheck(UserDetails userDetails) {
        return userDetails.isEnabled();
    }


    @Override
    protected UserDetails verifyToken(String token) {
        String username = jwt.validateToken(token);
        if(username==null){
            return null;
        }
        //解析用户名和对应的服务名
        String[] strs = username.split(SEPARATOR);
        if(strs.length!=2){
            return null;
        }
        String serviceName=strs[0];
        String userRealName=strs[1];
        //查询数据库验证用户是否存在
        UserDetails userDetails = userDetailsService.loadUserByUsername(userRealName);
        if(userDetails==null){
            return null;
        }
        //发送请求
        try{
            UserAuthorityDto userAuthorityDto = authClient.getUserAuthorities(userRealName, serviceName);
            if(userAuthorityDto==null || !userAuthorityDto.getLegal()){
                return null;
            }
            //填充权限集合
            Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
            userAuthorityDto.getAuthorities().forEach(a->{
                authorities.add(new SimpleGrantedAuthority(a));
            });
            return userDetails;
        } catch (Exception e){
           log.error("访问认证中心获取用户权限出现异常: ",e);
        }
        return null;
    }
}
