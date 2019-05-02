package com.pyg.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.SellerService;
import com.pyg.pojo.TbSeller;
import com.pyg.shop.service.impl.UserDtailServiceImpl;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {
	@Reference
	private SellerService sellerService;

	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/sellerMessage/{sellerId}")
	public TbSeller sellerMessage(@PathVariable String sellerId){
		return sellerService.sellerMessage(sellerId);
	}
	/**
	 * 修改密码
	 */
	@RequestMapping("/sellerPassword/{sellerId}/{oldPassword}/{twoNewPassword}")
	public PygResult sellerPassword(@PathVariable String sellerId,@PathVariable String oldPassword,@PathVariable String twoNewPassword){
			//根据id获取用户信息
			TbSeller tbSeller = sellerService.sellerMessage(sellerId);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			//对输入的原密码和数据库加密后的密码进行判断
			if (passwordEncoder.matches(oldPassword,tbSeller.getPassword())) {
                try {
					//修改密码,给密码加密
                    String newPwd = passwordEncoder.encode(twoNewPassword);
                    tbSeller.setPassword(newPwd);
                    sellerService.update(tbSeller);
                    return new PygResult(true, "修改成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    return new PygResult(false, "修改失败");
                }
            }else{
				return new PygResult(false,"与原密码不对应,请重新输入原密码");
			}
	}
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{page}/{rows}")
	public PageResult  findPage(@PathVariable int page,@PathVariable int rows){			
		return sellerService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param seller
	 * @return
	 */
	@RequestMapping("/add")
	public PygResult add(@RequestBody TbSeller seller){
		try {
			//商家入驻，必须给商家密码进行加密
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String newPwd = passwordEncoder.encode(seller.getPassword());
			seller.setPassword(newPwd);
			sellerService.add(seller);
			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seller
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbSeller seller){
		try {
			sellerService.update(seller);
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
	public TbSeller findOne(@PathVariable String id){
		return sellerService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public PygResult delete(@PathVariable String [] ids){
		try {
			sellerService.delete(ids);
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
	public PageResult search(@RequestBody TbSeller seller, int page, int rows  ){
		return sellerService.findPage(seller, page, rows);		
	}
	
}
