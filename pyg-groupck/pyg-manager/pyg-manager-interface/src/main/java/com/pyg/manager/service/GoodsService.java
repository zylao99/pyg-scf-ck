package com.pyg.manager.service;

import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbItem;
import com.pyg.utils.PageResult;
import com.pyg.vo.Goods;

import java.util.List;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbGoods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

	/**
	 * 需求：更新商品状态，商家商品审核
	 * @param ids
	 * @param status
	 */
	public void updateStatus(Long[] ids,String status);

	/**
	 * 需求：更新商品状态，商家上下架
	 * @param ids
	 * @param status
	 */
	public void isMarketable(Long[] ids,String status);

	/**
	 * 需求：根据spu id查询sku
	 */
	public List<TbItem> findSkuItemList(Long[] ids);
	
}
