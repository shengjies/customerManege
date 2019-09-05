package com.ruoyi.project.iso.sopLine.mapper;

import com.ruoyi.project.iso.sopLine.domain.SopLine;
import org.apache.ibatis.annotations.Param;

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
	 * 批量删除作业指导书  产线 配置
	 * @param companyId 公司id
	 * @param lineId 产线id
	 * @param wId 单工位id
	 * @param sopId SOP id
	 * @param sopTag sop配置标记
	 * @return 结果
	 */
	public int deleteSopLine(@Param("companyId")Integer companyId,
							 @Param("lineId")Integer lineId,
							 @Param("wId")Integer wId,
							 @Param("sopId")Integer sopId,
							 @Param("sopTag") Integer sopTag);

	/**
	 * 根据公司id 产线id SOP id查询所以的产线SOP 配置细心
	 * @param companyId 公司id
	 * @param lineId 产线id
	 * @param sopId SOP id
	 * @param sopTag sop配置标记流水线或者单工位
	 * @return 结果
	 */
	List<SopLine> selectLineAllSopConfig(@Param("companyId") int companyId,@Param("lineId") int lineId,
										 @Param("sopId") int sopId,@Param("sopTag") int sopTag);

	/**
	 * 通过作业指导书id查询产线配置列表信息
	 * @param companyId 公司第
	 * @param isoId 作业指导书id
	 * @return 结果
	 */
	List<SopLine> selectSopLineListBySopId(@Param("companyId") Integer companyId,@Param("sopId") Integer isoId);

    /**
     * 查询作业指导书配置信息
     * @param companyId 公司id
     * @param lineId 产线或者车间id
     * @param pnCode 产品code
	 * @param wId 工位或者单工位id
	 * @param sopTag sop产线车间配置标记
     * @return 结果
     */
	SopLine selectSopByCompanyAndLineAndCode(@Param("companyId") int companyId,@Param("lineId") int lineId,
                                             @Param("pnCode")String pnCode,@Param("wId") int wId,
											 @Param("sopTag")Integer sopTag);


	/**
	 * 单工位SOP配置列表
	 * @param sopLine sop信息
	 * @return 结果
	 */
	List<SopLine> selectSopLineList2(SopLine sopLine);

	/**
	 * 通过主表id查询配置的产品信息
	 * @param sId 主表id
	 * @return 结果
	 */
	List<SopLine> selectSopConfigProBySId(@Param("sId") Integer sId);

	/**
	 * 查询所有工位配置
	 * @param sId 主表id
	 * @return 结果
	 */
	List<SopLine> selectSopConfigWorkBySId(@Param("sId") Integer sId);

	/**
	 * 通过主表id删除SOP配置
	 * @param sId 主表id
	 * @return 结果
	 */
	int deleteSopLineBySId(@Param("sId") Integer sId);

	/**
	 * 删除sop配置明细
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
    int deleteSopLineBySIds(String[] ids);

	/**
	 * 通过页id查询配置列表
	 * @param companyId 公司id
	 * @param pageId 作业指导书页id
	 * @return 结果
	 */
	List<SopLine> selectSopLineListByPageId(@Param("companyId") Integer companyId, @Param("pageId") Integer pageId);
}