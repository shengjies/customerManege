package com.ruoyi.project.mes.mesBatch.service;

import com.ruoyi.project.mes.mesBatch.domain.MesBatch;
import java.util.List;

/**
 * MES批准追踪 服务层
 * 
 * @author sj
 * @date 2019-07-22
 */
public interface IMesBatchService 
{
	/**
     * 查询MES批准追踪信息
     * 
     * @param id MES批准追踪ID
     * @return MES批准追踪信息
     */
	public MesBatch selectMesBatchById(Integer id);
	
	/**
     * 查询MES批准追踪列表
     * 
     * @param mesBatch MES批准追踪信息
     * @return MES批准追踪集合
     */
	public List<MesBatch> selectMesBatchList(MesBatch mesBatch);
	
	/**
     * 新增MES批准追踪
     * 
     * @param mesBatch MES批准追踪信息
     * @return 结果
     */
	public int insertMesBatch(MesBatch mesBatch);
	
	/**
     * 修改MES批准追踪
     * 
     * @param mesBatch MES批准追踪信息
     * @return 结果
     */
	public int updateMesBatch(MesBatch mesBatch);
		
	/**
     * 删除MES批准追踪信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMesBatchByIds(String ids);
	
}
