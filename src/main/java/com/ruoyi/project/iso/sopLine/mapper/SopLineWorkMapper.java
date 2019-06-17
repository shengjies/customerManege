package com.ruoyi.project.iso.sopLine.mapper;


import com.ruoyi.project.iso.sopLine.domain.SopLineWork;
import org.apache.ibatis.annotations.Param;

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
	 * @param companyId 公司id
	 * @param lineId 产线id
	 * @param sopId SOP id
	 * @return
	 */
	public int deleteSopLineWork(@Param("companyId") int companyId,@Param("lineId") int lineId,
								 @Param("sopId") int sopId);

	/**
	 * 根据公司id 产线id SOP id查询所以的工位配置信息
	 * @param companyId 公司id
	 * @param lineId 产线 id
	 * @param sopId SOP id
	 * @return
	 */
	List<SopLineWork> selectWorkstionByCompanyAndLineIdAndSopId(@Param("companyId") int companyId,
																@Param("lineId") int lineId,@Param("sopId") int sopId);
}