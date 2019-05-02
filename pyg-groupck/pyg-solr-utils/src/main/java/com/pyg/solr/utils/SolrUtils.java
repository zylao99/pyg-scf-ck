package com.pyg.solr.utils;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/8/23.
 */
@Component
public class SolrUtils {

    //注入商品mapper接口代理对象
    @Autowired
    private TbItemMapper itemMapper;

    //注入solr模板对象
    @Autowired
    private SolrTemplate solrTemplate;

    //微服务
    //java -jar xx.jar
    public static void main(String[] args) {

        //加载solr配置文件，连接solrF服务器
        ApplicationContext app =
                new ClassPathXmlApplicationContext("classpath*:spring/*.xml");
        //从spring容器中获取solrUtils
        SolrUtils solrUtils = app.getBean(SolrUtils.class);
        //调用方法，实现索引库数据初始化
        solrUtils.initSolrIndex();
    }

    /**
     * 实现索引库数据初始化
     */
    private void initSolrIndex() {

        //查询所有商品数据，把数据导入索引库
        //创建example对象
        TbItemExample example = new TbItemExample();
        //创建criteria对象，设置条件查询
        TbItemExample.Criteria criteria = example.createCriteria();
        //设置查询参数
        //此商品必须处于启用状态
        criteria.andStatusEqualTo("1");
        //查询
        List<TbItem> itemList = itemMapper.selectByExample(example);

        //循环集合，给动态域字段对应属性赋值
        for (TbItem tbItem : itemList) {

            //获取规格属性
            //{"机身内存":"16G","网络":"联通3G"}
            String spec = tbItem.getSpec();
            //把spec转换成map对象
            Map<String,String> specMap = (Map<String, String>) JSON.parse(spec);
            //添加值
            tbItem.setSpecMap(specMap);

        }

        solrTemplate.saveBeans(itemList);
        //提交
        solrTemplate.commit();
    }

}
