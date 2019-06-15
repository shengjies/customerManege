package com.ruoyi.project.production.workstation.service;

import com.ruoyi.project.production.workstation.domain.Workstation;
import java.util.List;

/**
 * 工位配置 服务层
 * 
 * @author sj
 * @date 2019-06-13
 */
public interface IWorkstationService 
{
	/**
     * 查询工位配置信息
     * 
     * @param id 工位配置ID
     * @return 工位配置信息
     */
	public Workstation selectWorkstationById(Integer id);
	
	/**
     * 查询工位配置列表
     * 
     * @param workstation 工位配置信息
     * @return 工位配置集合
     */
	public List<Workstation> selectWorkstationList(Workstation workstation);
	
	/**
     * 新增工位配置
     * 
     * @param workstation 工位配置信息
     * @return 结果
     */
	public int insertWorkstation(Workstation workstation) throws Exception;
	
	/**
     * 修改工位配置
     * 
     * @param workstation 工位配置信息
     * @return 结果
     */
	public int updateWorkstation(Workstation workstation) throws Exception;
		
	/**
     * 删除工位配置信息
     * 
     * @param id 需要删除的数据ID
     * @return 结果
     */
	public int deleteWorkstationById(Integer id);
	
}
