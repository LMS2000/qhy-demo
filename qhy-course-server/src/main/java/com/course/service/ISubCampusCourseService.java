package com.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.course.entity.dao.SubCampusCourse;
import com.course.entity.dto.CourseDto;
import com.course.entity.dto.SubCampusCourseDto;
import com.course.entity.vo.SubCampusCourseVo;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;
public interface ISubCampusCourseService extends IService<SubCampusCourse> {
    Integer saveSubCampusCourse(SubCampusCourseVo subcampuscourseVo);
    SubCampusCourseDto getSubCampusCourseById(Integer id);
    List<SubCampusCourseDto> listSubCampusCourse(CustomPage customPage);
    void delSubCampusCourseById(Integer id);

    boolean userHasCourse(Integer courseId, Integer campusId);

    List<Integer> getCourseIdListOfUser(CustomPage customPage, Integer cId);

    void releaseCoursesToCampus(Integer cId, List<Integer> courseIdList);

    void revokeCoursesFromCampus(Integer cId, List<Integer> courseIdList);

    List<CourseDto> listCoursesReleaseToCampus(Integer cId, CustomPage customPage);

    Boolean existCourseUnderSubCampus(Integer subCampusId);

    void checkCourseReleationShip(Integer id);
}
