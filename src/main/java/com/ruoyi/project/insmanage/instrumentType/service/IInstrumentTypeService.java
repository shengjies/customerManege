package com.ruoyi.project.insmanage.instrumentType.service;

import com.ruoyi.project.insmanage.instrumentType.domain.InstrumentType;
import java.util.List;

/**
 * 设备类型 服务层
 * 
 * @author sj
 * @date 2019-07-02
 */
public interface IInstrumentTypeService 
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
     * 删除设备类型信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteInstrumentTypeByIds(String ids);

	/**
	 * 查询公司所有的设备类型
	 * @return 结果
	 */
	public List<InstrumentType> selectAllInsType();

	/**
	 * 校验设备类型名称的唯一性
	 * @param instrumentType 设备类型
	 * @return 结果
	 */
    String checkInsTypeUnique(InstrumentType instrumentType);
}
