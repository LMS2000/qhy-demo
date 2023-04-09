package com.auth.service;

import com.auth.entity.dao.Role;
import com.auth.entity.dto.RoleDto;
import com.auth.entity.vo.RoleVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;

public interface IRoleService extends IService<Role> {
    Integer saveRole(RoleVo roleVo);

    RoleDto getRoleById(Integer id);

    List<RoleDto> listRole(CustomPage customPage);

    void delRoleById(Integer id);

    void enableRole(Integer id);

    void disableRole(Integer id);
}
