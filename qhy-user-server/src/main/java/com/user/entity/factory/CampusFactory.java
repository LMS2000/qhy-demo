package com.user.entity.factory;

import com.user.entity.dao.Campus;
import com.user.entity.dto.CampusDto;
import com.user.entity.vo.CampusVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
public class CampusFactory {
    public static final CampusConverter CAMPUS_CONVERTER= Mappers.getMapper(CampusConverter.class);
    @Mapper
   public interface CampusConverter {
        @Mappings({
        @Mapping(target = "id",ignore = true),
    }
        )
    Campus toCampus(CampusVo campusVo);
        CampusDto toCampusDto(Campus campus);
        List<CampusDto> toListCampusDto(List<Campus> campus);
    }
}
