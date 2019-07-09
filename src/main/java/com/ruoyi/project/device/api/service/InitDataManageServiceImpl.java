package com.ruoyi.project.device.api.service;

import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.api.form.ApiWorkForm;
import com.ruoyi.project.device.api.form.WorkDataForm;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devDeviceCounts.domain.DevDataLog;
import com.ruoyi.project.device.devDeviceCounts.mapper.DevDataLogMapper;
import com.ruoyi.project.device.devDeviceCounts.service.IDevDeviceCountsService;
import com.ruoyi.project.device.devIo.domain.DevIo;
import com.ruoyi.project.device.devIo.mapper.DevIoMapper;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.production.devWorkData.domain.DevWorkData;
import com.ruoyi.project.production.devWorkData.mapper.DevWorkDataMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkMapper;
import com.ruoyi.project.production.workExceptionList.domain.WorkExceptionList;
import com.ruoyi.project.production.workExceptionList.mapper.WorkExceptionListMapper;
import com.ruoyi.project.production.workExceptionType.domain.WorkExceptionType;
import com.ruoyi.project.production.workExceptionType.mapper.WorkExceptionTypeMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class InitDataManageServiceImpl implements IInitDataManageService {

    @Autowired
    private DevListMapper devListMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private DevCompanyMapper devCompanyMapper;

    @Autowired
    private DevWorkOrderMapper workOrderMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;
    @Autowired
    private DevWorkDataMapper devWorkDataMapper;

    @Autowired
    private WorkExceptionTypeMapper exceptionTypeMapper;

    @Autowired
    private WorkExceptionListMapper workExceptionListMapper;

    @Autowired
    private DevDataLogMapper devDataLogMapper;

    @Autowired
    private WorkstationMapper workstationMapper;

    @Autowired
    private SingleWorkMapper singleWorkMapper;

    /**
     * 根据硬件编码查询对应的工单信息
     * @param code 硬件编码
     * @return
     */
    @Override
    public Map<String, Object> workOrder(String code) {
        Map<String,Object> map = new HashMap<>();
        try {
            //判断对应的硬件编码是否存在
            DevList devList = devListMapper.selectDevListByCode(code);
            if(devList == null || devList.getCompanyId() ==null){
                map.put("code",3);//硬件编码不存在
                map.put("data",null);
                return  map;
            }
            //查询对应硬件产线配置工位
            Workstation  workstation = workstationMapper.selectInfoByDevice(devList.getId(),0,0);
            SingleWork singleWork = null;
            ApiWorkForm workForm = null;
            if(workstation != null || workstation.getLineId() != null){
                workForm = findLineAndWork(devList,workstation,null);
            }else if(workstation == null){
                singleWork =  singleWorkMapper.selectSingleWorkByCode(devList.getId(),0,0);
                if(singleWork == null){
                    map.put("code",4);//硬件未配置产线
                    map.put("data",null);
                    return  map;
                }
                workForm = findLineAndWork(devList,null,singleWork);
            }
            if(workForm == null){
                map.put("code",2);//硬件为归属公司或者硬件未配置产线
                map.put("data",null);
                return map;
            }
            map.put("code",1);
            map.put("data",workForm);
        }catch (Exception e){
            e.printStackTrace();
               map.put("code",0);
               map.put("data",null);
        }
        return map;
    }


    @Override
    public Map<String, Object> workData(WorkDataForm data) {
        Map<String,Object> map = new HashMap<>();
        try {
            //判断对应的硬件编码是否存在
            DevList devList = devListMapper.selectDevListByCode(data.getCode());
            if(devList == null || devList.getCompanyId() ==null){
                map.put("code",3);//硬件编码不存在
                map.put("status",0);//没有正在进行的工单
                map.put("num",0);//没有正在进行的工单
                map.put("workCode",null);//工单编号为空
                return  map;
            }
            //查询对应硬件产线配置信息
            Workstation  workstation = workstationMapper.selectInfoByDevice(devList.getId(),0,0);
            if(workstation == null || workstation.getLineId() == null){
                map.put("code",4);//硬件未配置产线
                map.put("status",0);//没有正在进行的工单
                map.put("num",0);//没有正在进行的工单
                map.put("workCode",null);//工单编号为空
                return  map;
            }
            //查询对应的产线是否存在
            ProductionLine line = productionLineMapper.selectProductionLineById(workstation.getLineId());
            if(line != null){
                DevDataLog devDataLog = new DevDataLog();
                devDataLog.setCompanyId(devList.getCompanyId());
                devDataLog.setDataTotal(data.getD1());
                devDataLog.setDevId(devList.getId());
                devDataLog.setDelData(0);
                devDataLog.setLineId(workstation.getLineId());
                //查询正在进行工单
                DevWorkOrder workOrder = devWorkOrderMapper.selectWorkByCompandAndLine(devList.getCompanyId(),workstation.getLineId());
                if(workOrder != null && workOrder.getOperationStatus() == WorkConstants.OPERATION_STATUS_STARTING){
                    devDataLog.setWorkId(workOrder.getId());
                    devDataLog.setIoId(workstation.getId());//设置工位id
                    //查询对应日志上传数据数据
                    DevDataLog log = devDataLogMapper.selectLineWorkDevIo(workstation.getLineId(),workOrder.getId(),devList.getId(),workstation.getId());
                    if(log != null){
                        devDataLog.setDelData(devDataLog.getDataTotal() - log.getDataTotal());
                    }else{
                        devDataLog.setDelData(data.getD1());
                    }
                    //对相关数据进行记录
                    DevWorkData workData = devWorkDataMapper.selectWorkDataByCompanyLineWorkDev(devList.getCompanyId(),workstation.getLineId(),workOrder.getId(),
                            devList.getId(),workstation.getId());
                    if(workData != null){
                        workData.setIoSign(workstation.getSign());//标记数据 为报表数据
                        //记录累计产量
                        devWorkDataMapper.saveTotalWorkData(workData.getDataId(),data.getD1(),workData.getIoSign());
                        workOrder.setCumulativeNumber(data.getD1());
                        devWorkOrderMapper.updateDevWorkOrder(workOrder);
                    }else{
                        workData = new DevWorkData();
                        workData.setIoSign(workstation.getSign());
                        workData.setCompanyId(workstation.getCompanyId());
                        workData.setLineId(workstation.getLineId());
                        workData.setWorkId(workOrder.getId());
                        workData.setDevId(devList.getId());
                        workData.setIoId(workstation.getId());
                        workData.setCumulativeNum(data.getD1());
                        workData.setCreateTime(new Date());
                        devWorkDataMapper.insertDevWorkData(workData);
                    }
                }else if(workOrder == null){
                    //查询最近完成工单信息
                    workOrder = devWorkOrderMapper.selectLatelyCompleteWork(devList.getCompanyId(),workstation.getLineId());
                    if(workOrder != null && workOrder.getSign() == 1){
                        devDataLog.setWorkId(workOrder.getId());
                        devDataLog.setIoId(workstation.getId());//设置工位id
                        DevWorkData workData = devWorkDataMapper.selectWorkDataByCompanyLineWorkDev(devList.getCompanyId(),workstation.getLineId(),workOrder.getId(),
                                devList.getId(),workstation.getId());
                        if(workData != null){
                            workData.setIoSign(workstation.getSign());
                            //记录累计产量
                            devWorkDataMapper.saveTotalWorkData(workData.getDataId(),data.getD1(),workData.getIoSign());
                            workOrder.setCumulativeNumber(data.getD1());
                            devWorkOrderMapper.updateDevWorkOrder(workOrder);
                        }
                        //查询对应日志上传数据数据
                        DevDataLog log = devDataLogMapper.selectLineWorkDevIo(workstation.getLineId(),workOrder.getId(),devList.getId(),workstation.getId());
                        if(log != null){
                            devDataLog.setDelData(devDataLog.getDataTotal() - log.getDataTotal());
                        }
                    }
                }
                devDataLog.setCreateDate(new Date());
                devDataLog.setCreateTime(new Date());
                devDataLogMapper.insertDevDataLog(devDataLog);
            }

            map.put("code",1);//成功
            map.put("status",0);//没有正在进行的工单
            map.put("num",0);//没有正在进行的工单
            map.put("workCode",null);//工单编号为空
            DevWorkOrder workOrder = devWorkOrderMapper.selectWorkByCompandAndLine(devList.getCompanyId(),workstation.getLineId());
            if(workOrder != null){
                //查询对应工单的累计产量
                DevWorkData workData = devWorkDataMapper.selectWorkDataByCompanyLineWorkDev(workstation.getCompanyId(),workstation.getLineId(),
                        workOrder.getId(),devList.getId(),workstation.getId());
                if(workData != null && workData.getCumulativeNum() != null){
                    map.put("num",workData.getCumulativeNum());//没有正在进行的工单
                    map.put("status",workOrder.getOperationStatus());//工单正在进行
                    map.put("workCode",workOrder.getWorkorderNumber());//工单编号为空
                }
            }
            return map;
        }catch (Exception e){
            map.put("code",0);//异常错误
            map.put("status",0);//没有正在进行的工单
            map.put("num",0);//没有正在进行的工单
            map.put("workCode",null);//工单编号为空
        }
        return map;
    }
    /**
     * 异常上报
     * @param code 硬件编码
     * @return
     */
    @Override
    public Map<String, Object> workEx(String code) {
        Map<String,Object> map = new HashMap<>();
        try {
            //判断对应的硬件编码是否存在
            DevList devList = devListMapper.selectDevListByCode(code);
            if(devList == null || devList.getCompanyId() ==null){
                map.put("code",3);//硬件编码不存在
                map.put("status",0);//上报失败
                return  map;
            }
            //查询对应硬件是否配置产线
            Workstation workstation = workstationMapper.selectInfoByDevice(devList.getId(),0,0);
            if(workstation == null || workstation.getLineId() == null){
                map.put("code",4);//硬件未配置产线
                map.put("status",0);//上报失败
                return  map;
            }
            ApiWorkForm workForm = findLineAndWork(devList,workstation,null);
            if(workForm == null || workForm.getWorkId() == null){
                map.put("code",2);//不存在对应工单信息
                map.put("status",0);//上报失败
                return  map;
            }
            //查询对应异常类型是否存在
            WorkExceptionType exceptionType = exceptionTypeMapper.selectByCompanyAndTypeName(workForm.getCompanyId(),"异常上报");
            if(exceptionType == null){
                exceptionType = new WorkExceptionType();
                exceptionType.setCompanyId(workForm.getCompanyId());
                exceptionType.setTypeName("异常上报");
                exceptionType.setDefId(0);
                exceptionType.setCreateTime(new Date());
                exceptionTypeMapper.insertWorkExceptionType(exceptionType);
            }
            //添加异常信息
            WorkExceptionList exceptionList = new WorkExceptionList();
            exceptionList.setCompanyId(workForm.getCompanyId());
            exceptionList.setExceStatut(0);
            exceptionList.setExceType(exceptionType.getId());
            exceptionList.setWorkId(workForm.getWorkId());
            exceptionList.setRemark("工位:"+workstation.getwName()+",设备ID："+code+",上报异常");
            exceptionList.setCreateTime(new Date());
            workExceptionListMapper.insertWorkExceptionList(exceptionList);
            map.put("code",1);//创建成功
            map.put("status",1);//上报成功
        }catch (Exception e){
            map.put("code",0);//异常错误
            map.put("status",0);//上报失败
        }
        return map;
    }

    /**
     * 根据硬件查询对应的产线信息和正在进行的工单信息
     * @param workstation
     * @return
     */
    private ApiWorkForm findLineAndWork(DevList devList,Workstation workstation,SingleWork singleWork) throws Exception{
        int companyId = 0;
        if(workstation != null){
            companyId = workstation.getCompanyId();
        }else if(singleWork != null){
            companyId = singleWork.getCompanyId();
        }
        ApiWorkForm workForm = new ApiWorkForm();
        //查询公司信息
        DevCompany devCompany = devCompanyMapper.selectDevCompanyById(companyId);
        if(devCompany ==null)return null;
        workForm.setCompanyId(devCompany.getCompanyId());
        workForm.setCompanyLogo(devCompany.getComLogo());
        workForm.setCompanyName(devCompany.getComName());
        //查询对应产线正在进行的工单信息
        DevWorkOrder workOrder = null;
        //查询对应的产线信息
        ProductionLine line = null;
        if(workstation != null){
            line = productionLineMapper.selectProductionLineById(workstation.getLineId());
            if(line == null)return  null;
            workForm.setLineName(line.getLineName()+"-"+workstation.getwName());
            workForm.setLineId(line.getId());
            workOrder = workOrderMapper.selectWorkByCompandAndLine(devCompany.getCompanyId(),line.getId());
        }else if(singleWork != null){
            //查询对应的车间信息
            SingleWork work =  singleWorkMapper.selectSingleWorkById(singleWork.getParentId());
            if(work == null)return  null;
            workForm.setLineName(work.getWorkshopName()+"-"+singleWork.getWorkshopName());
            workForm.setLineId(work.getId());
        }else {
            return null;
        }
        //设置工单默认状态
        workForm.setWorkorderStatus(0);
        workForm.setActualNum(0);
        if(workOrder != null){
            workForm.setWorkId(workOrder.getId());
            workForm.setWorkCode(workOrder.getWorkorderNumber());//工单号
            workForm.setProductCode(workOrder.getProductCode());//产品编码
            workForm.setProductName(workOrder.getProductName());//产品名称
            workForm.setWorkNumber(workOrder.getProductNumber());
            workForm.setWorkorderStatus(workOrder.getOperationStatus()); // 生产状态
            workForm.setOp(workOrder.getOperationStatus());
            // 查询对应工单累计生产产量信息
            DevWorkData workData = devWorkDataMapper.selectWorkDataByIosign(devCompany.getCompanyId(),workOrder.getId(),line.getId(),devList.getId());
            if (workData != null) {
                workForm.setActualNum(workData.getCumulativeNum());
            }
        }
        return workForm;
    }

    /**
     * 根据机器设备上扫描上传工单，对工单进行绑定开始操作
     * @param code 设备绑定的硬件编号
     * @param orderCode 工单号
     * @return
     */
    @Override
    public AjaxResult startWorkOrder(String code, String orderCode) {
        //查询对应硬件是否存在
        DevList devList = devListMapper.selectDevListByCode(code);
        if(devList == null){
            return AjaxResult.api(1,"硬件编号不存在",null);
        }
        //查询对应的工单

        return AjaxResult.api(0,"操作成功",null);
    }
}
