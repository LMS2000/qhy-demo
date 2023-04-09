package com.auth.entity.factory;

import com.auth.entity.dao.UserRole;
import com.auth.entity.dao.UserRole.UserRoleBuilder;
import com.auth.entity.dto.UserRoleDto;
import com.auth.entity.dto.UserRoleDto.UserRoleDtoBuilder;
import com.auth.entity.factory.UserRoleFactory.UserRoleConverter;
import com.auth.entity.vo.UserRoleVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-24T10:26:55+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class UserRoleFactory$UserRoleConverterImpl implements UserRoleConverter {

    @Override
    public UserRole toUserRole(UserRoleVo userroleVo) {
        if ( userroleVo == null ) {
            return null;
        }

        UserRoleBuilder userRole = UserRole.builder();

        userRole.uid( userroleVo.getUid() );
        userRole.rid( userroleVo.getRid() );

        return userRole.build();
    }

    @Override
    public UserRoleDto toUserRoleDto(UserRole userrole) {
        if ( userrole == null ) {
            return null;
        }

        UserRoleDtoBuilder userRoleDto = UserRoleDto.builder();

        userRoleDto.id( userrole.getId() );
        userRoleDto.uid( userrole.getUid() );
        userRoleDto.rid( userrole.getRid() );

        return userRoleDto.build();
    }

    @Override
    public List<UserRoleDto> toListUserRoleDto(List<UserRole> userrole) {
        if ( userrole == null ) {
            return null;
        }

        List<UserRoleDto> list = new ArrayList<UserRoleDto>( userrole.size() );
        for ( UserRole userRole : userrole ) {
            list.add( toUserRoleDto( userRole ) );
        }

        return list;
    }
}
