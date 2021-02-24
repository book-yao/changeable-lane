package com.supcon.changeablelane;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@ComponentScan(basePackages = "com.supcon.changeablelane")
@MapperScan("com.supcon.changeablelane.mapper")
@SpringBootApplication
public class ChangeableLaneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChangeableLaneApplication.class, args);
    }

}
