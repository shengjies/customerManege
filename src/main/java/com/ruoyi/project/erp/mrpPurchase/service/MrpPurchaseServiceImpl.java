package com.ruoyi.project.erp.mrpPurchase.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.mrpPurchase.mapper.MrpPurchaseMapper;
import com.ruoyi.project.erp.mrpPurchase.domain.MrpPurchase;
import com.ruoyi.project.erp.mrpPurchase.service.IMrpPurchaseService;
import com.ruoyi.common.support.Convert;

/**
 * mrp采购单关联 服务层实现
 * 
 * @author sj
 * @date 2019-06-26
 */
@Service
public class MrpPurchaseServiceImpl implements IMrpPurchaseService 
{
	@Autowired
	private MrpPurchaseMapper mrpPurchaseMapper;

	/**
     * 查询mrp采购单关联信息
     * 
     * @param id mrp采购单关联ID
     * @return mrp采购单关联信息
     */
    @Override
	public MrpPurchase selectMrpPurchaseById(Integer id)
	{
	    return mrpPurchaseMapper.selectMrpPurchaseById(id);
	}
	
	/**
     * 查询mrp采购单关联列表
     * 
     * @param mrpPurchase mrp采购单关联信息
     * @return mrp采购单关联集合
     */
	@Override
	public List<MrpPurchase> selectMrpPurchaseList(MrpPurchase mrpPurchase)
	{
	    return mrpPurchaseMapper.selectMrpPurchaseList(mrpPurchase);
	}
	
    /**
     * 新增mrp采购单关联
     * 
     * @param mrpPurchase mrp采购单关联信息
     * @return 结果
     */
	@Override
	public int insertMrpPurchase(MrpPurchase mrpPurchase)
	{
	    return mrpPurchaseMapper.insertMrpPurchase(mrpPurchase);
	}
	
	/**
     * 修改mrp采购单关联
     * 
     * @param mrpPurchase mrp采购单关联信息
     * @return 结果
     */
	@Override
	public int updateMrpPurchase(MrpPurchase mrpPurchase)
	{
	    return mrpPurchaseMapper.updateMrpPurchase(mrpPurchase);
	}

	/**
     * 删除mrp采购单关联对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMrpPurchaseByIds(String ids)
	{
		return mrpPurchaseMapper.deleteMrpPurchaseByIds(Convert.toStrArray(ids));
	}
	
}
