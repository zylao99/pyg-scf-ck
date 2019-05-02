package com.pyg.pay.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbOrderItemMapper;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.pojo.*;
import com.pyg.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbPayLogMapper;
import com.pyg.pojo.TbPayLogExample.Criteria;
import com.pyg.pay.service.PayLogService;

import com.pyg.utils.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class PayLogServiceImpl implements PayLogService {

    @Autowired
    private TbPayLogMapper payLogMapper;

    @Autowired
    private TbOrderMapper orderMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbPayLog> findAll() {
        return payLogMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbPayLog> page = (Page<TbPayLog>) payLogMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     *
     * @param
     */
    @Override
    public void add(String userId, String out_trade_no) {

        //根据用户名查询
        //创建example
        TbOrderExample example = new TbOrderExample();
        //创建criteria对象
        TbOrderExample.Criteria criteria = example.createCriteria();
        //设置查询参数
        criteria.andUserIdEqualTo(userId);
        //执行
        List<TbOrder> tbOrders = orderMapper.selectByExample(example);
        List<Long> orderIdList = new ArrayList<>();
        //定义记录总金额变量
        Double totalFee = 0d;
        //循环多个订单，计算总金额
        for (TbOrder tbOrder : tbOrders) {
            totalFee += tbOrder.getPayment().doubleValue();
            orderIdList.add(tbOrder.getOrderId());
        }

        TbPayLog payLog = new TbPayLog();
        payLog.setTotalFee((long) totalFee.doubleValue());

        payLog.setOutTradeNo(out_trade_no);
        payLog.setPayTime(new Date());
        payLog.setCreateTime(new Date());
        //创建idworker对象，生成支付订单号
        IdWorker idWorker = new IdWorker();
        long transactionId = idWorker.nextId();
        payLog.setTransactionId(String.valueOf(transactionId));
        payLog.setTradeState("1"); //已支付
        payLog.setOrderList(JSON.toJSONString(orderIdList));
        payLog.setPayType("1");//在线支付
        payLog.setUserId(userId);

        payLogMapper.insertSelective(payLog);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbPayLog payLog) {
        payLogMapper.updateByPrimaryKey(payLog);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbPayLog findOne(String id) {
        return payLogMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            payLogMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbPayLog payLog, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbPayLogExample example = new TbPayLogExample();
        Criteria criteria = example.createCriteria();

        if (payLog != null) {

        }

        Page<TbPayLog> page = (Page<TbPayLog>) payLogMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
