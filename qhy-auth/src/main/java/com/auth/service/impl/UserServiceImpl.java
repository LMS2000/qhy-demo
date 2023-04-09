package com.auth.service.impl;

import com.auth.entity.dao.Role;
import com.auth.entity.dao.User;
import com.auth.entity.dao.UserRole;
import com.auth.entity.dto.UserDto;
import com.auth.entity.vo.UserVo;
import com.auth.exception.AuthException;
import com.auth.mapper.UserMapper;
import com.auth.service.IRoleService;
import com.auth.service.IUserRoleService;
import com.auth.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;

import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.auth.entity.factory.UserFactory.USER_CONVERTER;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private IRoleService roleService;

    @Override
    public Integer saveUser(UserVo userVo) {
        User user = USER_CONVERTER.toUser(userVo);
        save(user);
        return user.getId();
    }

    @Override
    public UserDto getUserById(Integer id) {
        return USER_CONVERTER.toUserDto(getById(id));
    }

    @Override
    public List<UserDto> listUser(CustomPage customPage) {
        List<User> result = CustomPage.getResult(customPage, new User(), this, null);
        return USER_CONVERTER.toListUserDto(result);
    }

    /**
     * 删除用户，并且删除关联的角色
     *
     * @param id
     */
    @Override
    @Transactional
    public void delUserById(Integer id) {
        removeById(id);
        userRoleService.delByUserId(id);
    }

    @Override
    public void enableUser(Integer id) {
        updateById(User.builder().id(id).enabled(1).build());
    }

    @Override
    public void disableUser(Integer id) {
        updateById(User.builder().id(id).enabled(0).build());
    }

    /**
     * 注册用户
     *
     * @param username
     * @param serviceName
     * @param roleName
     */
    @Override
    @Transactional
    public void registerUser(String username, String serviceName, String roleName) {
        log.info("当前发生的分支事务ID：{}", RootContext.getXID());
        Integer userId = saveUser(UserVo.builder().enabled(1).username(username).serviceName(serviceName).build());
        Role role = roleService.getOne(new QueryWrapper<Role>().eq("name", roleName));
        if (role == null) {
            throw new AuthException("该角色不存在！");
        }
        userRoleService.save(UserRole.builder().uid(userId).rid(role.getId()).build());
    }

    /**
     * 删除用户
     * @param username
     * @param serviceName
     */
    @Override
    @Transactional
    public void deleteUser(String username, String serviceName) {
        log.info("当前发生的分支事务ID：{}", RootContext.getXID());
        User one = getOne(new QueryWrapper<User>().eq("username", username).eq("service_name", serviceName));
        if(one==null){
            return;
        }
        removeById(one.getId());
        userRoleService.remove(new QueryWrapper<UserRole>().eq("uid",one.getId()));
    }
}
