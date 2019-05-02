package com.tensquare.articlecrawler.proceccor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 文章爬取处理类
 */
@Component
public class ArticleProcessor implements PageProcessor{

    @Override
    public void process(Page page) {
        //使用正则表达式匹配指定的url
        // 只需要抓取文章详细页面
        page.addTargetRequests(page.getHtml().regex("https://blog.csdn.net/[a-zA-Z0-9_]+/article/details/[0-9]{8}").all());


        //提取文章的标题
        String title = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1/text()").toString();
        //提取文章的内容
        String content = page.getHtml().xpath("//*[@id=\"article_content\"]").toString();

        //判断title和content不为空
        if(title!=null && content!=null){
            //把title和content存放ResultItems，交给Pipeline处理
            page.putField("title",title);
            page.putField("content",content);
        }else{
            //跳过这个请求
            page.setSkip(true);
        }
    }

    /**
     * 设置全局参数
     * @return
     */
    @Override
    public Site getSite() {
        return Site.me()
                //超时时间
                .setTimeOut(1000)
                //设置重试次数
                .setRetryTimes(3)
                .setSleepTime(100);
    }
}
