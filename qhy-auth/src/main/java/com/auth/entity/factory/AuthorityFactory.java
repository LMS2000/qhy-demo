package com.auth.entity.factory;

import com.auth.entity.dao.Authority;
import com.auth.entity.dto.AuthorityDto;
import com.auth.entity.vo.AuthorityVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class AuthorityFactory {
    public static final AuthorityConverter AUTHORITY_CONVERTER = Mappers.getMapper(AuthorityConverter.class);

    @Mapper
    public interface AuthorityConverter {
        @Mappings({
                @Mapping(target = "id", ignore = true),
        })
        Authority toAuthority(AuthorityVo authorityVo);

        AuthorityDto toAuthorityDto(Authority authority);

        List<AuthorityDto> toListAuthorityDto(List<Authority> authority);
    }
}
