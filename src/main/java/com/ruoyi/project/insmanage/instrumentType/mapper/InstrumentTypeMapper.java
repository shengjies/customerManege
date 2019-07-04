package com.ruoyi.project.insmanage.instrumentType.mapper;

import com.ruoyi.project.insmanage.instrumentType.domain.InstrumentType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备类型 数据层
 * 
 * @author sj
 * @date 2019-07-02
 */
public interface InstrumentTypeMapper 
{
	/**
     * 查询设备类型信息
     * 
     * @param id 设备类型ID
     * @return 设备类型信息
     */
	public InstrumentType selectInstrumentTypeById(Integer id);
	
	/**
     * 查询设备类型列表
     * 
     * @param instrumentType 设备类型信息
     * @return 设备类型集合
     */
	public List<InstrumentType> selectInstrumentTypeList(InstrumentType instrumentType);
	
	/**
     * 新增设备类型
     * 
     * @param instrumentType 设备类型信息
     * @return 结果
     */
	public int insertInstrumentType(InstrumentType instrumentType);
	
	/**
     * 修改设备类型
     * 
     * @param instrumentType 设备类型信息
     * @return 结果
     */
	public int updateInstrumentType(InstrumentType instrumentType);
	
	/**
     * 删除设备类型
     * 
     * @param id 设备类型ID
     * @return 结果
     */
	public int deleteInstrumentTypeById(Integer id);
	
	/**
     * 批量删除设备类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteInstrumentTypeByIds(String[] ids);

	/**
	 * 通过设备类型名称查询设备类型信息
	 * @param inType 设备类型名称
	 * @param companyId 公司id
	 * @return 结果
	 */
	InstrumentType selectInstrumentTypeByName(@Param("inType") String inType,@Param("companyId") Integer companyId);
}