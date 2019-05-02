package com.pyg.manager.service;
import com.pyg.pojo.TbSeller;

import com.pyg.utils.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SellerService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public TbSeller sellerMessage(String sellerId);
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSeller seller);
	
	/**
	 * 修改
	 */
	public void update(TbSeller seller);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbSeller findOne(String id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(String[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbSeller seller, int pageNum, int pageSize);

	/**
	 * 需求：商家审核，修改商家的状态
	 * //0 : 未审核
	 * //1: 审核通过
	 * //2: 审核未通过
	 * //3: 关闭商家
	 */
	public void updateStatus(String sellerId,String status);

    String getPassword(String sellerId);


	TbSeller findSeller(String loginName);
}
