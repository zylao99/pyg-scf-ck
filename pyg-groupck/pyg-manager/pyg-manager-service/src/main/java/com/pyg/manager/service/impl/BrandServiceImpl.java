package com.pyg.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pyg.manager.service.BrandService;
import com.pyg.mapper.BrandMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/8/10.
 */
@Service
public class BrandServiceImpl implements BrandService{

    //注入mapper接口代理对象
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 需求：查询所有品牌数据
     */
    public List<TbBrand> findAll() {
        return brandMapper.findAll();
    }

    /**
     * 需求：品牌分页查询展示
     * 参数：Integer page,Integer rows
     * 返回值：分页包装类对象
     */
    public PageResult findByPage(Integer page, Integer rows) {
        //设置分页查询
        PageHelper.startPage(page,rows);
        //分页查询
        Page<TbBrand> pageInfo = (Page<TbBrand>) brandMapper.findAll();
        //返回
        return new PageResult(pageInfo.getTotal(),pageInfo.getResult());
    }

    /**
     * 需求：实现品牌数据添加
     */
    public void insert(TbBrand brand) {

        brandMapper.insert(brand);

    }

    /**
     * 需求：根据id查询品牌数据
     */
    public TbBrand findOne(Long id) {
        TbBrand brand = brandMapper.findById(id);
        return brand;
    }

    /**
     * 需求：根据id查询品牌数据
     */
    public void update(TbBrand brand) {

        brandMapper.update(brand);

    }

    /**
     * 需求：删除品牌数据
     */
    public void delete(Long[] ids) {
        //循环删除
        for (Long id : ids) {
            brandMapper.delete(id);
        }
    }

    /**
     * 定义查询请求，查询品牌下拉列表，进行多项选择
     */
    public List<Map> findBrandSelect2List() {
        //调用接口方法
        List<Map> list = brandMapper.findBrandSelect2List();

        return list;
    }
}
