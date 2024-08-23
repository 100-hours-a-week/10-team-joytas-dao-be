package com.example.daobe.common.config;

import com.example.daobe.common.decorator.MdcDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    private static final String ASYNC_THREAD_NAME_PREFIX = "async-exec-";

    @Bean
    public TaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setTaskDecorator(new MdcDecorator());
        taskExecutor.setThreadNamePrefix(ASYNC_THREAD_NAME_PREFIX);
        return taskExecutor;
    }
}
