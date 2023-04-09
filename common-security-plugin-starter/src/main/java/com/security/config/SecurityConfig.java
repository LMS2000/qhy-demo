package com.security.config;

import cn.hutool.extra.spring.SpringUtil;
import com.security.client.AuthClient;
import com.security.loginFilter.UsernamePasswordLoginAuthFilter;
import com.security.verfiyTokenFilter.AbstractTokenAuthenticationFilter;
import com.security.verfiyTokenFilter.RpcTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

/**
 * 认证中心采用
 *
 * @author 大忽悠
 * @create 2023/2/9 11:23
 */
@Configuration
@EnableFeignClients(clients = AuthClient.class)
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityPluginProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final SecurityPluginProperties securityPluginProperties;

    /**
     * 需要放行的swagger相关请求
     */
    public static final List<String> SWAGGER_REQUEST_IGNORE = List.of("/css/**", "/fonts/**", "/images/**", "/js/**", "/webjars/**", "/doc.html#/**", "/swagger-resources", "/v3/**", "/doc.html", "/swagger-ui.html");

    @Override
    public void configure(WebSecurity web) throws Exception {
        //放行静态资源请求
        web.ignoring().antMatchers(SWAGGER_REQUEST_IGNORE.toArray(new String[]{}));
    }

    @Bean
    @ConditionalOnProperty(name = "login", prefix = "security-plugin", matchIfMissing = true, havingValue = "true")
    public ProviderManager providerManager(PasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    @Bean
    @ConditionalOnProperty(name = "login", prefix = "security-plugin", matchIfMissing = true, havingValue = "true")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "login", prefix = "security-plugin", matchIfMissing = true, havingValue = "true")
    public UsernamePasswordLoginAuthFilter usernamePasswordLoginAuthFilter(AuthenticationManager authenticationManager) {
        return new UsernamePasswordLoginAuthFilter(authenticationManager);
    }

    /**
     * token认证,搜集用户权限信息
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "tokenVerify", prefix = "security-plugin", matchIfMissing = true, havingValue = "true")
    public RpcTokenAuthenticationFilter rpcTokenAuthenticationFilter(UserDetailsService userDetailsService, AuthClient authClient) {
        return new RpcTokenAuthenticationFilter(userDetailsService, authClient);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (securityPluginProperties.getLogin()) {
            //添加自定义的Token登录认证过滤器
            http.addFilterAfter(SpringUtil.getBean(UsernamePasswordLoginAuthFilter.class), LogoutFilter.class);
        }
        //添加自定义的Token校验过滤器
        http.addFilterAfter(SpringUtil.getBean(AbstractTokenAuthenticationFilter.class), LogoutFilter.class);
        customMethodSecurityInterceptor(http);
        disAbleDefaultConfiguration(http);
        disableSessionRespiratory(http);
    }

    private void customMethodSecurityInterceptor(HttpSecurity http) {

    }

    private void disableSessionRespiratory(HttpSecurity http) throws Exception {
        //不要用session给我保存SecurityContext上下文,不然一次会话中,第一次用户携带了token,第二次没携带,仍然可以从上下文中获取到用户信息
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
