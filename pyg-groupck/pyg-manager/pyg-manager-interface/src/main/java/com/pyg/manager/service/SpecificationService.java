package com.pyg.manager.service;

import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbSpecification;
import com.pyg.utils.PageResult;
import com.pyg.vo.Specification;

import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/8/10.
 */
public interface SpecificationService {

    /**
     * 需求：规格分页查询展示
     * 参数：Integer page,Integer rows
     * 返回值：分页包装类对象
     */
    public PageResult findByPage(Integer page, Integer rows);

    /**
     * 需求：实现规格及规格选项数据添加
     */
    public void insert(Specification specification);

    /**
     * 需求：根据id查询规格及规格选项数据
     */
    public Specification findOne(Long id);

    /**
     * 需求：更新品牌数据
     */
    public void update(Specification specification);

    /**
     * 需求：删除品牌数据
     */
    public void delete(Long[] ids);

    //定义方法，实现规格下拉列表，实现规格多项选择
    public List<Map> findSpecList();
}
