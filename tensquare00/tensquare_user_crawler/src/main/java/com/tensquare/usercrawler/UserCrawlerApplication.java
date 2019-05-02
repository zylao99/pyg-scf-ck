package com.tensquare.usercrawler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import us.codecraft.webmagic.scheduler.RedisScheduler;
import utils.IdWorker;

/**
 * 用户爬虫微服务
 */
@SpringBootApplication
@EnableScheduling  // 开启定时任务注解@Scheduled
public class UserCrawlerApplication {

    @Value("${redis.host}")
    private String redisHost;


    public static void main(String[] args) {
        SpringApplication.run(UserCrawlerApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

    /**
     * 初始化RedisScheduler
     */
    @Bean
    public RedisScheduler redisScheduler(){
        return new RedisScheduler(redisHost);
    }

}
