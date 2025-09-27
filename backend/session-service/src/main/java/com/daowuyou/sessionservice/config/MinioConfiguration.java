package com.daowuyou.sessionservice.config;


import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class MinioConfiguration {
    private final MinioConfigProperties minioConfigProperties;
    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder().endpoint(minioConfigProperties.getEndpoint()).credentials(minioConfigProperties.getAccesskey(), minioConfigProperties.getSecretKey()).build();
        return client;
    }
}
