package com.tensquare.user.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 */
@Component
public class JwtFilter extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 在所有controller 方法执行前执行此方法
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器执行~~~");

        String authorization = request.getHeader("Authorization");

        System.out.println("==========="+authorization);

        if (authorization != null){
            if (authorization.startsWith("Bearer")){
                String token = authorization.substring(7);
                Claims claims = jwtUtil.parseJWT(token);
                if (claims!=null){
                    //如果是管理员
                    if ("admin".equals(claims.get("roles"))){
                        //存入标记
                        request.setAttribute("admin_claims",claims);
                    }

                    //如果是普通用户
                    if ("user".equals(claims.get("roles"))){
                        //存入标记
                        request.setAttribute("user_claims",claims);
                    }
                }
            }
        }
        return true;
    }
}
