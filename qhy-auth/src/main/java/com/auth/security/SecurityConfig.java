package com.auth.security;


import cn.hutool.extra.spring.SpringUtil;
import com.auth.constants.ManagerConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 需要放行的swagger相关请求
     */
    private final List<String> SWAGGER_REQUEST_IGNORE = List.of("/css/**", "/fonts/**", "/images/**", "/js/**", "/webjars/**", "/doc.html#/**", "/swagger-resources", "/v3/**", "/doc.html", "/swagger-ui.html");

    /**
     * 放行静态资源
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(SWAGGER_REQUEST_IGNORE.toArray(new String[]{}));
    }



    @Bean
    public ProviderManager providerManager(PasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UsernamePasswordLoginAuthFilter usernamePasswordLoginAuthFilter(AuthenticationManager authenticationManager){
        return new UsernamePasswordLoginAuthFilter(authenticationManager);
    }

    @Bean
    public DaoTokenAuthenticationFilter daoTokenAuthenticationFilter(UserDetailsService userDetailsService){
        return new DaoTokenAuthenticationFilter(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //不需要管理员身份认证的请求--认证中心暴露给外界微服务调用的接口
        http.authorizeRequests().antMatchers("/authentication/**").permitAll();
        //管理员请求只能由超级管理员才能操作--普通管理员没有操作认证中心的权限
        http.authorizeRequests().antMatchers("/manager/**","/operationLog/**").hasAnyRole(ManagerConstants.SUPER_MANAGER);
        //其他的操作普通管理员或者超级管理员才可以
        http.authorizeRequests().antMatchers("/**").hasAnyRole(ManagerConstants.COMMON_MANAGER,ManagerConstants.SUPER_MANAGER);
        //添加自定义的Token登录认证过滤器
        http.addFilterAfter(SpringUtil.getBean(UsernamePasswordLoginAuthFilter.class), LogoutFilter.class);
        //添加自定义的Token校验过滤器
        http.addFilterAfter(SpringUtil.getBean(DaoTokenAuthenticationFilter.class), LogoutFilter.class);
        disAbleDefaultConfiguration(http);
        disableSessionRespiratory(http);
    }


    private void disableSessionRespiratory(HttpSecurity http) throws Exception {
        //不用session去保存我的securitysession,不然第一次会话中用户带了token，而第二次没携带,仍然可以从上下文中获取到用户信息
        //下面配置本质是改变SecurityContextRepository实例化的子类,默认是HttpSessionSecurityContextRepository会负责保存上下文到session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void disAbleDefaultConfiguration(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable();
        http.formLogin().disable();
        http.removeConfigurer(LogoutConfigurer.class);
    }
}
