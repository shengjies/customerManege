package com.ruoyi.project.erp.mrpPurchase.domain;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * mrp采购单关联表 tab_mrp_purchase
 * 
 * @author sj
 * @date 2019-06-26
 */
public class MrpPurchase extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** mrp采购单关联表id主键 */
	private Integer id;
	/** MRP表主键id */
	private Integer mrpId;
	/** 采购单主表id */
	private Integer purId;
	/** 采购单详表id */
	private Integer purDetailId;
	/** 所差数量 */
	private Integer delNumber;
	/** 调整数量 */
	private Integer tiaoNumber;
	/** 总数(所差数量+调整数量，不显示) */
	private Integer totalNumber;
	/** 锁定的物料库存数量 */
	private Integer lockMatNumber;
	/** 原始锁定物料库存数量 */
	private Integer initLockMatNumber;

	private String materielCode;

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public Integer getInitLockMatNumber() {
		return initLockMatNumber;
	}

	public void setInitLockMatNumber(Integer initLockMatNumber) {
		this.initLockMatNumber = initLockMatNumber;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getDelNumber() {
		return delNumber;
	}

	public void setDelNumber(Integer delNumber) {
		this.delNumber = delNumber;
	}

	public Integer getTiaoNumber() {
		return tiaoNumber;
	}

	public void setTiaoNumber(Integer tiaoNumber) {
		this.tiaoNumber = tiaoNumber;
	}

	public Integer getLockMatNumber() {
		return lockMatNumber;
	}

	public void setLockMatNumber(Integer lockMatNumber) {
		this.lockMatNumber = lockMatNumber;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setMrpId(Integer mrpId) 
	{
		this.mrpId = mrpId;
	}

	public Integer getMrpId() 
	{
		return mrpId;
	}
	public void setPurId(Integer purId) 
	{
		this.purId = purId;
	}

	public Integer getPurId() 
	{
		return purId;
	}
	public void setPurDetailId(Integer purDetailId) 
	{
		this.purDetailId = purDetailId;
	}

	public Integer getPurDetailId() 
	{
		return purDetailId;
	}

	@Override
	public String toString() {
		return "MrpPurchase{" +
				"id=" + id +
				", mrpId=" + mrpId +
				", purId=" + purId +
				", purDetailId=" + purDetailId +
				", delNumber=" + delNumber +
				", tiaoNumber=" + tiaoNumber +
				", totalNumber=" + totalNumber +
				", lockMatNumber=" + lockMatNumber +
				", initLockMatNumber=" + initLockMatNumber +
				'}';
	}
}
