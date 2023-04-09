package com.user.entity.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.user.entity.dao.User;
import com.user.entity.dto.SubCampusDto;
import com.user.entity.dto.UserDto;
import com.user.entity.vo.UserVo;
import com.user.service.ISubCampusService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class UserFactory {
    public static final UserConverter USER_CONVERTER = Mappers.getMapper(UserConverter.class);

    @Mapper
    public interface UserConverter {
        User toUser(UserVo userVo);

        default UserDto toUserDto(User user){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user,userDto);
            userDto.setSubCampusDto(SCIdToSubCampusDto.toSubCampusDto(user.getCId()));
            return  userDto;
        };

        List<UserDto> toListUserDto(List<User> user);
    }

    public interface SCIdToSubCampusDto {
        static SubCampusDto toSubCampusDto(Integer cid){
            if(cid==null){
                return null;
            }
            ISubCampusService iSubCampusService = SpringUtil.getBean(ISubCampusService.class);
            return iSubCampusService.getSubCampusById(cid);
        }
    }
}
