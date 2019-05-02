package com.pyg.pay.controller;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbPayLog;
import com.pyg.pay.service.PayLogService;

import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;

import javax.servlet.http.HttpServletRequest;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/payLog")
public class PayLogController {

	@Reference(timeout = 10000000)
	private PayLogService payLogService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbPayLog> findAll(){			
		return payLogService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{page}/{rows}")
	public PageResult  findPage(@PathVariable int page,@PathVariable int rows){			
		return payLogService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param
	 * @return
	 */
	@RequestMapping("/add/{out_trade_no}")
	public PygResult add(@PathVariable String out_trade_no, HttpServletRequest request){
		try {
			//获取用户名
			String userId = request.getRemoteUser();
			payLogService.add(userId,out_trade_no);
			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param payLog
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbPayLog payLog){
		try {
			payLogService.update(payLog);
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
	public TbPayLog findOne(@PathVariable String id){
		return payLogService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public PygResult delete(@PathVariable String [] ids){
		try {
			payLogService.delete(ids);
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
	public PageResult search(@RequestBody TbPayLog payLog, int page, int rows  ){
		return payLogService.findPage(payLog, page, rows);		
	}
	
}
