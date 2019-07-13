package com.ruoyi.project.insmanage.instrumentManage.mapper;

import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import org.apache.ibatis.annotations.Param;

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

	/**
	 * 通过设备编码查询设备信息
	 * @param imCode 设备编码
	 * @param companyId 公司id
	 * @return 结果
	 */
    InstrumentManage selectInstrumentManageByImCode(@Param("imCode") String imCode,@Param("companyId") Integer companyId);

	/**
	 * 通过设备类型id查询设备列表
	 * @param inType 设备类型id
	 * @return 结果
	 */
	List<InstrumentManage> selectInstrumentManageByInsTypeId(@Param("inType") Integer inType);

	/**
	 * 通过设备标记状态查询设备列表信息
	 * @param companyId 公司id
	 * @param imStatus 设备开启停用状态
	 * @param imTag 设备标记状态
	 * @return 结果
	 */
	List<InstrumentManage> selectInstrumentManageListByImTag(@Param("companyId") Integer companyId,@Param("imStatus")Integer imStatus, @Param("imTag") Integer imTag);

	/**
	 * 通过设备id更新设备标记使用状态
	 * @param id 设备id
	 * @param imTag 设备标记状态
	 * @return 结果
	 */
	int updateInstrumentManageImTag(@Param("id") Integer id,@Param("imTag") Integer imTag);
}