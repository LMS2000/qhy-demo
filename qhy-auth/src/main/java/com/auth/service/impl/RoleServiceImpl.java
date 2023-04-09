package com.auth.service.impl;

import com.auth.entity.dao.Role;
import com.auth.entity.dto.RoleDto;
import com.auth.entity.vo.RoleVo;
import com.auth.mapper.RoleMapper;
import com.auth.service.IRoleAuthorityService;
import com.auth.service.IRoleService;
import com.auth.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.auth.entity.factory.RoleFactory.ROLE_CONVERTER;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private  IUserRoleService userRoleService;
    @Resource
    private  IRoleAuthorityService roleAuthorityService;
    @Override
    public Integer saveRole(RoleVo roleVo) {
        Role role = ROLE_CONVERTER.toRole(roleVo);
        save(role);
        return role.getId();
    }

    @Override
    public RoleDto getRoleById(Integer id) {
        return ROLE_CONVERTER.toRoleDto(getById(id));
    }

    @Override
    public List<RoleDto> listRole(CustomPage customPage) {
        List<Role> result = CustomPage.getResult(customPage, new Role(), this, null);
        return ROLE_CONVERTER.toListRoleDto(result);
    }

    @Override
    @Transactional
    public void delRoleById(Integer id) {
        removeById(id);
        userRoleService.removeByRoleId(id);
        roleAuthorityService.removeByRoleId(id);
    }

    @Override
    public void enableRole(Integer id) {
        updateById(Role.builder().enabled(1).id(id).build());
    }

    @Override
    public void disableRole(Integer id) {
          updateById(Role.builder().enabled(0).id(id).build());
    }
}
