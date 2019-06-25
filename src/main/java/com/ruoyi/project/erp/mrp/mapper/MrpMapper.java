package com.ruoyi.project.erp.mrp.mapper;

import com.ruoyi.project.erp.mrp.domain.Mrp;
import java.util.List;	

/**
 * MRP记录 数据层
 * 
 * @author sj
 * @date 2019-06-24
 */
public interface MrpMapper 
{
	/**
     * 查询MRP记录信息
     * 
     * @param id MRP记录ID
     * @return MRP记录信息
     */
	public Mrp selectMrpById(Integer id);
	
	/**
     * 查询MRP记录列表
     * 
     * @param mrp MRP记录信息
     * @return MRP记录集合
     */
	public List<Mrp> selectMrpList(Mrp mrp);
	
	/**
     * 新增MRP记录
     * 
     * @param mrp MRP记录信息
     * @return 结果
     */
	public int insertMrp(Mrp mrp);
	
	/**
     * 修改MRP记录
     * 
     * @param mrp MRP记录信息
     * @return 结果
     */
	public int updateMrp(Mrp mrp);
	
	/**
     * 删除MRP记录
     * 
     * @param id MRP记录ID
     * @return 结果
     */
	public int deleteMrpById(Integer id);
	
	/**
     * 批量删除MRP记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMrpByIds(String[] ids);
	
}