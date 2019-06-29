package com.ruoyi.project.erp.mrp.service;

import com.ruoyi.project.erp.mrp.domain.Mrp;
import java.util.List;

/**
 * MRP记录 服务层
 * 
 * @author sj
 * @date 2019-06-24
 */
public interface IMrpService 
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
     * 删除MRP记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMrpByIds(String ids);

	/**
	 * 将选中的订单明细生成mrp
	 * @param mrps mrp列表
	 * @return 结果
	 */
    int addMrpByOrDeIds(String mrps);

	/**
	 * 取消MRP
	 * @param mrp mrp信息
	 * @return 结果
	 */
	int cancelMrp(Mrp mrp);

	/**
	 * mrp的所有订单信息
	 * @return 结果
	 */
	List<Mrp> selectAllOrderCode();

	/**
	 * mrp的所有物料信息
	 * @return 结果
	 */
	List<Mrp> selectAllMatCode();

	/**
	 * 查看锁定物料的mrp订单信息
	 * @param mrp mrp信息
	 * @return 结果
	 */
	List<Mrp> selectMrpLockMatList(Mrp mrp);
	/**
	 * 查看订单锁定的物料信息
	 * @param mrp mrp信息
	 * @return 结果
	 */
	List<Mrp> selectMrpListByPIdAndOId(Mrp mrp);
}
