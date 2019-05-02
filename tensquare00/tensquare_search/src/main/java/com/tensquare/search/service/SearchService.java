package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import utils.IdWorker;

import java.util.Date;

@Service
public class SearchService {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 添加文章
     * @param article
     */
    public void add(Article article){
        article.setId(idWorker.nextId()+"");
        article.setCreatetime(new Date());
        article.setUpdatetime(new Date());
        article.setIspublic("1");
        article.setIstop("1");
        article.setVisits(0);
        article.setThumbup(0);
        article.setState("0");
        articleDao.save(article);
    }

    /**
     * 搜索文章
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findSearch(String keywords, int page, int size) {
        return articleDao.findByTitleOrContentLike(keywords,keywords, PageRequest.of(page-1,size));
    }
}
