package com.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.course.entity.dao.BigCourseType;
import com.course.entity.dto.BigCourseTypeDto;
import com.course.entity.dto.SmallCourseTypeDto;
import com.course.entity.vo.BigCourseTypeVo;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;
public interface IBigCourseTypeService extends IService<BigCourseType> {
    Integer saveBigCourseType(BigCourseTypeVo bigcoursetypeVo);
    BigCourseTypeDto getBigCourseTypeById(Integer id);
    List<BigCourseTypeDto> listBigCourseType(CustomPage customPage);
    void delBigCourseTypeById(Integer id);


    List<SmallCourseTypeDto> listSmallCourseTypes(Integer id, CustomPage customPage);
}
