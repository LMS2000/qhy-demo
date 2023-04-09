package com.course.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 大忽悠
 * @create 2023/2/18 9:47
 */
@FeignClient("qhy-manager")
public interface CampusClient {
    @GetMapping("/manager/sdk/existCampus")
    Boolean exist(@RequestParam("campusId")Integer campusId);
}
