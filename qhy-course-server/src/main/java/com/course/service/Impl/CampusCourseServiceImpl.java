package com.course.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.course.client.CampusClient;
import com.course.entity.dao.CampusCourse;
import com.course.entity.dto.CampusCourseDto;
import com.course.entity.dto.CourseDto;
import com.course.entity.vo.CampusCourseVo;
import com.course.exception.CourseException;
import com.course.mapper.CampusCourseMapper;
import com.course.service.ICampusCourseService;
import com.course.service.ICourseService;
import com.course.utils.MybaitsUtils;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.course.entity.factory.CampusCourseFactory.CAMPUSCOURSE_CONVERTER;
import static com.course.entity.factory.CourseFactory.COURSE_CONVERTER;

@Service
@RequiredArgsConstructor
public class CampusCourseServiceImpl extends ServiceImpl<CampusCourseMapper, CampusCourse> implements ICampusCourseService {
    @Autowired
    private ICourseService iCourseService;
    @Autowired
    private CampusClient campusClient;

    @Override
    public Integer saveCampusCourse(CampusCourseVo campuscourseVo) {
        CampusCourse campuscourse = CAMPUSCOURSE_CONVERTER.toCampusCourse(campuscourseVo);
        save(campuscourse);
        return campuscourse.getId();
    }

    @Override
    public CampusCourseDto getCampusCourseById(Integer id) {
        CampusCourse campuscourse = getById(id);
        return CAMPUSCOURSE_CONVERTER.toCampusCourseDto(campuscourse);
    }

    @Override
    public List<CampusCourseDto> listCampusCourse(CustomPage customPage) {
        List<CampusCourse> campuscourseList = CustomPage.getResult(customPage, new CampusCourse(), this, null);
        return CAMPUSCOURSE_CONVERTER.toListCampusCourseDto(campuscourseList);
    }

    @Override
    public void delCampusCourseById(Integer id) {
        removeById(id);
    }

    @Override
    public boolean managerHasCourse(Integer courseId, Integer cId) {
        return count(new QueryWrapper<CampusCourse>()
                .eq("course_id",courseId)
                .eq("campus_id",cId))==1;
    }

    @Override
    public List<Integer> getCourseIdListOfManager(CustomPage customPage, Integer cId) {
        return CustomPage.getResult(customPage,new CampusCourse(),this,new QueryWrapper<CampusCourse>()
                .eq("campus_id",cId)).stream().map(CampusCourse::getCourseId).collect(Collectors.toList());
    }

    /**
     *
     * @param cId  校区id
     * @param courseIdList
     */
    @Override
    public void releaseCoursesToCampus(Integer cId, List<Integer> courseIdList) {
        if (courseIdList == null) {
            return;
        }
        //主校区也需要存在,需要远程rpc确认存在才可以赋权
         if(!campusClient.exist(cId)){
             throw new CourseException("主校区不存在");
         }
        //跳过已经赋予的权限
        for (Integer id : courseIdList) {
            if (!MybaitsUtils.existCheck(this, Map.of("course_id", id, "campus_id", cId))
                    &&MybaitsUtils.existCheck(iCourseService,Map.of("id",id))) {
                save(CampusCourse.builder().courseId(id).campusId(cId).build());
            }
        }
    }

    @Override
    public void revokeCoursesFromCampus(Integer cId, List<Integer> courseIdList) {
        if (courseIdList == null) {
            return;
        }
        for (Integer id : courseIdList) {
            remove(new QueryWrapper<CampusCourse>().eq("campus_id",cId).eq("course_id", id));
        }
    }

    @Override
    public List<CourseDto> listCoursesReleaseToCampus(Integer cId, CustomPage customPage) {
        List<Integer> idList=getCourseIdListOfManager(customPage,cId);
        if(ObjectUtils.isEmpty(idList)){
            return null;
        }
        return COURSE_CONVERTER.toListCourseDto(iCourseService.listByIds(idList));
    }

    @Override
    public Boolean existCourseUnderCampus(Integer campusId) {
        return count(new QueryWrapper<CampusCourse>().eq("campus_id",campusId))>0;
    }

    @Override
    public void checkCourseReleationShip(Integer id) {
        long count = count(new QueryWrapper<CampusCourse>().eq("course_id", id));
        if (count > 0) {
            throw new CourseException("课程和相关主校区相关联,无法删除");
        }
    }
}
