package com.auth.entity.factory;

import com.auth.entity.dao.User;
import com.auth.entity.dao.User.UserBuilder;
import com.auth.entity.dto.UserDto;
import com.auth.entity.dto.UserDto.UserDtoBuilder;
import com.auth.entity.factory.UserFactory.UserConverter;
import com.auth.entity.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-24T10:26:55+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
public class UserFactory$UserConverterImpl implements UserConverter {

    @Override
    public User toUser(UserVo userVo) {
        if ( userVo == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.enabled( userVo.getEnabled() );
        user.username( userVo.getUsername() );
        user.serviceName( userVo.getServiceName() );

        return user.build();
    }

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.enabled( user.getEnabled() );
        userDto.username( user.getUsername() );
        userDto.serviceName( user.getServiceName() );
        userDto.createTime( user.getCreateTime() );
        userDto.updateTime( user.getUpdateTime() );

        return userDto.build();
    }

    @Override
    public List<UserDto> toListUserDto(List<User> user) {
        if ( user == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( user.size() );
        for ( User user1 : user ) {
            list.add( toUserDto( user1 ) );
        }

        return list;
    }
}
