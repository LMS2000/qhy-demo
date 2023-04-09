package com.course.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.course.client.SubCampusClient;
import com.course.entity.dao.SubCampusCourse;
import com.course.entity.dto.CourseDto;
import com.course.entity.dto.SubCampusCourseDto;
import com.course.entity.vo.SubCampusCourseVo;
import com.course.exception.CourseException;
import com.course.filter.VisitDto;
import com.course.mapper.SubCampusCourseMapper;
import com.course.service.ICourseService;
import com.course.service.ISubCampusCourseService;
import com.course.utils.MybaitsUtils;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.course.entity.factory.CourseFactory.COURSE_CONVERTER;
import static com.course.entity.factory.SubCampusCourseFactory.SUBCAMPUSCOURSE_CONVERTER;

@Service
@RequiredArgsConstructor
public class SubCampusCourseServiceImpl extends ServiceImpl<SubCampusCourseMapper, SubCampusCourse> implements ISubCampusCourseService {
    @Autowired
    private ICourseService iCourseService;
    @Autowired
    private SubCampusClient subCampusClient;


    @Override
    public Integer saveSubCampusCourse(SubCampusCourseVo subcampuscourseVo) {
        SubCampusCourse subcampuscourse = SUBCAMPUSCOURSE_CONVERTER.toSubCampusCourse(subcampuscourseVo);
        save(subcampuscourse);
        return subcampuscourse.getId();
    }

    @Override
    public SubCampusCourseDto getSubCampusCourseById(Integer id) {
        SubCampusCourse subcampuscourse = getById(id);
        return SUBCAMPUSCOURSE_CONVERTER.toSubCampusCourseDto(subcampuscourse);
    }

    @Override
    public List<SubCampusCourseDto> listSubCampusCourse(CustomPage customPage) {
        List<SubCampusCourse> subcampuscourseList = CustomPage.getResult(customPage, new SubCampusCourse(), this, null);
        return SUBCAMPUSCOURSE_CONVERTER.toListSubCampusCourseDto(subcampuscourseList);
    }

    @Override
    public void delSubCampusCourseById(Integer id) {
        removeById(id);
    }

    @Override
    public boolean userHasCourse(Integer courseId, Integer campusId) {
        return count(new QueryWrapper<SubCampusCourse>()
                .eq("course_id",courseId)
                .eq("sub_campus_id",campusId))==1;
    }

    @Override
    public List<Integer> getCourseIdListOfUser(CustomPage customPage, Integer cId) {
        return CustomPage.getResult(customPage,new SubCampusCourse(),this,new QueryWrapper<SubCampusCourse>()
                .eq("sub_campus_id",cId)).stream().map(SubCampusCourse::getCourseId).collect(Collectors.toList());
    }

    @Override
    public void releaseCoursesToCampus(Integer cId, List<Integer> courseIdList) {
        if(courseIdList==null){
            return;
        }
        //分校区需要rpc查询确认是否存在
        if(!subCampusClient.exist(cId)){
            throw new CourseException("分校区不存在");
        }
        VisitDto.handle(visitDto -> {
            //跳过已经赋予的权限
            for (Integer id : courseIdList) {
                if (!MybaitsUtils.existCheck(this, Map.of("course_id", id, "sub_campus_id", cId))
                        && MybaitsUtils.existCheck(iCourseService,Map.of("id",id))) {
                    save(SubCampusCourse.builder().courseId(id).subCampusId(cId).build());
                }
            }
            return null;
        },visitDto -> {
            //普通管理员只能操作自己管理的分校区
            authorityCheck(subCampusClient, visitDto, cId);
            //跳过已经赋予的权限
            for (Integer id : courseIdList) {
                if (!MybaitsUtils.existCheck(this, Map.of("course_id", id, "sub_campus_id", cId))
                        &&MybaitsUtils.existCheck(iCourseService,Map.of("id",id))) {
                    save(SubCampusCourse.builder().courseId(id).subCampusId(cId).build());
                }
            }
            return null;
        },null);
    }

    @Override
    public void revokeCoursesFromCampus(Integer cId, List<Integer> courseIdList) {
        if(courseIdList==null){
            return;
        }
        VisitDto.handle(visitDto -> {
            for (Integer id : courseIdList) {
                remove(new QueryWrapper<SubCampusCourse>().eq("sub_campus_id",cId).eq("course_id", id));
            }
            return null;
        },visitDto -> {
            authorityCheck(subCampusClient, visitDto, cId);
            for (Integer id : courseIdList) {
                remove(new QueryWrapper<SubCampusCourse>().eq("sub_campus_id",cId).eq("course_id", id));
            }
            return null;
        },null);
    }

    private void authorityCheck(SubCampusClient subCampusClient, VisitDto visitDto, Integer cId) {
        //普通管理员只能操作自己管理的分校区
        if(!visitDto.isSuperManager() && !subCampusClient.existSubCampusUnderCampus(visitDto.getCId(), cId)){
            throw new CourseException("权限不足");
        }
    }

    @Override
    public List<CourseDto> listCoursesReleaseToCampus(Integer cId, CustomPage customPage) {
        authorityCheck(subCampusClient, VisitDto.getCurrentVisit(), cId);
        List<Integer> idList=getCourseIdListOfUser(customPage,cId);
        if(ObjectUtils.isEmpty(idList)){
            return null;
        }
        return COURSE_CONVERTER.toListCourseDto(iCourseService.listByIds(idList));
    }

    @Override
    public Boolean existCourseUnderSubCampus(Integer subCampusId) {
        return count(new QueryWrapper<SubCampusCourse>().eq("sub_campus_id",subCampusId))>0;
    }

    @Override
    public void checkCourseReleationShip(Integer id) {
        long count = count(new QueryWrapper<SubCampusCourse>().eq("course_id", id));
        if (count > 0) {
            throw new CourseException("课程和相关分校区相关联,无法删除");
        }
    }
}
