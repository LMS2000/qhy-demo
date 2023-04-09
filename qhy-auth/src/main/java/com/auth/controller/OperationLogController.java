package com.auth.controller;

import com.auth.entity.dto.OperationLogDto;
import com.auth.service.IOperationLogService;
import com.easyCode.feature.mybaits.CustomPage;
import com.easyCode.feature.result.ResponseResultAdvice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/operationLog")
@RequiredArgsConstructor
@ResponseResultAdvice
@Api(tags = "操作日志请求")
public class OperationLogController {
    private final IOperationLogService iOperationLogService;

    @ApiOperation("根据id查询操作日志")
    @GetMapping("/{id}")
    public OperationLogDto getOperationLog(@PathVariable("id") Integer id) {
        return iOperationLogService.getOperationLogById(id);
    }

    @ApiOperation("分页批量查询操作日志")
    @GetMapping("/list")
    public List<OperationLogDto> listOperationLog(CustomPage customPage) {
        return iOperationLogService.listOperationLog(customPage);
    }

    @ApiOperation("删除操作日志")
    @DeleteMapping("/{id}")
    public void delOperationLog(@PathVariable("id") Integer id) {
        iOperationLogService.delOperationLogById(id);
    }
}
