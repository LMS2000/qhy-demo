package com.auth.entity.factory;

import com.auth.entity.dao.RoleAuthority;
import com.auth.entity.dto.RoleAuthorityDto;
import com.auth.entity.vo.RoleAuthorityVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class RoleAuthorityFactory {
    public static final RoleAuthorityConverter ROLEAUTHORITY_CONVERTER = Mappers.getMapper(RoleAuthorityConverter.class);

    @Mapper
    public interface RoleAuthorityConverter {
        @Mappings({
                @Mapping(target = "id", ignore = true),
        })
        RoleAuthority toRoleAuthority(RoleAuthorityVo roleauthorityVo);

        RoleAuthorityDto toRoleAuthorityDto(RoleAuthority roleauthority);

        List<RoleAuthorityDto> toListRoleAuthorityDto(List<RoleAuthority> roleauthority);
    }
}
