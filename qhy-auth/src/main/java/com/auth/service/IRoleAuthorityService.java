package com.auth.service;

import com.auth.entity.dao.RoleAuthority;
import com.auth.entity.dto.AuthorityDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IRoleAuthorityService extends IService<RoleAuthority> {

    void removeByRoleId(Integer id);

    void removeByAuthorityId(Integer id);

    void releaseAuthorityToRole(Integer roleId, List<Integer> authorityList);

    void revokeAuthorityFromRole(Integer roleId, List<Integer> authorityList);

    List<AuthorityDto> getAuthorityOfRole(Integer roleId);
}
