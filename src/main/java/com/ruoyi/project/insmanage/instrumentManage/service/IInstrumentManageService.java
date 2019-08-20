package com.ruoyi.project.insmanage.instrumentManage.service;

import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import com.ruoyi.project.system.user.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

	/**
	 * 校验设备编码唯一性
	 * @param instrumentManage 设备
	 * @return 结果
	 */
    String checkImCodeUnique(InstrumentManage instrumentManage);

	/**
	 * 导入仪器设备
	 * @param imList 设备列表
	 * @param updateSupport 是否更新原来的数据
	 * @return 结果
	 */
    String importInstrumentManageList(List<InstrumentManage> imList, boolean updateSupport);

	/**
	 * 查询各公司所有未配置过的已开启的设备信息
	 * @return 结果
	 */
	public List<InstrumentManage> selectAllIm(Integer imStatus,Integer imTag);

	/**
	 * app端查询设备列表
	 * @param instrumentManage 设备对象
	 * @return 结果
	 */
	public List<InstrumentManage> appSelectList(InstrumentManage instrumentManage);
}
