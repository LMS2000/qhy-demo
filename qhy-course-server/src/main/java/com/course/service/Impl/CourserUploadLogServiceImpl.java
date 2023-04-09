package com.course.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.course.client.CourseUploadClient;
import com.course.entity.dao.CourserUploadLog;
import com.course.entity.dto.CourserUploadLogDto;
import com.course.entity.vo.CourserUploadLogVo;
import com.course.mapper.CourserUploadLogMapper;
import com.course.service.ICourserUploadLogService;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.course.entity.factory.CourserUploadLogFactory.COURSERUPLOADLOG_CONVERTER;

@Service
@RequiredArgsConstructor
public class CourserUploadLogServiceImpl extends ServiceImpl<CourserUploadLogMapper, CourserUploadLog> implements ICourserUploadLogService {
    private final CourseUploadClient courseUploadClient;

    @Override
    public Integer saveCourserUploadLog(CourserUploadLogVo courseruploadlogVo) {
        CourserUploadLog courseruploadlog = COURSERUPLOADLOG_CONVERTER.toCourserUploadLog(courseruploadlogVo);
        save(courseruploadlog);
        return courseruploadlog.getId();
    }

    @Override
    public CourserUploadLogDto getCourserUploadLogById(Integer id) {
        CourserUploadLog courseruploadlog = getById(id);
        return COURSERUPLOADLOG_CONVERTER.toCourserUploadLogDto(courseruploadlog);
    }

    @Override
    public List<CourserUploadLogDto> listCourserUploadLog(CustomPage customPage) {
        List<CourserUploadLog> courseruploadlogList = CustomPage.getResult(customPage, new CourserUploadLog(), this, null);
        return COURSERUPLOADLOG_CONVERTER.toListCourserUploadLogDto(courseruploadlogList);
    }

    @Override
    public void delCourserUploadLogById(Integer id) {
        removeById(id);
    }

    @Override
    public void deleteUploadedFile(List<Integer> uploadFileLogRecordList) {
        if(uploadFileLogRecordList.isEmpty()){
            return;
        }
        List<CourserUploadLog> courserUploadLogs = listByIds(uploadFileLogRecordList);
        try{
            for (CourserUploadLog courserUploadLog : courserUploadLogs) {
                courseUploadClient.deleteFile(courserUploadLog.getBucketName(),courserUploadLog.getFileName());
            }
        } catch (Exception e){
            log.error("课程文件上传失败,删除已经上传成功的课程失败时出现异常: ",e);
        }
        removeBatchByIds(uploadFileLogRecordList);
    }
}
