package com.pyg.shop.service.impl;

import com.pyg.manager.service.SellerService;
import com.pyg.pojo.TbSeller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2018/8/16.
 * 使用spring security安全框架提供UserDetailsService自定义认证类来实现用户动态密码，用户名认证
 * 认证步骤：
 * 1，根据用户名查询数据库，查询出用户密码
 * 2，根据用户密码去匹配认证管理器
 * 3, 对拦截权限角色去匹配认证
 * 安全认证流程：
 * 1，用户在页面输入明文用户名，密码
 * 2，被spirng security安全框架拦截，使用认证管理器对密码进行加密
 * 3，和自定义认证管理器 UserDetails 去匹配密码是否正确
 * 4，如果匹配成功，再验证角色是否符合登录条件
 *
 */
public class UserDtailServiceImpl implements UserDetailsService {

    //注入sellerServie服务对象，查询用户信息
    private SellerService sellerService;

    public SellerService getSellerService() {
        return sellerService;
    }

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名sellerid查询用户信息：用户名，密码
        TbSeller seller = sellerService.findOne(username);
        //获取用户密码
        String newPwd = seller.getPassword();

        //定义集合封装角色
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        //返回对象
        return new User(username,newPwd,authorities);
    }
}
