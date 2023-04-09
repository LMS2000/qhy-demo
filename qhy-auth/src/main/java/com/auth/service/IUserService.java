package com.auth.service;

import com.auth.entity.dao.User;
import com.auth.entity.dto.UserDto;
import com.auth.entity.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;

public interface IUserService extends IService<User> {
    Integer saveUser(UserVo userVo);

    UserDto getUserById(Integer id);

    List<UserDto> listUser(CustomPage customPage);

    void delUserById(Integer id);

    void enableUser(Integer id);

    void disableUser(Integer id);

    void registerUser(String username, String serviceName, String role);

    void deleteUser(String username, String serviceName);
}
