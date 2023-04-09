package com.auth.controller;

import com.auth.aop.OpLog;
import com.auth.convert.StrToListFormatter;
import com.auth.entity.dto.AuthorityDto;
import com.auth.service.IRoleAuthorityService;
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
@RequestMapping("/roleAuthority")
@RequiredArgsConstructor
@ResponseResultAdvice
@Api(tags = "角色权限请求")
public class RoleAuthorityController {
    private final IRoleAuthorityService iRoleAuthorityService;

    @InitBinder
    public void initBinder(DataBinder dataBinder){
        dataBinder.addCustomFormatter(new StrToListFormatter());
    }

    @ApiOperation("获取角色权限")
    @PostMapping("/{roleId}")
    public List<AuthorityDto> getAuthorityOfRole(@PathVariable("roleId")@Positive(message = "id不合法")Integer roleId){
        return iRoleAuthorityService.getAuthorityOfRole(roleId);
    }

    @OpLog(desc = "权限%s赋予ID为%s的角色")
    @ApiOperation("赋予角色权限")
    @PostMapping("/releaseAuthorityToRole/{roleId}")
    public void releaseAuthorityToRole(@RequestParam("authorityList")@ApiParam("权限id列表,由'-'分割权限id,如: 1-2-3") List<Integer> authorityList,
                                       @PathVariable("roleId")@Positive(message = "id不合法")Integer roleId) {
        iRoleAuthorityService.releaseAuthorityToRole(roleId,authorityList);
    }

    @OpLog(desc = "权限%s从ID为%s的角色处回收")
    @ApiOperation("收回角色权限")
    @GetMapping("/revokeAuthorityFromRole/{roleId}")
    public void revokeAuthorityFromRole(@RequestParam("authorityList")@ApiParam("权限id列表,由'-'分割权限id,如: 1-2-3") List<Integer> authorityList, @PathVariable("roleId")@Positive(message = "id不合法")Integer roleId) {
        iRoleAuthorityService.revokeAuthorityFromRole(roleId,authorityList);
    }
}
