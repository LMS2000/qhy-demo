package com.course.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


/**
 * @author 大忽悠
 * @create 2023/2/8 22:30
 */
public class MybaitsUtils {

    public static <T> boolean existCheck(IService<T> iService, Map<String,Object> examples){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        examples.forEach(queryWrapper::eq);
        return iService.count(queryWrapper)>0;
    }
}
