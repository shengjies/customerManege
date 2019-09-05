package com.ruoyi.project.production.workstation.service;

import com.ruoyi.common.constant.DevConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 工位配置 服务层实现
 *
 * @author sj
 * @date 2019-06-13
 */
@Service
public class WorkstationServiceImpl implements IWorkstationService {
    @Autowired
    private WorkstationMapper workstationMapper;

    @Autowired
    private DevListMapper devListMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    /**
     * 查询工位配置信息
     *
     * @param id 工位配置ID
     * @return 工位配置信息
     */
    @Override
    public Workstation selectWorkstationById(Integer id) {
        return workstationMapper.selectWorkstationById(id);
    }

    /**
     * 查询工位配置列表
     *
     * @param workstation 工位配置信息
     * @return 工位配置集合
     */
    @Override
    public List<Workstation> selectWorkstationList(Workstation workstation) {
        return workstationMapper.selectWorkstationList(workstation);
    }

    /**
     * 查询工位配置列表
     *
     * @param workstation 工位配置信息
     * @return 工位配置集合
     */
    @Override
    public List<Workstation> selectWorkstationList2(Workstation workstation) {
        return workstationMapper.selectWorkstationList2(workstation);
    }

    /**
     * 新增工位配置
     *
     * @param workstation 工位配置信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertWorkstation(Workstation workstation) throws Exception {
        //查询计数器硬件
        if (workstation.getDevId() != null && workstation.getDevId() > 0) {
            DevList devList = devListMapper.selectDevListById(workstation.getDevId());
            if (devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0) {
                workstation.setDevCode(devList.getDeviceId());
                devList.setSign(1);
                devList.setDevType(DevConstants.DEV_TYPE_LINE);
                devListMapper.updateDevSign(devList);
            } else {
                throw new Exception("计数器硬件编码配置错误");
            }
        }
        //查询看板器硬件
        if (workstation.getcId() != null && workstation.getcId() > 0) {
            DevList devList = devListMapper.selectDevListById(workstation.getcId());
            if (devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0) {
                workstation.setcCode(devList.getDeviceId());
                devList.setSign(1);
                devList.setDevType(DevConstants.DEV_TYPE_LINE);
                devListMapper.updateDevSign(devList);
            } else {
                throw new Exception("看板硬件编码配置错误");
            }
        }
        //查询MES
        if (workstation.geteId() != null && workstation.geteId() > 0) {
            DevList devList = devListMapper.selectDevListById(workstation.geteId());
            if (devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0) {
                workstation.seteCode(devList.getDeviceId());
                devList.setSign(1);
                devList.setDevType(DevConstants.DEV_TYPE_LINE);
                devListMapper.updateDevSign(devList);
            } else {
                throw new Exception("MES硬件编码配置错误");
            }
        }
        //查询对应产线是否存在工位
        Workstation work = workstationMapper.selectWorkstationByLineId(workstation.getLineId(), workstation.getCompanyId());
        if (work == null) {
            workstation.setSign(1);
        } else if (work != null && workstation.getSign() == 1) {
            //修改其他工位的数据标识
            workstationMapper.editWorkstationSign(workstation.getLineId(), workstation.getCompanyId(), 0);
        }
        workstation.setcTime(new Date());
        int row = workstationMapper.insertWorkstation(workstation);
        //更新流水线手动或者自动
        updateLineManual(workstation);
        return row;
    }

    /**
     * 修改工位配置
     *
     * @param workstation 工位配置信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWorkstation(Workstation workstation) throws Exception {
        Workstation work = workstationMapper.selectWorkstationById(workstation.getId());
        if (work == null) {
            throw new Exception("工位被删除...");
        }
        //操作硬件
        DevList devList = null;
        //计数器
        if (work.getDevId() != workstation.getDevId()) {
            workstation.setDevCode(null);
            if (work.getDevId() > 0) {
                //将原来的硬件修改为未配置
                devList = devListMapper.selectDevListById(work.getDevId());
                if (devList != null) {
                    devList.setSign(0);
                    devList.setDevType(null);
                    devListMapper.updateDevSign(devList);
                }
            }
            if (workstation.getDevId() > 0) {
                //将现在的硬件修改为已配置
                devList = devListMapper.selectDevListById(workstation.getDevId());
                if (devList != null) {
                    devList.setSign(1);
                    devList.setDevType(DevConstants.DEV_TYPE_LINE);
                    devListMapper.updateDevSign(devList);
                    workstation.setDevCode(devList.getDeviceId());
                }
            }
        } else {
            workstation.setDevCode(work.getDevCode());
        }
        //看板
        if (work.getcId() != workstation.getcId()) {
            workstation.seteCode(null);
            if (work.getcId() > 0) {
                devList = devListMapper.selectDevListById(work.getcId());
                if (devList != null) {
                    devList.setSign(0);
                    devList.setDevType(null);
                    devListMapper.updateDevSign(devList);
                }
            }
            if (workstation.getcId() > 0) {
                devList = devListMapper.selectDevListById(workstation.getcId());
                if (devList != null) {
                    devList.setSign(1);
                    devList.setDevType(DevConstants.DEV_TYPE_LINE);
                    devListMapper.updateDevSign(devList);
                    workstation.setcCode(devList.getDeviceId());
                }
            }
        } else {
            workstation.setcCode(work.getcCode());
        }
        //MES
        if (work.geteId() != workstation.geteId()) {
            workstation.seteCode(null);
            if (work.geteId() > 0) {
                devList = devListMapper.selectDevListById(work.geteId());
                if (devList != null) {
                    devList.setSign(0);
                    devList.setDevType(null);
                    devListMapper.updateDevSign(devList);
                }
            }
            if (workstation.geteId() != null &&  workstation.geteId() > 0) {
                devList = devListMapper.selectDevListById(workstation.geteId());
                if (devList != null) {
                    devList.setSign(1);
                    devList.setDevType(DevConstants.DEV_TYPE_LINE);
                    devListMapper.updateDevSign(devList);
                    workstation.seteCode(devList.getDeviceId());
                }
            }
        } else {
            workstation.seteCode(work.geteCode());
        }
        //判断数据标识
        if (workstation.getSign() == 1) {
            //将其他工位全部修改为不是唯一标识
            workstationMapper.editWorkstationSign(work.getLineId(), work.getCompanyId(), 0);
        } else if (work.getSign() == 1 && workstation.getSign() == 0) {
            //将第一个工位修改为数据唯一标识
            workstationMapper.editFirstWorkstionSign(work.getLineId(), work.getCompanyId());
        }
        int row = workstationMapper.updateWorkstation(workstation);
        //更新流水线手动或者自动
        updateLineManual(work);
        return row;
    }

    /**
     * 删除工位配置对象
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteWorkstationById(Integer id) {
        User user = JwtUtil.getUser();
        Workstation work = workstationMapper.selectWorkstationById(id);
        int row = workstationMapper.deleteWorkstationById(id);
        if (work.getSign() == 1) {
            workstationMapper.editFirstWorkstionSign(work.getLineId(), work.getCompanyId());
        }
        /**
         * 更新生产线手动或者自动
         */
        updateLineManual(work);
        /**
         * 删除工位还原已该工位已配置的所有硬件信息
         */
        if (work.getDevId() > 0) {
            devListMapper.updateDevSignAndType(user.getCompanyId(), work.getDevId(), DevConstants.DEV_SIGN_NOT_USE, null);
        }
        if (work.getcId() > 0) {
            devListMapper.updateDevSignAndType(user.getCompanyId(), work.getcId(), DevConstants.DEV_SIGN_NOT_USE, null);
        }
        if (work.geteId() > 0) {
            devListMapper.updateDevSignAndType(user.getCompanyId(), work.geteId(), DevConstants.DEV_SIGN_NOT_USE, null);
        }
        return row;
    }

    /**
     * 根据产线查询所以工位信息
     *
     * @param lineId 产线id
     * @return
     */
    @Override
    public List<Workstation> selectAllByLineId(Integer lineId) {
        return workstationMapper.selectAllByLineId(lineId);
    }

    /**
     * 判断流水线是手动还是自动
     *
     * @param workstation 工位信息
     */
    private void updateLineManual(Workstation workstation) {
        Workstation w = workstationMapper.selectWorkstationSignByLineId(workstation.getLineId(), workstation.getCompanyId());
        if (w != null && w.getDevId() != null && w.getDevId() > 0) {
            //将产线修改为自动
            ProductionLine line = productionLineMapper.selectProductionLineById(workstation.getLineId());
            if (line != null) {
                line.setManual(0);
                productionLineMapper.updateProductionLine(line);
            }
        } else {
            ProductionLine line = productionLineMapper.selectProductionLineById(workstation.getLineId());
            if (line != null) {
                line.setManual(1);
                productionLineMapper.updateProductionLine(line);
            }
        }
    }

    /**
     * app端配置工位硬件
     *
     * @param workstation 工位信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int appUpdateWorkstation(Workstation workstation) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return 0;
        }
        if (workstation.getId() == null) {
            return 0;
        }
        Workstation work = workstationMapper.selectWorkstationById(workstation.getId());
        if (work == null) {
            throw new BusinessException("工位被删除");
        }
        //操作硬件
        DevList devList = null;
        // 计数器硬件
        if (StringUtils.isNotEmpty(workstation.getDevCode())) {
            devList = devListMapper.selectDevListByCode(workstation.getDevCode());
            if (devList == null || devList.getSign().equals(DevConstants.DEV_SIGN_USED)) {
                throw new BusinessException("硬件不存在或者被使用");
            }
            if (work.getDevId() > 0) {
                if (!workstation.getDevCode().equals(work.getDevCode())) {
                    // 还原之前硬件为未使用
                    devListMapper.updateDevSignAndType(user.getCompanyId(), work.getDevId(), DevConstants.DEV_SIGN_NOT_USE, null);
                    // 更新上传硬件被使用
                    devList.setSign(DevConstants.DEV_SIGN_USED);
                    devList.setDevType(DevConstants.DEV_TYPE_LINE);
                    devListMapper.updateDevSign(devList);
                    workstation.setDevId(devList.getId());
                }
            } else {
                // 更新上传硬件被使用
                devList.setSign(DevConstants.DEV_SIGN_USED);
                devList.setDevType(DevConstants.DEV_TYPE_LINE);
                devListMapper.updateDevSign(devList);
                workstation.setDevId(devList.getId());
            }
        } else {
            if (work.getDevId() > 0) {
                // 还原之前硬件为未使用
                devListMapper.updateDevSignAndType(user.getCompanyId(), work.getDevId(), DevConstants.DEV_SIGN_NOT_USE, null);
                workstation.setDevId(0);
                workstation.setDevCode(null);
            }
        }

        // 看板硬件
        if (StringUtils.isNotEmpty(workstation.getcCode())) {
            devList = devListMapper.selectDevListByCode(workstation.getcCode());
            if (devList == null || devList.getSign().equals(DevConstants.DEV_SIGN_USED)) {
                throw new BusinessException("硬件不存在或者被使用");
            }
            if (work.getcId() > 0) {
                if (!workstation.getcCode().equals(work.getcCode())) {
                    // 还原之前硬件为未使用
                    devListMapper.updateDevSignAndType(user.getCompanyId(), work.getcId(), DevConstants.DEV_SIGN_NOT_USE, null);
                    // 更新最新硬件为使用
                    devList.setSign(DevConstants.DEV_SIGN_USED);
                    devList.setDevType(DevConstants.DEV_TYPE_LINE);
                    devListMapper.updateDevSign(devList);
                    workstation.setcId(devList.getId());
                }
            } else {
                // 更新最新硬件为使用
                devList.setSign(DevConstants.DEV_SIGN_USED);
                devList.setDevType(DevConstants.DEV_TYPE_LINE);
                devListMapper.updateDevSign(devList);
                workstation.setcId(devList.getId());
            }
        } else {
            if (work.getcId() > 0) {
                // 还原之前硬件为未使用
                devListMapper.updateDevSignAndType(user.getCompanyId(), work.getcId(), DevConstants.DEV_SIGN_NOT_USE, null);
                workstation.setcId(0);
                workstation.setcCode(null);
            }
        }
        int row = workstationMapper.updateWorkstation(workstation);
        //更新流水线手动或者自动
        updateLineManual(work);
        return row;
    }
}
