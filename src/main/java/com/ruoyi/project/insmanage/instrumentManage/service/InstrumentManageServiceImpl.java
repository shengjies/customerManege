package com.ruoyi.project.insmanage.instrumentManage.service;

import com.ruoyi.common.constant.InstrumentConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import com.ruoyi.project.insmanage.instrumentManage.mapper.InstrumentManageMapper;
import com.ruoyi.project.insmanage.instrumentType.domain.InstrumentType;
import com.ruoyi.project.insmanage.instrumentType.mapper.InstrumentTypeMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 仪器设备管理 服务层实现
 * 
 * @author sj
 * @date 2019-06-19
 */
@Service("im")
public class InstrumentManageServiceImpl implements IInstrumentManageService 
{
	@Autowired
	private InstrumentManageMapper instrumentManageMapper;

	@Autowired
	private InstrumentTypeMapper instrumentTypeMapper;

	/**
     * 查询仪器设备管理信息
     * 
     * @param id 仪器设备管理ID
     * @return 仪器设备管理信息
     */
    @Override
	public InstrumentManage selectInstrumentManageById(Integer id)
	{
	    return instrumentManageMapper.selectInstrumentManageById(id);
	}
	
	/**
     * 查询仪器设备管理列表
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 仪器设备管理集合
     */
	@Override
	public List<InstrumentManage> selectInstrumentManageList(InstrumentManage instrumentManage, HttpServletRequest request)
	{
		User user = JwtUtil.getTokenUser(request);
		if (user == null ) {
			return Collections.emptyList();
		}
		instrumentManage.setCompanyId(user.getCompanyId());
		return instrumentManageMapper.selectInstrumentManageList(instrumentManage);
	}
	
    /**
     * 新增仪器设备管理
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertInstrumentManage(InstrumentManage instrumentManage,User user)
	{
		// 设备名称
		String inTypeName = instrumentManage.getInTypeName();
		if (StringUtils.isNotEmpty(inTypeName)) {
			updateInsType(instrumentManage, user, inTypeName);
		}
		instrumentManage.setCompanyId(user.getCompanyId());
		instrumentManage.setCreateId(user.getUserId().intValue());
		instrumentManage.setCreateTime(new Date());
	    return instrumentManageMapper.insertInstrumentManage(instrumentManage);
	}

	/**
	 * 更新设备的类型，没有就新增一条设备类型记录
	 * @param instrumentManage 设备对象
	 * @param user 用户
	 * @param inTypeName 设备类型名称
	 */
	private void updateInsType(InstrumentManage instrumentManage, User user, String inTypeName) {
		InstrumentType instrumentType = instrumentTypeMapper.selectInstrumentTypeByName(inTypeName, user.getCompanyId());
		if (StringUtils.isNull(instrumentType)) {
			instrumentType = new InstrumentType();
			instrumentType.setInType(inTypeName);
			instrumentType.setCompanyId(user.getCompanyId());
			instrumentType.setCreateId(user.getUserId().intValue());
			instrumentType.setCreateTime(new Date());
			instrumentTypeMapper.insertInstrumentType(instrumentType);
		}
		instrumentManage.setInType(instrumentType.getId());
	}

	/**
     * 修改仪器设备管理
     * 
     * @param instrumentManage 仪器设备管理信息
     * @return 结果
     */
	@Override
	public int updateInstrumentManage(InstrumentManage instrumentManage)
	{
		User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
		if (user == null) {
		    throw new BusinessException(UserConstants.NOT_LOGIN);
		}
		// 设备名称
		String inTypeName = instrumentManage.getInTypeName();
		if (StringUtils.isNotNull(inTypeName)) {
			updateInsType(instrumentManage, user, inTypeName);
		}
	    return instrumentManageMapper.updateInstrumentManage(instrumentManage);
	}

