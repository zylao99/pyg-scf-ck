package com.pyg.user.service;

import com.pyg.pojo.TbUser;
import com.pyg.utils.PageBean;
import com.pyg.utils.PageResult;
import com.pyg.vo.Orders;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {


	//分页查询
	PageBean<Orders> findOrder(int pageNum, int pageSize,String userId);

}
