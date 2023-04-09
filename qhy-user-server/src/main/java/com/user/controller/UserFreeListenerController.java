package com.user.controller;


import com.security.anno.IgnoreController;
import com.user.entity.vo.UserFreeListenerVo;
import com.user.service.IUserFreeListenerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

@RestController
@Api(tags = "用户试听")
@RequiredArgsConstructor
@IgnoreController
@Validated
@RequestMapping("/public")
public class UserFreeListenerController {

    private final IUserFreeListenerService iUserFreeListenerService;

    @ApiOperation("免费试听用户注册")
    @PostMapping("/freeListenerRegister")
    public int freeListenerRegister(@RequestParam("phoneNumber")@Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$",message = "手机号码格式错误") String phoneNumber,
                                    @RequestParam(value = "info", required = false) String info) {
        return iUserFreeListenerService.saveUserFreeListener(UserFreeListenerVo.builder().phoneNumber(phoneNumber).info(info).build());
    }
}
