package com.auth.controller;

import com.auth.aop.OpLog;
import com.auth.entity.dto.UserDto;
import com.auth.entity.vo.UserVo;
import com.auth.service.IUserService;
import com.easyCode.feature.mybaits.CustomPage;
import com.easyCode.feature.result.ResponseResultAdvice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@ResponseResultAdvice
@Api(tags = "用户请求")
public class UserController {
    private final IUserService iUserService;

    @ApiOperation("新增用户")
    @PostMapping("/save")
    public Integer saveUser(@Valid UserVo userVo) {
        return iUserService.saveUser(userVo);
    }

    @ApiOperation("根据id查询用户")
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id")@Positive(message = "id不合法") Integer id) {
        return iUserService.getUserById(id);
    }

    @OpLog(desc = "启用用户,用户ID为: %s")
    @ApiOperation("启用用户")
    @PostMapping("/enable/{id}")
    public void enableUser(@PathVariable("id") Integer id){
        iUserService.enableUser(id);
    }

    @OpLog(desc = "禁用用户,用户ID为: %s")
    @ApiOperation("禁用用户")
    @PostMapping("/disable/{id}")
    public void disableUser(@PathVariable("id") Integer id){
        iUserService.disableUser(id);
    }

    @ApiOperation("分页批量查询用户")
    @GetMapping("/list")
    public List<UserDto> listUser(CustomPage customPage) {
        return iUserService.listUser(customPage);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public void delUser(@PathVariable("id")@Positive(message = "id不合法") Integer id) {
        iUserService.delUserById(id);
    }
}
