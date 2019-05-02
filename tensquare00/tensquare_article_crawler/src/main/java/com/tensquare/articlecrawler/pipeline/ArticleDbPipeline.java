package com.tensquare.articlecrawler.pipeline;

import com.tensquare.articlecrawler.dao.ArticleDao;
import com.tensquare.articlecrawler.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.IdWorker;

/**
 * 文章入库类
 */
@Component
public class ArticleDbPipeline implements Pipeline{

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ArticleDao articleDao;

    //设置栏目ID
    private String channelId;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        //从ResultItems取出内容
        String title = (String)resultItems.get("title");
        String content = (String)resultItems.get("content");

        //创建Article对象
        Article article = new Article();
        article.setId(idWorker.nextId()+"");
        article.setTitle(title);
        article.setContent(content);
        //设置栏目
        article.setChannelid(channelId);

        //文章入库
        articleDao.save(article);
    }
}
