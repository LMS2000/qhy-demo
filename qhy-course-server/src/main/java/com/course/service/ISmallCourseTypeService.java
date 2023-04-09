package com.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.course.entity.dao.SmallCourseType;
import com.course.entity.dto.CourseDto;
import com.course.entity.dto.SmallCourseTypeDto;
import com.course.entity.vo.SmallCourseTypeVo;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;
public interface ISmallCourseTypeService extends IService<SmallCourseType> {
    Integer saveSmallCourseType(SmallCourseTypeVo smallcoursetypeVo);
    SmallCourseTypeDto getSmallCourseTypeById(Integer id);
    List<SmallCourseTypeDto> listSmallCourseType(CustomPage customPage);
    void delSmallCourseTypeById(Integer id);
    List<CourseDto> listCourseOfSmallCourseType(Integer id, CustomPage customPage);
}
