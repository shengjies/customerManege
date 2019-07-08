package com.ruoyi.project.production.devWorkOrder.service;

import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.TimeUtil;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.orderDetails.domain.OrderDetails;
import com.ruoyi.project.erp.orderDetails.mapper.OrderDetailsMapper;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.production.devWorkData.domain.DevWorkData;
import com.ruoyi.project.production.devWorkData.mapper.DevWorkDataMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.ecnLog.domain.EcnLog;
import com.ruoyi.project.production.ecnLog.mapper.EcnLogMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.workData.domain.WorkData;
import com.ruoyi.project.production.workData.mapper.WorkDataMapper;
import com.ruoyi.project.production.workDayHour.domain.WorkDayHour;
import com.ruoyi.project.production.workDayHour.mapper.WorkDayHourMapper;
import com.ruoyi.project.production.workOrderChange.domain.WorkOrderChange;
import com.ruoyi.project.production.workOrderChange.mapper.WorkOrderChangeMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 工单 服务层实现
 *
 * @author zqm
 * @date 2019-04-12
 */
@Service("workOrder")
public class DevWorkOrderServiceImpl implements IDevWorkOrderService {

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private DevWorkDataMapper devWorkDataMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper; // 产线

    @Autowired
    private DevProductListMapper productListMapper; // 产品

    @Autowired
    private WorkOrderChangeMapper orderChangeMapper;

    @Autowired
    private EcnLogMapper ecnLogMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private WorkstationMapper workstationMapper;

    @Autowired
    private WorkDataMapper workDataMapper;

    @Autowired
    private WorkDayHourMapper workDayHourMapper;

    @Autowired
    private WorkOrderChangeMapper workOrderChangeMapper;


    public String getWorkOrderCode(){
        return CodeUtils.getWorkOrderCode(JwtUtil.getTokenUser(ServletUtils.getRequest()).getCompanyId());
    }

    /**
     * 查询工单信息
     *
     * @param id 工单ID
     * @return 工单信息
     */
    @Override
    public DevWorkOrder selectDevWorkOrderById(Integer id) {
        DevWorkOrder workOrder = devWorkOrderMapper.selectDevWorkOrderById(id);
        ProductionLine productionLine = productionLineMapper.selectProductionLineById(workOrder.getLineId());
        User  user = userMapper.selectUserInfoById(productionLine.getDeviceLiable());
        productionLine.setDeviceLiableName(user == null?"":user.getUserName());
        user = userMapper.selectUserInfoById(productionLine.getDeviceLiableTow());
        productionLine.setDeviceLiableTowName(user == null?"":user.getUserName());
        workOrder.setProductionLine(productionLine);
        return workOrder;
    }

    /**
     * 查询工单列表
     *
     * @param devWorkOrder 工单信息
     * @return 工单集合
     */
    @Override
    public List<DevWorkOrder> selectDevWorkOrderList(DevWorkOrder devWorkOrder,HttpServletRequest request) {
        User sysUser = JwtUtil.getTokenUser(request);
        if (sysUser == null) {
            return Collections.emptyList();
        }
        if (!User.isSys(sysUser)) {
            devWorkOrder.setCompanyId(sysUser.getCompanyId());
        }
        List<DevWorkOrder> workOrders = devWorkOrderMapper.selectDevWorkOrderList(devWorkOrder);
        for (DevWorkOrder workOrder : workOrders) {
            User user = userMapper.selectUserInfoById(workOrder.getDeviceLiable());
            ProductionLine productionLine = productionLineMapper.selectProductionLineById(workOrder.getLineId());
            if (null != user) {
                workOrder.setUser(user);
            }
            if (null != productionLine) {
                workOrder.setProductionLine(productionLine);
            }
        }
        return workOrders;
    }

