package com.pyg.solr;

import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2018/8/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-solr.xml")
public class MySolrJ {

    //注入solr模板对象
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 需求：spring data solr入门案例
     * 要求：把数据添加到索引库
     * 问题：如何让数据和索引库域字段进行一一对应映射关系，从而可以把导入相应索引字段中？
     * spring-data-solr注解：@Field(”域字段名称“)  此注解必须添加到javabrand对象上面
     * 添加：如果id不存在，表示添加操作
     * 修改：如果id存在，表示修改操作
     */
    @Test
    public void addIndex() {
        //创建商品对象
        TbItem item = new TbItem();
        item.setId(1000000000L);
        item.setTitle("pojo数据，通过注解域字段名称映射到索引库");
        item.setSellPoint("非常简单，确实非常简单，只需要学会api使用");
        item.setBrand("spring data solr");

        solrTemplate.saveBean(item);

        solrTemplate.commit();

    }

    /**
     * 需求：索引库删除操作
     */
    @Test
    public void deleteIndex() {
        //根据id删除
        // solrTemplate.deleteById("1000000000");
        //创建集合
        List<String> ids = new ArrayList<>();
        ids.add("1000000000");
        //批量删除
        solrTemplate.deleteById(ids);
        solrTemplate.commit();

    }

    /**
     * 需求：根据id查询
     */
    @Test
    public void findByID(){
        TbItem item = solrTemplate.getById(1000000000L, TbItem.class);
        System.out.println(item);
    }

    /**
     * 分页查询
     */
    @Test
    public void findByPage(){
        //创建query对象，封装所有查询条件
        Query query = new SimpleQuery("*:*");

        //设置查询条件
        //设置分页查询起始位置
        query.setOffset(0);
        //设置分页查询每页显示条数
        query.setRows(10);

        //执行查询
        ScoredPage<TbItem> itemList = solrTemplate.queryForPage(query, TbItem.class);

        //获取查询总记录数
        long totalElements = itemList.getTotalElements();
        System.out.println("总记录数："+totalElements);
        //总记录集合数据
        List<TbItem> list= itemList.getContent();
        System.out.println(list);


    }

    /**
     * 条件查询
     */
    @Test
    public void findByCondition(){
        //创建query对象，封装所有查询条件
        Query query = new SimpleQuery();

        //创建criteria对象，封装条件
        //is : 包含  语法： item_title:数据
        Criteria criteria = new Criteria("item_title").is("数据");

        //包含
        criteria.and("item_title").contains("pojo");

        query.addCriteria(criteria);

        //执行查询
        ScoredPage<TbItem> itemList = solrTemplate.queryForPage(query, TbItem.class);

        //获取查询总记录数
        long totalElements = itemList.getTotalElements();
        System.out.println("总记录数："+totalElements);
        //总记录集合数据
        List<TbItem> list= itemList.getContent();
        System.out.println(list);


    }

    /**
     * 高亮查询
     */
    @Test
    public void findHighLight(){
        //创建高亮query对象，封装所有查询条件
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //创建criteria对象，封装条件
        //is : 包含  语法： item_title:数据
        Criteria criteria = new Criteria("item_title").is("数据");
        query.addCriteria(criteria);

        //设置高亮
        //创建高亮对象
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置高亮字段
        highlightOptions.addField("item_title");
        //设置高亮前缀
        highlightOptions.setSimplePrefix("<font color='red'>");
        //设置高亮后缀
        highlightOptions.setSimplePostfix("</font>");

        //把高亮对象添加到query查询对象
        query.setHighlightOptions(highlightOptions);


        //执行查询
        HighlightPage<TbItem> itemList = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //获取查询总记录数
        long totalElements = itemList.getTotalElements();
        System.out.println("总记录数："+totalElements);
        //总记录集合数据
        List<TbItem> list= itemList.getContent();

        //循环
        for (TbItem tbItem : list) {

            //获取高亮
            List<HighlightEntry.Highlight> highlights = itemList.getHighlights(tbItem);
            String title = highlights.get(0).getSnipplets().get(0);

            System.out.println(title);
        }

        System.out.println(list);


    }


}
