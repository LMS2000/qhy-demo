package com.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;
import com.user.entity.dao.SubCampus;
import com.user.entity.dto.SubCampusDto;
import com.user.entity.vo.SubCampusVo;
import com.user.mapper.SubCampusMapper;
import com.user.service.ISubCampusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.user.entity.factory.SubCampusFactory.SUBCAMPUS_CONVERTER;

@Service
@RequiredArgsConstructor
public class SubCampusServiceImpl extends ServiceImpl<SubCampusMapper,SubCampus> implements ISubCampusService {
    @Override
        public Integer saveSubCampus(SubCampusVo subcampusVo) {
        SubCampus subcampus = SUBCAMPUS_CONVERTER.toSubCampus(subcampusVo);
        save(subcampus);
        return subcampus.getId();
    }
    @Override
        public SubCampusDto getSubCampusById(Integer id) {
        SubCampus subcampus = getById(id);
        return SUBCAMPUS_CONVERTER.toSubCampusDto(subcampus);
    }
    @Override
        public List<SubCampusDto> listSubCampus(CustomPage customPage) {
        List<SubCampus> subcampusList = CustomPage.getResult(customPage, new SubCampus(), this, null);
        return SUBCAMPUS_CONVERTER.toListSubCampusDto(subcampusList);
    }
    @Override
        public void delSubCampusById(Integer id) {
        removeById(id);
    }
}
