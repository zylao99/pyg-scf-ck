package com.pyg.seckill.task;

import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillGoodsExample;
import com.pyg.utils.SysConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by on 2018/9/6.
 */
@Component
public class SeckillTask {


    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //注入秒杀商品mpper接口代理对象
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    /**
     * 需求：定时入库
     * 业务流程：
     * 1，查询数据库秒杀商品数据（只查询参与秒杀的商品）
     * 2，把秒杀放入redis数据库
     * 定时任务：cron表达式
     * 语法：
     * 表达式分为7位
     * 秒 分 时 天 月 周 年
     * 语法：参数cron表达式文档
     * 业务实现：
     * 查询秒杀商品
     * 入库（redis）
     * redis存储入库商品数据结构：hash
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void startSeckill() {
        //创建秒杀商品example对象
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
        //设置查询参数
        //审核通过商品
        criteria.andStatusEqualTo("1");
        //库存必须大于0
        criteria.andStockCountGreaterThan(0);
        //库存时间
        criteria.andStartTimeGreaterThanOrEqualTo(new Date());
        criteria.andEndTimeGreaterThan(new Date());

        //查询
        List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);

        //循环秒杀商品列表，存储秒杀商品
        for (TbSeckillGoods seckillGoods : seckillGoodsList) {
            //秒杀商品入库
            redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).put(seckillGoods.getId(), seckillGoods);

            //获取秒杀商品库存
            Integer stockCount = seckillGoods.getStockCount();

            //根据库存量循环
            for (int i = 0; i < stockCount; i++) {
                //队列
                redisTemplate.boundListOps(SysConstants.SECKILL_GOODSID_LIST+seckillGoods.getId()).leftPush(seckillGoods.getId());
            }
        }

        System.out.println("定时任务，每3秒执行一次次此方法，入库");
    }
}
