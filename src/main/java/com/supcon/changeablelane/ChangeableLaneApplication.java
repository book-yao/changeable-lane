package com.supcon.changeablelane;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@ComponentScan(basePackages = "com.supcon.changeablelane")
@MapperScan("com.supcon.changeablelane.mapper")
@SpringBootApplication
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

}
