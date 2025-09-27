package com.daowuyou.sessionservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "spring.minio")
public class MinioConfigProperties {
    private String endpoint;
    private String accesskey; // User
    private String secretKey;
}