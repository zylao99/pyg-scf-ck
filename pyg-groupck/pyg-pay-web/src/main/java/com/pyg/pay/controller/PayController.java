package com.pyg.pay.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pay.service.PayService;
import com.pyg.utils.PygResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by on 2018/9/4.
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    //注入远程支付对象
    @Reference(timeout = 10000000)
    private PayService payService;

    //生成二维码
    //1，向支付系统发送生成二维码请求
    //2,支付系统调用微信支付品台支付下单接口
    //3,微信支付品台返回支付地址
    //4,根据此地址生成二维码

    /**
     * 生成二维码
     *
     * @return
     */
    @RequestMapping("createQrCode")
    public Map createQrCode(HttpServletRequest request) {
        //获取用户名
        String userId = request.getRemoteUser();
        //调用服务层方法，向微信支付品台下单
        Map maps = payService.createQrCode(userId);
        return maps;
    }

    /**
     * 需求：实时监控二维码状态，及时发现二维码是否支付成功
     */
    @RequestMapping("queryStatus/{out_trade_no}")
    public PygResult queryStatus(@PathVariable String out_trade_no) {
        PygResult result = null;
        int i = 0;
        while (true) {
            //调用查询接口
            Map<String, String> map = payService.queryStatus(out_trade_no);
            if (map == null) {//出错
                result = new PygResult(false, "支付出错");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")) {//如果成功
                result = new PygResult(true, "支付成功");
                break;
            }
            try {
                Thread.sleep(3000);//间隔三秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i++;

            if (i >= 100) {
                result = new PygResult(false, "timeout");
                break;
            }
        }
        return result;

    }

}
