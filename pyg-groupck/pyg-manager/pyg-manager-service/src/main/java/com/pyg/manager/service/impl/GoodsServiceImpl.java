package com.pyg.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.GoodsService;
import com.pyg.mapper.*;
import com.pyg.pojo.*;
import com.pyg.pojo.TbGoodsExample.Criteria;
import com.pyg.utils.PageResult;
import com.pyg.vo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import sun.font.CreatedFontTracker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    //注入商品描述mapper接口代理对象
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    //注入sku商品mapper接口代理对象
    @Autowired
    private TbItemMapper itemMapper;

    //注入分类mapper接口代理对象
    @Autowired
    private TbItemCatMapper itemCatMapper;

    //注入商家mapper接口代理对象
    @Autowired
    private TbSellerMapper sellerMapper;

    //注入品牌mapper接口代理对象
    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        //创建example对象
        TbGoodsExample example = new TbGoodsExample();
        //创建criteria对象
        Criteria criteria = example.createCriteria();
        //设置参数，查询未删除的商品
        criteria.andIsDeleteEqualTo("1");

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {

        //1，保存tbGOODs spu 商品表
        //获取tbGoods对象
        TbGoods tbGoods = goods.getGoods();
        //保存
        goodsMapper.insertSelective(tbGoods);

        //2,保存 spu 商品描述表
        //获取spu 商品描述表对象
        TbGoodsDesc goodsDesc = goods.getGoodsDesc();
        //返回spu商品表主键，设置给商品描述表主键
        //useGeneratedKeys="true" keyProperty="id" 映射文件配置，返回主键
        //设置主键
        goodsDesc.setGoodsId(tbGoods.getId());
        //保存
        goodsDescMapper.insertSelective(goodsDesc);


        //获取是否启用规格属性值
        String isEnableSpec = tbGoods.getIsEnableSpec();

        //判断是否启用规格
        if ("1".equals(isEnableSpec)) {
            //3，保存商品sku表
            //获取sku列表
            List<TbItem> itemList = goods.getItemList();

            //循环遍历，保存商品对象
            for (TbItem tbItem : itemList) {
                //补全sku商品属性值
                //标题：spu+sku
                //spec = {“网络”:"联动4G"}
                String spec = tbItem.getSpec();
                //把json格式规格属性字符串转换成map对象
                Map<String, String> specMap = (Map<String, String>) JSON.parse(spec);
                //定义字符串，组装规格值
                String value = "";
                //循环map，获取规格中值
                for (String key : specMap.keySet()) {
                    value += " " + specMap.get(key);
                }

                //设置商品标题
                //spu+su
                tbItem.setTitle(tbGoods.getGoodsName() + value + tbGoods.getCaption());

                //抽取sku商品属性设置公共代码
                //调用保存sku代码
                this.saveItem(tbItem, tbGoods, goodsDesc);
            }

        } else {

            //未启用规格
            //创建一个sku商品对象，保存商品对象
            TbItem tbItem = new TbItem();

            //设置商品标题
            //spu+su
            tbItem.setTitle(tbGoods.getGoodsName() + tbGoods.getCaption());

            //调用保存sku代码
            this.saveItem(tbItem, tbGoods, goodsDesc);

        }


    }

    private void saveItem(TbItem tbItem, TbGoods tbGoods, TbGoodsDesc goodsDesc) {

        //买点
        tbItem.setSellPoint(tbGoods.getCaption());
        //图片地址
        //从商品描述对象中获取图片地址
        String itemImages = goodsDesc.getItemImages();
        //把json字符串转换为集合数组
        if (itemImages != null) {

            List<Map> imageList = JSON.parseArray(itemImages, Map.class);

            //判断图片地址是否存在
            if (imageList != null && imageList.size() > 0) {
                tbItem.setImage((String) imageList.get(0).get("url"));
            }

        }

        //分页id
        //只能设置三级节点，叶子节点分类id
        tbItem.setCategoryid(tbGoods.getCategory3Id());
        //时间
        Date date = new Date();
        tbItem.setUpdateTime(date);
        tbItem.setCreateTime(date);

        //设置spuid
        tbItem.setGoodsId(tbGoods.getId());
        //设置商家id
        tbItem.setSellerId(tbGoods.getSellerId());

        //查询分类名称
        TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
        tbItem.setCategory(tbItemCat.getName());

        //查询品牌数据
        TbBrand brand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
        tbItem.setBrand(brand.getName());

        //查询商家
        TbSeller seller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
        tbItem.setSeller(seller.getNickName());

        //保存
        itemMapper.insertSelective(tbItem);
    }


    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {
        goodsMapper.updateByPrimaryKey(goods.getGoods());
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbGoods findOne(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 逻辑删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //根据id查询商品对象
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsDelete("0");
            //修改
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        if (goods != null) {
            //判断参数是否有值
            String sellerId = goods.getSellerId();
            if (sellerId != null && !"".equals(sellerId)) {
                //查询当前商家的商品数据
                criteria.andSellerIdEqualTo(sellerId);
            }

            //获取商品状态
            String auditStatus = goods.getAuditStatus();
            if (auditStatus != null && !"".equals(auditStatus)) {
                //根据状态进行查询商品
                criteria.andAuditStatusEqualTo(auditStatus);

            }
            //获取用户名
            String goodsName = goods.getGoodsName();
            //根据商品名称进行模糊查询
            if (goodsName != null && !"".equals(goodsName)) {
                criteria.andGoodsNameLike("%" + goodsName + "%");
            }


        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 需求：更新商品状态，商家商品审核
     *
     * @param ids
     * @param status
     */
    public void updateStatus(Long[] ids, String status) {
        //循环ids，修改每一个id对应的商品状态
        for (Long id : ids) {
            //根据id查询出商品数据
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //设置状态
            tbGoods.setAuditStatus(status);
            //完成修改
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    /**
     * 需求：更新商品状态，商家上下架
     * @param ids
     * @param status
     */
    public void isMarketable(Long[] ids, String status) {
        //循环ids，修改每一个id对应的商品状态
        for (Long id : ids) {
            //根据id查询出商品数据
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //设置状态
            tbGoods.setIsMarketable(status);
            //完成修改
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    /**
     * 需求：根据spu id查询sku
     */
    public List<TbItem> findSkuItemList(Long[] ids) {
        //创建sku集合，封装多个spu对应sku集合
        List<TbItem> itemList = new ArrayList<>();
        //循环spu ids
        for (Long id : ids) {
            //创建tbitem的example对象
            TbItemExample example = new TbItemExample();
            //创建criteria对象
            TbItemExample.Criteria criteria = example.createCriteria();
            //设置外键查询
            criteria.andGoodsIdEqualTo(id);
            //执行查询
            List<TbItem> skuList = itemMapper.selectByExample(example);

            itemList.addAll(skuList);

        }
        return itemList;
    }

}
