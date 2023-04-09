package com.security.voter;


import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @author 大忽悠
 * @create 2023/2/14 22:59
 */
public class CustomVoter implements AccessDecisionVoter<Object> {
    private static final AntPathMatcher antPathMatcher=new AntPathMatcher();

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof CustomConfigAttribute;
    }

    @Override
    public int vote(Authentication authentication, Object object,Collection<ConfigAttribute> attributes) {
        Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
        String resourceAuthority = ((List<ConfigAttribute>) attributes).get(0).getAttribute();
        for (GrantedAuthority userAuthority : userAuthorities) {
                 if(antPathMatcher.match(userAuthority.getAuthority(),resourceAuthority)){
                      return ACCESS_GRANTED;
                 }
        }
        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
