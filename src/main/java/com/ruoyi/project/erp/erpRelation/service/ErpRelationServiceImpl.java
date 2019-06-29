package com.ruoyi.project.erp.erpRelation.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.erpRelation.mapper.ErpRelationMapper;
import com.ruoyi.project.erp.erpRelation.domain.ErpRelation;
import com.ruoyi.project.erp.erpRelation.service.IErpRelationService;
import com.ruoyi.common.support.Convert;

/**
 * 库存出入库MRP操作关联 服务层实现
 * 
 * @author sj
 * @date 2019-06-28
 */
@Service
public class ErpRelationServiceImpl implements IErpRelationService 
{
	@Autowired
	private ErpRelationMapper erpRelationMapper;

	/**
     * 查询库存出入库MRP操作关联信息
     * 
     * @param id 库存出入库MRP操作关联ID
     * @return 库存出入库MRP操作关联信息
     */
    @Override
	public ErpRelation selectErpRelationById(Integer id)
	{
	    return erpRelationMapper.selectErpRelationById(id);
	}
	
	/**
     * 查询库存出入库MRP操作关联列表
     * 
     * @param erpRelation 库存出入库MRP操作关联信息
     * @return 库存出入库MRP操作关联集合
     */
	@Override
	public List<ErpRelation> selectErpRelationList(ErpRelation erpRelation)
	{
	    return erpRelationMapper.selectErpRelationList(erpRelation);
	}
	
    /**
     * 新增库存出入库MRP操作关联
     * 
     * @param erpRelation 库存出入库MRP操作关联信息
     * @return 结果
     */
	@Override
	public int insertErpRelation(ErpRelation erpRelation)
	{
	    return erpRelationMapper.insertErpRelation(erpRelation);
	}
	
	/**
     * 修改库存出入库MRP操作关联
     * 
     * @param erpRelation 库存出入库MRP操作关联信息
     * @return 结果
     */
	@Override
	public int updateErpRelation(ErpRelation erpRelation)
	{
	    return erpRelationMapper.updateErpRelation(erpRelation);
	}

	/**
     * 删除库存出入库MRP操作关联对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteErpRelationByIds(String ids)
	{
		return erpRelationMapper.deleteErpRelationByIds(Convert.toStrArray(ids));
	}
	
}