    /**
     * 新增工单
     *
     * @param devWorkOrder 工单信息
     * @return 结果
     */
    @Override
    public int insertDevWorkOrder(DevWorkOrder devWorkOrder,HttpServletRequest request) {
        User u = JwtUtil.getTokenUser(request);
        if(u == null)return  0;
        Integer productId = Integer.valueOf(devWorkOrder.getProductCode());
        DevProductList devProductList = productListMapper.selectDevProductListById(productId);
        // 设置工单产品的名称
        devWorkOrder.setProductName(devProductList ==null?"":devProductList.getProductName());
        // 设置工单产品编码
        devWorkOrder.setProductCode(devProductList ==null?"":devProductList.getProductCode());
        // 设置工单属于哪个公司
        devWorkOrder.setCompanyId(u.getCompanyId());
        // 创建者
        devWorkOrder.setCreateBy(u.getUserName());
        devWorkOrderMapper.insertDevWorkOrder(devWorkOrder);
        if(devWorkOrder.getEcnStatus() == 1 ){//添加ecn备注信息
            // 新增工单变更记录
            EcnLog ecnLog = new EcnLog();
            ecnLog.setCompanyId(u.getCompanyId());
            ecnLog.setSaveId(devWorkOrder.getId());
            ecnLog.setSaveCode(devWorkOrder.getWorkorderNumber());
            ecnLog.setEcnType(2);
            ecnLog.setEcnText(devWorkOrder.getEcnText());
            ecnLog.setCreatePeople(devWorkOrder.getCreateBy());
            ecnLog.setCreateId(u.getUserId().intValue());
            ecnLog.setCreateTime(new Date());
            ecnLogMapper.insertEcnLog(ecnLog);
            // 更新产品ECN备注信息
            if (!devProductList.getEcnText().equals(devWorkOrder.getEcnText())) { // 新建工单时更改了产品ECN
                // 更新ECN日志
                EcnLog proEcnLog = new EcnLog();
                proEcnLog.setCompanyId(u.getCompanyId());
                proEcnLog.setSaveId(devProductList.getId());
                proEcnLog.setSaveCode(devProductList.getProductCode());
                proEcnLog.setEcnType(1);
                proEcnLog.setEcnText(devWorkOrder.getEcnText());
                proEcnLog.setCreatePeople(devWorkOrder.getCreateBy());
                proEcnLog.setCreateId(u.getUserId().intValue());
                proEcnLog.setCreateTime(new Date());
                ecnLogMapper.insertEcnLog(proEcnLog);
                // 更新产品ECN
                devProductList.setEcnText(devWorkOrder.getEcnText());
                productListMapper.updateDevProductList(devProductList);
            }
        }
        return 1;
    }

    /**
     * 修改工单
     *
     * @param devWorkOrder 工单信息
     * @return 结果
     */
    @Override
    public int updateDevWorkOrder(DevWorkOrder devWorkOrder,User user) {
        DevWorkOrder workOrder = devWorkOrderMapper.selectDevWorkOrderById(devWorkOrder.getId());
        Long userId = user.getUserId(); // 登录用户id
        ProductionLine productionLine = productionLineMapper.selectProductionLineById(workOrder.getLineId());
        // 不是工单负责人
        if (productionLine.getDeviceLiable() != userId.intValue() && productionLine.getDeviceLiableTow() != userId.intValue()) {
            throw new BusinessException("不是工单负责人");
        }
        return devWorkOrderMapper.updateDevWorkOrder(devWorkOrder);
    }

    /**
     * 删除工单对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
//    @DataSource(value = DataSourceType.SLAVE)
    public int deleteDevWorkOrderByIds(String ids) {
        return devWorkOrderMapper.deleteDevWorkOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 校验工单号是否存在
     *
     * @return
     */
    @Override
    public String checkWorkOrderNumber(DevWorkOrder devWorkOrder,HttpServletRequest request) {
        Integer companyId = JwtUtil.getTokenUser(request).getCompanyId();
        Long count = devWorkOrderMapper.checkWorkOrderNumber(devWorkOrder.getWorkorderNumber(), companyId);
        if (count > 0) { // 说明数据库存在工单数据
            return WorkConstants.WORKERORDER_NUMBER_NOT_UNIQUE;
        }
        return WorkConstants.WORKERORDER_NUMBER_UNIQUE;
    }

