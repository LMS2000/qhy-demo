package com.course.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.course.client.CourseUploadClient;
import com.course.config.FileServerConfigProperties;
import com.course.entity.dao.Course;
import com.course.entity.dao.SmallCourseType;
import com.course.entity.dto.CourseDto;
import com.course.entity.dto.SmallCourseTypeDto;
import com.course.entity.vo.SmallCourseTypeVo;
import com.course.entity.vo.UpLoadFileVo;
import com.course.exception.CourseException;
import com.course.executor.ServiceExecutor;
import com.course.mapper.SmallCourseTypeMapper;
import com.course.service.ICourseService;
import com.course.service.ISmallCourseTypeService;
import com.course.utils.FileSafeUploadUtil;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.course.entity.factory.CourseFactory.COURSE_CONVERTER;
import static com.course.entity.factory.SmallCourseTypeFactory.SMALLCOURSETYPE_CONVERTER;

@Service
@RequiredArgsConstructor
public class SmallCourseTypeServiceImpl extends ServiceImpl<SmallCourseTypeMapper, SmallCourseType> implements ISmallCourseTypeService {
    private final FileServerConfigProperties fileServerConfigProperties;
    private final CourseUploadClient courseUploadClient;
    @Autowired
    private ICourseService iCourseService;

    //---- 超级管理员才能访问

    /**
     * 小类型课程需要课程的封面文件，所以涉及到一个回滚操作，
     * 上传的时候记录下上传文件的日志，如果说在此过程中发生了报错，就根据日志删除文件，相当于回滚操作
     * @param smallcoursetypeVo
     * @return
     */
    @Override
    public Integer saveSmallCourseType(SmallCourseTypeVo smallcoursetypeVo) {
        SmallCourseType.repeatNameCheck(smallcoursetypeVo,this);
        List<Integer> uploadLogRecordList = new ArrayList<>();
        try {
            UpLoadFileVo upLoadFileVo = FileSafeUploadUtil
                    .uploadFile(smallcoursetypeVo.getCoverPath(), uploadLogRecordList,fileServerConfigProperties.getCoverBucket(),"");
            SmallCourseType smallcoursetype = SMALLCOURSETYPE_CONVERTER.toSmallCourseType(smallcoursetypeVo);
            SmallCourseType.fillInfo(smallcoursetype, upLoadFileVo);
            save(smallcoursetype);
            FileSafeUploadUtil.deleteLogRecord(uploadLogRecordList);
            return smallcoursetype.getId();
        } catch (Exception e) {
            FileSafeUploadUtil.deleteFile(uploadLogRecordList);
            log.error("课程小类新增失败: ", e);
            throw new RuntimeException("课程小类新增失败");
        }
    }

    @Override
    public SmallCourseTypeDto getSmallCourseTypeById(Integer id) {
        SmallCourseType smallcoursetype = getById(id);
        return SMALLCOURSETYPE_CONVERTER.toSmallCourseTypeDto(smallcoursetype);
    }

    @Override
    public List<SmallCourseTypeDto> listSmallCourseType(CustomPage customPage) {
        List<SmallCourseType> smallcoursetypeList = CustomPage.getResult(customPage, new SmallCourseType(), this, null);
        return SMALLCOURSETYPE_CONVERTER.toListSmallCourseTypeDto(smallcoursetypeList);
    }

    @Override
    public void delSmallCourseTypeById(Integer id) {
        //不存在
        SmallCourseType smallCourseType = getById(id);
        if (smallCourseType == null) {
            return;
        }
        //如果课程小类下管理着课程就禁止删除
        long count = iCourseService.count(new QueryWrapper<Course>().eq("small_type", id));
        if (count > 0) {
            throw new RuntimeException("当前课程小类关联着课程,不能删除");
        }
        //可以删除
        removeById(id);
        //可以考虑在fileServer端设置一次性删除多个文件 或者 采用日志记录下删除失败的文件
        CompletableFuture.runAsync(() -> {
            try {
                courseUploadClient.deleteFile(smallCourseType.getBucketName(), smallCourseType.getCoverName());
            } catch (Exception e) {
                log.error("课程小类封面文件异步删除过程中出现异常: ", e);
            }
        }, ServiceExecutor.COURSE_THREAD_POOL);
    }

    @Override
    public List<CourseDto> listCourseOfSmallCourseType(Integer id, CustomPage customPage) {
        SmallCourseType smallCourseType = getById(id);
        if (smallCourseType == null) {
            throw new CourseException("课程小类不存在");
        }
        return COURSE_CONVERTER.toListCourseDto(CustomPage.getResult(customPage, new Course(), iCourseService, new QueryWrapper<Course>().eq("small_type", id)));
    }
}
