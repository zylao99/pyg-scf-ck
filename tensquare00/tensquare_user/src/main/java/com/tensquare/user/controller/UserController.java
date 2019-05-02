package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
@RefreshScope
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(String code, @RequestBody User user) {
        userService.add(code, user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        //判断是否是管理员admin
        /*//从请求获取头信息 Authorization
        String authorization = request.getHeader("Authorization");
        if (authorization == null){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足 authorization==null");
        }

        //如果authorization的值不以Bearer开头
        if(!authorization.startsWith("Bearer")){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足 不以Bearer开头");
        }

        //截取token字符串
        String token = authorization.substring(7);

        //解析token
        Claims claims = jwtUtil.parseJWT(token);

        if(claims==null){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足 claims==null");
        }

        if (!"admin".equals(claims.get("roles"))){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足 roles");
        }*/

        //取出当前角色标记
        Claims claims = (Claims) request.getAttribute("admin_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESS_ERROR, "权限不足");
        }

        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {
        userService.sendsms(mobile);
        return new Result(true, StatusCode.OK, "发送成功");
    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@PathVariable String code, @RequestBody User user) {
        userService.add(code, user);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user) {
        User loginUser = userService.login(user.getMobile(), user.getPassword());
        if (loginUser != null) {
            String token = jwtUtil.createJWT(loginUser.getId(), loginUser.getMobile(), "user");
            HashMap<String, String> map = new HashMap<>();
            map.put("name",loginUser.getMobile());
            map.put("token",token);

            return new Result(true, StatusCode.OK, "登录成功",map);
        }
        return new Result(false, StatusCode.USER_PASS_ERROR, "登录失败，用户名或密码错误");
    }

    /**
     * 更新关注数
     * @param userid
     * @param x
     * @return
     */
    @RequestMapping(value = "/updateFollowcount/{userid}/{x}",method = RequestMethod.PUT)
    public Result updateFollowcount(@PathVariable String userid,@PathVariable int x){
        userService.updateFollowcount(userid,x);
        return new Result(true,StatusCode.OK,"更新关注数成功");
    }

    /**
     * 更新关注数
     * @param userid
     * @param x
     * @return
     */
    @RequestMapping(value = "/updateFanswcount/{userid}/{x}",method = RequestMethod.PUT)
    public Result updateFanswcount(@PathVariable String userid,@PathVariable int x){
        userService.updateFanswcount(userid,x);
        return new Result(true,StatusCode.OK,"更新关注数成功");
    }
}
