package com.pyg.search.listener;

import com.alibaba.fastjson.JSON;
import com.pyg.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/8/26.
 * 接受消息，实现索引库同步
 * 业务流程：
 * 1，接受消息
 * 2，把消息转换为对象
 * 3，使用solr模板把数据添加索引库即可实现索引库同步
 */
public class SearchListener implements MessageListener{

    //注入solr模板对象
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {

        if(message instanceof TextMessage){
            try {
                TextMessage m = (TextMessage) message;
                //获取消息
                String itemJson = m.getText();
                //转换list集合
                List<TbItem> itemList = JSON.parseArray(itemJson, TbItem.class);

                //循环集合设置规格值
                for (TbItem tbItem : itemList) {
                    //获取商品规格值
                    String spec = tbItem.getSpec();
                    //转换成Map
                    Map<String,String> specMap = (Map<String,String>) JSON.parse(spec);
                    //设置商品对象规格map
                    tbItem.setSpecMap(specMap);

                }

                //把集合保存到索引库即可
                solrTemplate.saveBeans(itemList);
                solrTemplate.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }
}
