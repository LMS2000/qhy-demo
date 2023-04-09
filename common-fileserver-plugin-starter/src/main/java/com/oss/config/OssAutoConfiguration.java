package com.oss.config;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.EndpointParams;
import com.oss.client.OssClient;
import com.oss.client.S3OssClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
//启用配置类
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(prefix = "oss", name = "enable", havingValue = "true")
public class OssAutoConfiguration {



    @Bean
    @ConditionalOnMissingBean(S3OssClient.class)
    public OssClient ossClient(AmazonS3 amazonS3){
        return new S3OssClient(amazonS3);
    }

    @Bean
    public AmazonS3 amazonS3(OssProperties ossProperties) {
        long nullSize = Stream.<String>builder()
                .add(ossProperties.getEndpoint())
                .add(ossProperties.getAccessKey())
                .add(ossProperties.getAccessSecret())
                .build()
                .filter(s -> Objects.isNull(s))
                .count();
        if (nullSize > 0) {
            throw new RuntimeException("oss 配置错误，请检查！");
        }
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(ossProperties.getAccessKey(), ossProperties.getAccessSecret());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        return AmazonS3Client.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ossProperties.getEndpoint(), ossProperties.getRegion()))
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(ossProperties.isPathStyleAccess())
                .build();
    }
}
