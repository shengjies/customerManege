package com.ruoyi.project.erp.mrp.mapper;

import com.ruoyi.project.erp.mrp.domain.Mrp;
import org.apache.ibatis.annotations.Param;

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
	 * 查询MRP记录列表
	 *
	 * @param supplierId 供应商id
	 * @return MRP记录集合
	 */
	public List<Mrp> selectMrpListBySupId(@Param("supplierId") Integer supplierId);
	
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

	/**
	 * 通过mrp编号查询mrp列表
	 * @param companyId 公司id
	 * @param mCode mrp批次编号
	 * @return 结果
	 */
	public List<Mrp> selectMrpListByMcode(@Param("companyId") Integer companyId, @Param("mCode") String mCode);

	/**
	 * 通过mrp批次编号删除mrp信息
	 * @param mCode mrp编号信息
	 * @return 结果
	 */
	public int deleteMrpByMcode(@Param("mCode") String mCode);
}