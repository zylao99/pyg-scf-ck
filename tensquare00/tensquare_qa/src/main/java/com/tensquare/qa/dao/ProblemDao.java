package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * 分页查询最新问答列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where id in( select problemid from Pl where labelid=?1 ) order by p.updatetime desc")
    Page<Problem> findNewlistByLabelId(String labelid, Pageable pageable);


    /**
     * 分页查询热门回答列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where id in( select problemid from Pl where labelid=?1 ) order by p.reply desc")
    Page<Problem> findHotlistByLabelId(String labelid, Pageable pageable);


    /**
     * 等待回答列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid=?1) and p.reply=0 order by p.createtime desc")
    Page<Problem> findWaitlistByLabelid(String labelid, Pageable pageable);
}
