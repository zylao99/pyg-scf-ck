package com.pyg.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by on 2018/8/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-consumer.xml")
public class SpringJMS {

    /**
     * 需求：接收点对点消息
     */
    @Test
    public  void receiveMEssage() throws  Exception{
        System.in.read();
    }

}
