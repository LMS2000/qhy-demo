package com.auth.entity.factory;

import com.auth.entity.dao.OperationLog;
import com.auth.entity.dto.OperationLogDto;
import com.auth.entity.vo.OperationLogVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class OperationLogFactory {
    public static final OperationLogConverter OPERATIONLOG_CONVERTER= Mappers.getMapper(OperationLogConverter.class);
    @Mapper
   public interface OperationLogConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    OperationLog toOperationLog(OperationLogVo operationlogVo);
        OperationLogDto toOperationLogDto(OperationLog operationlog);
        List<OperationLogDto> toListOperationLogDto(List<OperationLog> operationlog);
    }
}
