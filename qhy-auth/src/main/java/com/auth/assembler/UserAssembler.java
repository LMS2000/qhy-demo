package com.auth.assembler;


import com.auth.entity.dao.User;
import com.auth.entity.dto.AuthorityDto;
import com.auth.entity.dto.RoleDto;
import com.auth.entity.dto.UserAuthorityDto;
import com.auth.service.IRoleAuthorityService;
import com.auth.service.IUserRoleService;
import com.auth.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserAssembler {

    private final IUserService userService;
    private final IUserRoleService userRoleService;
    private final IRoleAuthorityService roleAuthorityService;

    public UserAuthorityDto getUserAuthorities(String username ,String serviceName){
        UserAuthorityDto.UserAuthorityDtoBuilder builder = UserAuthorityDto.builder();
        //获取用户对象
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username).eq("service_name", serviceName));
        //如果不存在就说明该用户是不合法的
        if(user==null||user.getEnabled()==1){
            return builder.legal(false).build();
        }
        List<RoleDto> roLeList = userRoleService.getRoleOfUser(user.getId());
        List<String> authorities=new ArrayList<>();
        //根据uid获取所持有的全部角色然后再根据角色获取全部的权限，然后添加到authorities中
        roLeList.stream().filter(roleDto -> roleDto.getEnabled()==1)
                .map(roleDto -> roleAuthorityService.getAuthorityOfRole(roleDto.getId())
                        .stream().filter(authorityDto -> authorityDto.getEnabled()==1)
                        .map(AuthorityDto::getName)
                        .collect(Collectors.toList())
                ).forEach(authorities::addAll);
        return builder.legal(true).authorities(authorities).build();

    }
}
