package com.ruoyi.project.insmanage.instrumentManage.service;

import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import com.ruoyi.project.system.user.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Handler;

/**
 * 仪器设备管理 服务层
 * 
 * @author sj
 * @date 2019-06-19
 */
public interface IInstrumentManageService 
{
	/**
     * 查询仪器设备管理信息
     * 
     * @param id 仪器设备管理ID
     * @return 仪器设备管理信息
     */
	public InstrumentManage selectInstrumentManageById(Integer id);
	
	/**
     * 查询仪器设备管理列表
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 仪器设备管理集合
     */
	public List<InstrumentManage> selectInstrumentManageList(InstrumentManage instrumentManage, HttpServletRequest request);
	
	/**
     * 新增仪器设备管理
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 结果
     */
	public int insertInstrumentManage(InstrumentManage instrumentManage, User user);
	
	/**
     * 修改仪器设备管理
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 结果
     */
	public int updateInstrumentManage(InstrumentManage instrumentManage);
		
	/**
     * 删除仪器设备管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteInstrumentManageByIds(String ids);

	/**
	 * 修改设备状态
	 * @param instrumentManage 设备信息
	 * @param request 请求信息
	 * @return 结果
	 */
	int changeStatus(InstrumentManage instrumentManage, HttpServletRequest request);
}
