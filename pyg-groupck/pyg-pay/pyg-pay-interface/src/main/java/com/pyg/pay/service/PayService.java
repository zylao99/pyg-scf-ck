package com.pyg.pay.service;

import java.util.Map;

/**
 * Created by on 2018/9/4.
 */
public interface PayService {
    /**
     * 需求：生成二维码
     * @param userId
     * @return Map
     */
    public Map createQrCode(String userId);

    /**
     * 需求：查询支付二维码状态，查询是否支付成功
     * @param out_trade_no
     * @return
     */
    public Map queryStatus(String out_trade_no);
}
