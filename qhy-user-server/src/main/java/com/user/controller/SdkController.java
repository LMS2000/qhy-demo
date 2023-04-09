package com.user.controller;

import com.security.anno.IgnoreController;
import com.user.entity.dto.SdkUserInfoDto;
import com.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 供外部服务调用获取管理员信息的接口
 *
 * @author 大忽悠
 * @create 2023/2/15 12:45
 */
@Validated
@RestController
@RequestMapping("/sdk")
@RequiredArgsConstructor
@Api("SDK请求")
@IgnoreController
public class SdkController {
    private final IUserService iUserService;

    @ApiOperation("返回用户ID,如果不存在当前用户,返回null")
    @GetMapping("/getIdByName")
    public SdkUserInfoDto loadByName(@RequestParam("name") String userRealName) {
        return iUserService.loadByName(userRealName);
    }
}
