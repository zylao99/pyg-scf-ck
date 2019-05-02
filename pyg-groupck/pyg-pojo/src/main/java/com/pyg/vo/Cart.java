package com.pyg.vo;

import com.pyg.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/9/1.
 * 商家对象
 */
public class Cart implements Serializable {

    private static final long serialVersionUID = -2071565876962058344L;

    //商家id
    private String sellerId;
    //商家名称
    private String sellerName;
    //商家购物列表
    private List<TbOrderItem> orderItemList;

    public String getSellerId() {

        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
