package com.user.controller;

import com.easyCode.feature.result.ResponseResultAdvice;
import com.user.entity.dto.UserDto;
import com.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@ResponseResultAdvice
@Api(tags = "用户请求")
public class UserController {
    private final IUserService iUserService;

    @GetMapping("/info")
    @ApiOperation("获取当前登录用户信息")
    public UserDto getCurrentUserInfo() {
        return iUserService.getCurrentUserInfo();
    }
}
