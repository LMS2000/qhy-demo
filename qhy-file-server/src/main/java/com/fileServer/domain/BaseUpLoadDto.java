package com.fileServer.domain;

import lombok.Data;

/**
 * @author 大忽悠
 * @create 2023/2/28 12:40
 */
@Data
public class BaseUpLoadDto {
    /**
     * 文件存放的存储桶
     */
    protected String bucketName;
    /**
     * 单文件为fileKey=dir+fileName,压缩文件为: 文件存放目录
     */
    protected String path;
}
