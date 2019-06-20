package com.ruoyi.project.insmanage.instrumentManage.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.insmanage.instrumentManage.mapper.InstrumentManageMapper;
import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import com.ruoyi.project.insmanage.instrumentManage.service.IInstrumentManageService;
import com.ruoyi.common.support.Convert;

import javax.servlet.http.HttpServletRequest;

/**
 * 仪器设备管理 服务层实现
 * 
 * @author sj
 * @date 2019-06-19
 */
@Service
public class InstrumentManageServiceImpl implements IInstrumentManageService 
{
	@Autowired
	private InstrumentManageMapper instrumentManageMapper;

	/**
     * 查询仪器设备管理信息
     * 
     * @param id 仪器设备管理ID
     * @return 仪器设备管理信息
     */
    @Override
	public InstrumentManage selectInstrumentManageById(Integer id)
	{
	    return instrumentManageMapper.selectInstrumentManageById(id);
	}
	
	/**
     * 查询仪器设备管理列表
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 仪器设备管理集合
     */
	@Override
	public List<InstrumentManage> selectInstrumentManageList(InstrumentManage instrumentManage, HttpServletRequest request)
	{
		User user = JwtUtil.getTokenUser(request);
		if (user == null ) {
			return Collections.emptyList();
		}
		instrumentManage.setCompanyId(user.getCompanyId());
		return instrumentManageMapper.selectInstrumentManageList(instrumentManage);
	}
	
    /**
     * 新增仪器设备管理
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 结果
     */
	@Override
	public int insertInstrumentManage(InstrumentManage instrumentManage,User user)
	{
		instrumentManage.setCompanyId(user.getCompanyId());
		instrumentManage.setCreateId(user.getUserId().intValue());
		instrumentManage.setCreateTime(new Date());
	    return instrumentManageMapper.insertInstrumentManage(instrumentManage);
	}
	
	/**
     * 修改仪器设备管理
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 结果
     */
	@Override
	public int updateInstrumentManage(InstrumentManage instrumentManage)
	{
	    return instrumentManageMapper.updateInstrumentManage(instrumentManage);
	}

	/**
     * 删除仪器设备管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteInstrumentManageByIds(String ids)
	{
		return instrumentManageMapper.deleteInstrumentManageByIds(Convert.toStrArray(ids));
	}


	/**
	 * 修改设备状态
	 * @param instrumentManage 设备信息
	 * @param request 请求信息
	 * @return 结果
	 */
	@Override
	public int changeStatus(InstrumentManage instrumentManage, HttpServletRequest request) {
		User user = JwtUtil.getTokenUser(request);
		if (user == null) {
		    return 0;
		}
		InstrumentManage ins= instrumentManageMapper.selectInstrumentManageById(instrumentManage.getId());
		if (StringUtils.isNotNull(ins) && StringUtils.isNull(ins.getImStartTime())) {
			// 设置启用日期
			instrumentManage.setImStartTime(new Date());
		}
		return instrumentManageMapper.updateInstrumentManage(instrumentManage);
	}
}
