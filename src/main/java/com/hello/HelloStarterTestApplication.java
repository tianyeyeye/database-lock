package com.hello;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("com.hello.mapper")
@SpringBootApplication
public class HelloStarterTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloStarterTestApplication.class, args);
    }

}
