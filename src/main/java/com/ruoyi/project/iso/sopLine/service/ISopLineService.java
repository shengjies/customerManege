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
	public int deleteSopLine(int companyId,int lineId,int sopId,int sopTag);

	/**
	 * 根据公司id 产线id SOP id查询所以的产线SOP 配置细心
	 * @param companyId 公司id
	 * @param lineId 产线id
	 * @param sopId SOP id
	 * @return
	 */
	List<SopLine> selectLineAllSopConfig(int companyId,int lineId,int sopId,int sopTag);

	/**
	 * 根据公司id 产线id SOP id查询所以的工位配置信息
	 * @param companyId 公司id
	 * @param lineId 产线 id
	 * @param sopId SOP id
	 * @return
	 */
	List<SopLineWork> selectWorkstionByCompanyAndLineIdAndSopId(int companyId,int lineId,int sopId);

	/**
	 * 查询作业指导书产线配置列表
	 *
	 * @param isoId 作业指导书id
	 * @param companyId 公司id
	 * @return 作业指导书产线配置集合
	 */
	List<SopLine> selectSopLineListBySopId(Integer companyId,Integer isoId);

	/**
	 * 查询作业指导书工位配置列表
	 * @param companyId 公司id
	 * @param pageId 作业指导书页id
	 * @return 结果
	 */
	List<SopLine> selectSopLineListByPageId(Integer companyId, Integer pageId);

	/**
	 * 单工位SOP配置列表
	 * @param sopLine sop信息
	 * @return 结果
	 */
	List<SopLine> selectSopLineList2(SopLine sopLine);

	/**
	 * 查询对应单工位的工位配置信息
	 * @param companyId 公司id
	 * @param lineId 车间id
	 * @param sopId sopid
	 * @param wId 工位id
	 * @param sopTag  sop生产配置标记
	 * @return 结果
	 */
	SopLineWork selectSopLineWorkInfo(int companyId, int lineId, int sopId, int wId, int sopTag);

	/**
	 * 通过配置主表id查询已经配置的产品
	 * @param sId 主表id
	 * @return 结果
	 */
	List<SopLine> selectSopConfigProBySId(Integer sId);

	/**
	 * 查询所有工位配置
	 * @param sId 主表id
	 * @return 结果
	 */
	List<SopLine> selectSopConfigWorkBySId(Integer sId);
}
