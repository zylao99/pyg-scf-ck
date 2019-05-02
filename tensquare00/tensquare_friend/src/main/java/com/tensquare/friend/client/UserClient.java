package com.tensquare.friend.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("tensquare-user")
public interface UserClient {
    /**
     * 更新关注数
     * @param userid
     * @param x
     * @return
     */
    @RequestMapping(value = "/user/updateFollowcount/{userid}/{x}",method = RequestMethod.PUT)
    Result updateFollowcount(@PathVariable("userid") String userid, @PathVariable("x") int x);


    /**
     * 更新关注数
     * @param userid
     * @param x
     * @return
     */
    @RequestMapping(value = "/user/updateFanswcount/{userid}/{x}",method = RequestMethod.PUT)
    Result updateFanswcount(@PathVariable("userid") String userid, @PathVariable("x") int x);

}
