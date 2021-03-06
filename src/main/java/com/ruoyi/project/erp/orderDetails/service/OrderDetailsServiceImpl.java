package com.ruoyi.project.erp.orderDetails.service;

import java.util.Collections;
import java.util.List;

import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.orderDetails.mapper.OrderDetailsMapper;
import com.ruoyi.project.erp.orderDetails.domain.OrderDetails;
import com.ruoyi.project.erp.orderDetails.service.IOrderDetailsService;
import com.ruoyi.common.support.Convert;

/**
 * 订单详情 服务层实现
 * 
 * @author zqm
 * @date 2019-05-08
 */
@Service("orderdetail")
public class OrderDetailsServiceImpl implements IOrderDetailsService 
{
	@Autowired
	private OrderDetailsMapper orderDetailsMapper;

	/**
     * 查询订单详情信息
     * 
     * @param id 订单详情ID
     * @return 订单详情信息
     */
    @Override
	public OrderDetails selectOrderDetailsById(Integer id)
	{
	    return orderDetailsMapper.selectOrderDetailsById(id);
	}
	
	/**
     * 查询订单详情列表
     * 
     * @param orderDetails 订单详情信息
     * @return 订单详情集合
     */
	@Override
	public List<OrderDetails> selectOrderDetailsList(OrderDetails orderDetails)
	{
	    return orderDetailsMapper.selectOrderDetailsList(orderDetails);
	}
	
    /**
     * 新增订单详情
     * 
     * @param orderDetails 订单详情信息
     * @return 结果
     */
	@Override
	public int insertOrderDetails(OrderDetails orderDetails)
	{
	    return orderDetailsMapper.insertOrderDetails(orderDetails);
	}
	
	/**
     * 修改订单详情
     * 
     * @param orderDetails 订单详情信息
     * @return 结果
     */
	@Override
	public int updateOrderDetails(OrderDetails orderDetails)
	{
	    return orderDetailsMapper.updateOrderDetails(orderDetails);
	}

	/**
     * 删除订单详情对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOrderDetailsByIds(String ids)
	{
		return orderDetailsMapper.deleteOrderDetailsByIds(Convert.toStrArray(ids));
	}

	/**
	 * 通过产品id查询订单明细大于0的库存信息
	 * @param orderDetails 订单明细
	 * @return 结果
	 */
	@Override
	public List<OrderDetails> selectOrderDetailsListByPid(OrderDetails orderDetails) {
		return orderDetailsMapper.selectOrderDetailsListByPid(orderDetails);
	}

	/**
	 * 查询所差数量即需要生产数量大于0的订单明细
	 * @param orderDetails 订单明细
	 * @return 结果
	 */
	@Override
	public List<OrderDetails> selectOrderDetailsListDifPro(OrderDetails orderDetails) {
		return orderDetailsMapper.selectOrderDetailsListDifPro(orderDetails);
	}

	/**
	 * 查询所差数量即需要生产数量大于0的订单明细
	 * @return 结果
	 */
	@Override
	public List<OrderDetails> selectLackNumAllOrder() {
		User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
		if (user == null) {
		    return Collections.emptyList();
		}
		return orderDetailsMapper.selectLackNumAllOrder(user.getCompanyId());
	}
}
