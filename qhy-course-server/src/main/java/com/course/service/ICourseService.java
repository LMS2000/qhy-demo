package com.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.course.entity.dao.Course;
import com.course.entity.dto.CourseDto;
import com.course.entity.vo.CourseVo;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;
import java.util.Map;

public interface ICourseService extends IService<Course> {
    String saveCourse(CourseVo courseVo);

    CourseDto getCourseById(Integer id);

    List<CourseDto> listCourse(CustomPage customPage);

    void delCourseById(Integer id);

    void enableOrDisableCourse(Integer id, Integer enable);

    Map<String, String> checkCourseUpload(List<String> taskId);
}
