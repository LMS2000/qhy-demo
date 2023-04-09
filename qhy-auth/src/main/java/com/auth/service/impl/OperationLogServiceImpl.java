package com.auth.service.impl;

import com.auth.entity.dao.OperationLog;
import com.auth.entity.dto.OperationLogDto;
import com.auth.entity.vo.OperationLogVo;
import com.auth.mapper.OperationLogMapper;
import com.auth.service.IOperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.auth.entity.factory.OperationLogFactory.OPERATIONLOG_CONVERTER;
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {


    @Override
    public Integer saveOperationLog(OperationLogVo operationlogVo) {
        OperationLog operationlog = OPERATIONLOG_CONVERTER.toOperationLog(operationlogVo);
        save(operationlog);
        return operationlog.getId();
    }
    @Override
    public OperationLogDto getOperationLogById(Integer id) {
        OperationLog operationlog = getById(id);
        return OPERATIONLOG_CONVERTER.toOperationLogDto(operationlog);
    }
    @Override
    public List<OperationLogDto> listOperationLog(CustomPage customPage) {
        List<OperationLog> operationlogList = CustomPage.getResult(customPage, new OperationLog(), this, null);
        return OPERATIONLOG_CONVERTER.toListOperationLogDto(operationlogList);
    }
    @Override
    public void delOperationLogById(Integer id) {
        removeById(id);
    }
}
