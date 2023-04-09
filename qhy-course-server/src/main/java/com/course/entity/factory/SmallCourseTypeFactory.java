package com.course.entity.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.course.entity.dao.SmallCourseType;
import com.course.entity.dto.SmallCourseTypeDto;
import com.course.entity.vo.SmallCourseTypeVo;
import com.course.service.IBigCourseTypeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class SmallCourseTypeFactory {
    public static final SmallCourseTypeConverter SMALLCOURSETYPE_CONVERTER = Mappers.getMapper(SmallCourseTypeConverter.class);

    @Mapper
    public interface SmallCourseTypeConverter {
        @Mappings({
                @Mapping(target = "id", ignore = true),
                @Mapping(target = "coverPath", ignore = true),
                @Mapping(target = "coverName",ignore = true)
        })
        SmallCourseType toSmallCourseType(SmallCourseTypeVo smallcoursetypeVo);

        default SmallCourseTypeDto toSmallCourseTypeDto(SmallCourseType smallcoursetype){
            if (smallcoursetype == null) {
                return null;
            } else {
                SmallCourseTypeDto.SmallCourseTypeDtoBuilder smallCourseTypeDto = SmallCourseTypeDto.builder();
                smallCourseTypeDto.id(smallcoursetype.getId());
                smallCourseTypeDto.name(smallcoursetype.getName());
                smallCourseTypeDto.parent(SpringUtil.getBean(IBigCourseTypeService.class).getBigCourseTypeById(smallcoursetype.getParentId()));
                smallCourseTypeDto.coverPath(smallcoursetype.getCoverPath());
                smallCourseTypeDto.createTime(smallcoursetype.getCreateTime());
                smallCourseTypeDto.updateTime(smallcoursetype.getUpdateTime());
                return smallCourseTypeDto.build();
            }
        };

        List<SmallCourseTypeDto> toListSmallCourseTypeDto(List<SmallCourseType> smallcoursetype);
    }
}
