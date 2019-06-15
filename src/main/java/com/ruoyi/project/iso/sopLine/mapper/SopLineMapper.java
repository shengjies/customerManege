package com.ruoyi.project.iso.sopLine.mapper;

import com.ruoyi.project.iso.sopLine.domain.SopLine;
import java.util.List;	

/**
 * 作业指导书  产线 配置 数据层
 * 
 * @author sj
 * @date 2019-06-15
 */
public interface SopLineMapper 
{
	/**
     * 查询作业指导书  产线 配置信息
     * 
     * @param id 作业指导书  产线 配置ID
     * @return 作业指导书  产线 配置信息
     */
	public SopLine selectSopLineById(Integer id);
	
	/**
     * 查询作业指导书  产线 配置列表
     * 
     * @param sopLine 作业指导书  产线 配置信息
     * @return 作业指导书  产线 配置集合
     */
	public List<SopLine> selectSopLineList(SopLine sopLine);
	
	/**
     * 新增作业指导书  产线 配置
     * 
     * @param sopLine 作业指导书  产线 配置信息
     * @return 结果
     */
	public int insertSopLine(SopLine sopLine);
	
	/**
     * 修改作业指导书  产线 配置
     * 
     * @param sopLine 作业指导书  产线 配置信息
     * @return 结果
     */
	public int updateSopLine(SopLine sopLine);
	
	/**
     * 删除作业指导书  产线 配置
     * 
     * @param id 作业指导书  产线 配置ID
     * @return 结果
     */
	public int deleteSopLineById(Integer id);
	
	/**
     * 批量删除作业指导书  产线 配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSopLineByIds(String[] ids);
	
}