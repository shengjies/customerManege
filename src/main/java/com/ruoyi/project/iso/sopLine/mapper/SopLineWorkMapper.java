package com.ruoyi.project.iso.sopLine.mapper;


import com.ruoyi.project.iso.sopLine.domain.SopLineWork;

import java.util.List;

/**
 * 产线工位配置指导书页 数据层
 * 
 * @author sj
 * @date 2019-06-15
 */
public interface SopLineWorkMapper 
{
	/**
     * 查询产线工位配置指导书页信息
     * 
     * @param id 产线工位配置指导书页ID
     * @return 产线工位配置指导书页信息
     */
	public SopLineWork selectSopLineWorkById(Integer id);
	
	/**
     * 查询产线工位配置指导书页列表
     * 
     * @param sopLineWork 产线工位配置指导书页信息
     * @return 产线工位配置指导书页集合
     */
	public List<SopLineWork> selectSopLineWorkList(SopLineWork sopLineWork);
	
	/**
     * 新增产线工位配置指导书页
     * 
     * @param sopLineWork 产线工位配置指导书页信息
     * @return 结果
     */
	public int insertSopLineWork(SopLineWork sopLineWork);
	
	/**
     * 修改产线工位配置指导书页
     * 
     * @param sopLineWork 产线工位配置指导书页信息
     * @return 结果
     */
	public int updateSopLineWork(SopLineWork sopLineWork);
	
	/**
     * 删除产线工位配置指导书页
     * 
     * @param id 产线工位配置指导书页ID
     * @return 结果
     */
	public int deleteSopLineWorkById(Integer id);
	
	/**
     * 批量删除产线工位配置指导书页
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSopLineWorkByIds(String[] ids);
	
}