package com.pyg.order.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2018/8/16.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     * 需求：获取用户登录名，进行回显
     */
    @RequestMapping("showName")
    public Map showLoginName(HttpServletRequest request){
        
        //获取用户登录名
        String loginName = request.getRemoteUser();
        //创建map封装登录名
        Map loginMap = new HashMap();
        loginMap.put("loginName",loginName);

        return loginMap;

    }

}
