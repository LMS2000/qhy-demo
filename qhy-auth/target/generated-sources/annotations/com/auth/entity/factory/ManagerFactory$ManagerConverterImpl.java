package com.auth.entity.factory;

import com.auth.entity.dao.Manager;
import com.auth.entity.dao.Manager.ManagerBuilder;
import com.auth.entity.dto.ManagerDto;
import com.auth.entity.dto.ManagerDto.ManagerDtoBuilder;
import com.auth.entity.factory.ManagerFactory.ManagerConverter;
import com.auth.entity.vo.ManagerVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-24T10:26:55+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class ManagerFactory$ManagerConverterImpl implements ManagerConverter {

    @Override
    public Manager toManager(ManagerVo managerVo) {
        if ( managerVo == null ) {
            return null;
        }

        ManagerBuilder manager = Manager.builder();

        manager.enable( managerVo.getEnabled() );
        manager.account( managerVo.getAccount() );
        manager.pwd( managerVo.getPwd() );

        return manager.build();
    }

    @Override
    public ManagerDto toManagerDto(Manager manager) {
        if ( manager == null ) {
            return null;
        }

        ManagerDtoBuilder managerDto = ManagerDto.builder();

        managerDto.enabled( manager.getEnable() );
        managerDto.id( manager.getId() );
        managerDto.account( manager.getAccount() );
        managerDto.identity( manager.getIdentity() );
        managerDto.createTime( manager.getCreateTime() );
        managerDto.updateTime( manager.getUpdateTime() );

        return managerDto.build();
    }

    @Override
    public List<ManagerDto> toListManagerDto(List<Manager> manager) {
        if ( manager == null ) {
            return null;
        }

        List<ManagerDto> list = new ArrayList<ManagerDto>( manager.size() );
        for ( Manager manager1 : manager ) {
            list.add( toManagerDto( manager1 ) );
        }

        return list;
    }
}
