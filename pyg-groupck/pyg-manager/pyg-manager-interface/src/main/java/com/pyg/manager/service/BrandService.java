package com.pyg.manager.service;

import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/8/10.
 */
public interface BrandService {
    /**
     * 需求：查询所有品牌数据
     */
    public List<TbBrand> findAll();

    /**
     * 需求：品牌分页查询展示
     * 参数：Integer page,Integer rows
     * 返回值：分页包装类对象
     */
    public PageResult findByPage(Integer page,Integer rows);

    /**
     * 需求：实现品牌数据添加
     */
    public void insert(TbBrand brand);

    /**
     * 需求：根据id查询品牌数据
     */
    public TbBrand findOne(Long id);

    /**
     * 需求：更新品牌数据
     */
    public void update(TbBrand brand);

    /**
     * 需求：删除品牌数据
     */
    public void delete(Long[] ids);

    /**
     * 定义查询请求，查询品牌下拉列表，进行多项选择
     */
    public List<Map> findBrandSelect2List();
}
