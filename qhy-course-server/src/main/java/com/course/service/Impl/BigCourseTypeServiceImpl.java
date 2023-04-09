package com.course.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.course.entity.dao.BigCourseType;
import com.course.entity.dao.SmallCourseType;
import com.course.entity.dto.BigCourseTypeDto;
import com.course.entity.dto.SmallCourseTypeDto;
import com.course.entity.vo.BigCourseTypeVo;
import com.course.exception.CourseException;
import com.course.mapper.BigCourseTypeMapper;
import com.course.service.IBigCourseTypeService;
import com.course.service.ISmallCourseTypeService;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.course.entity.factory.BigCourseTypeFactory.BIGCOURSETYPE_CONVERTER;
import static com.course.entity.factory.SmallCourseTypeFactory.SMALLCOURSETYPE_CONVERTER;

@Service
@RequiredArgsConstructor
public class BigCourseTypeServiceImpl extends ServiceImpl<BigCourseTypeMapper, BigCourseType> implements IBigCourseTypeService {
    @Autowired
    private ISmallCourseTypeService iSmallCourseTypeService;

    //---- 超级管理员才能访问

    @Override
    public Integer saveBigCourseType(BigCourseTypeVo bigcoursetypeVo) {
        BigCourseType bigcoursetype = BIGCOURSETYPE_CONVERTER.toBigCourseType(bigcoursetypeVo);

        BigCourseType.repeatNameCheck(bigcoursetype,this);
        save(bigcoursetype);
        return bigcoursetype.getId();
    }

    @Override
    public BigCourseTypeDto getBigCourseTypeById(Integer id) {
        BigCourseType bigcoursetype = getById(id);
        return BIGCOURSETYPE_CONVERTER.toBigCourseTypeDto(bigcoursetype);
    }

    @Override
    public List<BigCourseTypeDto> listBigCourseType(CustomPage customPage) {
        List<BigCourseType> bigcoursetypeList = CustomPage.getResult(customPage, new BigCourseType(), this, null);
        return BIGCOURSETYPE_CONVERTER.toListBigCourseTypeDto(bigcoursetypeList);
    }

    @Override
    public void delBigCourseTypeById(Integer id) {
        long count = iSmallCourseTypeService.count(new QueryWrapper<SmallCourseType>().eq("parent_id", id));
        if(count>0){
            throw new RuntimeException("当前课程大类关联着课程小类,不能删除");
        }
        removeById(id);
    }

    @Override
    public List<SmallCourseTypeDto> listSmallCourseTypes(Integer id, CustomPage customPage) {
        BigCourseType bigCourseType = getById(id);
        if(bigCourseType==null){
            throw new CourseException("课程大类不存在");
        }
        return SMALLCOURSETYPE_CONVERTER.toListSmallCourseTypeDto(CustomPage.getResult(customPage,new SmallCourseType(),iSmallCourseTypeService, new QueryWrapper<SmallCourseType>().eq("parent_id",id)));
    }
}
