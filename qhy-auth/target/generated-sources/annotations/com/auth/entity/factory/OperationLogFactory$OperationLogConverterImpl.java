package com.auth.entity.factory;

import com.auth.entity.dao.OperationLog;
import com.auth.entity.dao.OperationLog.OperationLogBuilder;
import com.auth.entity.dto.OperationLogDto;
import com.auth.entity.dto.OperationLogDto.OperationLogDtoBuilder;
import com.auth.entity.factory.OperationLogFactory.OperationLogConverter;
import com.auth.entity.vo.OperationLogVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-24T10:26:55+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class OperationLogFactory$OperationLogConverterImpl implements OperationLogConverter {

    @Override
    public OperationLog toOperationLog(OperationLogVo operationlogVo) {
        if ( operationlogVo == null ) {
            return null;
        }

        OperationLogBuilder operationLog = OperationLog.builder();

        operationLog.operationName( operationlogVo.getOperationName() );
        operationLog.operationContent( operationlogVo.getOperationContent() );

        return operationLog.build();
    }

    @Override
    public OperationLogDto toOperationLogDto(OperationLog operationlog) {
        if ( operationlog == null ) {
            return null;
        }

        OperationLogDtoBuilder operationLogDto = OperationLogDto.builder();

        operationLogDto.id( operationlog.getId() );
        operationLogDto.operationName( operationlog.getOperationName() );
        operationLogDto.operationContent( operationlog.getOperationContent() );
        operationLogDto.createTime( operationlog.getCreateTime() );
        operationLogDto.updateTime( operationlog.getUpdateTime() );

        return operationLogDto.build();
    }

    @Override
    public List<OperationLogDto> toListOperationLogDto(List<OperationLog> operationlog) {
        if ( operationlog == null ) {
            return null;
        }

        List<OperationLogDto> list = new ArrayList<OperationLogDto>( operationlog.size() );
        for ( OperationLog operationLog : operationlog ) {
            list.add( toOperationLogDto( operationLog ) );
        }

        return list;
    }
}
