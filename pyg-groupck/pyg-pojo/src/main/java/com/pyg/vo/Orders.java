package com.pyg.vo;

import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

public class Orders implements Serializable {
    //商家id
    private Long orderId;
    //商家名称
    private String userId;

    private TbOrder orderList;

    private List<TbOrderItem> orderItemList;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TbOrder getOrderList() {
        return orderList;
    }

    public void setOrderList(TbOrder orderList) {
        this.orderList = orderList;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
