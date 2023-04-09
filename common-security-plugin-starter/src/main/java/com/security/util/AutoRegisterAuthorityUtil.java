package com.security.util;

import cn.hutool.extra.spring.SpringUtil;
import com.security.anno.IgnoreController;
import com.security.client.AuthClient;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * 自动扫描容器中所有控制器实现类---标注了@Controller的类</br>
 * 搜集类中所有标注了@RequestMapping注解和@ApiOperation注解的方法</br>
 * 忽略标注了@IgnoreController注解的类
 *
 * @author 大忽悠
 * @create 2023/2/14 19:10
 */
@Slf4j
@Configuration
public class AutoRegisterAuthorityUtil implements ApplicationContextAware {
    private static ApplicationContext app;
    public static void scanThenRegister() {
        HashSet<AuthorityInfo> authorityInfoList = new HashSet<>();
        scanTargetMethods(authorityInfoList);
        registerToAuthCenter(authorityInfoList);
    }

    private static void registerToAuthCenter(HashSet<AuthorityInfo> authorityInfoList) {
        AuthClient authClient = SpringUtil.getBean(AuthClient.class);
        //记录注册失败的权限信息
        List<AuthorityInfo> registerFailed=new ArrayList<>();
        authorityInfoList.forEach(authorityInfo -> {
            try{
               authClient.registerAuthorities(authorityInfo.getName(),authorityInfo.getDesc());
            } catch (Exception e){
                log.error("自动注册权限信息过程中出现错误: ",e);
                registerFailed.add(authorityInfo);
            }
        });
        if(!registerFailed.isEmpty()){
            log.warn("权限信息自动注册过程中存在未能成功注册的权限列表个数由: {},具体如下: {}",registerFailed.size(),registerFailed);
        }
    }

    private static void scanTargetMethods(HashSet<AuthorityInfo> authorityInfoList) {
        String[] beans = app.getBeanNamesForAnnotation(Controller.class);
        for (String bean : beans) {
            Object controllerBean = app.getBean(bean);
            //忽略类上标注了@IgnoreController注解的类
            if(AnnotationUtils.findAnnotation(controllerBean.getClass(), IgnoreController.class)!=null){
                continue;
            }
            ReflectionUtils.doWithMethods(controllerBean.getClass(),
                    //将符号添加的方法添加进集合
                    method -> {
                        authorityInfoList.add(AuthorityInfo.builder().name(method.getName())
                                .desc(Objects.requireNonNull(AnnotationUtils.findAnnotation(method, ApiOperation.class)).value()).build());
                    },
                    //过滤掉不符合条件的方法
                    method -> AnnotationUtils.findAnnotation(method, RequestMapping.class) != null
                            && AnnotationUtils.findAnnotation(method, ApiOperation.class) != null);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        app=applicationContext;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class AuthorityInfo {
        private String name;
        private String desc;

        @Override
        public String toString(){
            return "权限名为: "+name+" ,权限描述信息为: "+desc;
        }
    }
}
