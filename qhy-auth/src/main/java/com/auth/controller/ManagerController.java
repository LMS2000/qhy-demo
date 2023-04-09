package com.auth.controller;

import com.auth.aop.OpLog;
import com.auth.entity.dto.ManagerDto;
import com.auth.entity.vo.ManagerVo;
import com.auth.service.IManagerService;
import com.easyCode.feature.mybaits.CustomPage;
import com.easyCode.feature.result.ResponseResultAdvice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@ResponseResultAdvice
@Api(tags = "管理员请求")
public class ManagerController {
    private final IManagerService iManagerService;

    @OpLog(desc = "新增管理员,管理员信息为: %s")
    @ApiOperation("新增管理员")
    @PostMapping("/save")
    public Integer saveManager(@Valid ManagerVo managerVo) {
        return iManagerService.saveManager(managerVo);
    }

    @OpLog(desc = "启用管理员,管理员ID为: %s")
    @ApiOperation("启用管理员")
    @PostMapping("/enable/{id}")
    public void enableManager(@PathVariable("id") Integer id){
        iManagerService.enableManager(id);
    }

    @OpLog(desc = "禁用管理员,管理员ID为: %s")
    @ApiOperation("禁用管理员")
    @PostMapping("/disable/{id}")
    public void disableManager(@PathVariable("id") Integer id){
        iManagerService.disableManager(id);
    }

    @ApiOperation("根据id查询管理员")
    @GetMapping("/{id}")
    public ManagerDto getManager(@PathVariable("id") Integer id) {
        return iManagerService.getManagerById(id);
    }

    @ApiOperation("分页批量查询管理员")
    @GetMapping("/list")
    public List<ManagerDto> listManager(CustomPage customPage) {
        return iManagerService.listManager(customPage);
    }

    @OpLog(desc = "删除管理员,管理员ID: %s")
    @ApiOperation("删除管理员")
    @DeleteMapping("/{id}")
    public void delManager(@PathVariable("id") Integer id) {
        iManagerService.delManagerById(id);
    }
}
