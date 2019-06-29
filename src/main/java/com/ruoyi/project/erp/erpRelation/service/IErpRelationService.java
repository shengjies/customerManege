package com.ruoyi.project.erp.erpRelation.service;

import com.ruoyi.project.erp.erpRelation.domain.ErpRelation;
import java.util.List;

/**
 * 库存出入库MRP操作关联 服务层
 * 
 * @author sj
 * @date 2019-06-28
 */
public interface IErpRelationService 
{
	/**
     * 查询库存出入库MRP操作关联信息
     * 
     * @param id 库存出入库MRP操作关联ID
     * @return 库存出入库MRP操作关联信息
     */
	public ErpRelation selectErpRelationById(Integer id);
	
	/**
     * 查询库存出入库MRP操作关联列表
     * 
     * @param erpRelation 库存出入库MRP操作关联信息
     * @return 库存出入库MRP操作关联集合
     */
	public List<ErpRelation> selectErpRelationList(ErpRelation erpRelation);
	
	/**
     * 新增库存出入库MRP操作关联
     * 
     * @param erpRelation 库存出入库MRP操作关联信息
     * @return 结果
     */
	public int insertErpRelation(ErpRelation erpRelation);
	
	/**
     * 修改库存出入库MRP操作关联
     * 
     * @param erpRelation 库存出入库MRP操作关联信息
     * @return 结果
     */
	public int updateErpRelation(ErpRelation erpRelation);
		
	/**
     * 删除库存出入库MRP操作关联信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteErpRelationByIds(String ids);
	
}
