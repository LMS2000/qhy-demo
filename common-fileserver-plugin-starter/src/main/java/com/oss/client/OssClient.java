package com.oss.client;

import com.amazonaws.http.apache.utils.ApacheUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;

public interface OssClient {

    /**
     * 创建文件捅
     * @param bucketName
     */
    void createBucket(String bucketName);

    /**
     * 获取文件的访问url
     * @param bucketName
     * @param objectName
     * @return
     */
    String getObjectUrl(String bucketName,String objectName);

    /**
     * 获取文件的相关信息（元数据）
     * @param bucketName
     * @param objectName
     * @return
     */
    S3Object getObjectInfo(String bucketName,String objectName);

    /**
     * 上传文件
     * @param bucketName
     * @param objectName
     * @param stream
     * @param size
     * @param contextType
     * @return
     * @throws IOException
     */
    PutObjectResult putObject(String bucketName, String objectName, InputStream stream,long size, String contextType) throws IOException;
   default PutObjectResult putObject(String bucketName,String objectName,InputStream stream) throws IOException{
       return putObject(bucketName,objectName,stream,stream.available(),"application/octet-stream");
   }

    /**
     * 获取AmazonS3实例
     * @return
     */
    AmazonS3 getS3Client();
}
