package com.auth.entity.factory;

import com.auth.entity.dao.Authority;
import com.auth.entity.dao.Authority.AuthorityBuilder;
import com.auth.entity.dto.AuthorityDto;
import com.auth.entity.dto.AuthorityDto.AuthorityDtoBuilder;
import com.auth.entity.factory.AuthorityFactory.AuthorityConverter;
import com.auth.entity.vo.AuthorityVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-24T10:26:55+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class AuthorityFactory$AuthorityConverterImpl implements AuthorityConverter {

    @Override
    public Authority toAuthority(AuthorityVo authorityVo) {
        if ( authorityVo == null ) {
            return null;
        }

        AuthorityBuilder authority = Authority.builder();

        authority.enabled( authorityVo.getEnabled() );
        authority.name( authorityVo.getName() );
        authority.description( authorityVo.getDescription() );

        return authority.build();
    }

    @Override
    public AuthorityDto toAuthorityDto(Authority authority) {
        if ( authority == null ) {
            return null;
        }

        AuthorityDtoBuilder authorityDto = AuthorityDto.builder();

        authorityDto.id( authority.getId() );
        authorityDto.enabled( authority.getEnabled() );
        authorityDto.name( authority.getName() );
        authorityDto.description( authority.getDescription() );

        return authorityDto.build();
    }

    @Override
    public List<AuthorityDto> toListAuthorityDto(List<Authority> authority) {
        if ( authority == null ) {
            return null;
        }

        List<AuthorityDto> list = new ArrayList<AuthorityDto>( authority.size() );
        for ( Authority authority1 : authority ) {
            list.add( toAuthorityDto( authority1 ) );
        }

        return list;
    }
}
