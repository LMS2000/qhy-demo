package com.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;
import com.user.entity.dao.UserFreeListener;
import com.user.entity.dto.UserFreeListenerDto;
import com.user.entity.vo.UserFreeListenerVo;

import java.util.List;
public interface IUserFreeListenerService extends IService<UserFreeListener> {
    Integer saveUserFreeListener(UserFreeListenerVo userfreelistenerVo);
    UserFreeListenerDto getUserFreeListenerById(Integer id);
    List<UserFreeListenerDto> listUserFreeListener(CustomPage customPage);
    void delUserFreeListenerById(Integer id);
}
