package com.course.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 大忽悠
 * @create 2023/2/18 9:47
 */
@FeignClient("qhy-manager")
public interface SubCampusClient {
    /**
     * 查询当前主校区下是否存在对应的分校区
     */
    @GetMapping("/manager/sdk/existSubCampusUnderCampus")
    Boolean existSubCampusUnderCampus(@RequestParam("campusId")Integer campusId,@RequestParam("subCampusId")Integer subCampusId);
    @GetMapping("/manager/sdk/existSubCampus")
    Boolean exist(@RequestParam("campusId")Integer campusId);
}
