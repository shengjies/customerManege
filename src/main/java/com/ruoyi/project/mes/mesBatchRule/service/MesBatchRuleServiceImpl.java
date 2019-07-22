package com.ruoyi.project.mes.mesBatchRule.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.mes.mesBatchRule.mapper.MesBatchRuleMapper;
import com.ruoyi.project.mes.mesBatchRule.domain.MesBatchRule;
import com.ruoyi.project.mes.mesBatchRule.service.IMesBatchRuleService;
import com.ruoyi.common.support.Convert;

/**
 * MES批准追踪规则 服务层实现
 * 
 * @author sj
 * @date 2019-07-22
 */
@Service
public class MesBatchRuleServiceImpl implements IMesBatchRuleService 
{
	@Autowired
	private MesBatchRuleMapper mesBatchRuleMapper;

	/**
     * 查询MES批准追踪规则信息
     * 
     * @param id MES批准追踪规则ID
     * @return MES批准追踪规则信息
     */
    @Override
	public MesBatchRule selectMesBatchRuleById(Integer id)
	{
	    return mesBatchRuleMapper.selectMesBatchRuleById(id);
	}
	
	/**
     * 查询MES批准追踪规则列表
     * 
     * @param mesBatchRule MES批准追踪规则信息
     * @return MES批准追踪规则集合
     */
	@Override
	public List<MesBatchRule> selectMesBatchRuleList(MesBatchRule mesBatchRule)
	{
	    return mesBatchRuleMapper.selectMesBatchRuleList(mesBatchRule);
	}
	
    /**
     * 新增MES批准追踪规则
     * 
     * @param mesBatchRule MES批准追踪规则信息
     * @return 结果
     */
	@Override
	public int insertMesBatchRule(MesBatchRule mesBatchRule)
	{
	    return mesBatchRuleMapper.insertMesBatchRule(mesBatchRule);
	}
	
	/**
     * 修改MES批准追踪规则
     * 
     * @param mesBatchRule MES批准追踪规则信息
     * @return 结果
     */
	@Override
	public int updateMesBatchRule(MesBatchRule mesBatchRule)
	{
	    return mesBatchRuleMapper.updateMesBatchRule(mesBatchRule);
	}

	/**
     * 删除MES批准追踪规则对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMesBatchRuleByIds(String ids)
	{
		return mesBatchRuleMapper.deleteMesBatchRuleByIds(Convert.toStrArray(ids));
	}
	
}
