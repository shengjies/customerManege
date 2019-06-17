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
	 * @param sopid SOP id
	 * @return
	 */
	public int deleteSopLine(@Param("companyId")int companyId,
							 @Param("lineId")int lineId,@Param("sopId")int sopid);

	/**
	 * 根据公司id 产线id SOP id查询所以的产线SOP 配置细心
	 * @param companyId 公司id
	 * @param lineId 产线id
	 * @param sopId SOP id
	 * @return
	 */
	List<SopLine> selectLineAllSopConfig(@Param("companyId") int companyId,@Param("lineId") int lineId,
										 @Param("sopId") int sopId);

	/**
	 * 通过作业指导书id查询产线配置列表信息
	 * @param companyId 公司第
	 * @param isoId 作业指导书id
	 * @return 结果
	 */
	List<SopLine> selectSopLineListBySopId(@Param("companyId") Integer companyId,@Param("sopId") Integer isoId);

    /**
     * 根据公司id 产线id 产品编号查询对应SOP 配置
     * @param companyId 公司id
     * @param lineId 产线id
     * @param code 产品code
     * @return
     */
	SopLine selectSopByCompanyAndLineAndCode(@Param("companyId") int companyId,@Param("lineId") int lineId,
                                             @Param("code")String code);

}