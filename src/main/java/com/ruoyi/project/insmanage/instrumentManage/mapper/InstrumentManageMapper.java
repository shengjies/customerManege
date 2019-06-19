package com.ruoyi.project.insmanage.instrumentManage.mapper;

import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import java.util.List;	

/**
 * 仪器设备管理 数据层
 * 
 * @author sj
 * @date 2019-06-19
 */
public interface InstrumentManageMapper 
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
	public List<InstrumentManage> selectInstrumentManageList(InstrumentManage instrumentManage);
	
	/**
     * 新增仪器设备管理
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 结果
     */
	public int insertInstrumentManage(InstrumentManage instrumentManage);
	
	/**
     * 修改仪器设备管理
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 结果
     */
	public int updateInstrumentManage(InstrumentManage instrumentManage);
	
	/**
     * 删除仪器设备管理
     * 
     * @param id 仪器设备管理ID
     * @return 结果
     */
	public int deleteInstrumentManageById(Integer id);
	
	/**
     * 批量删除仪器设备管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteInstrumentManageByIds(String[] ids);
	
}