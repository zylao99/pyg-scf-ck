package com.pyg.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

/**
 * Created by on 2018/8/26.
 */
public class QueueProducer {

    /**
     * 需求：发送消息
     * 模式：点对点模式
     */
    @Test
    public void sendMessage() throws Exception{
        //指定消息服务器发送地址
        String brokerURL = "tcp://192.168.66.66:61616";
        //创建消息连接工厂对象
        ConnectionFactory cf = new ActiveMQConnectionFactory(brokerURL);
        //从工厂中获取连接对象
        Connection connection = cf.createConnection();

        //开启连接
        connection.start();


        //从连接中获取消息回话对象session
        //参数1：
        // true: 表示使用的是消息事务确认提交模式 Session.SESSION_TRANSACTED
        // false: 消息自动确认模式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建消息存储空间，且给这个空间起一个名称
        Queue queue = session.createQueue("myQueue");

        //创建消息发送者,且告知消息往哪儿发送
        MessageProducer producer = session.createProducer(queue);

        //创建消息对象
        TextMessage message = new ActiveMQTextMessage();
        message.setText("齐天大圣孙悟空，修炼了八九玄功?");


        //发送消息
        producer.send(message);

        producer.close();
        session.close();
        connection.close();

    }
}
