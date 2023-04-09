package com.course.entity.factory;

import com.course.entity.dao.BigCourseType;
import com.course.entity.dto.BigCourseTypeDto;
import com.course.entity.vo.BigCourseTypeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class BigCourseTypeFactory {
    public static final BigCourseTypeConverter BIGCOURSETYPE_CONVERTER= Mappers.getMapper(BigCourseTypeConverter.class);
    @Mapper
   public interface BigCourseTypeConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    BigCourseType toBigCourseType(BigCourseTypeVo bigcoursetypeVo);
        BigCourseTypeDto toBigCourseTypeDto(BigCourseType bigcoursetype);
        List<BigCourseTypeDto> toListBigCourseTypeDto(List<BigCourseType> bigcoursetype);
    }
}
