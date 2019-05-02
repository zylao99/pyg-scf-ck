package com.pyg.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.cart.service.CartService;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbOrderItem;
import com.pyg.vo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2018/9/1.
 */
@Service
public class CartServiceImpl implements CartService {

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //注入商品mapper接口代理对象
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 需求：查询redis购物车数据
     * 参数：用户名
     * 返回值：List<Cart>
     */
    public List<Cart> findRedisCartList(String username) {
        //从redis查询当前用户购物车数据
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("redis_cart").get(username);
        //判断redis购物车数据是否存在
        if (cartList == null || cartList.size() == 0) {
            return new ArrayList<Cart>();
        }
        return cartList;
    }

    /**
     * 需求：把新添加的购物车数据追加到原有购物车列表中，组合成新的购物车列表
     *
     * @param cartList
     * @param itemId
     * @param num
     * @return List<Cart>
     * 业务思路分析：
     * 1，根据id查询出商品对象数据
     * 2，判断商品是否存在
     * 3，判断商品是否启用
     * 4，判断新添加的商品是否属于原有购物车列表中某个商家（判断是否具有相同的商家）
     * 5，如果有相同商家，判断是否具有相同的商品
     * 6，如果有相同的商品，商品数量相加，总价格重新计算
     * 7，如果没有相同的商品，把新的商品直接添加此商家
     * 8，如果没有相同的商家商品，新建一个商家对象，添加到购物车列表集合即可。
     */
    public List<Cart> addItemsToCartList(List<Cart> cartList, Long itemId, Integer num) {

        //根据id查询出商品对象数据
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //判断商品是否存在
        if (item == null) {
            throw new RuntimeException("商品不存在");
        }
        //判断商品是否启用
        if (!"1".equals(item.getStatus())) {
            throw new RuntimeException("商品不可用");
        }

        //获取商家id
        String sellerId = item.getSellerId();

        //判断原有购物车列表中是否具有相同商家
        Cart cart = this.isSameCart(cartList, sellerId);

        //判断是否具有相同的商家
        if (cart != null) {
            //有相同的商家
            //判断在此商家中，是否具有相同的商品（是否已经存在购买的商品）
            //获取此商品购物列表
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            //判断是否具有相同的商品
            TbOrderItem orderItem = this.isSameItem(orderItemList, itemId);

            //判断是否具有相同的商品
            if (orderItem != null) {
                //商品数量相加
                orderItem.setNum(orderItem.getNum() + num);
                //总价格重新计算
                orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * orderItem.getPrice().doubleValue()));

                //判断此商品的数量小于0
                if (orderItem.getNum() < 1) {
                    orderItemList.remove(orderItem);
                }

                //判断商家商品列表是否为空
                if (orderItemList.size() == 0) {
                    cartList.remove(cart);
                }


            } else {
                //没有相同的商品

                TbOrderItem orderItem1 = this.crreateOrderItem(item, itemId, num);


                //把新添加的商品放入此商家的购物车列表
                orderItemList.add(orderItem1);

            }


        } else {
            //没有相同商家
            //新建一个商家对象
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());

            //新建一个集合对象，封装此商家商品
            List<TbOrderItem> orderItems = new ArrayList<>();

            TbOrderItem orderItem1 = this.crreateOrderItem(item, itemId, num);

            orderItems.add(orderItem1);

            //把此集合放入此商家对象
            cart.setOrderItemList(orderItems);
            //把新的商家对象放入购物车列表
            cartList.add(cart);

        }


        return cartList;
    }

    /**
     * 需求：添加redis购物车
     *
     * @param cartList
     * @param username
     */
    public void addRedisCart(List<Cart> cartList, String username) {
        //向redis添加购物车数据
        redisTemplate.boundHashOps("redis_cart").put(username, cartList);
    }

    /**
     * 需求：合并购物车数据
     *
     * @param redisCartList
     * @param cookieCartList
     * @return
     */
    public List<Cart> mergeCart(List<Cart> redisCartList, List<Cart> cookieCartList) {

        //定义集合，封装合并结果
        List<Cart> cartList = null;

        //遍历cookie购物车列表
        for (Cart cart : cookieCartList) {
            //获取商家的商品列表
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            //循环商家列表
            for (TbOrderItem orderItem : orderItemList) {

                cartList = this.addItemsToCartList(redisCartList, orderItem.getItemId(), orderItem.getNum());

            }

        }
        return cartList;
    }

    /**
     * 设置商品数据
     *
     * @param item
     * @param itemId
     * @param num
     * @return
     */
    private TbOrderItem crreateOrderItem(TbItem item, Long itemId, Integer num) {
        //新建一个商品对象
        TbOrderItem orderItem1 = new TbOrderItem();
        //设置数据
        orderItem1.setGoodsId(item.getGoodsId());
        orderItem1.setItemId(itemId);
        orderItem1.setTitle(item.getTitle());
        orderItem1.setPrice(item.getPrice());
        orderItem1.setNum(num);
        orderItem1.setTotalFee(new BigDecimal(num * item.getPrice().doubleValue()));
        orderItem1.setPicPath(item.getImage());
        orderItem1.setSellerId(item.getSellerId());
        orderItem1.setChecked(true);

        return orderItem1;
    }

    /**
     * 判断是否具有相同的商品，（是否已经存在购买的商品）
     *
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem isSameItem(List<TbOrderItem> orderItemList, Long itemId) {
        //循环遍历商家购物列表，判断是否具有相同的商家
        for (TbOrderItem orderItem : orderItemList) {
            //如果商品id和新添加的商品id相等，说明有相同的商品对象
            if (orderItem.getItemId() == itemId.longValue()) {
                return orderItem;
            }

        }

        return null;
    }

    /**
     * 需求：判断原有购物车列表中是否具有相同的商家
     *
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart isSameCart(List<Cart> cartList, String sellerId) {
        //循环原有购物车列表数据
        for (Cart cart : cartList) {
            //判断如果商家对象商家id和此商品商家id相等，说明此商品属于这个商家
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }

        return null;
    }
}
