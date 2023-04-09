package com.user.entity.factory;

import com.user.entity.dao.UserFreeListener;
import com.user.entity.dto.UserFreeListenerDto;
import com.user.entity.vo.UserFreeListenerVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class UserFreeListenerFactory {
    public static final UserFreeListenerConverter USERFREELISTENER_CONVERTER= Mappers.getMapper(UserFreeListenerConverter.class);
    @Mapper
   public interface UserFreeListenerConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    UserFreeListener toUserFreeListener(UserFreeListenerVo userfreelistenerVo);
        UserFreeListenerDto toUserFreeListenerDto(UserFreeListener userfreelistener);
        List<UserFreeListenerDto> toListUserFreeListenerDto(List<UserFreeListener> userfreelistener);
    }
}
