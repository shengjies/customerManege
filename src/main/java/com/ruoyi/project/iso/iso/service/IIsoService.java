package com.ruoyi.project.iso.iso.service;

import com.ruoyi.project.iso.iso.domain.Iso;
import java.util.List;

/**
 * 文件管理 服务层
 * 
 * @author sj
 * @date 2019-06-13
 */
public interface IIsoService 
{
	/**
     * 查询文件管理信息
     * 
     * @param id 文件管理ID
     * @return 文件管理信息
     */
	public Iso selectIsoById(Integer id);
	
	/**
     * 查询文件管理列表
     * 
     * @param iso 文件管理信息
     * @return 文件管理集合
     */
	public List<Iso> selectIsoList(Iso iso);
	
	/**
     * 新增文件管理
     * 
     * @param iso 文件管理信息
     * @return 结果
     */
	public int insertIso(Iso iso);
	
	/**
     * 修改文件管理
     * 
     * @param iso 文件管理信息
     * @return 结果
     */
	public int updateIso(Iso iso);
		
	/**
     * 删除文件管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteIsoByIds(String ids);
	
}
