package com.pyg.mq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by on 2018/8/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/mq-producer.xml")
public class SpringJMS {

    //注入消息发送模板对象
    @Autowired
    private JmsTemplate jmsTemplate;
    //注入消息目的地：点对点
    @Autowired
    private ActiveMQQueue activeMQQueue;

    //注入消息目的地：发布订阅
    @Autowired
    private ActiveMQTopic activeMQTopic;


    /**
     * 需求：点对点发送消息
     */
    @Test
    public  void sendMessageByP2P(){
        //使用模板发现消息
        jmsTemplate.send(activeMQQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("只要988!");
            }
        });
    }

    /**
     * 需求：发布订阅发送消息
     */
    @Test
    public  void sendMessageByPS(){
        //使用模板发现消息
        jmsTemplate.send(activeMQTopic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("只要988!hhhhhhh");
            }
        });
    }




}
