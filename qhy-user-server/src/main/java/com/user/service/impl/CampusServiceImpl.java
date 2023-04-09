package com.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;
import com.user.entity.dao.Campus;
import com.user.entity.dto.CampusDto;
import com.user.entity.vo.CampusVo;
import com.user.exception.UserException;
import com.user.mapper.CampusMapper;
import com.user.service.ICampusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.user.entity.factory.CampusFactory.CAMPUS_CONVERTER;


@Service
@RequiredArgsConstructor
public class CampusServiceImpl extends ServiceImpl<CampusMapper, Campus> implements ICampusService {
    @Override
    public Integer saveCampus(CampusVo campusVo) {
        Campus campus = CAMPUS_CONVERTER.toCampus(campusVo);
        save(campus);
        return campus.getId();
    }

    @Override
    public CampusDto getCampusById(Integer id) {
        Campus campus = getById(id);
        return CAMPUS_CONVERTER.toCampusDto(campus);
    }

    @Override
    public List<CampusDto> listCampus(CustomPage customPage) {
        List<Campus> campusList = CustomPage.getResult(customPage, new Campus(), this, null);
        return CAMPUS_CONVERTER.toListCampusDto(campusList);
    }

    @Override
    public void delCampusById(Integer id) {
        removeById(id);
    }

    @Override
    public void checkExist(Integer cId) {
        long count = count(new QueryWrapper<Campus>().eq("id", cId));
        if(count==0){
            throw new UserException("校区不存在");
        }
    }
}
