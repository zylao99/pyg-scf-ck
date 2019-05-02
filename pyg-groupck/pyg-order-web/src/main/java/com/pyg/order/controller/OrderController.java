package com.pyg.order.controller;

import java.util.List;

import com.pyg.cart.service.CartService;
import com.pyg.vo.Cart;
import org.owasp.esapi.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbOrder;
import com.pyg.order.service.OrderService;

import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;

import javax.servlet.http.HttpServletRequest;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference(timeout = 1000000)
    private OrderService orderService;

    //注入购物车服务
    @Reference(timeout = 10000000)
    private CartService cartService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbOrder> findAll() {
        return orderService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage/{page}/{rows}")
    public PageResult findPage(@PathVariable int page, @PathVariable int rows) {
        return orderService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param order
     * @return
     */
    @RequestMapping("/add")
    public PygResult add(@RequestBody TbOrder order) {
        try {
            orderService.add(order);
            return new PygResult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param order
     * @return
     */
    @RequestMapping("/update")
    public PygResult update(@RequestBody TbOrder order) {
        try {
            orderService.update(order);
            return new PygResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public TbOrder findOne(@PathVariable Long id) {
        return orderService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public PygResult delete(@PathVariable Long[] ids) {
        try {
            orderService.delete(ids);
            return new PygResult(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbOrder order, int page, int rows) {
        return orderService.findPage(order, page, rows);
    }

    /**
     * 需求：查询送货请求  （从购物车查询送货清单）
     */
    @RequestMapping("findOrderItemList")
    public List<Cart> findOrderItemList(HttpServletRequest request) {
        //从request中获取用户登录名
        String userId = request.getRemoteUser();
        //调用购物车服务查询即可
        List<Cart> cartList = cartService.findRedisCartList(userId);
        //返回
        return cartList;

    }

    /**
     * 需求：提交订单
     * 请求：../order/submitOrder
     * 参数：订单对象
     * 返回值：pygResult
     */
    @RequestMapping("submitOrder")
    public PygResult submitOrder(@RequestBody TbOrder order, HttpServletRequest request) {
        try {
            //获取当前用户登录名
            String userId = request.getRemoteUser();

            //调用购物车服务，查询redis购物车数据
            List<Cart> cartList = cartService.findRedisCartList(userId);

            //调用远程服务方法，实现订单保存
            orderService.submitOrder(order, userId,cartList);

            //提交成功
            return new PygResult(true, "提交成功");

        } catch (Exception e) {
            e.printStackTrace();
            //提交失败
            return new PygResult(false, "提交失败");
        }
    }
}
