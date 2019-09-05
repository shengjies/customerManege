package com.ruoyi.project.iso.sop.service;

import com.ruoyi.common.constant.SopConstants;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.iso.sop.domain.Sop;
import com.ruoyi.project.iso.sop.mapper.SopMapper;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.mapper.SopLineMapper;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * SOP配置主 服务层实现
 * 
 * @author sj
 * @date 2019-08-30
 */
@Service
public class SopServiceImpl implements ISopService 
{
	@Autowired
	private SopMapper sopMapper;

	@Autowired
	private DevProductListMapper productMapper;

	@Autowired
	private SopLineMapper sopLineMapper;

	/**
     * 查询SOP配置主信息
     * 
     * @param id SOP配置主ID
     * @return SOP配置主信息
     */
    @Override
	public Sop selectSopById(Integer id)
	{
	    return sopMapper.selectSopById(id);
	}
	
	/**
     * 查询SOP配置主列表
     * 
     * @param sop SOP配置主信息
     * @return SOP配置主集合
     */
	@Override
	public List<Sop> selectSopList(Sop sop)
	{
	    return sopMapper.selectSopList(sop);
	}

	/**
	 * 查询SOP配置主列表
	 *
	 * @param sop SOP配置主信息
	 * @return SOP配置主集合
	 */
	@Override
	public List<Sop> selectSopList2(Sop sop)
	{
		return sopMapper.selectSopList2(sop);
	}
    /**
     * 新增SOP配置主
     * 
     * @param sop SOP配置主信息
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertSop(Sop sop)
	{
		User user = JwtUtil.getUser();
		if (user == null) {
		    return 0;
		}
		// 新增sop配置
		insertSopConfig(sop, user);
		return sopMapper.updateSop(sop);
	}
	
	/**
     * 修改SOP配置主
     * 
     * @param sop SOP配置主信息
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateSop(Sop sop)
	{
		User user = JwtUtil.getUser();
		if (user == null) {
			return 0;
		}
		// 删除之前记录
		sopMapper.deleteSopById(sop.getId());
		sopLineMapper.deleteSopLineBySId(sop.getId());
		// 新增sop配置
		insertSopConfig(sop, user);
		return sopMapper.updateSop(sop);
	}

	/**
	 * 新增sop配置
	 * @param sop sop信息
	 * @param user 用户信息
	 */
	private void insertSopConfig(Sop sop, User user) {
		sop.setCompanyId(user.getCompanyId());
		StringBuilder pnIds = new StringBuilder();
		StringBuilder pnCodes = new StringBuilder();
		sop.setCreateTime(new Date());
		sopMapper.insertSop(sop);
		if (sop.getPnsId() != null && sop.getPnsId().length > 0 && sop.getSopLines() != null && sop.getSopLines().size() > 0) {
			DevProductList product = null;
			for (Integer pnId : sop.getPnsId()) {
				product = productMapper.selectDevProductListById(pnId);
				if (product == null) {
					continue;
				}
				pnIds.append(pnId).append(",");
				pnCodes.append(product.getProductCode()).append(",");
				for (SopLine sopLine : sop.getSopLines()) {
					sopLine.setPnId(pnId);
					sopLine.setsId(sop.getId());
					sopLine.setCompanyId(user.getCompanyId());
					sopLine.setcId(user.getUserId().intValue());
					sopLine.setPnCode(product.getProductCode());
					sopLine.setcTime(new Date());
					sopLineMapper.insertSopLine(sopLine);
				}
			}
		}
		pnIds.replace(pnIds.lastIndexOf(","), pnIds.length(), "");
		pnCodes.replace(pnCodes.lastIndexOf(","), pnCodes.length(), "");
		sop.setpId(pnIds.toString());
		sop.setpCode(pnCodes.toString());
	}

	/**
     * 删除SOP配置主对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteSopByIds(String ids)
	{
		sopMapper.deleteSopByIds(Convert.toStrArray(ids));
		return sopLineMapper.deleteSopLineBySIds(Convert.toStrArray(ids));
	}

	/**
	 * 校验sop配置名称的唯一性
	 */
	@Override
	public String checkCnfNameUnique(Sop sop) { User user = JwtUtil.getUser();
		if (user == null) {
		    return SopConstants.CNF_NAME_NOT_UNIQUE;
		}
		Sop uniqueSop = sopMapper.selectSopByCnfName(user.getCompanyId(),sop.getCnfName());
		if (StringUtils.isNotNull(uniqueSop) && !uniqueSop.getId().equals(sop.getId())) {
		    return SopConstants.CNF_NAME_NOT_UNIQUE;
		}
		return SopConstants.CNF_NAME_UNIQUE;
	}

	/**
	 * 通过作业指导书id查询配置列表
	 * @param companyId 公司id
	 * @param sopId 作业指导书id
	 * @return 结果
	 */
	@Override
	public List<Sop> selectSopListBySopId(Integer companyId, Integer sopId) {
		return sopMapper.selectSopListBySopId(companyId,sopId);
	}
}
