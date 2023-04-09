package com.course.filter;

import com.course.client.ManagerClient;
import com.course.client.UserClient;
import com.security.client.AuthClient;
import com.security.client.UserAuthorityDto;
import com.security.verfiyTokenFilter.AbstractTokenAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.course.constants.ServiceConstants.MANAGER_SERVICE_NAME;
import static com.course.constants.ServiceConstants.USER_SERVICE_NAME;

/**
 * 通过远程访问用户微服务,获取访问者信息,通过远程访问认证中心,获取访问器权限信息
 * @author 大忽悠
 * @create 2023/2/15 12:17
 */
@Slf4j
@Component
public class CourseRpcTokenAuthFilter extends AbstractTokenAuthenticationFilter {
    private final ManagerClient managerClient;
    private final UserClient userClient;
    private final AuthClient authClient;
    public static final String SEPARATOR ="_";

    public CourseRpcTokenAuthFilter(ManagerClient managerClient, UserClient userClient, AuthClient authClient) {
        this.managerClient = managerClient;
        this.userClient = userClient;
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
        String serviceName=strs[0];
        String userRealName=strs[1];
        //构建访问者信息
        VisitDto visitDto = VisitDto.builder().visitName(userRealName).serviceName(serviceName).build();
        VisitDto visitId = queryVisitId(serviceName, userRealName);
        if(visitId==null){
            return null;
        }
        visitDto.copyRpcInfo(visitId);
        //发送请求
        try{
            UserAuthorityDto userAuthorityDto = authClient.getUserAuthorities(userRealName, serviceName);
            if(userAuthorityDto==null || !userAuthorityDto.getLegal()){
                return null;
            }
            //填充权限集合
            Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) visitDto.getAuthorities();
            userAuthorityDto.getAuthorities().forEach(a->{
                authorities.add(new SimpleGrantedAuthority(a));
            });
            return visitDto;
        } catch (Exception e){
            log.error("访问认证中心获取用户权限出现异常: ",e);
        }
        return null;
    }

    private VisitDto queryVisitId(String serviceName, String userRealName) {
        try{
            //管理员访问
            if(serviceName.equals(MANAGER_SERVICE_NAME)){
                return managerClient.loadByName(userRealName);
            }else if(serviceName.equals(USER_SERVICE_NAME)){
                //用户访问
                return userClient.loadByName(userRealName);
            }else {
                //非法访问
                return null;
            }
        } catch (Exception e){
           log.error("远程获取访问者ID过程中出现异常: ",e);
        }
        return null;
    }
}
