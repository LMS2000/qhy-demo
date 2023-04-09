package com.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;
import com.user.entity.dao.User;
import com.user.entity.dto.SdkUserInfoDto;
import com.user.entity.dto.UserDto;
import com.user.entity.vo.UserVo;

import java.util.List;
public interface IUserService extends IService<User> {
    Integer saveUser(UserVo userVo);
    UserDto getUserById(Integer id);
    List<UserDto> listUser(CustomPage customPage);
    void delUserById(Integer id);

    SdkUserInfoDto loadByName(String userRealName);
    UserDto getCurrentUserInfo();
}
