package com.ruoyi.project.iso.sopLine.service;

import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.domain.SopLineWork;

import java.util.List;

/**
 * 作业指导书  产线 配置 服务层
 * 
 * @author sj
 * @date 2019-06-15
 */
public interface ISopLineService 
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
	 * 删除作业指导书  产线 配置信息
	 * @param lineId 产线id
	 * @param sopId SOP id
	 * @return
	 */
	public int deleteSopLine(int companyId,int lineId,int sopId);

	/**
	 * 根据公司id 产线id SOP id查询所以的产线SOP 配置细心
	 * @param companyId 公司id
	 * @param lineId 产线id
	 * @param sopId SOP id
	 * @return
	 */
	List<SopLine> selectLineAllSopConfig(int companyId,int lineId,int sopId);

	/**
	 * 根据公司id 产线id SOP id查询所以的工位配置信息
	 * @param companyId 公司id
	 * @param lineId 产线 id
	 * @param sopId SOP id
	 * @return
	 */
	List<SopLineWork> selectWorkstionByCompanyAndLineIdAndSopId(int companyId,int lineId,int sopId);
}
