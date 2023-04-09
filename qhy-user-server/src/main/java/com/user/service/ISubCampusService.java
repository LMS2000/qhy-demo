package com.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;
import com.user.entity.dao.SubCampus;
import com.user.entity.dto.SubCampusDto;
import com.user.entity.vo.SubCampusVo;

import java.util.List;
public interface ISubCampusService extends IService<SubCampus> {
    Integer saveSubCampus(SubCampusVo subcampusVo);
    SubCampusDto getSubCampusById(Integer id);
    List<SubCampusDto> listSubCampus(CustomPage customPage);
    void delSubCampusById(Integer id);
}
