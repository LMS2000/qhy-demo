package com.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.course.entity.dao.CourserUploadLog;
import com.course.entity.dto.CourserUploadLogDto;
import com.course.entity.vo.CourserUploadLogVo;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;
public interface ICourserUploadLogService extends IService<CourserUploadLog> {
    Integer saveCourserUploadLog(CourserUploadLogVo courseruploadlogVo);
    CourserUploadLogDto getCourserUploadLogById(Integer id);
    List<CourserUploadLogDto> listCourserUploadLog(CustomPage customPage);
    void delCourserUploadLogById(Integer id);

    void deleteUploadedFile(List<Integer> uploadFileLogRecordList);
}
