package com.pyg.user.service;
import java.util.List;
import com.pyg.pojo.TbUser;

import com.pyg.utils.PageResult;
import com.pyg.vo.Orders;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbUser> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbUser user,String smsCode);
	
	
	/**
	 * 修改
	 */
	public void update(TbUser user);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbUser findOne(String id);
	
	
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
	public PageResult findPage(TbUser user, int pageNum, int pageSize);
	/**
	 * 需求：发送消息：手机号，模板，验证码，签名，获取短信验证码
	 * 参数：phone
	 * 返回值：void
	 */
	public void sendSms(String phone);

	/**
	 * 判断验证码是否匹配
	 * @param phone
	 * @param smsCode
	 * @return
	 */
	boolean checkCode(String phone, String smsCode);

	/**
	 * 保存个人信息
	 */
	void savePersonalData(String name, TbUser user);

	//修改新的手机号
	void updatePhone(String loginName, String phone);

}
