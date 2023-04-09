package com.security.anno;

import java.lang.annotation.*;

/**
 * 忽略对该控制器权限的自动注册,忽略对该控制器的拦截
 * @author 大忽悠
 * @create 2023/2/15 12:46
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreController {
}
