package com.pyg.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pyg.cart.service.CartService;
import com.pyg.utils.CookieUtil;
import com.pyg.utils.PygResult;
import com.pyg.vo.Cart;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by on 2018/9/1.
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    //注入购物车服务
    @Reference(timeout = 100000000)
    private CartService cartService;

    /**
     * 需求：添加购物车数据
     * 请求：http://localhost:8086/cart/addGoodsToCartList/1369377/1
     * 参数：Long itemId,Integer num
     * 返回值:PygResult
     * 业务思路：
     * 1，从request中获取用户身份信息
     * 2，查询购物车列表数据
     * 3，添加购物车数据（把新的数据添加到原来的购物车列表中）
     * 4，判断用户是否处于登录状态
     * 5，登录，把购物车列表添加到redis购物车
     * 6，否则没有登录，把购物车列表添加到cookie购物车
     * 购物车列表数据结构：
     * 思路：用户可以购买多个商家的多个商品
     * 1，购物车中有多个商家
     * 2，每一个商家可能具有多个商品
     * List<商家（购物商品集合）>
     * List<Cart>
     * CrossOrigin:必须满足spirng 4.2以上支持
     */
    @RequestMapping("addGoodsToCartList/{itemId}/{num}")
    @CrossOrigin(origins = "http://pinyougou.com")
    public PygResult addGoodsToCartList(@PathVariable Long itemId,
                                        @PathVariable Integer num,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        try {
            //1，从request中获取用户身份信息
            String username = request.getRemoteUser();
            //2，查询购物车列表数据
            List<Cart> cartList = this.findCartList(request, response);

            //3，添加购物车数据（把新的数据添加到原来的购物车列表中）
            cartList = cartService.addItemsToCartList(cartList, itemId, num);

            //4,判断用户此时是否登录
            if (username != null && !"".equals(username)) {
                //登录状态，把购物车列表放入redis购物车
                cartService.addRedisCart(cartList, username);
            } else {
                //未登录
                CookieUtil.setCookie(request,
                        response,
                        "cookie_cart",
                        JSON.toJSONString(cartList),
                        172800,
                        true);

            }

            //添加购物车成功
            return new PygResult(true, "购物车添加成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "购物车添加失败");
        }


    }


    @RequestMapping("findCartList")
    public List<Cart> findCartList(HttpServletRequest request, HttpServletResponse response) {

        //从request中获取用户登录信息
        String username = request.getRemoteUser();

        //查询cookie购物车
        String cookie_cart =
                CookieUtil.getCookieValue(request, "cookie_cart", true);

        //判断cookie购物车数据是否为空
        if (cookie_cart == null || "".equals(cookie_cart)) {
            cookie_cart = "[]";
        }
        //把cookie购物车转换成集合对象
        List<Cart> cookieCartList = JSON.parseArray(cookie_cart, Cart.class);


        //判断用户是否登录
        if (username != null && !"".equals(username)) {

            //登录状态
            //查询redis购物车
            List<Cart> redisCartList = cartService.findRedisCartList(username);

            //判断cookie购物车是否存在
            if (cookieCartList != null && cookieCartList.size() > 0) {
                //合并购物车
                //把cookie购物车列表数据合并到redis购物车列表
                redisCartList = cartService.mergeCart(redisCartList, cookieCartList);
                //添加到redis购物车
                cartService.addRedisCart(redisCartList, username);
                //清空cookie购物车
                CookieUtil.setCookie(request, response,
                        "cookie_cart",
                        "",
                        0,
                        true);

            }


            return redisCartList;


        } else {
            //未登录
            return cookieCartList;

        }

    }

}
