package com.supcon.changeablelane;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.*;


@ComponentScan(basePackages = "com.supcon.changeablelane")
@MapperScan("com.supcon.changeablelane.mapper")
@SpringBootApplication
@Slf4j
public class ChangeableLaneApplication {

    /**
     * HTTP连接超时为1秒
     */
    private static final int REST_TEMPLATE_CONNECT_TIMEOUT = 30 * 1000;
    /**
     * HTTP读取超时为5秒
     */
    private static final int REST_TEMPLATE_READ_TIMEOUT = 60 * 1000;

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

    @Bean
    public ExecutorService executorService() {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(
                        1,
                        1,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(500),
                        new ThreadFactoryBuilder()
                                .setNameFormat("changeable-lane--pool-%d")
                                .setUncaughtExceptionHandler(
                                        (t, e) -> {
                                            log.error(
                                                    "thread name:{}, error info:{}",
                                                    t.getName(),
                                                    ExceptionUtils.getStackTrace(e));
                                        })
                                .build(),
                        // 如果线程池的线程数量达到上限，该策略会把任务队列中的任务放在调用者线程当中运行
                        new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolExecutor;
    }

}
