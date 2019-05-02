package com.pyg.controller;

import com.pyg.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.resources.Messages_pt_BR;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2018/8/29.
 */
@RestController
public class UserController {

    @Autowired
    private Environment env;

    /**
     * 需求：springboot入门
     */
    @RequestMapping("/hello")
    public String showHello1(){
        return "hello,springboot!";
    }

    /**
     *  需求：springboot入门
     */
    @RequestMapping("/hello2/{name}")
    public String showHello2(@PathVariable String name){
        return "hello,springboot!"+name;
    }

    /**
     * 需求：springboot入门
     */
    @RequestMapping("/hello3")
    public String showHello3(){
        return "hello,springboot! url = "+env.getProperty("url");
    }

    /**
     * 需求：展示对象
     */
    @RequestMapping("hello4")
    public Person showHello4(){
        Person p = new Person();
        p.setId(1000);
        p.setName("张三丰");
        return  p;
    }

    //注入消息模板对象
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 需求：使用springboot框架内置MQ消息服务器发送及接受消息
     */
    @RequestMapping("send/{message}")
    public String sendMessage(@PathVariable String message){
        jmsTemplate.convertAndSend("oneQueue",message);
        return "success";
    }

    /**
     * 需求:接受消息
     *
     */
    @JmsListener(destination = "oneQueue")
    public void receiveMessage(String message){
        System.out.println("接受消息："+message);
    }

    /**
     * 需求：发送短信测试
     */
    @RequestMapping("/send/sms")
    public String sendMessage(){
        Map map = new HashMap();
        map.put("sign_name","黑马");
        map.put("template_code","SMS_125028677");
        map.put("code","666666");
        map.put("phone","18814121387");

        jmsTemplate.convertAndSend("sms",map);

        return "success";

    }
}
