package com.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 大忽悠
 * @create 2023/2/15 12:38
 */
@ConfigurationProperties(prefix = "security-plugin")
@Data
public class SecurityPluginProperties {
    /**
     * 是否开启登录拦截器的配置
     */
    private Boolean login = Boolean.TRUE;
    /**
     * 是否开启token认证拦截器的配置
     */
    private Boolean tokenVerify = Boolean.TRUE;
}
