package com.pyg.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.content.service.ContentService;
import com.pyg.pojo.TbCities;
import com.pyg.pojo.TbContent;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by on 2018/8/22.
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference(timeout = 10000000)
    private ContentService contentService;
    /**
     * 需求：根据广告分类id查询广告内容
     * 参数：Long categoryId
     * 返回值：List<TbCOntent>
     * 开发一个restfull风格接口
     * 接口地址：http://www.pinyougou.com/content/findContentListByCategoryId/1
     */
    @RequestMapping("findContentListByCategoryId/{categoryId}")
    public List<TbContent> findContentListByCategoryId(@PathVariable Long categoryId){
        //调用广告内容服务对象方法
        List<TbContent> contentList = contentService.findContentListByCategoryId(categoryId);
        return contentList;
    }
}
