package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;


    /**
     * 添加好友
     * @param friendid
     * @param type
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}", method = RequestMethod.PUT)
    public Result like(@PathVariable String friendid, @PathVariable String type) {

        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESS_ERROR, "请先登录");
        }

        if ("1".equals(type)) {
            //添加好友
            int i = friendService.addFriend(claims.getId(), friendid);
            if (i == 0) {
                return new Result(false, StatusCode.REPEATE_ERROR,"已经添加过好友");
            }
            return new Result(true, StatusCode.OK, "添加好友成功");
        } else {
            //添加非好友（黑名单）
            return new Result(true, StatusCode.OK, "添加非好友成功");
        }
    }


    /**
     * 删除好友
     * @param friendid
     * @return
     */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid){
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESS_ERROR, "请先登录");
        }

        //删除好友
        friendService.deleteFriend(claims.getId(),friendid);

        return new Result(true,StatusCode.OK,"删除好友成功");
    }

}
