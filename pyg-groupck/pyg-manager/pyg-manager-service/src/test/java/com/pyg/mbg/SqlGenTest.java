package com.pyg.mbg;

import com.pyg.mapper.TbBrandMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by on 2018/8/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-dao.xml")
public class SqlGenTest {

    //注入mapper接口代理对象
    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 需求：添加一个商品数据
     */
    @Test
    public void insertItem(){
        //创建商品对象
        TbBrand brand = new TbBrand();
        brand.setName("牛逼的品牌");
        brand.setFirstChar("N");

        brandMapper.insertSelective(brand);
    }

    /**
     * 需求：查询所有
     */
    @Test
    public  void findAll(){
        List<TbBrand> list = brandMapper.selectByExample(null);
        System.out.println(list);
    }

    /**
     * 需求：条件查询
     * 条件：根据id查询
     *
     */
    @Test
    public void findByCondition(){

        TbBrand brand = brandMapper.selectByPrimaryKey(12L);
        System.out.println(brand);
    }

    /**
     * 需求：添加查询
     * 条件：查询首字母是H, 包含 “黑” 的品牌数据
     * example:做个工作条件组装
     */
    @Test
    public void findByBrandAnd(){
        //创建example对象
        TbBrandExample example = new TbBrandExample();
        //创建criteria对象条件查询对象
        TbBrandExample.Criteria criteria = example.createCriteria();
        //设置条件查询
        //查询首字母是H
        criteria.andFirstCharEqualTo("H");
        //包含 “黑” 的品牌数据
        criteria.andNameLike("%黑%");

        //执行查询
        List<TbBrand> brandList = brandMapper.selectByExample(example);

        System.out.println(brandList);
    }

    @Test
    public  void  findByBrandOr(){
        //创建example对象
        TbBrandExample example = new TbBrandExample();
        example.or().andFirstCharEqualTo("H");
        example.or().andNameLike("%黑%");
        //执行查询
        List<TbBrand> brandList = brandMapper.selectByExample(example);

        System.out.println(brandList);

    }

}
