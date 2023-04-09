package com.oss.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RequiredArgsConstructor
public class S3OssClient implements OssClient{

    //使用@RequiredArgsConstructor注解并且字段上用了final修饰lombok会在字段上修饰@Autowired注入
    private final AmazonS3 amazonS3;

    /**
     * 创建文件桶
     * @param bucketName
     */
    @Override
    public void createBucket(String bucketName) {
          if(!amazonS3.doesBucketExistV2(bucketName)){
              amazonS3.createBucket(bucketName);
          }
    }

    /**
     * 获取文件访问url
     * @param bucketName
     * @param objectName
     * @return
     */
    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        URL url = amazonS3.getUrl(bucketName, objectName);
        return url.toString();
    }

    /**
     * 获取文件元数据
     * @param bucketName
     * @param objectName
     * @return
     */
    @Override
    public S3Object getObjectInfo(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName,objectName);
    }

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
    @Override
    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) throws IOException {
        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentType(contextType);
        objectMetadata.setContentLength(size);
        PutObjectRequest putObjectRequest=
                new PutObjectRequest(bucketName,objectName,stream,objectMetadata);
        putObjectRequest.getRequestClientOptions().setReadLimit(Long.valueOf(size).intValue()+1);
        return amazonS3.putObject(putObjectRequest);
    }

    /**
     * 获取AmazonS3实例
     * @return
     */
    @Override
    public AmazonS3 getS3Client() {
       return amazonS3;
    }
}
