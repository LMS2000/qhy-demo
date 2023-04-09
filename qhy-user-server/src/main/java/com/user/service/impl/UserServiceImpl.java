package com.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;
import com.user.entity.dao.User;
import com.user.entity.dto.SdkUserInfoDto;
import com.user.entity.dto.UserDto;
import com.user.entity.vo.UserVo;
import com.user.mapper.UserMapper;
import com.user.service.ICampusService;
import com.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.user.entity.factory.UserFactory.USER_CONVERTER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, UserDetailsService {
    private final ICampusService iCampusService;

    @Override
    public Integer saveUser(UserVo userVo) {
        //校区不合法抛出异常
        iCampusService.checkExist(userVo.getCId());
        User user = USER_CONVERTER.toUser(userVo);
        save(user);
        return user.getId();
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = getById(id);
        return USER_CONVERTER.toUserDto(user);
    }

    @Override
    public List<UserDto> listUser(CustomPage customPage) {
        List<User> userList = CustomPage.getResult(customPage, new User(), this, null);
        return USER_CONVERTER.toListUserDto(userList);
    }

    @Override
    public void delUserById(Integer id) {
        removeById(id);
    }

    @Override
    public SdkUserInfoDto loadByName(String userRealName) {
        if(StringUtils.isEmpty(userRealName)){
            return null;
        }
        User user = getOne(new QueryWrapper<User>().eq("name", userRealName));
        return user==null?null: SdkUserInfoDto.builder().id(user.getId()).cId(user.getCId()).build();
    }

    @Override
    public UserDto getCurrentUserInfo() {
        User visit = User.getVisit();
        return USER_CONVERTER.toUserDto(visit);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getOne(new QueryWrapper<User>().eq("name", username));
    }
}
