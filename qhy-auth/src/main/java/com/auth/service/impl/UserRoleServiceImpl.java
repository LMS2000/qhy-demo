package com.auth.service.impl;

import com.auth.entity.dao.UserRole;
import com.auth.entity.dto.RoleDto;
import com.auth.mapper.UserRoleMapper;
import com.auth.service.IRoleService;
import com.auth.service.IUserRoleService;

import com.auth.utils.MybatisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.auth.entity.factory.RoleFactory.ROLE_CONVERTER;
import static com.auth.entity.factory.UserRoleFactory.USERROLE_CONVERTER;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Resource
    private IRoleService roleService;


    /**
     * 根据uid删除
     * @param id
     */

    @Override
    public void delByUserId(Integer id) {
        remove(new QueryWrapper<UserRole>().eq("uid",id));
    }

    @Override
    public void removeByRoleId(Integer id) {
      remove(new QueryWrapper<UserRole>().eq("rid",id));
    }

    /**
     * 根据uid获取角色列表
     * @param userId
     * @return
     */
    @Override
    public List<RoleDto> getRoleOfUser(Integer userId) {
        List<Integer> ridList = listByMap(Map.of("uid", userId)).stream().map(UserRole::getRid).collect(Collectors.toList());
        return ObjectUtils.isEmpty(ridList)?null:ROLE_CONVERTER.toListRoleDto(roleService.listByIds(ridList));
    }


    /**
     * 根据uid添加角色列表
     * @param userId
     * @param roleList
     */
    @Override
    public void releaseRoleToUser(Integer userId, List<Integer> roleList) {
        if(roleList==null||roleList.size()<1){
            return;
        }
        for (Integer rid : roleList) {
            if(!MybatisUtils.existCheck(this,Map.of("uid",userId,"rid",rid))
            &&MybatisUtils.existCheck(roleService,Map.of("rid",rid))){
                save(UserRole.builder().rid(rid).uid(userId).build());
            }
        }
    }

    /**
     * 根据uid删除角色列表
     * @param userId
     * @param roleList
     */
    @Override
    public void revokeRoleFromUser(Integer userId, List<Integer> roleList) {
        if(roleList==null||roleList.size()<1){
            return;
        }
        for (Integer  rid : roleList) {
               remove(new QueryWrapper<UserRole>().eq("uid",userId).eq("rid",rid));
        }
    }
}