    /**
     * 工单状态控制 <br>
     * 点击开始，开始工单，数据进行初始化
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editWorkerOrderById(Integer id,HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        Long userId = user.getUserId();
        DevWorkOrder devWorkOrder = devWorkOrderMapper.selectDevWorkOrderById(id);

        ProductionLine productionLine = productionLineMapper.selectProductionLineById(devWorkOrder.getLineId());
        // 不是工单负责人
        if (productionLine.getDeviceLiable() != userId.intValue() && productionLine.getDeviceLiableTow() != userId.intValue()) {
            throw new BusinessException("不是工单负责人");
        }
        if (devWorkOrder.getOperationStatus() == 0) {
            Long count = devWorkOrderMapper.checkWorkLineUnique(devWorkOrder.getLineId());
            // 判断流水线是否只有一个正在进行生产的工单
            if (count > 0) {
                throw new BusinessException("该流水线有工单未完成");
            }
        }

        // 工单生产状态处于进行中
        if (null != devWorkOrder && devWorkOrder.getWorkorderStatus().equals(WorkConstants.WORK_STATUS_STARTING)) {
            // 页面点击暂停按钮暂时暂停工单生产
            if (devWorkOrder.getOperationStatus().equals(WorkConstants.OPERATION_STATUS_STARTING)) {
                devWorkOrder.setOperationStatus(WorkConstants.OPERATION_STATUS_PAUSE);
                devWorkOrder.setUpdateBy(user.getUserName());
                //将其工单对应的数据需要重新记录初始值
                devWorkDataMapper.updateWorkSigInit(devWorkOrder.getId());
                //计数时间
                devWorkOrder.setSignHuor(devWorkOrder.getSignHuor()+TimeUtil.getDateDel(devWorkOrder.getSignTime(),new Date()));
            } else if (devWorkOrder.getOperationStatus().equals(WorkConstants.OPERATION_STATUS_PAUSE)) {
                //页面点击开始按钮继续工单生产
                devWorkOrder.setOperationStatus(WorkConstants.OPERATION_STATUS_STARTING);
                devWorkOrder.setUpdateBy(user.getUserName());
                devWorkOrder.setSignTime(new Date());
            }
        }
        //首次点击开始，工单处于未进行、未开始的状态，页面点击开始按钮
        if (null != devWorkOrder && devWorkOrder.getWorkorderStatus().equals(WorkConstants.WORK_STATUS_NOSTART)) {
            if (devWorkOrder.getOperationStatus().equals(WorkConstants.OPERATION_STATUS_NOSTART)) {
                // 实际开始时间
                devWorkOrder.setStartTime(new Date());
                devWorkOrder.setStartTime(new Date());  // 实际开始时间
                devWorkOrder.setSignTime(new Date());//标记开始时间
                devWorkOrder.setSignHuor(0F);//标记用时
            }
            devWorkOrder.setWorkorderStatus(WorkConstants.WORK_STATUS_STARTING);  // 修改工单的状态为进行中
            devWorkOrder.setOperationStatus(WorkConstants.OPERATION_STATUS_STARTING);   // 修改工单的操作状态为正在进行，页面显示暂停按钮
            devWorkOrder.setUpdateBy(user.getUserName());   // 工单的更新者
            // 通过产线id获取各个工位信息
            List<Workstation> workstationList = workstationMapper.selectWorkstationListByLineId(user.getCompanyId(),devWorkOrder.getLineId());
            WorkData workData = null;
            WorkDayHour workDayHour = null;
            if (com.ruoyi.common.utils.StringUtils.isNotEmpty(workstationList)) {
                for (Workstation workstation : workstationList) {
                    // 初始化工单数据
                    workData = new WorkData();
                    workData.setWorkId(devWorkOrder.getId());
                    workData.setCompanyId(devWorkOrder.getCompanyId());
                    workData.setLineId(devWorkOrder.getLineId());
                    // 设置计数器硬件
                    workData.setDevId(workstation.getDevId());
                    workData.setDevName(workstation.getDevName());
                    // 设置工位
                    workData.setIoId(workstation.getId());
                    workData.setCreateTime(new Date());
                    workDataMapper.insertWorkData(workData);

                    // 初始化工单各个IO口每小时数据
                    workDayHour = new WorkDayHour();
                    workDayHour.setWorkId(devWorkOrder.getId());
                    workDayHour.setCompanyId(devWorkOrder.getCompanyId());
                    workDayHour.setLineId(devWorkOrder.getLineId());
                    // 初始化硬件名称以及工位信息
                    workDayHour.setDevId(workstation.getDevId());
                    workDayHour.setDevName(workstation.getDevName());
                    workDayHour.setIoId(workstation.getId());
                    workDayHour.setDataTime(new Date()); // 创建时间年月日
                    workDayHour.setCreateTime(new Date()); // 创建时间年月日时分秒
                    workDayHourMapper.insertWorkDayHour(workDayHour); // 保存工单各个IO口每小时数据
                }
            }
        }

        return devWorkOrderMapper.updateDevWorkOrder(devWorkOrder);
    }

    /**
     * 校验流水线是否只有一个处于生产状态的工单
     *
     * @param lineId
     * @return
     */
    @Override
    public Long checkWorkLineUnique(Integer lineId) {
        return devWorkOrderMapper.checkWorkLineUnique(lineId);
    }

