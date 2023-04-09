package com.auth.entity.factory;

import com.auth.entity.dao.Role;
import com.auth.entity.dao.Role.RoleBuilder;
import com.auth.entity.dto.RoleDto;
import com.auth.entity.dto.RoleDto.RoleDtoBuilder;
import com.auth.entity.factory.RoleFactory.RoleConverter;
import com.auth.entity.vo.RoleVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-24T10:26:55+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class RoleFactory$RoleConverterImpl implements RoleConverter {

    @Override
    public Role toRole(RoleVo roleVo) {
        if ( roleVo == null ) {
            return null;
        }

        RoleBuilder role = Role.builder();

        role.enabled( roleVo.getEnabled() );
        role.name( roleVo.getName() );
        role.description( roleVo.getDescription() );

        return role.build();
    }

    @Override
    public RoleDto toRoleDto(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDtoBuilder roleDto = RoleDto.builder();

        roleDto.id( role.getId() );
        roleDto.enabled( role.getEnabled() );
        roleDto.name( role.getName() );
        roleDto.description( role.getDescription() );

        return roleDto.build();
    }

    @Override
    public List<RoleDto> toListRoleDto(List<Role> role) {
        if ( role == null ) {
            return null;
        }

        List<RoleDto> list = new ArrayList<RoleDto>( role.size() );
        for ( Role role1 : role ) {
            list.add( toRoleDto( role1 ) );
        }

        return list;
    }
}
