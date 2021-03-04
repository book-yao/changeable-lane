package com.supcon.changeablelane;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.*;


@ComponentScan(basePackages = "com.supcon.changeablelane")
@MapperScan("com.supcon.changeablelane.mapper")
@SpringBootApplication
@Slf4j
public class ChangeableLaneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChangeableLaneApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate build =
                restTemplateBuilder
                        .setConnectTimeout(Duration.ofSeconds(30))
                        .setReadTimeout(Duration.ofSeconds(60))
                        //            .detectRequestFactory(true)
                        .requestFactory(() -> new SimpleClientHttpRequestFactory())
                        .build();

        return build;
    }

    @Bean(value = "scheduledExecutorService", destroyMethod = "shutdown")
    public ScheduledExecutorService traceableScheduledExecutorService() {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(
                        200,
                        new ThreadFactoryBuilder()
                                .setNameFormat("self-adaptive-thread-pool-%d")
                                .setUncaughtExceptionHandler(
                                        (t, e) -> {
                                            log.error(
                                                    "thread name:{}, error info:{}",
                                                    t.getName(),
                                                    ExceptionUtils.getStackTrace(e));
                                        })
                                .build());

        return scheduledExecutorService;
    }

    @Bean(value = "executorService", destroyMethod = "shutdown")
    public ExecutorService traceableExecutorService() {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(
                        20,
                        500,
                        3000,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(100),
                        new ThreadFactoryBuilder()
                                .setNameFormat("traceable-thread-pool-%d")
                                .setUncaughtExceptionHandler(
                                        (t, e) -> log.error("thread name:{}, error info:{}", t.getName(), ExceptionUtils.getStackTrace(e))
                                )
                                .build(),
                        // 如果线程池的线程数量达到上限，该策略会把任务队列中的任务放在调用者线程当中运行
                        new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolExecutor;
    }

}
