package com.ruoyi.project.erp.mrpPurchase.mapper;

import com.ruoyi.project.erp.mrpPurchase.domain.MrpPurchase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mrp采购单关联 数据层
 * 
 * @author sj
 * @date 2019-06-26
 */
public interface MrpPurchaseMapper 
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
     * 删除mrp采购单关联
     * 
     * @param id mrp采购单关联ID
     * @return 结果
     */
	public int deleteMrpPurchaseById(Integer id);
	
	/**
     * 批量删除mrp采购单关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMrpPurchaseByIds(String[] ids);

	/**
	 * 通过采购单id查询mrp采购单关联信息
	 * @param purId 采购单id
	 * @return 结果
	 */
    List<MrpPurchase> selectMrpPurchaseByPurId(@Param("purId") Integer purId);

	/**
	 * 通过采购单id删除mrp采购单关联信息
	 * @param purId 采购单id
	 * @return 结果
	 */
	int deleteMrpPurchaseByPurId(@Param("purId") Integer purId);

	/**
	 * 通过采购单明细id查询mrp采购单关联信息
	 * @param purDetailId 采购单明细id
	 * @return 结果
	 */
	MrpPurchase selectMrpPurchaseByPurDetailId(@Param("purDetailId") Integer purDetailId);

	/**
	 * 通过物料编码采购单id查询总数大于0的mrp采购单关联列表
	 * @param purId 采购单id
	 * @param materielCode 物料编码
	 * @return 结果
	 */
	List<MrpPurchase> selectMrpPurchaseByPurIdAndMatCodeGtNum(@Param("purId") Integer purId,@Param("materielCode") String materielCode);

	/**
	 * 通过物料编码和采购单id查询实际锁定库存大于0的mrp采购单关联列表
	 * @param purId 采购单id
	 * @param materielCode 物料编码
	 * @return 结果
	 */
	List<MrpPurchase> selectMrpPurchaseByPurIdAndMatCodeGtLockNum(@Param("purId") Integer purId,@Param("materielCode") String materielCode);

}