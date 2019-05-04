package com.pinyougoou.service;

import com.pinyougou.dao.po.TbBrand;

import java.util.List;

public interface Iservice {

    /**
     * 查询所有品牌列表的接口的方法
     * @return
     */
    public List<TbBrand> findAll();

}
