package com.security.voter;

import org.springframework.security.access.ConfigAttribute;

/**
 * @author 大忽悠
 * @create 2023/2/14 23:00
 */
public class CustomConfigAttribute implements ConfigAttribute {
    private String authority;

    public CustomConfigAttribute(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAttribute() {
        return authority;
    }
}
