package com.ruoyi.project.erp.mrp.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * MRP记录表 tab_mrp
 * 
 * @author sj
 * @date 2019-06-24
 */
public class Mrp extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** mrp主键自增长id */
	private Integer mrpId;
	/** 公司id */
	private Integer companyId;
	/** MRP编号 */
	private String mCode;
	/** 物料编号 */
	private String materielCode;
	/** 物料名称 */
	private String materielName;
	/** 物料型号 */
	private String materielModel;
	/** 物料id */
	private Integer materielId;
	/** 供应商id */
	private Integer supplierId;
	/** 订单id */
	private Integer orderId;
	/** 订单编号 */
	private String orderCode;
	/** 产品id */
	private Integer productId;
	/** 产品编号 */
	private String productCode;
	/** 产品名称 */
	private String productName;
	/** 所需数量 */
	private Integer needNumber;
	/** 所差数量 */
	private Integer delNumber;
	/** 调整数量 */
	private Integer tiaoNumber;
	/** 总数(所差数量+调整数量，不显示) */
	private Integer totalNumber;
	/** 状态(0、需采购 1、采购中、2、采购完成 3、不采购) */
	private Integer mStatus;
	/** 锁定的物料库存数量 */
	private Integer lockMatNumber;

	public Integer getMrpId() {
		return mrpId;
	}

	public void setMrpId(Integer mrpId) {
		this.mrpId = mrpId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getmCode() {
		return mCode;
	}

	public void setmCode(String mCode) {
		this.mCode = mCode;
	}

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getMaterielName() {
		return materielName;
	}

	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}

	public String getMaterielModel() {
		return materielModel;
	}

	public void setMaterielModel(String materielModel) {
		this.materielModel = materielModel;
	}

	public Integer getMaterielId() {
		return materielId;
	}

	public void setMaterielId(Integer materielId) {
		this.materielId = materielId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getNeedNumber() {
		return needNumber;
	}

	public void setNeedNumber(Integer needNumber) {
		this.needNumber = needNumber;
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

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getmStatus() {
		return mStatus;
	}

	public void setmStatus(Integer mStatus) {
		this.mStatus = mStatus;
	}

	public Integer getLockMatNumber() {
		return lockMatNumber;
	}

	public void setLockMatNumber(Integer lockMatNumber) {
		this.lockMatNumber = lockMatNumber;
	}

	@Override
	public String toString() {
		return "Mrp{" +
				"mrpId=" + mrpId +
				", companyId=" + companyId +
				", mCode='" + mCode + '\'' +
				", materielCode='" + materielCode + '\'' +
				", materielName='" + materielName + '\'' +
				", materielModel='" + materielModel + '\'' +
				", materielId=" + materielId +
				", supplierId=" + supplierId +
				", orderId=" + orderId +
				", orderCode='" + orderCode + '\'' +
				", productId=" + productId +
				", productCode='" + productCode + '\'' +
				", productName='" + productName + '\'' +
				", needNumber=" + needNumber +
				", delNumber=" + delNumber +
				", tiaoNumber=" + tiaoNumber +
				", totalNumber=" + totalNumber +
				", mStatus=" + mStatus +
				", lockMatNumber=" + lockMatNumber +
				'}';
	}
}
