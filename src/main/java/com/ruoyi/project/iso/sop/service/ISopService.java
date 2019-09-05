package com.ruoyi.project.iso.sop.service;

import com.ruoyi.project.iso.sop.domain.Sop;

import java.util.List;

/**
 * SOP配置主 服务层
 * 
 * @author sj
 * @date 2019-08-30
 */
public interface ISopService 
{
	/**
     * 查询SOP配置主信息
     * 
     * @param id SOP配置主ID
     * @return SOP配置主信息
     */
	public Sop selectSopById(Integer id);
	
	/**
     * 查询SOP配置主列表
     * 
     * @param sop SOP配置主信息
     * @return SOP配置主集合
     */
	public List<Sop> selectSopList(Sop sop);
	
	/**
     * 新增SOP配置主
     * 
     * @param sop SOP配置主信息
     * @return 结果
     */
	public int insertSop(Sop sop);
	
	/**
     * 修改SOP配置主
     * 
     * @param sop SOP配置主信息
     * @return 结果
     */
	public int updateSop(Sop sop);
		
	/**
     * 删除SOP配置主信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSopByIds(String ids);

	/**
	 * 校验配置名称是否重复
	 */
	String checkCnfNameUnique(Sop sop);

	/**
	 * 查询SOP配置主列表
	 *
	 * @param sop SOP配置主信息
	 * @return SOP配置主集合
	 */
	public List<Sop> selectSopList2(Sop sop);

	/**
	 * 通过作业指导书id查询sop配置列表
	 * @param companyId 公司id
	 * @param sopId 作业指导书id
	 * @return 结果
	 */
	List<Sop> selectSopListBySopId(Integer companyId, Integer sopId);
}
