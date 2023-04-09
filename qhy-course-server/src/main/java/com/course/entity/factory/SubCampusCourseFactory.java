package com.course.entity.factory;

import com.course.entity.dao.SubCampusCourse;
import com.course.entity.dto.SubCampusCourseDto;
import com.course.entity.vo.SubCampusCourseVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class SubCampusCourseFactory {
    public static final SubCampusCourseConverter SUBCAMPUSCOURSE_CONVERTER = Mappers.getMapper(SubCampusCourseConverter.class);

    @Mapper
    public interface SubCampusCourseConverter {
        @Mappings({
                @Mapping(target = "id", ignore = true),
        }
        )
        SubCampusCourse toSubCampusCourse(SubCampusCourseVo subcampuscourseVo);

        SubCampusCourseDto toSubCampusCourseDto(SubCampusCourse subcampuscourse);

        List<SubCampusCourseDto> toListSubCampusCourseDto(List<SubCampusCourse> subcampuscourse);
    }
}
