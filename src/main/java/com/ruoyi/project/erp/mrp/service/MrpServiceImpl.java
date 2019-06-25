package com.ruoyi.project.erp.mrp.service;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.MrpConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.project.erp.orderDetails.domain.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.mrp.mapper.MrpMapper;
import com.ruoyi.project.erp.mrp.domain.Mrp;
import com.ruoyi.project.erp.mrp.service.IMrpService;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * MRP记录 服务层实现
 * 
 * @author sj
 * @date 2019-06-24
 */
@Service
public class MrpServiceImpl implements IMrpService 
{
	@Autowired
	private MrpMapper mrpMapper;

	/**
     * 查询MRP记录信息
     * 
     * @param id MRP记录ID
     * @return MRP记录信息
     */
    @Override
	public Mrp selectMrpById(Integer id)
	{
	    return mrpMapper.selectMrpById(id);
	}
	
	/**
     * 查询MRP记录列表
     * 
     * @param mrp MRP记录信息
     * @return MRP记录集合
     */
	@Override
	public List<Mrp> selectMrpList(Mrp mrp)
	{
	    return mrpMapper.selectMrpList(mrp);
	}
	
    /**
     * 新增MRP记录
     * 
     * @param mrp MRP记录信息
     * @return 结果
     */
	@Override
	public int insertMrp(Mrp mrp)
	{
	    return mrpMapper.insertMrp(mrp);
	}
	
	/**
     * 修改MRP记录
     * 
     * @param mrp MRP记录信息
     * @return 结果
     */
	@Override
	public int updateMrp(Mrp mrp)
	{
	    return mrpMapper.updateMrp(mrp);
	}

	/**
     * 删除MRP记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMrpByIds(String ids)
	{
		return mrpMapper.deleteMrpByIds(Convert.toStrArray(ids));
	}


	/**
	 * 将选中的订单明细生成mrp
	 * @param mrps
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int addMrpByOrDeIds(String mrps) {
		List<OrderDetails> orderDetailsList = JSON.parseArray(mrps, OrderDetails.class);
		// 勾选中需生成mrp的订单明细列表
		for (OrderDetails orderDetails : orderDetailsList) {
			if (MrpConstants.ORDER_TO_MRP.equals(orderDetails.getMrpStatus())) {
			    throw new BusinessException("已生成MRP，请勿重复操作");
			}
			// 查询出最新版本的产品bom
			// 通过产品bomId查询最新的物料组成信息
			// 锁定物料库存数量
			// 更新订单锁定物料库存信息,更新订单为已生成mrp记录
		}
		return 0;
	}
}
