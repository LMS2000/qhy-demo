package com.course.entity.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.course.constants.ServiceConstants;
import com.course.entity.dao.Course;
import com.course.entity.dto.CourseDto;
import com.course.entity.vo.CourseVo;
import com.course.service.IBigCourseTypeService;
import com.course.service.ISmallCourseTypeService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class CourseFactory {
    public static final CourseConverter COURSE_CONVERTER = Mappers.getMapper(CourseConverter.class);

    @Mapper
    public interface CourseConverter {
        Course toCourse(CourseVo courseVo);

        default CourseDto toCourseDto(Course course){
            if(course==null){
                return null;
            }
            CourseDto courseDto = new CourseDto();
            courseDto.setCourseName(course.getCourseName());
            courseDto.setCourseCoverPath(List.of(course.getCourseCoverPath().split(ServiceConstants.IMG_FILE_PATH_SEPARATOR)));
            courseDto.setCourseFilePath(course.getCourseFilePath());
            courseDto.setId(course.getId());
            IBigCourseTypeService bigCourseTypeService = SpringUtil.getBean(IBigCourseTypeService.class);
            courseDto.setBigType(bigCourseTypeService.getBigCourseTypeById(course.getBigType()));
            ISmallCourseTypeService smallCourseTypeService = SpringUtil.getBean(ISmallCourseTypeService.class);
            courseDto.setSmallType(smallCourseTypeService.getSmallCourseTypeById(course.getSmallType()));
            courseDto.setCreateTime(course.getCreateTime());
            courseDto.setUpdateTime(course.getUpdateTime());
            courseDto.setEnable(course.getEnable());
            return  courseDto;
        };

        List<CourseDto> toListCourseDto(List<Course> course);
    }
}
