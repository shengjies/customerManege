package com.ruoyi.project.erp.contractContent.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.contract.domain.Contract;
import com.ruoyi.project.erp.contract.mapper.ContractMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.contractContent.mapper.ContractContentMapper;
import com.ruoyi.project.erp.contractContent.domain.ContractContent;
import com.ruoyi.common.support.Convert;

import javax.servlet.http.HttpServletRequest;

/**
 * 合同内容 服务层实现
 * 
 * @author zqm
 * @date 2019-05-10
 */
@Service
public class ContractContentServiceImpl implements IContractContentService 
{
	@Autowired
	private ContractContentMapper contractContentMapper;

	@Autowired
	private ContractMapper contractMapper;

	/**
     * 查询合同内容信息
     *
     * @param id 合同内容ID
     * @return 合同内容信息
     */
    @Override
//	@DataSource(DataSourceType.ERP)
	public ContractContent selectContractContentById(Integer id)
	{
	    return contractContentMapper.selectContractContentById(id);
	}

	/**
     * 查询合同内容列表
     * 
     * @param contractContent 合同内容信息
     * @return 合同内容集合
     */
	@Override
//	@DataSource(DataSourceType.ERP)
	public List<ContractContent> selectContractContentList(ContractContent contractContent)
	{
	    return contractContentMapper.selectContractContentList(contractContent);
	}
	
    /**
     * 新增合同内容
     * 
     * @param contractContent 合同内容信息
     * @return 结果
     */
	@Override
//	@DataSource(DataSourceType.ERP)
	public int insertContractContent(ContractContent contractContent, HttpServletRequest request)
	{
		User user = JwtUtil.getTokenUser(request);
		if(user ==null)return 0;
		Contract contract = contractMapper.selectContractByCompanyId(user.getCompanyId());
		if(contract ==null)return 0;
		contractContent.setCId(contract.getId());
		contractContent.setCreateTime(new Date());
	    return contractContentMapper.insertContractContent(contractContent);
	}
	
	/**
     * 修改合同内容
     * 
     * @param contractContent 合同内容信息
     * @return 结果
     */
	@Override
//	@DataSource(DataSourceType.ERP)
	public int updateContractContent(ContractContent contractContent)
	{
	    return contractContentMapper.updateContractContent(contractContent);
	}

	/**
     * 删除合同内容对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
//	@DataSource(DataSourceType.ERP)
	public int deleteContractContentByIds(String ids)
	{
		return contractContentMapper.deleteContractContentByIds(Convert.toStrArray(ids));
	}
	
}
