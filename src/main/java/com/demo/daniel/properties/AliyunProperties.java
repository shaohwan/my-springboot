package com.demo.daniel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliyunProperties {

    private String ossEndpoint;
    private String accessKeyId;
    private String secretAccessKey;
}
