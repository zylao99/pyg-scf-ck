package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)
public interface LabelClient {

    /**
     * 查询一个标签
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/label/{id}",method = RequestMethod.GET)
    Result findById(@PathVariable("id") String id);
}
