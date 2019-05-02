package com.tensquare.usercrawler.task;

import com.tensquare.usercrawler.pipeline.UserDbPipeline;
import com.tensquare.usercrawler.proceccor.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * 用户爬取任务类
 */
@Component
public class UserTask {

    @Autowired
    private UserProcessor userProcessor;

    @Autowired
    private UserDbPipeline userDbPipeline;

    @Autowired
    private RedisScheduler redisScheduler;

    /**
     * 爬取用户任务方法
     * 秒 分 时 天 月 星期几
     */
    @Scheduled(cron = "30 21 12 * * ?")
    public void userTask(){
        Spider.create(userProcessor)
                .addUrl("https://blog.csdn.net")
                .addPipeline(userDbPipeline)
                .setScheduler(redisScheduler)
                .start();
    }

}
