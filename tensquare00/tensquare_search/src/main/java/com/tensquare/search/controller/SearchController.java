package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.SearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@CrossOrigin
@RefreshScope
public class SearchController {
    @Autowired
    private SearchService searchService;


    /**
     * 添加文章到 Elasticsearch
     *
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Article article){
        searchService.add(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @RequestMapping(value = "/{keywords}/{page}/{size}",method = RequestMethod.GET)
    public Result findSearch(@PathVariable String keywords,@PathVariable int page,@PathVariable int size){
        Page<Article> pageList = searchService.findSearch(keywords,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()));
    }
}
