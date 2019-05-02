package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleDao extends ElasticsearchRepository<Article,String> {

    /**
     * 从字段 title  和 content 搜索文章
     * @param keywords
     * @param keywords1
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContentLike(String keywords, String keywords1, Pageable pageable);
}
