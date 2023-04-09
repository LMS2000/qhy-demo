package com.auth.controller;

import com.auth.assembler.UserAssembler;
import com.auth.entity.dto.UserAuthorityDto;
import com.auth.entity.vo.AuthorityVo;
import com.auth.service.IAuthorityService;
import com.auth.service.IUserService;
import com.easyCode.feature.result.ResponseResultAdvice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 大忽悠
 * @create 2023/2/9 19:46
 */
@Validated
@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Api(tags = "认证接口")
public class AuthenticationController {
    private final UserAssembler userAssembler;
    private final IAuthorityService iAuthorityService;
    private final IUserService iUserService;

    @ApiOperation("获取用户权限信息")
    @PostMapping("/getAuthorties")
    public UserAuthorityDto getUserAuthorities(@RequestParam("username") String username, @RequestParam("serviceName") String serviceName){
        return userAssembler.getUserAuthorities(username,serviceName);
    }

    /**
     * 方便自动化注册权限,生产环境关闭该接口
     */
    @ApiOperation("注册权限")
    @PostMapping("/registerAuthorities")
    public void registerAuthorities(@RequestParam("name") String name, @RequestParam("desc") String desc){
        iAuthorityService.saveAuthority(AuthorityVo.builder().name(name).description(desc).build());
    }

    @ApiOperation("注册用户")
    @PostMapping("/register")
    public void registerUser(@RequestParam("username") String username, @RequestParam("serviceName") String serviceName,
                             @RequestParam("role") String role){
        iUserService.registerUser(username,serviceName,role);
    }

    @ApiOperation("注销用户")
    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam("username")  String username, @RequestParam("serviceName") String serviceName){
        iUserService.deleteUser(username,serviceName);
    }
}
