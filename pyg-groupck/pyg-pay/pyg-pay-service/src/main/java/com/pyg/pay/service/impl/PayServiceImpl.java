package com.pyg.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.pay.service.PayService;
import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbOrderExample;
import com.pyg.utils.HttpClient;
import com.pyg.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/9/4.
 */
@Service
public class PayServiceImpl implements PayService {

    //注入商户appid
    @Value("${appid}")
    private String appid;

    //注入商户号
    @Value("${partner}")
    private String partner;

    //注入商户秘钥
    @Value("${partnerkey}")
    private String partnerkey;

    //注入下单地址
    @Value("${payUrl}")
    private String payUrl;

    //注入订单mapper接口代理对象
    @Autowired
    private TbOrderMapper orderMapper;

    /**
     * 需求：生成二维码
     *
     * @param userId
     * @return Map
     * 商户订单号和保存的订单订单号有关系没有？ 支付订单
     */
    public Map createQrCode(String userId) {

        try {
            //查询支付金额
            //创建订单example对象
            TbOrderExample example = new TbOrderExample();
            //创建criteria对象
            TbOrderExample.Criteria criteria = example.createCriteria();
            //设置参数
            criteria.andUserIdEqualTo(userId);


            //定义记录总金额变量
            Double totalFee = 0d;

            //执行查询
            List<TbOrder> orderList = orderMapper.selectByExample(example);

            //循环多个订单，计算总金额
            for (TbOrder tbOrder : orderList) {
                totalFee += tbOrder.getPayment().doubleValue();
            }

            //创建一个map对象，封装支付下单参数
            Map<String, String> maps = new HashMap<>();
            //公众账号ID
            maps.put("appid", appid);
            //商户号
            maps.put("mch_id", partner);
            //随机字符串
            maps.put("nonce_str", WXPayUtil.generateNonceStr());
            //商品描述
            maps.put("body", "品优购");

            //创建idworker对象，生成支付订单号
            IdWorker idWorker = new IdWorker();
            long payId = idWorker.nextId();

            //out_trade_no
            maps.put("out_trade_no", payId + "");

            //设置支付金额
            maps.put("total_fee", "1");
            maps.put("spbill_create_ip", "127.0.0.1");
            maps.put("notify_url", "http://test.itcast.cn");
            maps.put("trade_type", "NATIVE");

            //使用微信支付工具类对象，生成一个具有签名的xml格式参数
            String xmlparam = WXPayUtil.generateSignedXml(maps, partnerkey);

            //创建httpClient对象，向微信支付品台发送请求，获取支付地址
            HttpClient httpClient = new HttpClient(payUrl);
            //设置请求方式
            httpClient.setHttps(true);
            //设置请求参数
            httpClient.setXmlParam(xmlparam);
            //设置请求方式
            httpClient.post();

            //获取回调结果
            String content = httpClient.getContent();
            //把xml格式转换成对象
            //返回支付地址
            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(content);

            //金额
            stringStringMap.put("total_fee", totalFee + "");
            //订单号
            stringStringMap.put("out_trade_no", payId + "");

            return stringStringMap;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 需求：查询支付二维码状态，查询是否支付成功
     *
     * @param out_trade_no
     * @return 向微信支付品台发送请求，查询订单状态
     * 1，封装向微信传递参数
     * 2，把map参数转换为xml格式（此格式必须带签名）
     * 3，使用httpClient发送请求（向微信支付品台）
     * 4，获取微信支付返回结果
     * 5，把结果返回给表现层
     * 6，判断是否支付成功
     */
    public Map queryStatus(String out_trade_no) {

        try {
            //创建map对象，封装查询微信支付状态参数
            Map<String, String> maps = new HashMap<>();
            //商户appid 唯一标识
            maps.put("appid", appid);
            //商户号 唯一标识
            maps.put("mch_id", partner);
            //设置订单号
            maps.put("out_trade_no", out_trade_no);
            //随机字符串
            maps.put("nonce_str", WXPayUtil.generateNonceStr());

            //具有签名的xml格式参数
            String xmlParam = WXPayUtil.generateSignedXml(maps, partnerkey);

            //使用httpClient向微信支付品台发送查询订单请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            //设置请求方式
            httpClient.setHttps(true);
            //设置参数
            httpClient.setXmlParam(xmlParam);
            //请求方式
            httpClient.post();

            //获取查询结果
            String xmlMap = httpClient.getContent();

            //把返回结果xml转换成map对象
            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(xmlMap);

            //返回支付状态
            return stringStringMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
