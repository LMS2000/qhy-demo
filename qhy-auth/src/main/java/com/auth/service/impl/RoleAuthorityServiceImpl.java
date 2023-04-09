package com.auth.service.impl;

import com.auth.entity.dao.RoleAuthority;
import com.auth.entity.dto.AuthorityDto;
import com.auth.mapper.RoleAuthorityMapper;
import com.auth.service.IAuthorityService;
import com.auth.service.IRoleAuthorityService;

import com.auth.utils.MybatisUtils;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.auth.entity.factory.AuthorityFactory.AUTHORITY_CONVERTER;

@Service
@RequiredArgsConstructor
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> implements IRoleAuthorityService {

    @Resource
    private IAuthorityService authorityService;

    @Override
    public void removeByRoleId(Integer id) {
        remove(new QueryWrapper<RoleAuthority>().eq("rid", id));
    }

    @Override
    public void removeByAuthorityId(Integer id) {
        remove(new QueryWrapper<RoleAuthority>().eq("aid", id));
    }


    /**
     * 为指定的角色添加权限
     * @param roleId
     * @param authorityList
     */
    @Override
    @Transactional
    public void releaseAuthorityToRole(Integer roleId, List<Integer> authorityList) {
        if (authorityList == null || authorityList.size() < 1) {
            return;
        }
        for (Integer aid : authorityList) {
            if (!MybatisUtils.existCheck(this, Map.of("rid", roleId, "aid", aid))
                    && MybatisUtils.existCheck(authorityService, Map.of("id", aid))) {
                save(RoleAuthority.builder().aid(aid).rid(roleId).build());
            }
        }

    }


    /**
     * 根据角色和权限删除记录
     * @param roleId
     * @param authorityList
     */
    @Override
    @Transactional
    public void revokeAuthorityFromRole(Integer roleId, List<Integer> authorityList) {
        if(authorityList==null||authorityList.size()<1){
            return;
        }
        for (Integer id : authorityList) {
            remove(new QueryWrapper<RoleAuthority>().eq("aid",id).eq("rid",roleId));
        }
    }

    /**
     * 根据角色查找权限列表
     * @param roleId
     * @return
     */
    @Override
    public List<AuthorityDto> getAuthorityOfRole(Integer roleId) {
        List<Integer> aidList =
                listByMap(Map.of("rid", roleId)).stream().map(RoleAuthority::getAid).collect(Collectors.toList());
        return ObjectUtils.isEmpty(aidList)?null:AUTHORITY_CONVERTER.toListAuthorityDto(authorityService.listByIds(aidList));

    }
}
