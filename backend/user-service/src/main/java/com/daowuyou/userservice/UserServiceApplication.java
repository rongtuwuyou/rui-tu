package com.daowuyou.userservice;

import com.daowuyou.api.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "com.daowuyou.api.client", defaultConfiguration= FeignConfig.class)
@SpringBootApplication
//@MapperScan({"com.daowuyou.userservice.mapper"}) //mybatisConfig中配置了
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}