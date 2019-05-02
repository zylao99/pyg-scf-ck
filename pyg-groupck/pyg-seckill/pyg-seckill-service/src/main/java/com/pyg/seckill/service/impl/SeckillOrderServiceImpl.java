package com.pyg.seckill.service.impl;

import java.util.Date;
import java.util.List;

import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.utils.SysConstants;
import com.pyg.vo.OrderRecode;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbSeckillOrderMapper;
import com.pyg.pojo.TbSeckillOrder;
import com.pyg.pojo.TbSeckillOrderExample;
import com.pyg.pojo.TbSeckillOrderExample.Criteria;
import com.pyg.seckill.service.SeckillOrderService;

import com.pyg.utils.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    //注入秒杀商品mapper接口代理对象
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //注入多线程对象
    @Autowired
    private CreateOrder createOrder;

    //注入调度对象
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 查询全部
     */
    @Override
    public List<TbSeckillOrder> findAll() {
        return seckillOrderMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSeckillOrder> page = (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbSeckillOrder seckillOrder) {
        seckillOrderMapper.insert(seckillOrder);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbSeckillOrder seckillOrder) {
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbSeckillOrder findOne(Long id) {
        return seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            seckillOrderMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSeckillOrderExample example = new TbSeckillOrderExample();
        Criteria criteria = example.createCriteria();

        if (seckillOrder != null) {
            if (seckillOrder.getUserId() != null && seckillOrder.getUserId().length() > 0) {
                criteria.andUserIdLike("%" + seckillOrder.getUserId() + "%");
            }
            if (seckillOrder.getSellerId() != null && seckillOrder.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + seckillOrder.getSellerId() + "%");
            }
            if (seckillOrder.getStatus() != null && seckillOrder.getStatus().length() > 0) {
                criteria.andStatusLike("%" + seckillOrder.getStatus() + "%");
            }
            if (seckillOrder.getReceiverAddress() != null && seckillOrder.getReceiverAddress().length() > 0) {
                criteria.andReceiverAddressLike("%" + seckillOrder.getReceiverAddress() + "%");
            }
            if (seckillOrder.getReceiverMobile() != null && seckillOrder.getReceiverMobile().length() > 0) {
                criteria.andReceiverMobileLike("%" + seckillOrder.getReceiverMobile() + "%");
            }
            if (seckillOrder.getReceiver() != null && seckillOrder.getReceiver().length() > 0) {
                criteria.andReceiverLike("%" + seckillOrder.getReceiver() + "%");
            }
            if (seckillOrder.getTransactionId() != null && seckillOrder.getTransactionId().length() > 0) {
                criteria.andTransactionIdLike("%" + seckillOrder.getTransactionId() + "%");
            }

        }

        Page<TbSeckillOrder> page = (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 需求：提交订单
     * 参数：Long id String userId
     * 1），从redis服务器中获取入库的秒杀商品
     * 2），判断商品是否存在，或是是商品库存是否小于等于0
     * 3），如果秒杀商品存在，创建秒杀订单,此订单此时处于未支付状态
     * 4），把新增订单存储在redis服务器中
     * 5），下订单后，把秒杀商品库存减一
     * 6），判断库存是否小于0,卖完需要同步数据库
     * 7），否则把库存减少（但是此时没有减为0）的秒杀商品同步redis
     * 优化方法：调用多线程下单即可
     * 1）判断商品入库商品是否存在，如果不存在，表示已售罄
     * 2）判断用户是否在排队，如果在排序，获取用户订单，如果有订单，表示有支付订单，不能下单
     * 3）用户在排队，那就是排队中
     * 4）否则，没有排队，排序秒杀人数是否超限
     * 5）如果秒杀人数没有超限，那么就可以参与排队，参与秒杀。
     * 6）参与排队： 存储排队记录，存储排队用户，存储排序人数+1
     */
    public void submitOrder(Long id, String userId) {

        //1），从redis服务器中获取入库的秒杀商品
        TbSeckillGoods seckillGoods =
                (TbSeckillGoods) redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).get(id);
        if (seckillGoods == null || seckillGoods.getStockCount() < 1) {
            throw new RuntimeException("已售罄");
        }

        //从用户队列获取用户对象
        Boolean member = redisTemplate.boundSetOps(SysConstants.SECKILL_USER_QUEUE).isMember(userId);
        //如果为true，表示此用户正在排队
        if (member) {
            //查询用户是否有订单
            Object order = redisTemplate.boundHashOps(TbSeckillOrder.class.getSimpleName()).get(userId);
            //判断订单是否为空
            if (order != null) {
                throw new RuntimeException("您还有订单未支付！");
            }

            throw new RuntimeException("您正在排队中.......");

        }

        //获取抢购此商品的人数
        Long persons = redisTemplate.boundValueOps(SysConstants.SECKILL_USER_COUNT + id).increment(0);

        //判断商品库存是否满足抢购人数需求
        if(seckillGoods.getStockCount()+200<=persons){
            throw new RuntimeException("排队人数过多...");
        }

        //把用户信息存入list集合进行排队
        redisTemplate.boundListOps(SysConstants.SECKILL_USER_RECODE).leftPush(new OrderRecode(userId,id));
        //使用set集合记录用户排队
        redisTemplate.boundSetOps(SysConstants.SECKILL_USER_QUEUE).add(userId);

        //存储下单人数
        redisTemplate.boundValueOps(SysConstants.SECKILL_USER_COUNT+id).increment(1);

        //调用多线程下单
        taskExecutor.execute(createOrder);


    }

}
