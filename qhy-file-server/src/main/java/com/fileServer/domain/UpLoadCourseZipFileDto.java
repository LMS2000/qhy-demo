package com.fileServer.domain;

import lombok.Data;

/**
 * 课程压缩文件包上传需要特殊处理
 * @author 大忽悠
 * @create 2023/2/27 21:03
 */
@Data
public class UpLoadCourseZipFileDto extends BaseUpLoadDto{
    /**
     * 课程封面路径--多个,使用字符串拼接
     */
    private String courseCoverFileUrl;
    /**
     * 课程文件路径--html文件路径
     */
    private String courseFileUrl;

    public UpLoadCourseZipFileDto(String dirName, String bucketName, String courseFileUrl, String imgPathUrl) {
        this.bucketName=bucketName;
        this.path=dirName;
        this.courseFileUrl=courseFileUrl;
        this.courseCoverFileUrl=imgPathUrl;
    }
}
