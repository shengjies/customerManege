package com.ruoyi.project.insmanage.instrumentType.service;

import com.ruoyi.common.constant.InstrumentConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import com.ruoyi.project.insmanage.instrumentManage.mapper.InstrumentManageMapper;
import com.ruoyi.project.insmanage.instrumentType.domain.InstrumentType;
import com.ruoyi.project.insmanage.instrumentType.mapper.InstrumentTypeMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 设备类型 服务层实现
 *
 * @author sj
 * @date 2019-07-02
 */
@Service("ins")
public class InstrumentTypeServiceImpl implements IInstrumentTypeService {
    @Autowired
    private InstrumentTypeMapper instrumentTypeMapper;

    @Autowired
    private InstrumentManageMapper instrumentManageMapper;

    /**
     * 查询设备类型信息
     *
     * @param id 设备类型ID
     * @return 设备类型信息
     */
    @Override
    public InstrumentType selectInstrumentTypeById(Integer id) {
        return instrumentTypeMapper.selectInstrumentTypeById(id);
    }

    /**
     * 查询设备类型列表
     *
     * @param instrumentType 设备类型信息
     * @return 设备类型集合
     */
    @Override
    public List<InstrumentType> selectInstrumentTypeList(InstrumentType instrumentType) {
        return instrumentTypeMapper.selectInstrumentTypeList(instrumentType);
    }

    /**
     * 新增设备类型
     *
     * @param instrumentType 设备类型信息
     * @return 结果
     */
    @Override
    public int insertInstrumentType(InstrumentType instrumentType) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return 0;
        }
        instrumentType.setCompanyId(user.getCompanyId());
        instrumentType.setCreateId(user.getUserId().intValue());
        instrumentType.setCreateTime(new Date());
        return instrumentTypeMapper.insertInstrumentType(instrumentType);
    }

    /**
     * 修改设备类型
     *
     * @param instrumentType 设备类型信息
     * @return 结果
     */
    @Override
    public int updateInstrumentType(InstrumentType instrumentType) {
        return instrumentTypeMapper.updateInstrumentType(instrumentType);
    }

    /**
     * 删除设备类型对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteInstrumentTypeByIds(String ids) {
        Integer[] insTypeIds = Convert.toIntArray(ids);
        List<InstrumentManage> instrumentManages = null;
        for (Integer insTypeId : insTypeIds) {
            instrumentManages = instrumentManageMapper.selectInstrumentManageByInsTypeId(insTypeId);
            if (StringUtils.isNotEmpty(instrumentManages)) {
                throw new BusinessException("该类型存在仪器设备,请先删除关联设备");
            }
        }
        return instrumentTypeMapper.deleteInstrumentTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询公司所有的设备类型
     *
     * @return 结果
     */
    @Override
    public List<InstrumentType> selectAllInsType() {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return Collections.emptyList();
        }
        InstrumentType instrumentType = new InstrumentType();
        instrumentType.setCompanyId(user.getCompanyId());
        return instrumentTypeMapper.selectInstrumentTypeList(instrumentType);
    }

    /**
     * 校验设备类型名称的唯一性
     *
     * @param instrumentType 设备类型名称
     * @return 结果
     */
    @Override
    public String checkInsTypeUnique(InstrumentType instrumentType) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        Integer id = instrumentType.getId();
        if (StringUtils.isNotNull(id)) {
            InstrumentType typeById = instrumentTypeMapper.selectInstrumentTypeById(id);
            if (typeById.getInType().equals(instrumentType.getInType())) {
                return InstrumentConstants.INS_TYPE_UNIQUE;
            }
        }
        InstrumentType insUnique = instrumentTypeMapper.selectInstrumentTypeByName(instrumentType.getInType(),user.getCompanyId());
        if (StringUtils.isNull(insUnique)) {
            return InstrumentConstants.INS_TYPE_UNIQUE;
        }
        return InstrumentConstants.INS_TYPE_NOT_UNIQUE;
    }
}
