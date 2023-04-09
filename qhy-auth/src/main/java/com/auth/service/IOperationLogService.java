package com.auth.service;

import com.auth.entity.dao.OperationLog;
import com.auth.entity.dto.OperationLogDto;
import com.auth.entity.vo.OperationLogVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;
public interface IOperationLogService extends IService<OperationLog> {
    Integer saveOperationLog(OperationLogVo operationlogVo);
    OperationLogDto getOperationLogById(Integer id);
    List<OperationLogDto> listOperationLog(CustomPage customPage);
    void delOperationLogById(Integer id);
}
