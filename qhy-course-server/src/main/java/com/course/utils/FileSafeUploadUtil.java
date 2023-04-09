package com.course.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.extra.spring.SpringUtil;
import com.course.client.CourseUploadClient;
import com.course.entity.vo.CourserUploadLogVo;
import com.course.entity.vo.UpLoadFileVo;
import com.course.exception.CourseException;
import com.course.service.ICourserUploadLogService;
import com.easyCode.feature.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
public class FileSafeUploadUtil {


    /**
     * 上传文件，过程中异常就回滚
     * @param file
     * @param uploadFileLogRecordList
     * @param bucket
     * @param dir
     * @param zipFile
     * @return
     */
    public static UpLoadFileVo doUpload(MultipartFile file, List<Integer> uploadFileLogRecordList,
                                        String bucket, String dir, boolean zipFile) {
        CourseUploadClient uploadClient = SpringUtil.getBean(CourseUploadClient.class);
        ICourserUploadLogService courserUploadLogService = SpringUtil.getBean(ICourserUploadLogService.class);
        Result result;
        if(zipFile){
          result = uploadClient.uploadZipFile(file,bucket,dir);
        }else{
          result  = uploadClient.uploadFile(file,bucket,dir);
        }
        UpLoadFileVo upLoadFileVo=null;
        try{
            upLoadFileVo = BeanUtil.toBean(result,UpLoadFileVo.class, CopyOptions.create().ignoreCase().ignoreError().ignoreNullValue());
            uploadFileLogRecordList.add(
                    courserUploadLogService
                            .saveCourserUploadLog(CourserUploadLogVo.builder()
                                    .fileMd5(upLoadFileVo.getFileMd5())
                                    .bucketName(bucket)
                                    .fileName(upLoadFileVo.getPath()).build()
                          ));
            return upLoadFileVo;
        }catch (Exception e){
            if (upLoadFileVo != null) {
                uploadClient.deleteFile(bucket, upLoadFileVo.getPath());
            }
            log.error("课程文件上传过程中出现异常: ", e);
            throw new CourseException("课程文件上传出现异常");
        }
    }


    /**
     * 上传文件
     * @param file
     * @param uploadFileLogRecordList
     * @param bucket
     * @param dir
     * @return
     */
    public static UpLoadFileVo uploadFile(MultipartFile file, List<Integer> uploadFileLogRecordList, String bucket, String dir) {
      return   doUpload(file,uploadFileLogRecordList,bucket,dir,false);
    }

    /**
     * 上传压缩文件
     * @param file
     * @param uploadFileLogRecordList
     * @param bucket
     * @param dir
     * @return
     */
    public static UpLoadFileVo uploadZipFile(MultipartFile file, List<Integer> uploadFileLogRecordList, String bucket, String dir){
        return doUpload(file,uploadFileLogRecordList,bucket,dir,true);
    }

    /**
     * 删除文件
     *
     * @param uploadFileLogRecordList
     */
    public static void deleteFile(List<Integer> uploadFileLogRecordList) {
        ICourserUploadLogService courserUploadLogService = SpringUtil.getBean(ICourserUploadLogService.class);
        courserUploadLogService.deleteUploadedFile(uploadFileLogRecordList);
    }

    /**
     * 删除上传文件日志
     *
     * @param uploadFileLogRecordList
     */
    public static void deleteLogRecord(List<Integer> uploadFileLogRecordList) {
        ICourserUploadLogService courserUploadLogService = SpringUtil.getBean(ICourserUploadLogService.class);
        courserUploadLogService.removeByIds(uploadFileLogRecordList);
    }
}
