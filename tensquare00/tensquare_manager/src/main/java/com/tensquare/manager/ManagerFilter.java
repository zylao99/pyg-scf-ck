package com.tensquare.manager;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Object run() throws ZuulException {
        //获取请求对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //单独放行管理员登录请求： /admin/login
        String uri = request.getRequestURI();
        if (uri.indexOf("/admin/login")>0){
            //直接放行
            return null;
        }

        //获取验证信息
        String authorization = request.getHeader("Authorization");

        //判断验证信息是否符合基本规则
        if (authorization!=null && authorization.startsWith("Bearer ")){
            //截取token
            String token = authorization.substring(7);
            //校验token是否合法
            Claims claims = jwtUtil.parseJWT(token);

            //判断是否是管理员
            if (claims!=null && "admin".equals(claims.get("roles"))){
                return null;
            }
        }

        //终止请求
        //返回提示信息
        //设置返回状态码
        currentContext.setResponseStatusCode(401);  //无权访问
        //设置输出内容
        currentContext.setResponseBody("你不是管理员，无权访问");
        //设置输出内容的编码
        currentContext.getResponse().setContentType("text/html;charset=utf-8");

        //终止请求
        currentContext.setSendZuulResponse(false);
        return null;
    }
}
