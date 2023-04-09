package com.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;
import com.user.entity.dao.UserFreeListener;
import com.user.entity.dto.UserFreeListenerDto;
import com.user.entity.vo.UserFreeListenerVo;
import com.user.mapper.UserFreeListenerMapper;
import com.user.service.IUserFreeListenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.user.entity.factory.UserFreeListenerFactory.USERFREELISTENER_CONVERTER;

@Service
@RequiredArgsConstructor
public class UserFreeListenerServiceImpl extends ServiceImpl<UserFreeListenerMapper,UserFreeListener> implements IUserFreeListenerService {
    @Override
        public Integer saveUserFreeListener(UserFreeListenerVo userfreelistenerVo) {
        UserFreeListener userfreelistener = USERFREELISTENER_CONVERTER.toUserFreeListener(userfreelistenerVo);
        UserFreeListener.repeatCheck(userfreelistener.getPhoneNumber(),this);
        save(userfreelistener);
        return userfreelistener.getId();
    }
    @Override
        public UserFreeListenerDto getUserFreeListenerById(Integer id) {
        UserFreeListener userfreelistener = getById(id);
        return USERFREELISTENER_CONVERTER.toUserFreeListenerDto(userfreelistener);
    }
    @Override
        public List<UserFreeListenerDto> listUserFreeListener(CustomPage customPage) {
        List<UserFreeListener> userfreelistenerList = CustomPage.getResult(customPage, new UserFreeListener(), this, null);
        return USERFREELISTENER_CONVERTER.toListUserFreeListenerDto(userfreelistenerList);
    }
    @Override
        public void delUserFreeListenerById(Integer id) {
        removeById(id);
    }
}
