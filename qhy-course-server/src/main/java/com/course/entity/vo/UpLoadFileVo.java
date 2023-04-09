package com.course.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpLoadFileVo {
    //--- 通用返回结果

    /**
     * 文件存放的存储桶
     */
    private String bucketName;
    /**
     * 对于单文件上传而言为: dir+fileName,如果上传的是课程压缩文件,则为dir
     */
    private String path;

    //----上传普通单文件传回的结果

    private String url;
    private String fileMd5;

    //----上传压缩课程文件传回的结果

    /**
     * 课程封面路径--多个,使用字符串拼接
     */
    private String courseCoverFileUrl;
    /**
     * 课程文件路径--html文件路径
     */
    private String courseFileUrl;
}
