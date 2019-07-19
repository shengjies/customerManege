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
public interface SopLineWorkMapper {
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
     * @param companyId 公司id
     * @param lineId    产线id
     * @param sopId     SOP id
     * @return
     */
    public int deleteSopLineWork(@Param("companyId") int companyId, @Param("lineId") int lineId,
                                 @Param("sopId") int sopId,@Param("sopTag") int sopTag);

    /**
     * 根据公司id 产线id SOP id查询所以的工位配置信息
     *
     * @param companyId 公司id
     * @param lineId    产线 id
     * @param sopId     SOP id
     * @return
     */
    List<SopLineWork> selectWorkstionByCompanyAndLineIdAndSopId(@Param("companyId") int companyId,
                                                                @Param("lineId") int lineId, @Param("sopId") int sopId);

    /**
     * 根据公司id 产线id SOP id 工位id查询对应工位配置的手册
     *
     * @param companyId 公司id
     * @param lineId    产线id
     * @param sopId     SOP  id
     * @param wId       工位id
     * @param sopTag    sop配置标记流水线或者车间
     * @return
     */
    SopLineWork selectInfoByApi(@Param("companyId") int companyId,
                                @Param("lineId") int lineId, @Param("sopId") int sopId,
                                @Param("wId") int wId, @Param("sopTag") int sopTag);

    /**
     * 根据公司id作业指导书id查询工位配置列表
     *
     * @param companyId 公司id
     * @param pageId    作业指导书页id
     * @return 结果
     */
    List<SopLineWork> selectSopLineWorkListBySopId(@Param("companyId") Integer companyId, @Param("pageId") Integer pageId);

    /**
     * 删除车间单工位配置
     *
     * @param companyId 公司id
     * @param wId       单工位id
     * @param sopId     sop作业指导书id
     * @return 结果
     */
    int deleteSopLineWorkByWId(@Param("companyId") Integer companyId, @Param("wId") Integer wId, @Param("sopId") Integer sopId,@Param("sopTag") int sopTag);
}