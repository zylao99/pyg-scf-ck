package com.tensquare.articlecrawler.pipeline;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.HTMLUtil;
import util.IKUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * 把文章保存为txt文本
 */
@Component
public class ArticleTxtPipline implements Pipeline {


    @Value("${ai.dataPath}")
    private String dataPath;

    private String channelId;
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        //取出title和content
        String title = (String)resultItems.get("title");
        String content = (String)resultItems.get("content");

        //把content的内容去掉html的标签
        content = HTMLUtil.delHTMLTag(content);

        //把内容保存到文本中
        try {
            //构建字符输出流
            PrintWriter writer = new PrintWriter(new File(dataPath+"/"+channelId+"/"+ UUID.randomUUID()+".txt"));
            //先分词再写出内容
            writer.print( IKUtil.split(title+" "+content," ")  );
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
