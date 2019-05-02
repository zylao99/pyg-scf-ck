package com.pyg.jedis;

import com.pyg.pojo.TbSeckillGoods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

/**
 * Created by on 2018/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-redis.xml")
public class MyJedis {

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 需求：操作string类型数据结构
     * 实现：添加操作
     */
    @Test
    public  void addString(){
        redisTemplate.boundValueOps("username").set("照明");
    }

    @Test
    public  void findString(){
        String username = (String) redisTemplate.boundValueOps("username").get();
        System.out.println(username);
    }


    /**
     * 需求：操作Set类型数据结构
     * 实现：添加操作
     */
    @Test
    public  void addSet(){
        redisTemplate.boundSetOps("myset").add("张飞");
        redisTemplate.boundSetOps("myset").add("刘备");
        redisTemplate.boundSetOps("myset").add("关羽");
    }

    /**
     * 需求：操作Set类型数据结构
     * 实现：获取操作
     */
    @Test
    public  void addGet(){
        Set myset = redisTemplate.boundSetOps("myset").members();
        System.out.println(myset);
    }

    /**
     * 需求：操作List类型数据结构
     * 实现：添加操作
     */
    @Test
    public  void addList(){
        redisTemplate.boundListOps("uList").rightPush("张无忌");
        redisTemplate.boundListOps("uList").rightPush("小昭");
        redisTemplate.boundListOps("uList").rightPush("周芷若");
        redisTemplate.boundListOps("uList").rightPush("赵敏");
    }

    /**
     * 需求：操作List类型数据结构
     * 实现：出队操作
     */
    @Test
    public  void addListPop(){
        String username = (String) redisTemplate.boundListOps("uList").rightPop();
        System.out.println(username);
    }

    /**
     * 需求：操作List类型数据结构
     * 实现：获取操作
     */
    @Test
    public  void addListRange(){
        List uList = redisTemplate.boundListOps("uList").range(0, -1);
        System.out.println(uList);

    }

    /**
     * 需求：操作Hash类型数据结构
     * 实现：添加操作
     */
    @Test
    public  void addHash(){
        redisTemplate.boundHashOps("user").put("username","张君宝");
        redisTemplate.boundHashOps("user").put("age",12);
        redisTemplate.boundHashOps("user").put("address","少林寺");

    }

    /**
     * 需求：操作Hash类型数据结构
     * 实现：查询操作
     */
    @Test
    public  void findHash(){
        String username = (String) redisTemplate.boundHashOps("user").get("username");
        System.out.println(username);

    }

    @Test
    public void deleteHash(){
        redisTemplate.delete(TbSeckillGoods.class.getSimpleName());
    }

}
