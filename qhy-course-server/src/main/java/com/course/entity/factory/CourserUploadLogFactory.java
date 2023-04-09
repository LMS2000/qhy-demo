package com.course.entity.factory;

import com.course.entity.dao.CourserUploadLog;
import com.course.entity.dto.CourserUploadLogDto;
import com.course.entity.vo.CourserUploadLogVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class CourserUploadLogFactory {
    public static final CourserUploadLogConverter COURSERUPLOADLOG_CONVERTER= Mappers.getMapper(CourserUploadLogConverter.class);
    @Mapper
   public interface CourserUploadLogConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    CourserUploadLog toCourserUploadLog(CourserUploadLogVo courseruploadlogVo);
        CourserUploadLogDto toCourserUploadLogDto(CourserUploadLog courseruploadlog);
        List<CourserUploadLogDto> toListCourserUploadLogDto(List<CourserUploadLog> courseruploadlog);
    }
}
