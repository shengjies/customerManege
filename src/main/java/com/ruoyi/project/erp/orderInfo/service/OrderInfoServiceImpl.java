package com.ruoyi.project.erp.orderInfo.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.StockConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.orderDetails.domain.OrderDetails;
import com.ruoyi.project.erp.orderDetails.mapper.OrderDetailsMapper;
import com.ruoyi.project.erp.productStock.domain.ProductStock;
import com.ruoyi.project.erp.productStock.mapper.ProductStockMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.orderInfo.mapper.OrderInfoMapper;
import com.ruoyi.project.erp.orderInfo.domain.OrderInfo;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单数据 服务层实现
 * 
 * @author zqm
 * @date 2019-05-08
 */
@Service("order")
public class OrderInfoServiceImpl implements IOrderInfoService 
{
	@Autowired
	private OrderInfoMapper orderInfoMapper;

	@Autowired
	private OrderDetailsMapper detailsMapper;

	@Autowired
	private ProductStockMapper productStockMapper;

	/**
     * 查询订单数据信息
     * 
     * @param id 订单数据ID
     * @return 订单数据信息
     */
    @Override
	public OrderInfo selectOrderInfoById(Integer id)
	{   OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(id);
		OrderDetails details = new OrderDetails();
		details.setOrderId(orderInfo.getId());
		orderInfo.setOrderDetails(detailsMapper.selectOrderDetailsList(details));
	    return orderInfo;
	}
	
