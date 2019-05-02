package com.tensquare.usercrawler.proceccor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 用户爬取处理类
 */
@Component
public class UserProcessor implements PageProcessor{

    @Override
    public void process(Page page) {
        //使用正则表达式匹配指定的url
        // 只需要抓取文章详细页面
        page.addTargetRequests(page.getHtml().regex("https://blog.csdn.net/[a-zA-Z0-9_]+/article/details/[0-9]{8}").all());

        //昵称
        String nickname = page.getHtml().xpath("//*[@id=\"uid\"]/text()").toString();
        //用户头像
        //css(tagname,attrname)：   .css("img","src"): 取当前标签下的img标签的src属性值
        String avatar = page.getHtml().xpath("//*[@id=\"asideProfile\"]/div[1]/div[1]/a").css("img","src").toString();


        //nickname
        if(nickname!=null && avatar!=null){
            //把nickname和avatar存放ResultItems，交给Pipeline处理
            page.putField("nickname",nickname);
            page.putField("avatar",avatar);
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
                .setSleepTime(3000);
    }
}
