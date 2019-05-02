package com.tensquare.articlecrawler.task;

import com.tensquare.articlecrawler.pipeline.ArticleDbPipeline;
import com.tensquare.articlecrawler.pipeline.ArticleTxtPipline;
import com.tensquare.articlecrawler.proceccor.ArticleProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * 文章爬取任务类
 */
@Component
public class ArticleTask {

    @Autowired
    private ArticleProcessor articleProcessor;

    @Autowired
    private ArticleTxtPipline articleTxtPipline;

    @Autowired
    private ArticleDbPipeline articleDbPipeline;

    @Autowired
    private RedisScheduler redisScheduler;

    /**
     * 爬取ai文章的任务方法
     * 秒 分 时 天 月 星期几
     */
    @Scheduled(cron = "10 16 10 * * ?")
    public void aiTask(){
        //设置栏目id
        articleDbPipeline.setChannelId("ai");
        articleTxtPipline.setChannelId("ai");

        Spider.create(articleProcessor)
                .addUrl("https://blog.csdn.net/nav/ai")
                .addPipeline(articleDbPipeline)
                .addPipeline(articleTxtPipline)
                .setScheduler(redisScheduler)
                .start();
    }


    /**
     * 爬取db文章的任务方法
     * 秒 分 时 天 月 星期几
     */
    @Scheduled(cron = "40 26 10 * * ?")
    public void dbTask(){
        //设置栏目id
        articleDbPipeline.setChannelId("db");
        articleTxtPipline.setChannelId("db");

        Spider.create(articleProcessor)
                .addUrl("https://blog.csdn.net/nav/db")
                .addPipeline(articleDbPipeline)
                .addPipeline(articleTxtPipline)
                .setScheduler(redisScheduler)
                .start();
    }

    /**
     * 爬取web文章的任务方法
     * 秒 分 时 天 月 星期几
     */
    @Scheduled(cron = "0 29 10 * * ?")
    public void webTask(){
        //设置栏目id
        articleDbPipeline.setChannelId("web");
        articleTxtPipline.setChannelId("web");

        Spider.create(articleProcessor)
                .addUrl("https://blog.csdn.net/nav/web")
                .addPipeline(articleDbPipeline)
                .addPipeline(articleTxtPipline)
                .setScheduler(redisScheduler)
                .start();
    }
}
