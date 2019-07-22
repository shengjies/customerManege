package com.ruoyi.project.mes.mesBatchRule.service;

import com.ruoyi.project.mes.mesBatchRule.domain.MesBatchRule;
import java.util.List;

/**
 * MES批准追踪规则 服务层
 * 
 * @author sj
 * @date 2019-07-22
 */
public interface IMesBatchRuleService 
{
	/**
     * 查询MES批准追踪规则信息
     * 
     * @param id MES批准追踪规则ID
     * @return MES批准追踪规则信息
     */
	public MesBatchRule selectMesBatchRuleById(Integer id);
	
	/**
     * 查询MES批准追踪规则列表
     * 
     * @param mesBatchRule MES批准追踪规则信息
     * @return MES批准追踪规则集合
     */
	public List<MesBatchRule> selectMesBatchRuleList(MesBatchRule mesBatchRule);
	
	/**
     * 新增MES批准追踪规则
     * 
     * @param mesBatchRule MES批准追踪规则信息
     * @return 结果
     */
	public int insertMesBatchRule(MesBatchRule mesBatchRule);
	
	/**
     * 修改MES批准追踪规则
     * 
     * @param mesBatchRule MES批准追踪规则信息
     * @return 结果
     */
	public int updateMesBatchRule(MesBatchRule mesBatchRule);
		
	/**
     * 删除MES批准追踪规则信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMesBatchRuleByIds(String ids);
	
}
