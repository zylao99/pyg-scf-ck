package com.pyg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by on 2018/8/29.
 */
@SpringBootApplication
public class MyAppplication {

    public static void main(String[] args) {
        //入口方法
        //1，加载内置tomcat服务器
        //2,引导项目启动环境
        SpringApplication.run(MyAppplication.class,args);
    }

}
