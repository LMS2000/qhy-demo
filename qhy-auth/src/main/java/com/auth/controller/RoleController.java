package com.auth.controller;

import com.auth.aop.OpLog;
import com.auth.entity.dto.RoleDto;
import com.auth.entity.vo.RoleVo;
import com.auth.service.IRoleService;
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
@RequestMapping("/role")
@RequiredArgsConstructor
@ResponseResultAdvice
@Api(tags = "角色请求")
public class RoleController {
    private final IRoleService iRoleService;

    @OpLog(desc = "新增角色,角色信息为: %s")
    @ApiOperation("新增角色")
    @PostMapping("/save")
    public Integer saveRole(@Valid RoleVo roleVo) {
        return iRoleService.saveRole(roleVo);
    }

    @ApiOperation("根据id查询角色")
    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable("id")@Positive(message = "id不合法") Integer id) {
        return iRoleService.getRoleById(id);
    }

    @OpLog(desc = "启用角色,角色ID为: %s")
    @ApiOperation("启用角色")
    @PostMapping("/enable/{id}")
    public void enableRole(@PathVariable("id") Integer id){
        iRoleService.enableRole(id);
    }

    @OpLog(desc = "禁用角色,角色ID为: %s")
    @ApiOperation("禁用角色")
    @PostMapping("/disable/{id}")
    public void disableRole(@PathVariable("id") Integer id){
        iRoleService.disableRole(id);
    }


    @ApiOperation("分页批量查询角色")
    @GetMapping("/list")
    public List<RoleDto> listRole(CustomPage customPage) {
        return iRoleService.listRole(customPage);
    }

    @OpLog(desc = "删除角色,角色ID为: %s")
    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public void delRole(@PathVariable("id")@Positive(message = "id不合法") Integer id) {
        iRoleService.delRoleById(id);
    }
}
