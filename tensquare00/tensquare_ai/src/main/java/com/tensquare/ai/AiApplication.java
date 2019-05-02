package com.tensquare.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 人工智能微服务
 */
@SpringBootApplication
@EnableScheduling // 开启定时任务
public class AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class,args);
    }


}
