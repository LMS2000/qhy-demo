package com.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class DaoTokenAuthenticationFilter extends AbstractTokenAuthenticationFilter{
 private final UserDetailsService userDetailsService;

    public DaoTokenAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean userStatusCheck(UserDetails userDetails) {
        return userDetails.isEnabled();
    }

    @Override
    protected UserDetails verifyToken(String token) {
        String username = jwt.validateToken(token);
        return userDetailsService.loadUserByUsername(username);
    }
}