    /**
     * 页面点击完成工单，工单可进行修改
     *
     * @param id
     * @return
     */
    @Override
    public int finishWorkerOrder(Integer id,HttpServletRequest request) {
        User tokenUser = JwtUtil.getTokenUser(request);
        Long userId = tokenUser.getUserId();
        DevWorkOrder devWorkOrder = devWorkOrderMapper.selectDevWorkOrderById(id);
        ProductionLine productionLine = productionLineMapper.selectProductionLineById(devWorkOrder.getLineId());
        // 不是工单负责人
        if (productionLine.getDeviceLiable() != userId.intValue() && productionLine.getDeviceLiableTow() != userId.intValue()) {
            throw new BusinessException("不是工单负责人");
        }
        devWorkOrder.setWorkorderStatus(WorkConstants.WORK_STATUS_END); // 设置工单的生产状态为已经完成
        devWorkOrder.setOperationStatus(WorkConstants.OPERATION_STATUS_FINISH); // 设置工单的操作状态为结束
        devWorkOrder.setEndTime(new Date()); // 设置结束时间
        devWorkOrder.setUpdateBy(tokenUser.getUserName());
        devWorkOrder.setUpdateTime(new Date());
        if(devWorkOrder.getSignTime() != null){
            devWorkOrder.setSignHuor(devWorkOrder.getSignHuor()+TimeUtil.getDateDel(devWorkOrder.getSignTime(),new Date()));
        }
        return devWorkOrderMapper.updateDevWorkOrder(devWorkOrder); // 更新
    }

    /**
     * 页面点击提交工单，工单状态不可修改和删除
     *
     * @param id
     * @return
     */
    @Override
    public int submitWorkOrder(Integer id,HttpServletRequest request) {
        User tokenUser = JwtUtil.getTokenUser(request);
        Long userId = tokenUser.getUserId(); // 登录用户id
        DevWorkOrder devWorkOrder = devWorkOrderMapper.selectDevWorkOrderById(id);
        if (!devWorkOrder.getWorkorderStatus().equals(WorkConstants.WORK_STATUS_END)) {
            throw new BusinessException("未完成的工单不能提交");
        }
        ProductionLine productionLine = productionLineMapper.selectProductionLineById(devWorkOrder.getLineId());
        // 不是工单负责人
        if (productionLine.getDeviceLiable() != userId.intValue() && productionLine.getDeviceLiableTow() != userId.intValue()) {
            throw new BusinessException("不是工单负责人");
        }
        if (devWorkOrder.getWorkSign().equals(WorkConstants.WORK_SIGN_YES)) {
            throw new BusinessException("该工单已经提交过，不能重复提交");
        }
        //计数生产用时
//        devWorkOrder.setSignHuor(devWorkOrder.getSignHuor()+TimeUtil.getDateDel(devWorkOrder.getSignTime(),new Date()));
        devWorkOrder.setWorkSign(WorkConstants.WORK_SIGN_YES); // 设置状态为已确认数据不可进行修改和删除
        devWorkOrder.setUpdateTime(new Date());
        devWorkOrder.setUpdateBy(tokenUser.getUserName());
        return devWorkOrderMapper.updateDevWorkOrder(devWorkOrder);
    }

    /**
     * 查询生产状态处于正在进行的所有工单
     *
     * @return
     */
    public List<DevWorkOrder> selectWorkOrderAllBeIn(Cookie[] cookies) {
        Integer companyId = JwtUtil.getTokenCookie(cookies).getCompanyId();
        return devWorkOrderMapper.selectWorkOrderAllBeIn(companyId);
    }

    /**
     * 查询当天所有工单
     *
     * @return
     */
    public List<DevWorkOrder> selectWorkOrderAllToday(Cookie[] cookies) {
        User user = JwtUtil.getTokenCookie(cookies);
        List<DevWorkOrder> workOrders = devWorkOrderMapper.selectWorkOrderAllToday(user.getCompanyId());
        for (DevWorkOrder workOrder : workOrders) {
            ProductionLine productionLine = productionLineMapper.selectProductionLineById(workOrder.getLineId());
            workOrder.setProductionLine(productionLine); // 工单产线信息
        }
        return workOrders;
    }

    /**
     * 通过产线Id查询该产线正在生产的工单
     *
     * @param lineId
     * @return
     */
    @Override
    public DevWorkOrder selectWorkOrderBeInByLineId(Integer lineId,HttpServletRequest request) {
        return devWorkOrderMapper.selectWorkByCompandAndLine(JwtUtil.getTokenUser(request).getCompanyId(), lineId);
    }

