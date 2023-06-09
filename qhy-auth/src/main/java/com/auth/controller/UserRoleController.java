package com.auth.controller;

import com.auth.aop.OpLog;
import com.auth.convert.StrToListFormatter;
import com.auth.entity.dto.RoleDto;
import com.auth.service.IUserRoleService;
import com.easyCode.feature.result.ResponseResultAdvice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/userRole")
@RequiredArgsConstructor
@ResponseResultAdvice
@Api(tags = "用户角色请求")
public class UserRoleController {
    private final IUserRoleService iUserRoleService;

    @InitBinder
    public void initBinder(DataBinder dataBinder){
        dataBinder.addCustomFormatter(new StrToListFormatter());
    }

    @ApiOperation("获取用户角色")
    @PostMapping("/{userId}")
    public List<RoleDto> getRoleOfUser(@PathVariable("userId")@Positive(message = "id不合法")Integer userId){
        return iUserRoleService.getRoleOfUser(userId);
    }

    @OpLog(desc ="角色%s赋予ID为%s的用户")
    @ApiOperation("赋予用户角色")
    @PostMapping("/releaseRoleToUser/{userId}")
    public void releaseRoleToUser(@RequestParam("roleList")@ApiParam("角色id列表,由'-'分割角色id,如: 1-2-3")List<Integer> authorityList, @PathVariable("userId")@Positive(message = "id不合法")Integer userId) {
        iUserRoleService.releaseRoleToUser(userId,authorityList);
    }

    @OpLog(desc = "角色%s从ID为%s的用户处回收")
    @ApiOperation("收回用户角色")
    @GetMapping("/revokeRoleFromUser/{userId}")
    public void revokeRoleFromUser(@RequestParam("roleList")@ApiParam("角色id列表,由'-'分割角色id,如: 1-2-3") List<Integer> authorityList, @PathVariable("userId")@Positive(message = "id不合法")Integer userId) {
        iUserRoleService.revokeRoleFromUser(userId,authorityList);
    }
}
