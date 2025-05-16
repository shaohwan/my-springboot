package com.demo.daniel.config;

import com.demo.daniel.properties.AwsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@ConditionalOnProperty(name = "cloud.provider", havingValue = "aws")
public class AwsConfig {

    @Bean
    public S3Client s3Client(AwsProperties awsProperties) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(awsProperties.getAccessKeyId()
                , awsProperties.getSecretAccessKey());
        return S3Client.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
