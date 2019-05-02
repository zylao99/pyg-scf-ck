package com.pyg.html;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.*;
import com.pyg.utils.FMUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.*;

/**
 * Created by on 2018/8/28.
 * 同步静态业务：
 * 1，接受消息
 * 2，根据消息准备静态所需要数据
 * 3，生成静态页面
 */
public class HtmlListener implements MessageListener {

    //模板需要商品spu,商品描述，商品sku
    //注入商品对象sku
    @Autowired
    private TbItemMapper itemMapper;

    //注入商品spu对象
    @Autowired
    private TbGoodsMapper goodsMapper;

    //注入商品spu描述对象
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    //注入商品分类mapper接口代理对象
    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public void onMessage(Message message) {

        try {
            //接受数据
            if (message instanceof TextMessage) {
                TextMessage m = (TextMessage) message;
                //获取消息
                String skuJson = m.getText();
                //把转换list集合
                //包含多个spu 对应的sku
                List<TbItem> itemList = JSON.parseArray(skuJson, TbItem.class);

                //定义set集合
                Set<Long> spuIds = new HashSet();
                //循环多个spu 对应的sku
                for (TbItem tbItem : itemList) {
                    spuIds.add(tbItem.getGoodsId());
                }

                //循环set集合，根据spu生成静态页面
                for (Long spuId : spuIds) {

                    //查询spu商品数据
                    TbGoods tbGoods = goodsMapper.selectByPrimaryKey(spuId);
                    //查询spu描述对象
                    TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(spuId);
                    //查询sku列表
                    //创建tbitmExample对象
                    TbItemExample example = new TbItemExample();
                    TbItemExample.Criteria criteria = example.createCriteria();
                    //设置参数
                    criteria.andGoodsIdEqualTo(spuId);
                    //执行查询
                    List<TbItem> skuList = itemMapper.selectByExample(example);

                    //分类
                    //查询第一级分类
                    TbItemCat cat1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());
                    //查询第二级分类
                    TbItemCat cat2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());
                    //查询第三级分类
                    TbItemCat cat3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());

                    //创建map对象，封装模板页面需要的数据
                    Map maps = new HashMap();
                    maps.put("itemList",skuList);
                    maps.put("goodsDesc",tbGoodsDesc);
                    maps.put("goods",tbGoods);
                    //面包屑导航
                    maps.put("cat1",cat1.getName());
                    maps.put("cat2",cat2.getName());
                    maps.put("cat3",cat3.getName());

                    //创建生成静态页面的工具类对象
                    FMUtils fmUtils = new FMUtils();
                    fmUtils.ouputFile("item.ftl",spuId+".html",maps);


                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
