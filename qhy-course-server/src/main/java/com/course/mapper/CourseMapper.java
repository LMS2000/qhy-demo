package com.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.course.entity.dao.Course;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
