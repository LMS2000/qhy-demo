package com.course.entity.factory;

import com.course.entity.dao.CampusCourse;
import com.course.entity.dto.CampusCourseDto;
import com.course.entity.vo.CampusCourseVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class CampusCourseFactory {
    public static final CampusCourseConverter CAMPUSCOURSE_CONVERTER= Mappers.getMapper(CampusCourseConverter.class);
    @Mapper
   public interface CampusCourseConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    CampusCourse toCampusCourse(CampusCourseVo campuscourseVo);
        CampusCourseDto toCampusCourseDto(CampusCourse campuscourse);
        List<CampusCourseDto> toListCampusCourseDto(List<CampusCourse> campuscourse);
    }
}
