package com.auth.controller;


import com.auth.entity.dto.AuthorityDto;
import com.auth.entity.vo.AuthorityVo;
import com.auth.service.IAuthorityService;
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
@RequestMapping("/authority")
@RequiredArgsConstructor
@ResponseResultAdvice
@Api(tags = "权限请求")
public class AuthorityController {
    private final IAuthorityService iAuthorityService;

    @ApiOperation("新增权限")
    @PostMapping("/save")
    public Integer saveAuthority(@Valid AuthorityVo authorityVo) {
        return iAuthorityService.saveAuthority(authorityVo);
    }

    @ApiOperation("根据id查询权限")
    @GetMapping("/{id}")
    public AuthorityDto getAuthority(@PathVariable("id")@Positive(message = "id不合法") Integer id) {
        return iAuthorityService.getAuthorityById(id);
    }

    @ApiOperation("分页批量查询权限")
    @GetMapping("/list")
    public List<AuthorityDto> listAuthority(CustomPage customPage) {
        return iAuthorityService.listAuthority(customPage);
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/{id}")
    public void delAuthority(@PathVariable("id")@Positive(message = "id不合法") Integer id) {
        iAuthorityService.delAuthorityById(id);
    }
}
