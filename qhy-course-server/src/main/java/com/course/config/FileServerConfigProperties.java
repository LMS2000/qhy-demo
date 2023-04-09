package com.course.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 大忽悠
 * @create 2023/2/13 20:21
 */
@ConfigurationProperties("file.course")
@Component
@Data
public class FileServerConfigProperties {
     private String coverBucket;
     private String fileBucket;
}
