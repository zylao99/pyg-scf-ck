package com.pyg.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbUser;
import com.pyg.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2018/8/31.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference(timeout = 10000)
    private UserService userService;

    /**
     * 需求：获取用户登录信息
     */
    @RequestMapping("loadLoginName")
    public Map loadLoginName(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        TbUser user = userService.findOne(username);
        //创建map对象，获取用户名
        Map maps = new HashMap();
        maps.put("loginName",username);
        maps.put("loginPic",user.getHeadPic());
        return  maps;
    }

}
