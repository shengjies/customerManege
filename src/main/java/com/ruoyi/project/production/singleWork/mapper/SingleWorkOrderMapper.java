package com.ruoyi.project.production.singleWork.mapper;

import com.ruoyi.project.production.singleWork.domain.SingleWorkOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单工位与工单进行配置 数据层
 *
 * @author sj
 * @date 2019-07-09
 */
public interface SingleWorkOrderMapper {
    /**
     * 查询单工位与工单进行配置信息
     *
     * @param id 单工位与工单进行配置ID
     * @return 单工位与工单进行配置信息
     */
    public SingleWorkOrder selectSingleWorkOrderById(Integer id);

    /**
     * 查询单工位与工单进行配置列表
     *
     * @param singleWorkOrder 单工位与工单进行配置信息
     * @return 单工位与工单进行配置集合
     */
    public List<SingleWorkOrder> selectSingleWorkOrderList(SingleWorkOrder singleWorkOrder);

    /**
     * 新增单工位与工单进行配置
     *
     * @param singleWorkOrder 单工位与工单进行配置信息
     * @return 结果
     */
    public int insertSingleWorkOrder(SingleWorkOrder singleWorkOrder);

    /**
     * 修改单工位与工单进行配置
     *
     * @param singleWorkOrder 单工位与工单进行配置信息
     * @return 结果
     */
    public int updateSingleWorkOrder(SingleWorkOrder singleWorkOrder);

    /**
     * 删除单工位与工单进行配置
     *
     * @param id 单工位与工单进行配置ID
     * @return 结果
     */
    public int deleteSingleWorkOrderById(Integer id);

    /**
     * 批量删除单工位与工单进行配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSingleWorkOrderByIds(String[] ids);

    /**
     * 通过工单id车间id查询相关单工位工位记录信息
     *
     * @param singleP 车间id
     * @param workId 工单id
     * @return 结果
     */
    List<SingleWorkOrder> selectSingleWorkByWorkIdAndPid(@Param("singleP") Integer singleP, @Param("workId") Integer workId);

    /**
     * 根据公司id和工单id查询对应的车间单工位配置的SOP看板硬件编码
     * @param companyId 公司id
     * @param workId 工单id
     * @return
     */
    List<String> countSingleWorkKBCode(@Param("companyId")Integer companyId,@Param("workId")Integer workId);

    /**
     * 根据工单id和单工位id查询单工位分配的工单信息
     * @param workId 工单id
     * @param singleId 工单位id
     * @return 结果
     */
    SingleWorkOrder selectSingleWorkByWorkIdAndSId(@Param("workId") Integer workId, @Param("singleId") Integer singleId);
}