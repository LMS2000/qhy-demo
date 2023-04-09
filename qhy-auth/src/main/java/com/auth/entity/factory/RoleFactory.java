package com.auth.entity.factory;

import com.auth.entity.dao.Role;
import com.auth.entity.dto.RoleDto;
import com.auth.entity.vo.RoleVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class RoleFactory {
    public static final RoleConverter ROLE_CONVERTER= Mappers.getMapper(RoleConverter.class);
    @Mapper
   public interface RoleConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    Role toRole(RoleVo roleVo);
        RoleDto toRoleDto(Role role);
        List<RoleDto> toListRoleDto(List<Role> role);
    }
}
