package com.pyg.vo;

import java.io.Serializable;

/**
 * Created by on 2018/9/7.
 */
public class OrderRecode implements Serializable {



    private String userId;

    private Long seckillId;

    public OrderRecode(String userId, Long seckillId) {
        this.userId = userId;
        this.seckillId = seckillId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }
}
