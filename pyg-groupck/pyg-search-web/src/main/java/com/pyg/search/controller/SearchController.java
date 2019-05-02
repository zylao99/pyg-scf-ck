package com.pyg.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.search.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by on 2018/8/23.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    //注入搜索服务对象
    @Reference(timeout = 1000000)
    private SearchService searchService;
    /**
     * 需求：根据查询的关键词，搜索商品数据
     * 参数：Map searchMap
     * 返回值：Map
     */
    @RequestMapping("searchList")
    public Map searchList(@RequestBody Map searchMap){
        //调用远程service服务方法，实现搜索
        Map map = searchService.searchList(searchMap);
        return  map;
    }

}
