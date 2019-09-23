package com.chinamobile.sd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages =  "com.chinamobile.sd")
@MapperScan("com.chinamobile.sd.dao")
public class FanfouApplication {

    public static void main(String[] args) {
        SpringApplication.run(FanfouApplication.class, args);
    }

}
