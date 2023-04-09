package com.auth.entity.factory;

import com.auth.entity.dao.RoleAuthority;
import com.auth.entity.dao.RoleAuthority.RoleAuthorityBuilder;
import com.auth.entity.dto.RoleAuthorityDto;
import com.auth.entity.dto.RoleAuthorityDto.RoleAuthorityDtoBuilder;
import com.auth.entity.factory.RoleAuthorityFactory.RoleAuthorityConverter;
import com.auth.entity.vo.RoleAuthorityVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-24T10:26:54+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class RoleAuthorityFactory$RoleAuthorityConverterImpl implements RoleAuthorityConverter {

    @Override
    public RoleAuthority toRoleAuthority(RoleAuthorityVo roleauthorityVo) {
        if ( roleauthorityVo == null ) {
            return null;
        }

        RoleAuthorityBuilder roleAuthority = RoleAuthority.builder();

        roleAuthority.rid( roleauthorityVo.getRid() );
        roleAuthority.aid( roleauthorityVo.getAid() );

        return roleAuthority.build();
    }

    @Override
    public RoleAuthorityDto toRoleAuthorityDto(RoleAuthority roleauthority) {
        if ( roleauthority == null ) {
            return null;
        }

        RoleAuthorityDtoBuilder roleAuthorityDto = RoleAuthorityDto.builder();

        roleAuthorityDto.rid( roleauthority.getRid() );
        roleAuthorityDto.aid( roleauthority.getAid() );

        return roleAuthorityDto.build();
    }

    @Override
    public List<RoleAuthorityDto> toListRoleAuthorityDto(List<RoleAuthority> roleauthority) {
        if ( roleauthority == null ) {
            return null;
        }

        List<RoleAuthorityDto> list = new ArrayList<RoleAuthorityDto>( roleauthority.size() );
        for ( RoleAuthority roleAuthority : roleauthority ) {
            list.add( toRoleAuthorityDto( roleAuthority ) );
        }

        return list;
    }
}
