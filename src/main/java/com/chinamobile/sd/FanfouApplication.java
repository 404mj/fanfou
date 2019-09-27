package com.chinamobile.sd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.chinamobile.sd")
@MapperScan("com.chinamobile.sd.dao")
@EnableScheduling
public class FanfouApplication {

    public static void main(String[] args) {
        SpringApplication.run(FanfouApplication.class, args);
    }

}
