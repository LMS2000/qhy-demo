package com.course.client;

import com.course.filter.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 大忽悠
 * @create 2023/2/15 12:19
 */
@FeignClient("qhy-manager")
public interface ManagerClient {

    /**
     * 返回管理员ID,如果不存在当前管理员,返回null
     */
    @GetMapping("/manager/sdk/getIdByName")
    VisitDto loadByName(@RequestParam("name") String userRealName);
}

