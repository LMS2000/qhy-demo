package com.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties  {

    private boolean enable = true;

    private String accessKey;

    private String accessSecret;

    /**
     * endpoint 配置格式为
     * 通过外网访问OSS服务时，以URL的形式表示访问的OSS资源，详情请参见OSS访问域名使用规则。OSS的URL结构为[$Schema]://[$Bucket].[$Endpoint]/[$Object]
     * 。例如，您的Region为华东1（杭州），Bucket名称为examplebucket，Object访问路径为destfolder/example.txt，
     * 则外网访问地址为https://examplebucket.oss-cn-hangzhou.aliyuncs.com/destfolder/example.txt
     * https://help.aliyun.com/document_detail/375241.html
     */
    private String endpoint;
    /**
     * refer com.amazonaws.regions.Regions;
     * 阿里云region 对应表
     * https://help.aliyun.com/document_detail/31837.htm?spm=a2c4g.11186623.0.0.695178eb0nD6jp
     */
    private String region;

    private String rootPath="";

    private boolean pathStyleAccess = true;



}
