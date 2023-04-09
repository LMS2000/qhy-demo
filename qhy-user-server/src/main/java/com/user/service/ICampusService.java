package com.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;
import com.user.entity.dao.Campus;
import com.user.entity.dto.CampusDto;
import com.user.entity.vo.CampusVo;

import java.util.List;

public interface ICampusService extends IService<Campus> {
    Integer saveCampus(CampusVo campusVo);
    CampusDto getCampusById(Integer id);
    List<CampusDto> listCampus(CustomPage customPage);
    void delCampusById(Integer id);

    void checkExist(Integer cId);
}
