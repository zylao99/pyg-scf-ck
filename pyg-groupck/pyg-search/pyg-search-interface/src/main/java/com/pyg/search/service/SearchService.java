package com.pyg.search.service;

import java.util.Map;

/**
 * Created by on 2018/8/23.
 */
public interface SearchService {

    /**
     * 需求：根据查询的关键词，搜索商品数据
     * 参数：Map searchMap
     * 返回值：Map
     */
    public Map searchList(Map searchMap);

}
