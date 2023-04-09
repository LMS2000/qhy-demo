package com.auth.service;

import com.auth.entity.dao.Manager;
import com.auth.entity.dto.ManagerDto;
import com.auth.entity.vo.ManagerVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;

public interface IManagerService extends IService<Manager> {
    Integer saveManager(ManagerVo managerVo);

    ManagerDto getManagerById(Integer id);

    List<ManagerDto> listManager(CustomPage customPage);

    void delManagerById(Integer id);

    void enableManager(Integer id);

    void disableManager(Integer id);
}
