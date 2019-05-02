package com.pyg.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.aspectj.weaver.ast.Var;
import org.junit.Test;

import javax.jms.*;
import java.util.Map;

/**
 * Created by on 2018/8/26.
 */
public class QueueConsumer {

    /**
     * 需求：接受消息
     * 模式：点对点模式
     */
    @Test
    public void receiveMessage() throws Exception{
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
        //指定消息存储空间，且给这个空间起一个名称
        Queue queue = session.createQueue("myQueue");

        //指定消息接受者，且指定从哪儿接受消息
        MessageConsumer consumer = session.createConsumer(queue);

                //监听接受
                consumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {

                        if(message instanceof TextMessage){
                            try {
                                TextMessage m = (TextMessage) message;
                                //获取消息
                                String text = m.getText();

                                System.out.println("接受消息："+text);

                            } catch (JMSException e) {
                                e.printStackTrace();
                            }


                        }

            }
        });

        //等待输入,让端口阻塞，服务一直开启
        System.in.read();

        consumer.close();
        session.close();
        connection.close();

    }

}
