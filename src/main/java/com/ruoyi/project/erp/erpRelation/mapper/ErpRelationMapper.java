package com.ruoyi.project.erp.erpRelation.mapper;

import com.ruoyi.project.erp.erpRelation.domain.ErpRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存出入库MRP操作关联 数据层
 * 
 * @author sj
 * @date 2019-06-28
 */
public interface ErpRelationMapper 
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
     * 删除库存出入库MRP操作关联
     * 
     * @param id 库存出入库MRP操作关联ID
     * @return 结果
     */
	public int deleteErpRelationById(Integer id);
	
	/**
     * 批量删除库存出入库MRP操作关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteErpRelationByIds(String[] ids);

	/**
	 * 通过物料出入库id主键查询关联表信息
	 * @param matDetailId 物料出入库id
	 * @param reStatus 出入库状态
	 * @return 结果
	 */
	ErpRelation selectErpRelationByMatDetailId(@Param("matDetailId") Integer matDetailId, @Param("reStatus") Integer reStatus);
}