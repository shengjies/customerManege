package com.ruoyi.project.production.workData.service;

import com.ruoyi.project.production.workData.domain.WorkData;

import java.util.List;

/**
 * 工单数据 服务层
 *
 * @author zqm
 * @date 2019-04-16
 */
public interface IWorkDataService {
    /**
     * 查询工单数据信息
     *
     * @param dataId 工单数据ID
     * @return 工单数据信息
     */
    public WorkData selectWorkDataById(Integer dataId);

    /**
     * 查询工单数据列表
     *
     * @param workData 工单数据信息
     * @return 工单数据集合
     */
    public List<WorkData> selectWorkDataList(WorkData workData);

    /**
     * 新增工单数据
     *
     * @param workData 工单数据信息
     * @return 结果
     */
    public int insertWorkData(WorkData workData);

    /**
     * 修改工单数据
     *
     * @param workData 工单数据信息
     * @return 结果
     */
    public int updateWorkData(WorkData workData);

    /**
     * 删除工单数据信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkDataByIds(String ids);

}
