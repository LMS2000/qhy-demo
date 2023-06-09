package com.auth.entity.factory;

import com.auth.entity.dao.UserRole;
import com.auth.entity.dto.UserRoleDto;
import com.auth.entity.vo.UserRoleVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class UserRoleFactory {
    public static final UserRoleConverter USERROLE_CONVERTER= Mappers.getMapper(UserRoleConverter.class);
    @Mapper
   public interface UserRoleConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    UserRole toUserRole(UserRoleVo userroleVo);
        UserRoleDto toUserRoleDto(UserRole userrole);
        List<UserRoleDto> toListUserRoleDto(List<UserRole> userrole);
    }
}
