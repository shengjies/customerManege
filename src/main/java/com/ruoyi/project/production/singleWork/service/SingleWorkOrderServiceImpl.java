package com.ruoyi.project.production.singleWork.service;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.production.singleWork.domain.SingleWorkOrder;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkMapper;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkOrderMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 单工位与工单进行配置 服务层实现
 * 
 * @author sj
 * @date 2019-07-09
 */
@Service
public class SingleWorkOrderServiceImpl implements ISingleWorkOrderService 
{
	@Autowired
	private SingleWorkOrderMapper singleWorkOrderMapper;

	@Autowired
	private DevWorkOrderMapper devWorkOrderMapper;

	@Autowired
	private SingleWorkMapper singleWorkMapper;

	/**
     * 查询单工位与工单进行配置信息
     * 
     * @param id 单工位与工单进行配置ID
     * @return 单工位与工单进行配置信息
     */
    @Override
	public SingleWorkOrder selectSingleWorkOrderById(Integer id)
	{
	    return singleWorkOrderMapper.selectSingleWorkOrderById(id);
	}
	
	/**
     * 查询单工位与工单进行配置列表
     * 
     * @param singleWorkOrder 单工位与工单进行配置信息
     * @return 单工位与工单进行配置集合
     */
	@Override
	public List<SingleWorkOrder> selectSingleWorkOrderList(SingleWorkOrder singleWorkOrder)
	{
	    return singleWorkOrderMapper.selectSingleWorkOrderList(singleWorkOrder);
	}
	
    /**
     * 新增单工位与工单进行配置
     * 
     * @param singleWorkOrder 单工位与工单进行配置信息
     * @return 结果
     */
	@Override
	public int insertSingleWorkOrder(SingleWorkOrder singleWorkOrder) throws Exception
	{
		if(singleWorkOrder == null){
			throw new Exception("系统异常");
		}
		//查询对应的工单信息
		DevWorkOrder workOrder = devWorkOrderMapper.selectDevWorkOrderById(singleWorkOrder.getWorkId());
		if(workOrder ==null){
			throw new Exception("操作异常,该工单不存在");
		}
		//查询对应的单工位信息
		SingleWork work =  singleWorkMapper.selectSingleWorkById(singleWorkOrder.getSingleId());
		if(work == null){
			throw new Exception("操作异常,对应工位不存在");
		}
		singleWorkOrder.setWorkCode(workOrder.getWorkorderNumber());
		singleWorkOrder.setSingleP(work.getParentId());
		singleWorkOrder.setcTime(new Date());
		return singleWorkOrderMapper.insertSingleWorkOrder(singleWorkOrder);
	}
	
	/**
     * 修改单工位与工单进行配置
     * 
     * @param singleWorkOrder 单工位与工单进行配置信息
     * @return 结果
     */
	@Override
	public int updateSingleWorkOrder(SingleWorkOrder singleWorkOrder)
	{
		User user = JwtUtil.getUser();
		if (user == null) {
		    throw new BusinessException(UserConstants.NOT_LOGIN);
		}
		if (singleWorkOrder.getType() == 0){
			// 车间设备配置工单
			DevWorkOrder workOrder = devWorkOrderMapper.selectWorkOrderInfoById(user.getCompanyId(), singleWorkOrder.getWorkId());
			singleWorkOrder.setWorkCode(workOrder.getWorkorderNumber());
		}
		return singleWorkOrderMapper.updateSingleWorkOrder(singleWorkOrder);
	}

	/**
     * 删除单工位与工单进行配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteSingleWorkOrderByIds(String ids)
	{
		return singleWorkOrderMapper.deleteSingleWorkOrderByIds(Convert.toStrArray(ids));
	}


	/******************************************************************************************************
	 *********************************** app单工位工单进行工单分配 ******************************************
	 ******************************************************************************************************/
	@Override
	public int appSaveShareWork(SingleWorkOrder singleWorkOrder) {
		String jsCode = singleWorkOrder.getJsCode();
		SingleWork singleWork = singleWorkMapper.selectSingleWorkByDevCode(jsCode,null);
		if (singleWork == null) {
		    throw new BusinessException("硬件不存在或单工位未配置该硬件");
		}
		//查询对应的工单信息
		DevWorkOrder workOrder = devWorkOrderMapper.selectDevWorkOrderById(singleWorkOrder.getWorkId());
		if(workOrder ==null){
			throw new BusinessException("操作异常,该工单不存在");
		}
		if (!workOrder.getLineId().equals(singleWork.getmParentId())) {
		    throw new BusinessException("请确保工位属于工单对应的车间");
		}
		// 查询上传的硬件是否已经配置过该工单
		SingleWorkOrder uniqueWork = singleWorkOrderMapper.selectSingleWorkByWorkIdAndSId(singleWorkOrder.getWorkId(),singleWork.getId());
		if (uniqueWork != null) {
		    throw new BusinessException("该单工位已经分配过工单勿重复配置");
		}
		singleWorkOrder.setWorkCode(workOrder.getWorkorderNumber());
		singleWorkOrder.setSingleP(singleWork.getParentId());
		singleWorkOrder.setSingleId(singleWork.getId());
		singleWorkOrder.setcTime(new Date());
		return singleWorkOrderMapper.insertSingleWorkOrder(singleWorkOrder);
	}
}