    /**
     * 根据工单编号查询对应的工单信息
     *
     * @param work_id 工单编号
     * @return
     */
    @Override
    public DevWorkOrder findWorkInfoById(int work_id) {
        //查询对应的工单是否存在
        DevWorkOrder order = devWorkOrderMapper.selectDevWorkOrderById(work_id);
        if (order == null || order.getLineId() == null) {
            return null;
        }
        //查询对应的产线信息
        ProductionLine line = productionLineMapper.selectProductionLineById(order.getLineId());
        if (line == null) return null;
        //判断产线是否是手动
        if (line.getManual() == 0) {
            order.setCumulativeNumber(0);//默认为0
            //为自动、查询对应的产线的警戒线标记IO口
            Workstation workstation = workstationMapper.selectWorkstationSignByLineId(line.getId(),line.getCompanyId());
            if (workstation != null) {
                //查询对应的累计数据
                DevWorkData data = devWorkDataMapper.selectWorkDataByCompanyLineWorkDev(order.getCompanyId(), line.getId(),
                        order.getId(), workstation.getDevId(), workstation.getId());
                if (data != null) order.setCumulativeNumber(data.getCumulativeNum());
            }
        }
        float standardHour = order.getSignHuor();
        //达成率默认为0
        order.setReachRate(0.0F);
        if (order.getWorkorderStatus() == WorkConstants.WORK_STATUS_STARTING && order.getCumulativeNumber() != null) {
            //计数标准产量
            if(order.getOperationStatus() == WorkConstants.OPERATION_STATUS_STARTING){//工单正在开始中
                standardHour += TimeUtil.getDateDel(order.getSignTime(),new Date());
            }
            int standardTotal = (int)(order.getProductStandardHour()*standardHour);
            order.setReachRate(standardTotal == 0 ? 0.0F : new BigDecimal(((float) order.getCumulativeNumber() / ((float)standardTotal))*100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        }
        order.setSignHuor(standardHour);
        return order;
    }

    /**
     * 查询昨天生产的工单
     *
     * @return
     */
    public List<DevWorkOrder> selectWorkOrderAllYesterday() {
        Integer companyId = ShiroUtils.getSysUser().getCompanyId();
        List<DevWorkOrder> workOrders = devWorkOrderMapper.selectWorkOrderAllYesterday(companyId);
        for (DevWorkOrder workOrder : workOrders) {
            ProductionLine productionLine = productionLineMapper.selectProductionLineById(workOrder.getLineId());
            workOrder.setProductionLine(productionLine); // 工单产线信息
        }
        return workOrders;
    }

    /**
     * 通过产线id查询已经提交的工单列表
     * @param lineId 产线
     * @return 结果
     */
    @Override
    public List<DevWorkOrder> selectWorkOrderFinishByLineId(int lineId,HttpServletRequest request) {
        List<DevWorkOrder> workOrders = devWorkOrderMapper.selectWorkOrderFinishByLineIdOrWorkOrderId(JwtUtil.getTokenUser(request).getCompanyId(), lineId, null);
        for (DevWorkOrder workOrder : workOrders) {
            DevProductList productList = productListMapper.selectDevProductByCode(JwtUtil.getTokenUser(request).getCompanyId(), workOrder.getProductCode());
            workOrder.setProductId(productList.getId());
            workOrder.setProductModel(productList.getProductModel());
        }
        return workOrders;
    }

    /**
     * 根据产线id或工单id查询工单信息
     * @param lineId
     * @param workOrderId
     * @return 结果
     */
    @Override
    public DevWorkOrder selectWorkOrderFinishByLineIdAndWorkOrderId(int lineId, int workOrderId,HttpServletRequest request) {
        return devWorkOrderMapper.selectWorkOrderFinishByLineIdOrWorkOrderId(JwtUtil.getTokenUser(request).getCompanyId(),lineId,workOrderId).get(0);
    }

    /**
     * 工单变更
     * @param order
     * @return
     */
    @Override
    public int changeOrder(DevWorkOrder order,HttpServletRequest request) {
        if(order == null)return 0;
        User user = JwtUtil.getTokenUser(request);
        if(user == null)return 0;
        DevWorkOrder devWorkOrder = devWorkOrderMapper.selectDevWorkOrderById(order.getId());
        if(devWorkOrder == null)return 0;
        //查询对应的产线信息
        ProductionLine line = productionLineMapper.selectProductionLineById(devWorkOrder.getLineId());
        if (line ==null)return 0;
        //保存变更记录
        WorkOrderChange change = new WorkOrderChange();
        change.setCompanyId(user.getCompanyId());
        change.setOrderId(devWorkOrder.getId());
        change.setOrderCode(devWorkOrder.getWorkorderNumber());
        change.setLineId(line.getId());
        change.setLineName(line.getLineName());
        User  u1 = userMapper.selectUserInfoById(line.getDeviceLiable());
        change.setDeviceLiable(u1 ==null?"":u1.getUserName());
        u1 = userMapper.selectUserInfoById(line.getDeviceLiableTow());
        change.setDeviceLiable2(u1==null?"":u1.getUserName());
        change.setProductNumber(devWorkOrder.getProductNumber());
        change.setProductionStart(devWorkOrder.getProductionStart());
        change.setProductionEnd(devWorkOrder.getProductionEnd());
        change.setCreatePeople(user.getUserName());
        change.setCreateTime(new Date());
        change.setRemark(order.getRemark());
        orderChangeMapper.insertWorkOrderChange(change);
        return devWorkOrderMapper.updateDevWorkOrder(order);
    }

    /**
     * 根据工单id查询对应的ECN信息
     * @param workId 工单id
     * @return
     */
    @Override
    public DevWorkOrder selectWorkOrderEcn(int workId) {
        DevWorkOrder order = devWorkOrderMapper.selectDevWorkOrderById(workId);
        if(order != null && !StringUtils.isEmpty(order.getOrderCode()) && !order.getOrderCode().equals("NaN")){
            //查询对应的工单备注信息
            OrderDetails details = orderDetailsMapper.findByOrderCodeAndProductCode(order.getCompanyId(),order.getOrderCode(),order.getProductCode());
            if(details != null){
                order.setOrderRemark(details.getRemark());
            }
            //查询对应的产品信息
            DevProductList productList = productListMapper.findByCompanyIdAndProductCode(order.getCompanyId(),order.getProductCode());
            if(productList != null){
                order.setProductEcn(productList.getEcnStatus());
                //查询对应的产品ecn信息
                if(productList.getEcnStatus() == 1){
                   order.setEcnLog(ecnLogMapper.findByCompanyIdAndProductId(order.getCompanyId(),productList.getId()));
                }
            }
        }
        return order;
    }

    /**
     * 工单合并验证，合并的前提是 所以工单是未进行工单并且是同一产品
     * @param workIds 需合并工单id
     *                * @param type 0、合单 1、拆单
     * @return
     * @throws Exception
     */
    @Override
    public String workMergeVerif(int[] workIds,int type) throws Exception {
        int failNum = 0;//统计错误次数
        StringBuilder failMsg = new StringBuilder();//错误提示信息
        StringBuilder successMsg = new StringBuilder();
        if(workIds == null){
            failMsg.insert(0,"很抱歉，您没有选中需要操作的工单");
            throw new Exception(failMsg.toString());
        }
        if(type == 1 && workIds.length != 1){
            failMsg.insert(0,"很抱歉，拆除的工单操作，需要选择一条工单信息");
            throw new Exception(failMsg.toString());
        }
        if(type ==0 &&  workIds.length <= 1){
            failMsg.insert(0,"很抱歉，合并的工单操作，工单条数至少2条以上");
            throw new Exception(failMsg.toString());
        }
        String pCode = null;//用于记录第一次查询出来的工单不为空的产品编码
        int index =0;//用于记录循环次数
        DevWorkOrder order = null;
        for (int workId : workIds) {
            index ++;
            //根据id查询对应工单信息
            order = devWorkOrderMapper.selectDevWorkOrderById(workId);
            if(order == null){
                failNum ++;
                failMsg.append("<br/>很抱歉，您选中的第"+(index)+"工单信息不存在");
                continue;
            }
            if(order.getAbolish() == 1){//工单已经作废
                failNum ++;
                failMsg.append("<br/>很抱歉，您选中的第"+(index)+"工单信息已经作废，不能进行操作");
                continue;
            }
            if(StringUtils.isEmpty(pCode)){
                pCode = order.getProductCode();
            }
            //判断工单是否是未进行
            if(order.getWorkorderStatus() !=  WorkConstants.WORK_STATUS_NOSTART){
                failNum++;
                failMsg.append("<br/>很抱歉，请选择工单状态为未进行状态，您选中的第"+(index)+"工单状态不是未进行状态");
                continue;
            }
            //判断是否是同一产品
            if(!pCode.equals(order.getProductCode())){
                failNum++;
                failMsg.append("<br/>很抱歉，请选择同一产品的工单，您选中的第"+(index)+"工单所生产的产品与选中的第一条工单所生产的产品不同");
                continue;
            }
        }
        if(failNum > 0){
            failMsg.insert(0,"很抱歉，操作失败:");
            throw new Exception(failMsg.toString());
        }
        successMsg.insert(0,"验证通过");
        return successMsg.toString();
    }

    /**
     * 初始化合并工单信息
     * @param workIds 工单id
     * @return
     */
    @Override
    public Map<String, Object> workMergePage(String workIds) {
        User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
        Map<String,Object> map = new HashMap<>();
        String[] ids = workIds.split(",");
        DevWorkOrder order = null;
        int num = 0;
        for (String id : ids) {
            //查询工单
            order = devWorkOrderMapper.selectDevWorkOrderById(Integer.parseInt(id));
            num += order.getProductNumber();
        }
        //查询产品
        DevProductList productList = productListMapper.selectDevProductByCode(u.getCompanyId(),order.getProductCode());
        map.put("code",CodeUtils.getWorkOrderCode(u.getCompanyId()));
        map.put("num",num);
        map.put("product",productList);
        return map;
    }

    /**
     * 合并工单信息
     * @param order 工单信息
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int workMerge(DevWorkOrder order) throws Exception {
        User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
        ProductionLine line = null;
        StringBuilder hCode =new StringBuilder();
        WorkOrderChange change =null;
        String ids[] = order.getParam1().split(",");
        for (String id : ids) {
            //查询工单信息
            DevWorkOrder workOrder = devWorkOrderMapper.selectDevWorkOrderById(Integer.parseInt(id));
            hCode.append(workOrder.getWorkorderNumber()+"<br/>");
            devWorkOrderMapper.updateWorkOrderAbolish(workOrder.getId());
           line = productionLineMapper.selectProductionLineById(workOrder.getLineId());
            //添加工单变更记录
            change = new WorkOrderChange();
            change.setCompanyId(u.getCompanyId());
            change.setOrderId(workOrder.getId());
            change.setOrderCode(workOrder.getOrderCode());
            change.setLineId(workOrder.getLineId());
            change.setLineName(line.getLineName());
            change.setDeviceLiable(userMapper.selectUserInfoById(line.getDeviceLiable()).getUserName());
            change.setDeviceLiable2(userMapper.selectUserInfoById(line.getDeviceLiableTow()).getUserName());
            change.setProductionStart(workOrder.getProductionStart());
            change.setProductionEnd(workOrder.getProductionEnd());
            change.setCreatePeople(u.getUserName());
            change.setProductNumber(workOrder.getProductNumber());
            change.setCreateTime(new Date());
            change.setcStatus(1);
            change.setRemark("该工单合并为"+order.getWorkorderNumber()+"工单");
            workOrderChangeMapper.insertWorkOrderChange(change);
        }
        line =productionLineMapper.selectProductionLineById(order.getLineId());
        order.setCompanyId(u.getCompanyId());
        order.setCreateTime(new Date());
        order.setCreateBy(u.getUserName());
        order.setDeviceLiable(line.getDeviceLiable());
        //查询产线
        devWorkOrderMapper.insertDevWorkOrder(order);
        //添加工单变更记录
        change = new WorkOrderChange();
        change.setCompanyId(u.getCompanyId());
        change.setOrderId(order.getId());
        change.setOrderCode(order.getOrderCode());
        change.setLineId(order.getLineId());
        change.setLineName(line.getLineName());
        change.setDeviceLiable(userMapper.selectUserInfoById(line.getDeviceLiable()).getUserName());
        change.setDeviceLiable2(userMapper.selectUserInfoById(line.getDeviceLiableTow()).getUserName());
        change.setProductionStart(order.getProductionStart());
        change.setProductionEnd(order.getProductionEnd());
        change.setCreatePeople(u.getUserName());
        change.setProductNumber(order.getProductNumber());
        change.setCreateTime(new Date());
        change.setcStatus(1);
        change.setRemark("该工单由"+hCode.toString()+"工单合并而成;");
        workOrderChangeMapper.insertWorkOrderChange(change);

       DevProductList devProductList = productListMapper.selectDevProductByCode(u.getCompanyId(),order.getProductCode());
        if(order.getEcnStatus() == 1 ){//添加ecn备注信息
            // 新增工单变更记录
            EcnLog ecnLog = new EcnLog();
            ecnLog.setCompanyId(u.getCompanyId());
            ecnLog.setSaveId(order.getId());
            ecnLog.setSaveCode(order.getWorkorderNumber());
            ecnLog.setEcnType(2);
            ecnLog.setEcnText(order.getEcnText());
            ecnLog.setCreatePeople(order.getCreateBy());
            ecnLog.setCreateId(u.getUserId().intValue());
            ecnLog.setCreateTime(new Date());
            ecnLogMapper.insertEcnLog(ecnLog);
            // 更新产品ECN备注信息
            if (!devProductList.getEcnText().equals(order.getEcnText())) { // 新建工单时更改了产品ECN
                // 更新ECN日志
                EcnLog proEcnLog = new EcnLog();
                proEcnLog.setCompanyId(u.getCompanyId());
                proEcnLog.setSaveId(devProductList.getId());
                proEcnLog.setSaveCode(devProductList.getProductCode());
                proEcnLog.setEcnType(1);
                proEcnLog.setEcnText(order.getEcnText());
                proEcnLog.setCreatePeople(order.getCreateBy());
                proEcnLog.setCreateId(u.getUserId().intValue());
                proEcnLog.setCreateTime(new Date());
                ecnLogMapper.insertEcnLog(proEcnLog);
                // 更新产品ECN
                devProductList.setEcnText(order.getEcnText());
                productListMapper.updateDevProductList(devProductList);
            }
        }
        return 1;
    }

    /**
     * 拆分工单
     * @param orders 拆单详情
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int workDismantleInfo(List<DevWorkOrder> orders) throws Exception {
        User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
        //获取原始工单
        if(orders == null)return 0;
        DevWorkOrder workOrder = devWorkOrderMapper.selectDevWorkOrderById(orders.get(0).getSign());
        ProductionLine line =null;
        DevProductList devProductList =null;
        WorkOrderChange change =null;
        for (DevWorkOrder order : orders) {
            order.setWorkorderNumber(CodeUtils.getWorkOrderCode(u.getCompanyId()));
            try {
                Thread.sleep(5000);
            }catch (Exception e){ }
            order.setCompanyId(u.getCompanyId());
            order.setSign(1);
            line = productionLineMapper.selectProductionLineById(order.getLineId());
            order.setDeviceLiable(line.getDeviceLiable());
            order.setCreateTime(new Date());
            order.setCreateBy(u.getUserName());
            devWorkOrderMapper.insertDevWorkOrder(order);
            if(order.getEcnStatus() == 1 ){//添加ecn备注信息
                // 新增工单变更记录
                EcnLog ecnLog = new EcnLog();
                ecnLog.setCompanyId(u.getCompanyId());
                ecnLog.setSaveId(order.getId());
                ecnLog.setSaveCode(order.getWorkorderNumber());
                ecnLog.setEcnType(2);
                ecnLog.setEcnText(order.getEcnText());
                ecnLog.setCreatePeople(order.getCreateBy());
                ecnLog.setCreateId(u.getUserId().intValue());
                ecnLog.setCreateTime(new Date());
                ecnLogMapper.insertEcnLog(ecnLog);
                // 更新产品ECN备注信息
                if (!devProductList.getEcnText().equals(order.getEcnText())) { // 新建工单时更改了产品ECN
                    // 更新ECN日志
                    EcnLog proEcnLog = new EcnLog();
                    proEcnLog.setCompanyId(u.getCompanyId());
                    proEcnLog.setSaveId(devProductList.getId());
                    proEcnLog.setSaveCode(devProductList.getProductCode());
                    proEcnLog.setEcnType(1);
                    proEcnLog.setEcnText(order.getEcnText());
                    proEcnLog.setCreatePeople(order.getCreateBy());
                    proEcnLog.setCreateId(u.getUserId().intValue());
                    proEcnLog.setCreateTime(new Date());
                    ecnLogMapper.insertEcnLog(proEcnLog);
                    // 更新产品ECN
                    devProductList.setEcnText(order.getEcnText());
                    productListMapper.updateDevProductList(devProductList);
                }
            }
        }
        return 0;
    }

    /**
     * 查询所有下到车间的工单信息
     * @return 结果
     */
    @Override
    public List<DevWorkOrder> selectWorkListInSw(Integer wlSign) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return Collections.emptyList();
        }
        return devWorkOrderMapper.selectWorkListInSw(user.getCompanyId(),wlSign);
    }
}
