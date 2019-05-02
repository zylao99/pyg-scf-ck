package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
@RefreshScope
public class SpitController {
    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    //增加
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    //全部列表
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询全部",spitService.findAll());
    }

    //根据ID查询
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true,StatusCode.OK,"根据ID",spitService.findById(spitId));
    }

    //修改
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId,@RequestBody Spit spit){
        spit.setId(spitId);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改");
    }

    //删除
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId){
        spitService.delete(spitId);
        return new Result(true,StatusCode.OK,"删除");
    }

    //根据上级ID查询
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> pageList = spitService.findByParentid(parentid,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()));
    }


    //点赞
    @RequestMapping(value = "/thumbup/{id}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String id){
        //模拟当前登录用户
        String userid = "1";

        //1. 从redis 查询该用户是否已经点赞过
        String flag = (String) redisTemplate.opsForValue().get("spit_"+userid+"_"+id);
        if (flag != null){
            //点赞过
            return new Result(false,StatusCode.REPEATE_ERROR,"已经点赞过");
        }

        spitService.thumbup(id);
        //2.把数据存入redis
        redisTemplate.opsForValue().set("spit_"+userid+"_"+id,"1");

        return new Result(true,StatusCode.OK,"点赞成功");
    }

    //浏览量
    @RequestMapping(value = "/visited/{id}",method = RequestMethod.PUT)
    public Result visit(@PathVariable String id){
        spitService.visit(id);
        return new Result(true,StatusCode.OK,"访问成功");
    }

    //转发分享
    @RequestMapping(value = "/share/{id}",method = RequestMethod.PUT)
    public Result share(@PathVariable String id){
        spitService.share(id);
        return new Result(true,StatusCode.OK,"分享成功");
    }
}
