package com.ruoyi.project.production.singleWork.service;

import com.ruoyi.common.constant.DevConstants;
import com.ruoyi.common.constant.InstrumentConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.insmanage.instrumentManage.mapper.InstrumentManageMapper;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 单工位数据 服务层实现
 *
 * @author sj
 * @date 2019-07-03
 */
@Service
public class SingleWorkServiceImpl implements ISingleWorkService {
    @Autowired
    private SingleWorkMapper singleWorkMapper;

    @Autowired
    private DevListMapper devListMapper;

    @Autowired
    private InstrumentManageMapper instrumentManageMapper;

    /**
     * 查询单工位数据信息
     *
     * @param id 单工位数据ID
     * @return 单工位数据信息
     */
    @Override
    public SingleWork selectSingleWorkById(Integer id) {
        return singleWorkMapper.selectSingleWorkById(id);
    }

    /**
     * 查询单工位数据列表
     *
     * @param singleWork 单工位数据信息
     * @return 单工位数据集合
     */
    @Override
    public List<SingleWork> selectSingleWorkList(SingleWork singleWork) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return Collections.emptyList();
        }
        singleWork.setCompanyId(user.getCompanyId());
        return singleWorkMapper.selectSingleWorkList(singleWork);
    }

    /**
     * 新增单工位数据
     *
     * @param singleWork 单工位数据信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSingleWork(SingleWork singleWork) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            throw new BusinessException("未登录，请登录再进行操作");
        }
        Integer parentId = singleWork.getParentId();
        // 新增设备
        if (StringUtils.isNotNull(parentId) && parentId != 0) {
            Integer imId = singleWork.getImId();
            if (StringUtils.isNotNull(imId) && imId > 0) {
                // 更新设备已经被配置过
                instrumentManageMapper.updateInstrumentManageImTag(imId, InstrumentConstants.IM_TAG_USED);
            }
        }
        singleWork.setCompanyId(user.getCompanyId());
        singleWork.setcTime(new Date());
        return singleWorkMapper.insertSingleWork(singleWork);
    }

    /**
     * 修改单工位数据
     *
     * @param singleWork 单工位数据信息
     * @return 结果
     */
    @Override
    public int updateSingleWork(SingleWork singleWork) {
        // 原始单工位信息
        SingleWork oldSingleWork = singleWorkMapper.selectSingleWorkById(singleWork.getId());
        Integer parentId = singleWork.getParentId();
        // 单工位设备类型
        if (StringUtils.isNotNull(parentId) && parentId != 0) {
            // 更新硬件使用状态，还原之前硬件状态
            if (!oldSingleWork.getDevId().equals(singleWork.getDevId())) {
                DevList oldDev = devListMapper.selectDevListById(oldSingleWork.getDevId());
                oldDev.setSign(0);
                devListMapper.updateDevSign(oldDev);
                DevList newDev = devListMapper.selectDevListById(singleWork.getDevId());
                newDev.setSign(1);
                devListMapper.updateDevSign(newDev);
            }
            // 更新设备使用状态，还原之前设备状态
            if (!oldSingleWork.getImId().equals(singleWork.getImId())) {
                instrumentManageMapper.updateInstrumentManageImTag(oldSingleWork.getImId(), InstrumentConstants.IM_TAG_NOUSE);
                instrumentManageMapper.updateInstrumentManageImTag(singleWork.getImId(), InstrumentConstants.IM_TAG_USED);
            }
        }
        return singleWorkMapper.updateSingleWork(singleWork);
    }

    /**
     * 删除单工位数据对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSingleWorkByIds(String ids) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        Integer[] swIds = Convert.toIntArray(ids);
        List<SingleWork> singleWorkList = null;
        SingleWork singleWork = null;
        for (Integer swId : swIds) {
            singleWorkList = singleWorkMapper.selectSingleWorkByParentId(user.getCompanyId(), swId);
            if (StringUtils.isNotEmpty(singleWorkList) || singleWorkList.size() > 0) {
                throw new BusinessException("该车间存在单工位，请先删除其关联信息");
            }
            // 更新设备配置标记
            singleWork = singleWorkMapper.selectSingleWorkById(swId);
            if (singleWork.getImId() != null) {
                instrumentManageMapper.updateInstrumentManageImTag(singleWork.getImId(), InstrumentConstants.IM_TAG_NOUSE);
            }
            // 更新计数器硬件配置标记
            if (singleWork.getDevId() != null && singleWork.getDevId() != 0) {
                devListMapper.updateDevBySign(user.getCompanyId(),singleWork.getDevId(),DevConstants.DEV_SIGN_NOT_USE);
            }
            // 更新看板硬件配置标记
            if (singleWork.getWatchId() != null && singleWork.getWatchId() != 0) {
                devListMapper.updateDevBySign(user.getCompanyId(),singleWork.getWatchId(),DevConstants.DEV_SIGN_NOT_USE);
            }
            // 更新扫码枪硬件配置标记
            if (singleWork.geteId() != null && singleWork.geteId() != 0 ) {
                devListMapper.updateDevBySign(user.getCompanyId(),singleWork.geteId(),DevConstants.DEV_SIGN_NOT_USE);
            }
        }
        return singleWorkMapper.deleteSingleWorkByIds(Convert.toStrArray(ids));
    }


    /**
     * 校验车间名称唯一性
     *
     * @param singleWork 单工位信息
     * @return 结果
     */
    @Override
    public String checkNameUnique(SingleWork singleWork) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        Integer id = singleWork.getId();
        if (StringUtils.isNotNull(id)) {
            SingleWork work = singleWorkMapper.selectSingleWorkById(id);
            if (StringUtils.isNotNull(work) && work.getWorkshopName().equals(singleWork.getWorkshopName())) {
                return WorkConstants.WORKHOUSE_NAME_UNIQUE;
            }
        }
        String workshopName = singleWork.getWorkshopName();
        if (StringUtils.isNotEmpty(workshopName)) {
            SingleWork unique = singleWorkMapper.selectSingleWorkByWorkshopName(user.getCompanyId(), workshopName);
            if (StringUtils.isNull(unique)) {
                return WorkConstants.WORKHOUSE_NAME_UNIQUE;
            }
        }
        return WorkConstants.WORKHOUSE_NAME_NOT_UNIQUE;
    }

    /**
     * 单工位硬件配置
     *
     * @param singleWork 单工位信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveConfigDev(SingleWork singleWork) {
        User user = JwtUtil.getUser();
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        SingleWork sw = singleWorkMapper.selectSingleWorkById(singleWork.getId());
        // 计数器硬件
        if (sw.getDevId() == null || sw.getDevId() == 0) {
            // 新增
            if (singleWork.getDevId() != null && singleWork.getDevId() > 0) {
                DevList devList = devListMapper.selectDevListById(singleWork.getDevId());
                if (devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0) {
                    singleWork.setDevCode(devList.getDeviceId());
                    devListMapper.updateDevBySign(user.getCompanyId(), singleWork.getDevId(), DevConstants.DEV_SIGN_USED);
                } else {
                    throw new BusinessException("计数器硬件编码配置错误");
                }
            }
        } else {
            // 修改
            if (singleWork.getDevId() != null && singleWork.getDevId() > 0) {
                if (!singleWork.getDevId().equals(sw.getDevId())) {
                    // 还原之前硬件
                    devListMapper.updateDevBySign(user.getCompanyId(), sw.getDevId(), DevConstants.DEV_SIGN_NOT_USE);
                    // 修改最新硬件
                    DevList devList = devListMapper.selectDevListById(singleWork.getDevId());
                    singleWork.setDevCode(devList.getDeviceId());
                    devListMapper.updateDevBySign(user.getCompanyId(), singleWork.getDevId(), DevConstants.DEV_SIGN_USED);
                }
            } else {
                devListMapper.updateDevBySign(user.getCompanyId(), sw.getDevId(), DevConstants.DEV_SIGN_NOT_USE);
            }
        }

        // 看板硬件
        if (sw.getWatchId() == null || sw.getWatchId() == 0) {
            // 新增
            if (singleWork.getWatchId() != null && singleWork.getWatchId() > 0) {
                DevList devList = devListMapper.selectDevListById(singleWork.getWatchId());
                if (devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0) {
                    singleWork.setWatchCode(devList.getDeviceId());
                    devListMapper.updateDevBySign(user.getCompanyId(),singleWork.getWatchId(),DevConstants.DEV_SIGN_USED);
                } else {
                    throw new BusinessException("看板硬件编码配置错误");
                }
            }
        } else {
            // 修改
            if (singleWork.getWatchId() != null && singleWork.getWatchId() > 0) {
                if (!singleWork.getWatchId().equals(sw.getWatchId())) {
                    // 还原之前硬件
                    devListMapper.updateDevBySign(user.getCompanyId(), sw.getWatchId(), DevConstants.DEV_SIGN_NOT_USE);
                    // 修改最新硬件
                    DevList devList = devListMapper.selectDevListById(singleWork.getWatchId());
                    singleWork.setWatchCode(devList.getDeviceId());
                    devListMapper.updateDevBySign(user.getCompanyId(), singleWork.getWatchId(), DevConstants.DEV_SIGN_USED);
                }
            } else {
                devListMapper.updateDevBySign(user.getCompanyId(), sw.getWatchId(), DevConstants.DEV_SIGN_NOT_USE);
            }
        }

        // 扫码枪硬件
        if (sw.geteId() == null || sw.geteId() == 0) {
            // 新增
            if (singleWork.geteId() != null && singleWork.geteId() > 0) {
                DevList devList = devListMapper.selectDevListById(singleWork.geteId());
                if (devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0) {
                    singleWork.seteCode(devList.getDeviceId());
                    devListMapper.updateDevBySign(user.getCompanyId(), singleWork.geteId(), DevConstants.DEV_SIGN_USED);
                } else {
                    throw new BusinessException("扫码枪硬件编码配置错误");
                }
            }
        } else {
            // 修改
            if (singleWork.geteId() != null && singleWork.geteId() > 0) {
                if (!singleWork.geteId().equals(sw.geteId())) {
                    // 还原之前硬件
                    devListMapper.updateDevBySign(user.getCompanyId(), sw.geteId(), DevConstants.DEV_SIGN_NOT_USE);
                    // 修改最新硬件
                    DevList devList = devListMapper.selectDevListById(singleWork.geteId());
                    singleWork.seteCode(devList.getDeviceId());
                    devListMapper.updateDevBySign(user.getCompanyId(), singleWork.geteId(), DevConstants.DEV_SIGN_USED);
                }
            } else {
                devListMapper.updateDevBySign(user.getCompanyId(), sw.geteId(), DevConstants.DEV_SIGN_NOT_USE);
            }
        }
        return singleWorkMapper.updateSingleWork(singleWork);
    }
}
