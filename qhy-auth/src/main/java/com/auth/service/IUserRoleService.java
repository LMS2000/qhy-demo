package com.auth.service;

import com.auth.entity.dao.UserRole;
import com.auth.entity.dto.RoleDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IUserRoleService extends IService<UserRole> {

    void delByUserId(Integer id);

    void removeByRoleId(Integer id);

    List<RoleDto> getRoleOfUser(Integer userId);

    void releaseRoleToUser(Integer userId, List<Integer> roleList);

    void revokeRoleFromUser(Integer userId, List<Integer> roleList);
}
