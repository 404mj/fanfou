package com.chinamobile.sd.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/2 23:02
 */
@Configuration
public class AsyncTaskPoolConfig {

    @Bean("picTaskExecutor")
    public Executor picTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(6);
        executor.setMaxPoolSize(12);
        executor.setQueueCapacity(24);
        executor.setKeepAliveSeconds(600);
        executor.setThreadNamePrefix("picTaskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
