package com.auth.service.impl;

import com.auth.constants.ManagerConstants;
import com.auth.entity.dao.Manager;
import com.auth.entity.dto.ManagerDto;
import com.auth.entity.vo.ManagerVo;
import com.auth.exception.AuthException;
import com.auth.mapper.ManagerMapper;
import com.auth.service.IManagerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

import static com.auth.entity.factory.ManagerFactory.MANAGER_CONVERTER;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements UserDetailsService, IManagerService {


    private final static  String SLAT="lms2000";
    private final PasswordEncoder passwordEncoder;

    @Override
    public Integer saveManager(ManagerVo managerVo) {
        Manager manager = MANAGER_CONVERTER.toManager(managerVo);
//        String newpwd= manager.getPassword()+SLAT;
//        manager.setPwd(DigestUtils.md5DigestAsHex(newpwd.getBytes()));
        manager.setPwd(passwordEncoder.encode(manager.getPassword()));
        save(manager);
        return manager.getId();
    }

    @Override
    public ManagerDto getManagerById(Integer id) {
        Manager byId = getById(id);
        return MANAGER_CONVERTER.toManagerDto(byId);

    }

    @Override
    public List<ManagerDto> listManager(CustomPage customPage) {
        List<Manager> result = CustomPage.getResult(customPage, new Manager(), this, null);
        return MANAGER_CONVERTER.toListManagerDto(result);
    }

    @Override
    public void delManagerById(Integer id) {
        operationSuperException(id,"不能对管理员操作");
        removeById(id);
    }

    @Override
    public void enableManager(Integer id) {
        operationSuperException(id,"不能对管理员操作");
        updateById(Manager.builder().id(id).enable(1).build());

    }

    @Override
    public void disableManager(Integer id) {
        operationSuperException(id,"不能对管理员操作");
        updateById(Manager.builder().id(id).enable(0).build());
    }


    private void operationSuperException(Integer id, String errMsg) {
        if (getById(id).getIdentity().equals(ManagerConstants.SUPER_MANAGER_SIGN)) {
            throw new AuthException(errMsg);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getOne(new QueryWrapper<Manager>().eq("account",username));
    }
}
