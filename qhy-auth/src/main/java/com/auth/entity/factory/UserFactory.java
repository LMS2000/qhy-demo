package com.auth.entity.factory;

import com.auth.entity.dao.User;
import com.auth.entity.dto.UserDto;
import com.auth.entity.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class UserFactory {
    public static final UserConverter USER_CONVERTER= Mappers.getMapper(UserConverter.class);
    @Mapper
   public interface UserConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    User toUser(UserVo userVo);
        UserDto toUserDto(User user);
        List<UserDto> toListUserDto(List<User> user);
    }
}
