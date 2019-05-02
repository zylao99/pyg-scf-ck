package com.pyg.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pojo.TbItem;
import com.pyg.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by on 2018/8/23.
 */
@Service
public class SearchServiceImpl implements SearchService {


    //注入solr模板对象
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 需求：根据查询的关键词，搜索商品数据
     * 参数：Map searchMap
     * 返回值：Map
     */
    public Map searchList(Map searchMap) {
        //创建高亮query查询对象，封装所有查询条件
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //1,关键词查询，
        // 获取主查询条件参数
        String keywords = (String) searchMap.get("keywords");
        //创建criteria对象，设置查询条件
        Criteria criteria = null;
        //判断参数是否存在
        if (keywords != null && !"".equals(keywords)) {
            criteria = new Criteria("item_keywords").is(keywords);
        } else {
            //查询所有
            criteria = new Criteria().expression("*:*");
        }

        //把条件对象放入query对象
        query.addCriteria(criteria);

        //2，高亮查询
        //创建高亮对象
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置高亮字段
        highlightOptions.addField("item_title");
        //设置高亮前缀
        highlightOptions.setSimplePrefix("<font color='red'>");
        //设置高亮后缀
        highlightOptions.setSimplePostfix("</font>");

        //把高亮对象添加到查询对象中
        query.setHighlightOptions(highlightOptions);


        //3,分类条件过滤查询
        //获取分类参数
        String category = (String) searchMap.get("category");
        //判断分类参数是否存在
        if (category != null && !"".equals(category)) {
            //创建criteria对象，添加查询条件
            Criteria criteria1 = new Criteria("item_category").is(category);
            //创建过滤查询对象
            FilterQuery filterQuery = new SimpleFilterQuery(criteria1);

            //把过滤查询对象添加到查询对象中
            query.addFilterQuery(filterQuery);

        }

        //4,品牌条件过滤查询
        //获取品牌参数
        String brand = (String) searchMap.get("brand");
        //判断分类参数是否存在
        if (brand != null && !"".equals(brand)) {
            //创建criteria对象，添加查询条件
            Criteria criteria1 = new Criteria("item_brand").is(brand);
            //创建过滤查询对象
            FilterQuery filterQuery = new SimpleFilterQuery(criteria1);
            //把过滤查询对象添加到查询对象中
            query.addFilterQuery(filterQuery);

        }

        //5,价格区间过滤查询
        //价格参数：0-500元 500-1000元 1000-1500元 1500-2000元 2000-3000元 3000-*元以上
        //获取价格参数
        String price = (String) searchMap.get("price");
        //判断价格参数是否存在
        if (price != null && !"".equals(price)) {
            //切分价格参数
            String[] prices = price.split("-");
            //判断是否是0区间
            if (!prices[0].equals("0")) {
                //创建criteria对象，添加查询条件
                Criteria criteria1 = new Criteria("item_price").greaterThanEqual(prices[0]);
                //创建过滤查询对象
                FilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                //把过滤查询对象添加到查询对象中
                query.addFilterQuery(filterQuery);

            }

            if (!prices[1].equals("*")) {
                //创建criteria对象，添加查询条件
                Criteria criteria1 = new Criteria("item_price").lessThanEqual(prices[1]);
                //创建过滤查询对象
                FilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                //把过滤查询对象添加到查询对象中
                query.addFilterQuery(filterQuery);
            }


        }


        //6,规格条件过滤查询
        //获取规格参数
        Map<String,String> specMap = (Map<String, String>) searchMap.get("spec");
        //判断规格参数是否存在
        if(specMap!=null){
            //循环规格参数
            for(String key:specMap.keySet()){

                //创建criteria对象，添加查询条件
                Criteria criteria1 = new Criteria("item_spec_"+key).is(specMap.get(key));
                //创建过滤查询对象
                FilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                //把过滤查询对象添加到查询对象中
                query.addFilterQuery(filterQuery);

            }
        }

        //7,商品搜索排序功能实现
        //获取排序字段
        String sortField= (String) searchMap.get("sortField");
        //获取排序方式
        String sort = (String) searchMap.get("sort");
        if(sortField!=null && !"".equals(sortField) && sort!=null && !"".equals(sort)){
            //判断排序方式是升序，还是降序
            if("ASC".equals(sort)){
                Sort sort1 = new Sort(Sort.Direction.ASC,"item_"+sortField);
                //把排序对象放入查询对象
                query.addSort(sort1);
            }

            if("DESC".equals(sort)){
                Sort sort1 = new Sort(Sort.Direction.DESC,"item_"+sortField);
                //把排序对象放入查询对象
                query.addSort(sort1);
            }
        }

        //8,分页查询
        //获取分页起始页
        Integer page = (Integer) searchMap.get("page");
        Integer pageSize = (Integer) searchMap.get("pageSize");

        //判断
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize = 40;
        }

        //计算查询起始角标
        int startNo = (page-1)*pageSize;

        //设置分页查询
        query.setOffset(startNo);
        query.setRows(pageSize);




        //执行查询
        HighlightPage<TbItem> hList = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //获取总记录数
        long totalElements = hList.getTotalElements();
        //获取总记录
        List<TbItem> itemList = hList.getContent();

        //循环获取文档集合数据，获取高亮数据
        for (TbItem tbItem : itemList) {

            //获取高查询结果对象获取高亮结果
            List<HighlightEntry.Highlight> highlights = hList.getHighlights(tbItem);
            //判断高亮是否存在
            if (highlights != null && highlights.size() > 0) {

                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();

                tbItem.setTitle(snipplets.get(0));

            }

        }

        //创建map对象，封装查询结果
        Map maps = new HashMap();
        maps.put("totalPages",hList.getTotalPages());
        maps.put("total", totalElements);
        maps.put("rows", itemList);


        return maps;
    }
}
