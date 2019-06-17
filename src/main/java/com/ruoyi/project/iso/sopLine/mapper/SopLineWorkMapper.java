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
     * 新增产线工位配置指导书页
     * 
     * @param sopLineWork 产线工位配置指导书页信息
     * @return 结果
     */
	public int insertSopLineWork(SopLineWork sopLineWork);

	
	/**
     * 删除产线工位配置指导书页
     * 
     * @param id 产线工位配置指导书页ID
     * @return 结果
     */
	public int deleteSopLineWorkById(Integer id);

	
}