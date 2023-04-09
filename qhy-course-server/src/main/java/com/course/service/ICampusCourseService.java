package com.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.course.entity.dao.CampusCourse;
import com.course.entity.dto.CampusCourseDto;
import com.course.entity.dto.CourseDto;
import com.course.entity.vo.CampusCourseVo;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;
public interface ICampusCourseService extends IService<CampusCourse> {
    Integer saveCampusCourse(CampusCourseVo campuscourseVo);
    CampusCourseDto getCampusCourseById(Integer id);
    List<CampusCourseDto> listCampusCourse(CustomPage customPage);
    void delCampusCourseById(Integer id);

    boolean managerHasCourse(Integer courseId, Integer cId);

    List<Integer> getCourseIdListOfManager(CustomPage customPage, Integer cId);

    void releaseCoursesToCampus(Integer cId, List<Integer> courseIdList);

    void revokeCoursesFromCampus(Integer cId, List<Integer> courseIdList);

    List<CourseDto> listCoursesReleaseToCampus(Integer cId, CustomPage customPage);

    Boolean existCourseUnderCampus(Integer campusId);

    void checkCourseReleationShip(Integer id);
}
