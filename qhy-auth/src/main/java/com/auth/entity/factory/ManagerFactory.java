package com.auth.entity.factory;

import com.auth.entity.dao.Manager;
import com.auth.entity.dto.ManagerDto;
import com.auth.entity.vo.ManagerVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class ManagerFactory {
    public static final ManagerConverter MANAGER_CONVERTER = Mappers.getMapper(ManagerConverter.class);

    @Mapper
    public interface ManagerConverter {
        @Mappings({
                @Mapping(target = "id", ignore = true),
                @Mapping(target = "enable",source = "enabled")
        }
        )
        Manager toManager(ManagerVo managerVo);

        @Mapping(target = "enabled",source = "enable")
        ManagerDto toManagerDto(Manager manager);

        @Mapping(target = "enabled",source = "enable")
        List<ManagerDto> toListManagerDto(List<Manager> manager);
    }
}
