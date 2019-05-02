package com.pyg.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.user.service.OrderService;
import com.pyg.utils.PageBean;
import com.pyg.utils.PageResult;
import com.pyg.vo.Orders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    //注入服务层
    @Reference(timeout = 10000000)
    private OrderService orderService;

    /**
     * 查询订单
     * @param request
     * @return
     */
    /*@RequestMapping("/findOrder")
    public List<Orders> findOrder(HttpServletRequest request){
        String userId = request.getRemoteUser();
        //获取订单名称
        List<Orders> ordersList =  orderService.findOrder(userId);
        return ordersList;
    }*/


    /**
     * 返回全部列表
     * @return
     */
    //分页查询
    @RequestMapping("/findByPage/{pageNum}/{pageSize}")
    public PageBean findByPage(
            @PathVariable int pageNum,
            @PathVariable int pageSize,
            HttpServletRequest request) {

        String userId = request.getRemoteUser();
        //获取订单名称
        PageBean<Orders> pageBean = orderService.findOrder(pageNum, pageSize, userId);

        return pageBean;
    }
}
