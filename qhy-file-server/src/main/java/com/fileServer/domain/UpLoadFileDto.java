package com.fileServer.domain;

import lombok.Data;

/**
 * @author 大忽悠
 */
@Data
public class UpLoadFileDto extends BaseUpLoadDto{
    private String url;
    private String fileMd5;

    public UpLoadFileDto(String bucketName, String fileName, String fileUrl, String fileMd5) {
        this.bucketName=bucketName;
        this.path=fileName;
        this.url=fileUrl;
        this.fileMd5=fileMd5;
    }
}
