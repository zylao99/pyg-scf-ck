package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
    /**
     * 设置过滤器
     * pre:可以在请求路由之前被调用
     * route:在路由请求是被调用
     * post:在route和error过滤器之后被调用
     * error：处理请求时发生错误被调用
     */
    @Override
    public String filterType() {
        return "pre";
    }


    /**
     * 何止过滤器执行顺序
     */
    @Override
    public int filterOrder() {
        return 0;  //数值越大，执行优先级越低
    }


    /**
     * 是否执行过滤器
     * true 执行过滤器
     * false 不执行过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的逻辑
     *      如果return null 代表放行
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("执行了zuul的过滤器....");

        //获取当前请求的头信息
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        String auth = request.getHeader("Authorization");
        System.out.println("========="+auth);

        if (auth!=null){
            //把头信息带回到网关后续的请求里面
            currentContext.addZuulRequestHeader("Authorization",auth);
        }
        return null;
    }
}
