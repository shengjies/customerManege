package com.ruoyi.project.erp.erpRelation.domain;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 库存出入库MRP操作关联表 tab_erp_relation
 * 
 * @author sj
 * @date 2019-06-28
 */
public class ErpRelation extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 库存出入库MRP操作关联主键id */
	private Integer id;
	/** MRP采购单关联主键id */
	private Integer mpId;
	/** 物料出入库明细主键id */
	private Integer matDetailId;
	/** 产品出入库明细主键id */
	private Integer proDetailId;
	/** 类型(0、物料入库，1、物料退货，2、产品出库，3、客户退货) */
	private Integer reStatus;
	/** 生产出入库明细id */
	private Integer lineDetailId;
	/** 生产出入库的类型(0、物料，1、半成品，2、成品) */
	private Integer lineType;

	public Integer getLineDetailId() {
		return lineDetailId;
	}

	public void setLineDetailId(Integer lineDetailId) {
		this.lineDetailId = lineDetailId;
	}

	public Integer getLineType() {
		return lineType;
	}

	public void setLineType(Integer lineType) {
		this.lineType = lineType;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setMpId(Integer mpId) 
	{
		this.mpId = mpId;
	}

	public Integer getMpId() 
	{
		return mpId;
	}
	public void setMatDetailId(Integer matDetailId) 
	{
		this.matDetailId = matDetailId;
	}

	public Integer getMatDetailId() 
	{
		return matDetailId;
	}
	public void setProDetailId(Integer proDetailId) 
	{
		this.proDetailId = proDetailId;
	}

	public Integer getProDetailId() 
	{
		return proDetailId;
	}
	public void setReStatus(Integer reStatus) 
	{
		this.reStatus = reStatus;
	}

	public Integer getReStatus() 
	{
		return reStatus;
	}

	@Override
	public String toString() {
		return "ErpRelation{" +
				"id=" + id +
				", mpId=" + mpId +
				", matDetailId=" + matDetailId +
				", proDetailId=" + proDetailId +
				", reStatus=" + reStatus +
				", lineDetailId=" + lineDetailId +
				", lineType=" + lineType +
				'}';
	}
}
