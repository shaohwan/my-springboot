package com.demo.daniel.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.demo.daniel.properties.AliyunProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "cloud.provider", havingValue = "aliyun")
public class AliyunConfig {

    @Bean
    public OSS ossClient(AliyunProperties aliyunProperties) {
        return new OSSClientBuilder()
                .build(aliyunProperties.getOssEndpoint(),
                        aliyunProperties.getAccessKeyId(),
                        aliyunProperties.getSecretAccessKey());
    }
}
