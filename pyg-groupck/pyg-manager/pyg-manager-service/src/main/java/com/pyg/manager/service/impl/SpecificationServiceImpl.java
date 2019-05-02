package com.pyg.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.BrandService;
import com.pyg.manager.service.SpecificationService;
import com.pyg.mapper.BrandMapper;
import com.pyg.mapper.TbSpecificationMapper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbSpecificationOptionExample;
import com.pyg.utils.PageResult;
import com.pyg.vo.Specification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/8/10.
 */
@Service
public class SpecificationServiceImpl implements SpecificationService{

    //注入mapper接口代理对象
    @Autowired
    private TbSpecificationMapper specificationMapper;

    //注入规格选项mapper接口代理对象
    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;


    /**
     * 需求：品牌分页查询展示
     * 参数：Integer page,Integer rows
     * 返回值：分页包装类对象
     */
    public PageResult findByPage(Integer page, Integer rows) {
        //设置分页查询
        PageHelper.startPage(page,rows);
        //分页查询
        Page<TbSpecification> pageInfo = (Page<TbSpecification>)specificationMapper.selectByExample(null);
        //返回
        return new PageResult(pageInfo.getTotal(),pageInfo.getResult());
    }

    /**
     * 需求：需求：实现规格及规格选项数据添加
     */
    public void insert(Specification specification) {

        //保存规格数据，规格数据保存成功返回主键，把主键设置规格选项的外键字段上面
        //返回主键：配置映射文件 <selectKey> select last_insert_id()
        //返回主键：useGeneratedKeys="true" keyProperty="id"
        //获取规格对象
        TbSpecification tbSpecification = specification.getTbSpecification();
        //保存
        specificationMapper.insert(tbSpecification);

        //获取规格选项集合对象
        List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
        //循环遍历规格选项，把每一个规格选项对象都保存
        for (TbSpecificationOption tbSpecificationOption : specificationOptionList) {
            //设置外键
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            //保存
            specificationOptionMapper.insertSelective(tbSpecificationOption);
        }

    }

    /**
     * 需求：根据id查询品牌数据
     */
    public Specification findOne(Long id) {
        //先查询规格数据
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        //根据规格主键，查询规格选项，规格主键是规格选项的外键
        //创建example对象
        //select * from tb_speciftication_option where spec_id = #{specId}
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        //设查询参数，根据外键查询
        criteria.andSpecIdEqualTo(tbSpecification.getId());

        //执行查询
        List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.selectByExample(example);

        //创建包装类对象
        Specification specification = new Specification();
        //添加规格数据
        specification.setTbSpecification(tbSpecification);
        //添加规格选项
        specification.setSpecificationOptionList(specificationOptionList);

        return specification;
    }

    /**
     * 需求：更新规格及规格选项数据
     * 业务：
     * 在更新规格数据时候，又新添加了一行规格选项。此时这行规格选项不可能更新成功。
     * 步骤：
     * 1，先更新规格数据
     * 2，根据外键删除数据库规格选项数据
     * 3，再把页面传递规格选项数据保存到数据库即可实现更新。
     */
    public void update(Specification specification) {

        //获取规格对象
        TbSpecification tbSpecification = specification.getTbSpecification();
        //更新规格对象
        specificationMapper.updateByPrimaryKeySelective(tbSpecification);

        //获取规格主键，也就是规格选项的外键，根据此外键删除数据库规格选项数据
        //创建规格选项的example对象
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        //创建criteria对象
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        //设置删除条件
        criteria.andSpecIdEqualTo(tbSpecification.getId());

        //删除
        specificationOptionMapper.deleteByExample(example);

        //获取页面传递的规格选项对象数据
        List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();

        //循环遍历规格选项，实现保存
        for (TbSpecificationOption tbSpecificationOption : specificationOptionList) {

            //设置外键
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            //保存
            specificationOptionMapper.insertSelective(tbSpecificationOption);

        }


    }

    /**
     * 需求：删除品牌数据
     */
    public void delete(Long[] ids) {
        //循环删除
        for (Long id : ids) {
            specificationMapper.deleteByPrimaryKey(id);
            //根据外键删除规格选项
            //创建规格选项的example对象
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            //创建criteria对象
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            //设置删除条件
            criteria.andSpecIdEqualTo(id);

            //删除
            specificationOptionMapper.deleteByExample(example);

        }
    }

    //定义方法，实现规格下拉列表，实现规格多项选择
    public List<Map> findSpecList() {
        List<Map> specList = specificationMapper.findSpecList();
        return specList;
    }
}
