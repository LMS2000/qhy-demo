package com.course.client;

import com.course.filter.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 大忽悠
 * @create 2023/2/15 12:20
 */
@FeignClient("qhy-user")
public interface UserClient {
    /**
     * 返回用户ID,如果不存在当前用户,返回null
     */
    @GetMapping("/user/sdk/getIdByName")
    VisitDto loadByName(@RequestParam("name") String userRealName);
}
