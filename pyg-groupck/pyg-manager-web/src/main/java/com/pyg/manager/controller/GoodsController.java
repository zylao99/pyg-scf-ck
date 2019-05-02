package com.pyg.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pyg.manager.service.GoodsService;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbItem;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import com.pyg.vo.Goods;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference(timeout = 100000000)
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{page}/{rows}")
	public PageResult  findPage(@PathVariable int page,@PathVariable int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param
	 * @return
	 */
	@RequestMapping("/add")
	public PygResult add(@RequestBody Goods goods){
		try {
			goodsService.add(goods);
			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
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
	public TbGoods findOne(@PathVariable Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public PygResult delete(@PathVariable Long [] ids){
		try {
			goodsService.delete(ids);
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
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);
	}


	//注入消息发送模板
	@Autowired
	private JmsTemplate jmsTemplate;

	//注入消息发送目的地
	@Autowired
	private ActiveMQTopic activeMQTopic;



	/**
	 * 需求：更新商品状态，商家商品审核
	 * 商品添加，修改，删除更新数据库数据，导致数据库数据和索引库数据不一致，因此做数据库
	 * 数据和索引库数据同步工作。
	 * 无论是添加，修改，删除的商品都需要通过审核，然后才能同步索引库。因此在审核商品时候
	 * 发送消息，同步索引库。
	 *
	 * 商品修改，添加 ， 都需要做商品重新审核 审核完成后需要同步索引库，同步静态页面。
	 *
	 * @param ids
	 * @param status
	 */
	@RequestMapping("updateStatus/{ids}/{status}")
	public PygResult updateStatus(@PathVariable Long[] ids,@PathVariable String status){
		try {
			//调用远程service服务方法
			goodsService.updateStatus(ids,status);

			//只有审核才发送消息
			if("1".equals(status)){
				//查询审核通过sku数据
				List<TbItem> itemList = goodsService.findSkuItemList(ids);
				//把集合转换为json字符串
				String itemJson = JSON.toJSONString(itemList);
				//发送消息
				jmsTemplate.send(activeMQTopic, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						//发送消息
						return session.createTextMessage(itemJson);
					}
				});

			}

			return  new PygResult(true,"审核通过");
		} catch (Exception e) {
			e.printStackTrace();
			return  new PygResult(false,"审核失败");
		}
	}

	
}
