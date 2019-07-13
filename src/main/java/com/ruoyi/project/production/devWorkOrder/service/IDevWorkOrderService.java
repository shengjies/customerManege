package com.ruoyi.project.production.devWorkOrder.service;

import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 工单 服务层
 *
 * @author zqm
 * @date 2019-04-12
 */
public interface IDevWorkOrderService {
    /**
     * 查询工单信息
     *
     * @param id 工单ID
     * @return 工单信息
     */
    public DevWorkOrder selectDevWorkOrderById(Integer id);

    /**
     * 查询工单列表
     *
     * @param devWorkOrder 工单信息
     * @return 工单集合
     */
    public List<DevWorkOrder> selectDevWorkOrderList(DevWorkOrder devWorkOrder);

    /**
     * 新增工单
     *
     * @param devWorkOrder 工单信息
     * @return 结果
     */
    public int insertDevWorkOrder(DevWorkOrder devWorkOrder);

    /**
     * 修改工单
     *
     * @param devWorkOrder 工单信息
     * @return 结果
     */
    public int updateDevWorkOrder(DevWorkOrder devWorkOrder);

    /**
     * 删除工单信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDevWorkOrderByIds(String ids);

    /**
     * 校验工单号是否存在
     * @return
     */
    public String checkWorkOrderNumber(DevWorkOrder devWorkOrder, HttpServletRequest request);

    /**
     * 工单开始暂停状态修改，第一次点击开始初始化数据
     * @param id
     * @return
     */
    int editWorkerOrderById(Integer id);

    /**
     * 校验流水线是否只有一个处于生产状态的工单
     * @param lineId
     * @return
     */
    DevWorkOrder checkWorkLineUnique(Integer lineId);

    /**
     * 结束工单
     * @param id
     * @return
     */
    int finishWorkerOrder(Integer id,HttpServletRequest request);

    /**
     * 提交工单确认工单
     * @param id
     * @return
     */
    int submitWorkOrder(Integer id);

    /**
     * 根据工单编号查询对应的工单信息
     * @param work_id 工单编号
     * @return
     */
    DevWorkOrder findWorkInfoById(int work_id);

    /**
     * 通过产线Id查询该产线正在生产的工单
     * @param lineId
     * @return
     */
    DevWorkOrder selectWorkOrderBeInByLineId(Integer lineId,HttpServletRequest request);

    /**
     * 通过产线id查询已经提交的工单列表
     * @param lineId 产线
     * @return
     */
    List<DevWorkOrder> selectWorkOrderFinishByLineId(int lineId,HttpServletRequest request);

    /**
     * 根据产线id或工单id查询工单信息
     * @param lineId
     * @param workOrderId
     * @return 结果
     */
    DevWorkOrder selectWorkOrderFinishByLineIdAndWorkOrderId(int lineId, int workOrderId,HttpServletRequest request);

    /**
     * 工单变更
     * @param order
     * @return
     */
    int changeOrder(DevWorkOrder order);

    /**
     * 根据工单id查询对应的ECN信息
     * @param workId 工单id
     * @return
     */
    DevWorkOrder selectWorkOrderEcn(int workId);

    /**
     * 工单合并验证，合并的前提是 所以工单是未进行工单并且是同一产品
     * @param workIds 需合并工单id
     * @param type 0、合单 1、拆单
     * @return
     * @throws Exception
     */
    String workMergeVerif(int[] workIds,int type) throws Exception;

    /**
     * 初始化合并工单信息
     * @param workIds 工单id
     * @return
     */
    Map<String,Object> workMergePage(String workIds);

    /**
     * 合并工单信息
     * @param order 工单信息
     * @return
     * @throws Exception
     */
    int workMerge(DevWorkOrder order) throws Exception;

    /**
     * 拆分工单
     * @param orders 拆单详情
     * @return
     * @throws Exception
     */
    int workDismantleInfo(List<DevWorkOrder> orders) throws Exception;

    /**
     * 查询所有下到车间的工单信息
     * @param wlSign 工单车间流水线标记状态
     * @return 结果
     */
    public List<DevWorkOrder> selectWorkListInSw(Integer wlSign);

    /**
     * 通过工单的进行状态查询所有的工单信息
     * @param workOrderStatus 工作进行状态
     * @return 结果
     */
    public List<DevWorkOrder> selectWorkListInWorkStatus(Integer workOrderStatus);

    /**
     * 查询工位未配置的工单信息
     * @param lineId 车间id
     * @param workSign 工单确认标记
     * @param wlSign 工单下到车间标记
     * @param singleId 单工位id
     * @param companyId 公司id
     * @return 结果
     */
    List<DevWorkOrder> selectAllNotConfigBySwId(Integer lineId,Integer workSign, Integer wlSign, Integer singleId, Integer companyId);
}
