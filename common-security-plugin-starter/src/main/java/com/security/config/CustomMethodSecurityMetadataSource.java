package com.security.config;

import com.security.anno.IgnoreController;
import com.security.voter.CustomConfigAttribute;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;


/**
 * @author 大忽悠
 * @create 2023/2/14 18:50
 */
@Slf4j
public class CustomMethodSecurityMetadataSource implements MethodSecurityMetadataSource {

    /**
     * 方法上存在@RequestMapping注解,此时将方法名视为权限名</br>
     * 注意: 不要把swagger的controller控制器也拦截了 </br>
     * 该方法在初始化bean作为pointcut进行过滤,在方法执行时,调用该方法获取方法权限
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
        if (AnnotationUtils.findAnnotation(method, RequestMapping.class) != null
                && AnnotationUtils.findAnnotation(targetClass, Controller.class) != null
                && AnnotationUtils.findAnnotation(method, ApiOperation.class) != null
                //控制器类上添加了该注解,则不进行拦截
                && AnnotationUtils.findAnnotation(targetClass, IgnoreController.class) == null) {
            return List.of(new CustomConfigAttribute(method.getName()));
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
