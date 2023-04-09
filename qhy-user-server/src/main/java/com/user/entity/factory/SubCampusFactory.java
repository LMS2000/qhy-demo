package com.user.entity.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.user.entity.dao.SubCampus;
import com.user.entity.dto.SubCampusDto;
import com.user.entity.vo.SubCampusVo;
import com.user.service.ICampusService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class SubCampusFactory {
    public static final SubCampusConverter SUBCAMPUS_CONVERTER = Mappers.getMapper(SubCampusConverter.class);

    @Mapper
    public interface SubCampusConverter {
        SubCampus toSubCampus(SubCampusVo subcampusVo);

        default SubCampusDto toSubCampusDto(SubCampus subcampus){
            if (subcampus == null) {
                return null;
            } else {
                SubCampusDto.SubCampusDtoBuilder subCampusDto = SubCampusDto.builder();
                subCampusDto.id(subcampus.getId());
                subCampusDto.name(subcampus.getName());
                subCampusDto.desc(subcampus.getDesc());
                subCampusDto.parent(SpringUtil.getBean(ICampusService.class).getCampusById(subcampus.getParentId()));
                subCampusDto.createTime(subcampus.getCreateTime());
                subCampusDto.updateTime(subcampus.getUpdateTime());
                return subCampusDto.build();
            }
        };

        List<SubCampusDto> toListSubCampusDto(List<SubCampus> subcampus);
    }
}
