package com.pyg.user.service.impl;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.mapper.TbOrderItemMapper;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.pojo.*;
import com.pyg.user.service.OrderService;
import com.pyg.utils.PageBean;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import com.pyg.vo.Orders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 定义方法,实现分组查询订单
     */
    @Override
    public PageBean<Orders> findOrder(int pageNum, int pageSize, String userId) {

        if (pageNum < 1) {
            pageNum = 1;
        }
        PageBean<Orders> page = new PageBean<>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);

        //创建TbOrderExample实体类
        TbOrderExample example = new TbOrderExample();
        TbOrderExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);

        //先查询总数
        int count = orderMapper.countByExample(example);
        page.setTotalCount(count);


        //执行查询
        // 再分页查询数据
        //PageHelper.startPage((pageNum - 1) * pageSize, pageSize);
        List<TbOrder> tbOrders = orderMapper.findByPage((pageNum - 1) * pageSize, pageSize,userId);


        //创建list集合,封装查询数据
        List<Orders> orderList = new ArrayList<>();
        //循环
        for (TbOrder order : tbOrders) {
            //获取orderId
            Long orderId = order.getOrderId();
            //创建TbOrderItemExample对象
            TbOrderItemExample itemExample = new TbOrderItemExample();
            TbOrderItemExample.Criteria itemExampleCriteria = itemExample.createCriteria();
            //设置查询id
            itemExampleCriteria.andOrderIdEqualTo(orderId);
            //执行查询
            List<TbOrderItem> tbOrderItems = orderItemMapper.selectByExample(itemExample);

            //查找规格
            for (TbOrderItem orderItem : tbOrderItems) {
                Long itemId = orderItem.getItemId();
                TbItem item = itemMapper.selectByPrimaryKey(itemId);
                String spec = item.getSpec();
                orderItem.setSpec(spec);

            }

            //创建Order实体类
            Orders orders = new Orders();
            orders.setOrderId(orderId);
            orders.setUserId(userId);
            orders.setOrderList(order);
            orders.setOrderItemList(tbOrderItems);

            orderList.add(orders);
        }

        page.setList(orderList);
        return page;
    }


}