	/**
     * 删除仪器设备管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteInstrumentManageByIds(String ids)
	{
		Integer[] ims = Convert.toIntArray(ids);
		InstrumentManage instrumentManage;
		int failureNum = 0;
		StringBuilder failureMsg = new StringBuilder();
		for (Integer id : ims) {
			instrumentManage = instrumentManageMapper.selectInstrumentManageById(id);
			if (StringUtils.isNotNull(instrumentManage) && instrumentManage.getImTag().equals(InstrumentConstants.IM_TAG_USED)) {
				failureNum++;
				failureMsg.append("<br/>" + failureNum + "、设备编号 " + instrumentManage.getImCode() + " 已配置单工位，请先删除其关联");
			}
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，删除失败！错误如下：");
			throw new BusinessException(failureMsg.toString());
		}
		return instrumentManageMapper.deleteInstrumentManageByIds(Convert.toStrArray(ids));
	}


	/**
	 * 修改设备状态
	 * @param instrumentManage 设备信息
	 * @param request 请求信息
	 * @return 结果
	 */
	@Override
	public int changeStatus(InstrumentManage instrumentManage, HttpServletRequest request) {
		User user = JwtUtil.getTokenUser(request);
		if (user == null) {
		    return 0;
		}
		InstrumentManage ins= instrumentManageMapper.selectInstrumentManageById(instrumentManage.getId());
		if (StringUtils.isNotNull(ins) && StringUtils.isNull(ins.getImStartTime())) {
			// 设置启用日期为第一次开启的时间
			instrumentManage.setImStartTime(new Date());
		}
		return instrumentManageMapper.updateInstrumentManage(instrumentManage);
	}

	/**
	 * 校验设备编码唯一性
	 * @param instrumentManage 设备
	 * @return 结果
	 */
	@Override
	public String checkImCodeUnique(InstrumentManage instrumentManage) {
		User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
		if (user == null) {
		    return InstrumentConstants.IM_CODE_NOT_UNIQUE;
		}
		InstrumentManage ins = instrumentManageMapper.selectInstrumentManageByImCode(instrumentManage.getImCode(),user.getCompanyId());
		if (StringUtils.isNotNull(ins) && !ins.getId().equals(instrumentManage.getId())) {
			return InstrumentConstants.IM_CODE_NOT_UNIQUE;
		}
		return InstrumentConstants.IM_CODE_UNIQUE;
	}

	/**
	 * 导入仪器设备列表
	 * @param imList 设备列表
	 * @param updateSupport 是否更新原来的数据
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String importInstrumentManageList(List<InstrumentManage> imList, boolean updateSupport) {
		User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
		if (user == null) {
		    throw new BusinessException("请先登录再进行操作");
		}
		if (StringUtils.isEmpty(imList) || imList.size() == 0) {
		    throw new BusinessException("导入数据不能为空");
		}
		int successNum = 0;
		int failureNum = 0;
		InstrumentManage instrumentManage = null;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		for (InstrumentManage im : imList) {
			try {
				if (StringUtils.isNotEmpty(im.getImCode())) {
					// 验证是否有这个仪器编号
					instrumentManage = instrumentManageMapper.selectInstrumentManageByImCode(im.getImCode(), user.getCompanyId());
					if (StringUtils.isNull(instrumentManage)) {
						this.insertInstrumentManage(im,user);
						successNum++;
						successMsg.append("<br/>" + successNum + "、编号 " + im.getImCode() + " 导入成功");
						// 更新操作
					} else if (updateSupport) {
						im.setId(instrumentManage.getId());
						this.updateInstrumentManage(im);
						successNum++;
						successMsg.append("<br/>" + successNum + "、编号 " + im.getImCode() + " 更新成功");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + "、编号 " + im.getImCode() + " 已存在");
					}
				}
			} catch (Exception e) {
				failureNum++;
				String msg = "<br/>" + failureNum + "、编号 " + im.getImCode() + " 导入失败：";
				failureMsg.append(msg + e.getMessage());
			}
		}
		return ExcelUtil.getResultString(successNum, failureNum, successMsg, failureMsg);
	}

	/**
	 * 查询各公司所有未配置过的已启用的设备信息
	 * @return 结果
	 */
	@Override
	public List<InstrumentManage> selectAllIm(Integer imStatus,Integer imTag) {
		User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
		if (user == null) {
		    return Collections.emptyList();
		}
		return instrumentManageMapper.selectInstrumentManageListByImTag(user.getCompanyId(),imStatus,imTag);
	}

	/**
	 * app端查询设备列表
	 * @param instrumentManage 设备对象
	 * @return 结果
	 */
	@Override
	public List<InstrumentManage> appSelectList(InstrumentManage instrumentManage) {
		return instrumentManageMapper.selectInstrumentManageList(instrumentManage);
	}
}
