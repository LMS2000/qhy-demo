package com.security.config;

import com.security.voter.CustomVoter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 跳过对GlobalMethodSecurityConfiguration的默认自动注入,我们这里需要重写customMethodSecurityMetadataSource方法</br>
 * 注册一个单独的MethodSecurityMetadataSource,注解加载继承至GlobalMethodSecurityConfiguration的配置上,就会跳过对</br>
 * GlobalMethodSecurityConfiguration的自动配置,源码在GlobalMethodSecuritySelector中 </br>
 * 灵感来源: 深入浅出spring security第13章
 * @author 大忽悠
 * @create 2023/2/14 18:47
 */
@EnableGlobalMethodSecurity
@Configuration
public class CustomGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return new CustomMethodSecurityMetadataSource();
    }

    @SneakyThrows
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        AccessDecisionManager accessDecisionManager = super.accessDecisionManager();
        Field decisionVoters = AbstractAccessDecisionManager.class.getDeclaredField("decisionVoters");
        decisionVoters.setAccessible(true);
        List<AccessDecisionVoter<?>> accessDecisionVoters = (List<AccessDecisionVoter<?>>) decisionVoters.get(accessDecisionManager);
        accessDecisionVoters.add(new CustomVoter());
        return accessDecisionManager;
    }
}
