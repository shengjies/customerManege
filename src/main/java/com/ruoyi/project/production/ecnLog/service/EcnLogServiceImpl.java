package com.ruoyi.project.production.ecnLog.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.production.ecnLog.mapper.EcnLogMapper;
import com.ruoyi.project.production.ecnLog.domain.EcnLog;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * ECN 变更记录 服务层实现
 * 
 * @author zqm
 * @date 2019-05-16
 */
@Service
public class EcnLogServiceImpl implements IEcnLogService 
{
	@Autowired
	private EcnLogMapper ecnLogMapper;

	@Autowired
	private DevProductListMapper productListMapper;

	@Autowired
	private DevWorkOrderMapper devWorkOrderMapper;

	/**
	 * 查询ECN 变更记录信息
	 *
	 * @param id ECN 变更记录ID
	 * @return ECN 变更记录信息
	 */
	@Override
	public EcnLog selectEcnLogById(Integer id)
	{
		return ecnLogMapper.selectEcnLogById(id);
	}

	/**
     * 查询ECN 变更记录列表
     * 
     * @param ecnLog ECN 变更记录信息
     * @return ECN 变更记录集合
     */
	@Override
//	@DataSource(DataSourceType.SLAVE)
	public List<EcnLog> selectEcnLogList(EcnLog ecnLog, HttpServletRequest request)
	{
		User u = JwtUtil.getTokenUser(request);
		if(u == null)return Collections.emptyList();
		ecnLog.setCompanyId(u.getCompanyId());
	    return ecnLogMapper.selectEcnLogList(ecnLog);
	}
	
    /**
     * 新增ECN 变更记录
     * 
     * @param ecnLog ECN 变更记录信息
     * @return 结果
     */
	@Override
	@Transactional
	public int insertEcnLog(EcnLog ecnLog)
	{
		User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
		if(u ==null)return 0;//新增异常
		ecnLog.setCompanyId(u.getCompanyId());
		ecnLog.setCreateTime(new Date());
		ecnLog.setCreateId(u.getUserId().intValue());
		ecnLog.setCreatePeople(u.getUserName());
		if(ecnLog.getEcnType() == 1 || ecnLog.getEcnType() == 3){
			//产品变更，查询产品
			DevProductList product = productListMapper.selectDevProductListById(ecnLog.getSaveId());
			if(product == null)return 0;//操作异常
			ecnLog.setSaveCode(product.getProductCode());
			devWorkOrderMapper.editCompanyProductWorkOrderEcn(u.getCompanyId(), product.getProductCode());
			product.setEcnStatus(1);//有变更
			product.setEcnText(ecnLog.getEcnText());//变更内容
			productListMapper.updateDevProductList(product);
		}else if(ecnLog.getEcnType() == 2){//工单变更
			DevWorkOrder workOrder =  devWorkOrderMapper.selectDevWorkOrderById(ecnLog.getSaveId());
			if(workOrder == null)return 0;//操作异常
			ecnLog.setSaveCode(workOrder.getWorkorderNumber());
		}else{
			//操作异常
			return 0;
		}
	    return ecnLogMapper.insertEcnLog(ecnLog);
	}

	/**
	 * 关闭ECN 状态变更记录
	 *
	 * @param type ECN 变更记录信息
	 * @return 结果
	 */
	@Override
	public int updateEcnLog(int type,int save_id)
	{
		if(type == 1 || type == 3){
			//产品或者半产品
			DevProductList productList = productListMapper.selectDevProductListById(save_id);
			if(productList == null)return 0;
			productList.setEcnStatus(0);
			productList.setEcnText("");
			productListMapper.updateDevProductList(productList);
			return 1;
		}else if(type == 2){
			//半成品
		}
		return 0;
	}

}
