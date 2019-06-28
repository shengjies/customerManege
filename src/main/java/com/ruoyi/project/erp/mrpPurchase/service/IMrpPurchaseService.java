package com.ruoyi.project.erp.mrpPurchase.service;

import com.ruoyi.project.erp.mrpPurchase.domain.MrpPurchase;
import java.util.List;

/**
 * mrp采购单关联 服务层
 * 
 * @author sj
 * @date 2019-06-26
 */
public interface IMrpPurchaseService 
{
	/**
     * 查询mrp采购单关联信息
     * 
     * @param id mrp采购单关联ID
     * @return mrp采购单关联信息
     */
	public MrpPurchase selectMrpPurchaseById(Integer id);
	
	/**
     * 查询mrp采购单关联列表
     * 
     * @param mrpPurchase mrp采购单关联信息
     * @return mrp采购单关联集合
     */
	public List<MrpPurchase> selectMrpPurchaseList(MrpPurchase mrpPurchase);
	
	/**
     * 新增mrp采购单关联
     * 
     * @param mrpPurchase mrp采购单关联信息
     * @return 结果
     */
	public int insertMrpPurchase(MrpPurchase mrpPurchase);
	
	/**
     * 修改mrp采购单关联
     * 
     * @param mrpPurchase mrp采购单关联信息
     * @return 结果
     */
	public int updateMrpPurchase(MrpPurchase mrpPurchase);
		
	/**
     * 删除mrp采购单关联信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMrpPurchaseByIds(String ids);
	
}
