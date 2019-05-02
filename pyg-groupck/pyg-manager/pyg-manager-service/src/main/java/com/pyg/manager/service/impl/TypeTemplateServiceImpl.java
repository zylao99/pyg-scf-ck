package com.pyg.manager.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbSpecificationOptionExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbTypeTemplateMapper;
import com.pyg.pojo.TbTypeTemplate;
import com.pyg.pojo.TbTypeTemplateExample;
import com.pyg.pojo.TbTypeTemplateExample.Criteria;
import com.pyg.manager.service.TypeTemplateService;

import com.pyg.utils.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;

    //注入规格选项mapper接口代理对象
    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbTypeTemplate> findAll() {
        return typeTemplateMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbTypeTemplate typeTemplate) {
        typeTemplateMapper.insert(typeTemplate);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbTypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKey(typeTemplate);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbTypeTemplate findOne(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            typeTemplateMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbTypeTemplateExample example = new TbTypeTemplateExample();
        Criteria criteria = example.createCriteria();

        if (typeTemplate != null) {
            if (typeTemplate.getName() != null && typeTemplate.getName().length() > 0) {
                criteria.andNameLike("%" + typeTemplate.getName() + "%");
            }
            if (typeTemplate.getSpecIds() != null && typeTemplate.getSpecIds().length() > 0) {
                criteria.andSpecIdsLike("%" + typeTemplate.getSpecIds() + "%");
            }
            if (typeTemplate.getBrandIds() != null && typeTemplate.getBrandIds().length() > 0) {
                criteria.andBrandIdsLike("%" + typeTemplate.getBrandIds() + "%");
            }
            if (typeTemplate.getCustomAttributeItems() != null && typeTemplate.getCustomAttributeItems().length() > 0) {
                criteria.andCustomAttributeItemsLike("%" + typeTemplate.getCustomAttributeItems() + "%");
            }

        }

        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 需求：商品录入，根据不同的规格选项组合成不同sku商品，因此查询规格选项
     *
     * @param typeId
     * @return
     */
    public List<Map> findSpecOptionsList(Long typeId) {

        //根据模板id查询模板对象
        TbTypeTemplate template = typeTemplateMapper.selectByPrimaryKey(typeId);
        //获取模板中规格属性
        //[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = template.getSpecIds();
        //把规格json字符串转换为json对象
        List<Map> specMap = JSON.parseArray(specIds, Map.class);
        //循环规格集合
        for (Map map : specMap) {

            //获取map的id
            Long id = new Long((Integer) map.get("id"));

            //创建规格选项example对象
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            //创建criteria对象
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            //设置查询参数，根据外键查询
            criteria.andSpecIdEqualTo(id);

            //根据id查询规格选项
            List<TbSpecificationOption> optionList = specificationOptionMapper.selectByExample(example);

            //把规格选项集合放入map
            //[{"id":27,"text":"网络","options":[]},{"id":32,"text":"机身内存","options":[]}]
            map.put("options", optionList);


        }


        return specMap;
    }

}
