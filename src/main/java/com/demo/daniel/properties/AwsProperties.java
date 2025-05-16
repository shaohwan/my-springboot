package com.demo.daniel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws")
@Data
public class AwsProperties {

    private String region;

    private String accessKeyId;

    private String secretAccessKey;

    private S3 s3;

    @Data
    public static class S3 {
        private String bucket;
    }
}
