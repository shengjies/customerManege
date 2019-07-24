package com.ruoyi.project.mes.mesBatchRule.service;

import com.ruoyi.common.constant.MesConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.materiel.domain.Materiel;
import com.ruoyi.project.erp.materiel.mapper.MaterielMapper;
import com.ruoyi.project.mes.mesBatchRule.domain.MesBatchRule;
import com.ruoyi.project.mes.mesBatchRule.mapper.MesBatchRuleMapper;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * MES批准追踪规则 服务层实现
 * 
 * @author sj
 * @date 2019-07-22
 */
@Service("mesRule")
public class MesBatchRuleServiceImpl implements IMesBatchRuleService 
{
	@Autowired
	private MesBatchRuleMapper mesBatchRuleMapper;

	@Autowired
	private MaterielMapper materielMapper;

	@Autowired
	private DevProductListMapper productListMapper;

	/**
     * 查询MES批准追踪规则信息
     * 
     * @param id MES批准追踪规则ID
     * @return MES批准追踪规则信息
     */
    @Override
	public MesBatchRule selectMesBatchRuleById(Integer id)
	{
		MesBatchRule mesBatchRule = mesBatchRuleMapper.selectMesBatchRuleById(id);
		// 查询未配置的物料编码信息
		String materiels = mesBatchRule.getMateriels();
		if (StringUtils.isNotNull(materiels)) {
			String[] matCodeList = materiels.split(",");
			mesBatchRule.setMaterielList(matCodeList);
			// 查询未配置的物料信息
			List<Materiel> notMaterielList = materielMapper.selectMaterielByMatCodes(Convert.toStrArray(materiels));
			mesBatchRule.setNotMaterielList(notMaterielList);
		}
		return mesBatchRule;
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
		User user = JwtUtil.getUser();
		if (user == null) {
		    return Collections.emptyList();
		}
		mesBatchRule.setCompanyId(user.getCompanyId());
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
		User user = JwtUtil.getUser();
		if (user == null) {
		    throw new BusinessException(UserConstants.NOT_LOGIN);
		}
		mesBatchRule.setCompanyId(user.getCompanyId());
		mesBatchRule.setcTime(new Date());
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
		mesBatchRule.setuTime(new Date());
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
		User user = JwtUtil.getUser();
		if (user == null) {
		    throw new BusinessException(UserConstants.NOT_LOGIN);
		}
		Integer[] ruleIds = Convert.toIntArray(ids);
		for (Integer ruleId : ruleIds) {
			List<DevProductList> productList = productListMapper.selectDevProductByRuleId(user.getCompanyId(), ruleId);
			if (StringUtils.isNotEmpty(productList) && productList.size() > 0) {
			    throw new BusinessException("该追踪规则配置了产品，请先删除其关联信息");
			}
		}
		return mesBatchRuleMapper.deleteMesBatchRuleByIds(Convert.toStrArray(ids));
	}

	/**
	 * 校验追踪规格的唯一性
	 * @param mesBatchRule 追踪规格
	 * @return 结果
	 */
	@Override
	public String checkMesRuleNameUnique(MesBatchRule mesBatchRule) {
		User user = JwtUtil.getUser();
		if (user == null) {
		    return MesConstants.MES_RULENAME_NOT_UNIQUE;
		}
		MesBatchRule mesBatchRuleUnique = mesBatchRuleMapper.selectMesBatchRuleByName(user.getCompanyId(),mesBatchRule.getRuleName());
		if (StringUtils.isNotNull(mesBatchRuleUnique) && !mesBatchRule.getId().equals(mesBatchRuleUnique.getId())) {
		    return MesConstants.MES_RULENAME_NOT_UNIQUE;
		}
		return MesConstants.MES_RULENAME_UNIQUE;
	}

	/**
	 * 根据规则类型查询对应的规则列表
	 * @param type 规则类型 0、产品 1、半成品
	 * @return
	 */
	@Override
	public List<MesBatchRule> selectMesRuleByType(int type) {
		return mesBatchRuleMapper.selectMesRuleByType(type);
	}

	/**
	 * 查询所有的MES规则信息
	 * @return 结果
	 */
	@Override
	public List<MesBatchRule> selectAllMesRule() {
		return mesBatchRuleMapper.selectAllMesRule();
	}

}
