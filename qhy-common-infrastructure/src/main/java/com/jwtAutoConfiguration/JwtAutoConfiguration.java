package com.jwtAutoConfiguration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "jwt", name = "open", havingValue = "true")
public class JwtAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public Jwt jwt(JwtProperties jwtProperties){
       return new JwtImpl(jwtProperties);
    }
}