	/**
     * 查询订单数据列表
     * 
     * @param orderInfo 订单数据信息
     * @return 订单数据集合
     */
	@Override
//	@DataSource(DataSourceType.ERP)
	public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo,HttpServletRequest request)
	{
		User user = JwtUtil.getTokenUser(request);
		if(user == null)return Collections.emptyList();
		orderInfo.setCompanyId(user.getCompanyId());
	    return orderInfoMapper.selectOrderInfoList(orderInfo);
	}
	
    /**
     * 新增订单数据
     * 
     * @param orderInfo 订单数据信息
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertOrderInfo(OrderInfo orderInfo, HttpServletRequest request)
	{
		User user = JwtUtil.getTokenUser(request);
		if(user ==null)return  0;
		orderInfo.setCompanyId(user.getCompanyId());
		orderInfo.setCreate_by(user.getUserId().intValue());
		orderInfo.setCreateTime(new Date());
		//添加订单主信息
		orderInfoMapper.insertOrderInfo(orderInfo);
		int totalNum =0;//订单总数量
		float totalPrice = 0F;//订单总金额
		if(!StringUtils.isEmpty(orderInfo.getDetails())){
			List<OrderDetails> details = JSON.parseArray(orderInfo.getDetails(),OrderDetails.class);
			for (OrderDetails detail : details) {
				detail.setOrderId(orderInfo.getId());
				detail.setOrderCode(orderInfo.getOrderCode());
				detail.setCompanyId(user.getCompanyId());
				detail.setCreateTime(new Date());
				detailsMapper.insertOrderDetails(detail);
				totalNum+=detail.getNumber();
				totalPrice+=detail.getTotalPrict();
			}
		}
		orderInfo.setOrderNumber(totalNum);
		orderInfo.setTotalPrice(totalPrice);
		orderInfo.setOrderDeliverNum(0);
		return orderInfoMapper.updateOrderInfo(orderInfo);
	}
	
	/**
     * 修改订单数据
     * 
     * @param orderInfo 订单数据信息
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateOrderInfo(OrderInfo orderInfo)
	{
		//将对应订单的详情进行标记 为1
		detailsMapper.editOrderDetailsSign(orderInfo.getId(),1);
		int totalNum =0;//订单总数量
		float totalPrice = 0F;//订单总金额
		if(!StringUtils.isEmpty(orderInfo.getDetails())){
			List<OrderDetails> details = JSON.parseArray(orderInfo.getDetails(),OrderDetails.class);
			for (OrderDetails detail : details) {
				//查询对应的详情是否存在
				OrderDetails de = detailsMapper.findOrderDetailsByOidAndPCode(orderInfo.getId(),detail.getProdectCode());
				if(de != null){
					de.setProductModel(detail.getProductModel());
					de.setProductName(detail.getProductName());
					de.setCustomerCode(detail.getCustomerCode());
					de.setProductPrict(detail.getProductPrict());
					de.setNumber(detail.getNumber());
					de.setTotalPrict(detail.getTotalPrict());
					de.setSign(0);
					totalNum+=detail.getNumber();
					totalPrice+=detail.getTotalPrict();
					detailsMapper.updateOrderDetails(de);
				}else{
					detail.setOrderId(orderInfo.getId());
					detail.setOrderCode(orderInfo.getOrderCode());
					detail.setCreateTime(new Date());
					detail.setSign(0);
					detailsMapper.insertOrderDetails(detail);
					totalNum+=detail.getNumber();
					totalPrice+=detail.getTotalPrict();
				}
			}
		}
		//删除对应标记为1的详情数据
		detailsMapper.deleteOrderDetailsByOrderId(orderInfo.getId());
		orderInfo.setOrderNumber(totalNum);
		orderInfo.setTotalPrice(totalPrice);
	    return orderInfoMapper.updateOrderInfo(orderInfo);
	}


	/**
	 * 订单状态业务逻辑
	 * @param orderInfo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int cancelOrder(OrderInfo orderInfo,HttpServletRequest request) {
		User user = JwtUtil.getTokenUser(request);
		if (user == null) {
		    return 0;
		}
		// 订单状态
		OrderInfo info = orderInfoMapper.selectOrderInfoById(orderInfo.getId());
		Integer orderStatus = orderInfo.getOrderStatus();
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCompanyId(user.getCompanyId());
		orderDetails.setOrderId(orderInfo.getId());
		ProductStock productStock = null;
		List<OrderDetails> orderDetailsList = detailsMapper.selectOrderDetailsList(orderDetails);
		if (StringUtils.isNotEmpty(orderDetailsList)) {
			Integer totalLockNumber = 0;
			/**
			 * 订单审核过程
			 */
			if (StockConstants.ORDER_STATUS_TWO.equals(orderStatus)) {
				for (OrderDetails details : orderDetailsList) {
					// 订单明细数量
					Integer number = details.getNumber();
					productStock = productStockMapper.selectProductStockByProId(details.getProductId());
					if (StringUtils.isNull(productStock)) {
						throw new BusinessException("没有所下订单库存记录信息");
					}
					// 库存良品数量
					Integer goodNumber = productStock.getGoodNumber();
					if (number <= goodNumber) {
						totalLockNumber += number;
						// 库存足够，订单明细锁定库存数量,锁定订单明细数量
						details.setLockNumber(number);
						productStock.setGoodNumber(goodNumber - number);
						productStock.setLockNumber(productStock.getLockNumber() + number);
					} else {
						totalLockNumber += goodNumber;
						//库存不足，订单明细锁定库存数量,良品库存数量全部锁定
						details.setLockNumber(goodNumber);
						productStock.setGoodNumber(0);
						productStock.setLockNumber(productStock.getLockNumber() + goodNumber);
					}
					// 订单明细锁定总数增加
					detailsMapper.updateOrderDetails(details);
					productStockMapper.updateProductStock(productStock);
				}
				orderInfo.setLockNumber(info.getLockNumber() + totalLockNumber);
				/**
				 * 取消订单
				 */
			} else if (StockConstants.ORDER_STATUS_FOUR.equals(orderStatus)) {
				for (OrderDetails details : orderDetailsList) {
					// 库存数量操作
					productStock = productStockMapper.selectProductStockByProId(details.getProductId());
					if (StringUtils.isNull(productStock)) {
						throw new BusinessException("没有所下订单库存记录信息");
					}
					productStock.setGoodNumber(productStock.getGoodNumber() + details.getLockNumber());
					productStock.setLockNumber(productStock.getLockNumber() - details.getLockNumber() <= 0?
							0:productStock.getLockNumber() - details.getLockNumber());
					// 订单主表锁定数量减少
					totalLockNumber += details.getLockNumber();
					// 订单明细锁定库存数量减少为0
					details.setLockNumber(0);

					detailsMapper.updateOrderDetails(details);
					productStockMapper.updateProductStock(productStock);
				}
				orderInfo.setLockNumber(info.getLockNumber() - totalLockNumber <= 0 ?
						0:info.getLockNumber() - totalLockNumber);
			}
		}
		return orderInfoMapper.updateOrderInfo(orderInfo);
	}

	/**
     * 删除订单数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOrderInfoByIds(String ids)
	{
		return orderInfoMapper.deleteOrderInfoByIds(Convert.toStrArray(ids));
	}

	/**
	 * 查询对应的公司所以未交付的订单信息
	 * @return
	 */
	@Override
	public List<OrderInfo> selectAllOrder(Cookie[] cookies) {
		User user = JwtUtil.getTokenCookie(cookies);
		if(user == null)return Collections.emptyList();
		return orderInfoMapper.selectAllOrder(user.getCompanyId());
	}

	/**
	 * 关闭订单
	 * @param orderInfo 订单
	 * @return 结果
	 */
	@Override
	public int closeOrderInfo(OrderInfo orderInfo) {
		OrderInfo order = orderInfoMapper.selectOrderInfoById(orderInfo.getId());
		if (StockConstants.ORDER_STATUS_THREE.equals(order.getOrderStatus())) { // 订单已经处于关闭状态
		    throw new BusinessException("订单已经关闭，请勿重复操作");
		}
		return orderInfoMapper.updateOrderInfo(orderInfo);
	}
}
