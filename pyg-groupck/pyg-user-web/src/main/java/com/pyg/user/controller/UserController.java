package com.pyg.user.controller;
import java.util.List;

import com.pyg.pojo.TbOrder;
import com.pyg.vo.Orders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbUser;
import com.pyg.user.service.UserService;

import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference(timeout = 10000000)
	private UserService userService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{page}/{rows}")
	public PageResult  findPage(@PathVariable int page,@PathVariable int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add/{smsCode}")
	public PygResult add(@RequestBody TbUser user,@PathVariable String smsCode){
		try {
			//定义方法判断验证码是否匹配
			boolean flag = userService.checkCode(user.getPhone(),smsCode);
			if(!flag){
				return  new PygResult(false,"验证码错误");
			}
			userService.add(user,smsCode);
			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbUser user){
		try {
			userService.update(user);
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	public TbUser findOne(@PathVariable String id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public PygResult delete(@PathVariable Long [] ids){
		try {
			userService.delete(ids);
			return new PygResult(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}

	/**
	 * 需求：发送消息：手机号，模板，验证码，签名，获取短信验证码
	 * 参数：phone
	 * 返回值：PygReulst
	 */
	@RequestMapping("sendSms/{phone}")
	public PygResult sendSms(@PathVariable String phone){
		try {
			//调用服务
			userService.sendSms(phone);
			return  new PygResult(true,"发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			return  new PygResult(false,"发送失败");
		}
	}
    /**
     * 密码设置
     */
    @RequestMapping("/passwordSetting/{nickName}/{newPassword}")
    public PygResult passwordSetting(@PathVariable String nickName,
                                     @PathVariable String newPassword) {
        try {
            // 获取用户名
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            TbUser user = new TbUser();
            user.setNickName(nickName);
            String newPwd = DigestUtils.md5DigestAsHex(newPassword.getBytes());
            user.setPassword(newPwd);
            userService.savePersonalData(name, user);
            return new PygResult(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(true, "保存失败");
        }
    }

    //验证验证码
    @RequestMapping("/checkCode/{phone}/{code}")
    public PygResult showPhone(@PathVariable String phone,@PathVariable String code){
        try {
            boolean flag = userService.checkCode(phone,code);
            if (flag){
                return new PygResult(true,"验证成功");
            }else {
                return new PygResult(false,"验证码有误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false,"验证有误");
        }
    }
    /**
     * 修改手机号
     * @param
     * @return
     */
    @RequestMapping("/updatePhone/{smsCode}/{loginName}")
    public PygResult updatePhone(@RequestBody TbUser user,@PathVariable String smsCode,@PathVariable String loginName){
        try {
            //定义方法判断验证码是否匹配
            boolean flag = userService.checkCode(user.getPhone(),smsCode);
            if(!flag){
                return  new PygResult(false,"验证码错误");
            }
            userService.updatePhone(loginName,user.getPhone());
            return new PygResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "修改失败");
        }
    }
}
