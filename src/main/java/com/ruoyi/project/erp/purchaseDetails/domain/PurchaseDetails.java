package com.ruoyi.project.erp.purchaseDetails.domain;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 采购清单表 tab_purchase_details
 * 
 * @author zqm
 * @date 2019-05-10
 */
public class PurchaseDetails extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键，自增长 */
	private Integer id;
	/** 采购id */
	private Integer purchaseId;
	/** 采购单号 */
	private String purchaseCode;
	/** 物料编码 */
	private String materielCode;
	/** 物料名称 */
	private String materielName;
	/** 物料型号 */
	private String materielModel;
	/** 单价(含税) */
	private Float price;
	/** 供应商编号 */
	private String supplierCode;
	/** 数量 */
	private Integer number;
	/** 交付数量 */
	private Integer deliverNum;
	/**
	 * 仓库预收数量
	 */
	private Integer prereceiveNumber;
	/** 合计 */
	private Float totalPrict;
	/**  */
	private String remark;
	/** moq */
	private Integer moq;

	private Integer supplierId;
	//用于标记 默认未 0
	private int sign;

	private Integer companyId;
	/** 封装mrp主键自增长id */
	private Integer mrpId;
	/** 封装mrp物料id */
	private Integer materielId;
	/** 封装mrp所差数量 */
	private Integer delNumber;
	/** 封装mrp调整数量 */
	private Integer tiaoNumber;
	/** 封装mrp总数(所差数量+调整数量，不显示) */
	private Integer totalNumber;
	/** 封装mrp锁定的物料库存数量 */
	private Integer lockMatNumber;

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getLockMatNumber() {
		return lockMatNumber;
	}

	public void setLockMatNumber(Integer lockMatNumber) {
		this.lockMatNumber = lockMatNumber;
	}

	public Integer getMrpId() {
		return mrpId;
	}

	public void setMrpId(Integer mrpId) {
		this.mrpId = mrpId;
	}

	public Integer getMaterielId() {
		return materielId;
	}

	public void setMaterielId(Integer materielId) {
		this.materielId = materielId;
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

	public Integer getPrereceiveNumber() {
		return prereceiveNumber;
	}

	public void setPrereceiveNumber(Integer prereceiveNumber) {
		this.prereceiveNumber = prereceiveNumber;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setPurchaseId(Integer purchaseId) 
	{
		this.purchaseId = purchaseId;
	}

	public Integer getPurchaseId() 
	{
		return purchaseId;
	}
	public void setMaterielCode(String materielCode) 
	{
		this.materielCode = materielCode;
	}

	public String getMaterielCode() 
	{
		return materielCode;
	}
	public void setMaterielName(String materielName) 
	{
		this.materielName = materielName;
	}

	public String getMaterielName() 
	{
		return materielName;
	}
	public void setMaterielModel(String materielModel) 
	{
		this.materielModel = materielModel;
	}

	public String getMaterielModel() 
	{
		return materielModel;
	}
	public void setPrice(Float price) 
	{
		this.price = price;
	}

	public Float getPrice() 
	{
		return price;
	}
	public void setSupplierCode(String supplierCode) 
	{
		this.supplierCode = supplierCode;
	}

	public String getSupplierCode() 
	{
		return supplierCode;
	}
	public void setNumber(Integer number) 
	{
		this.number = number;
	}

	public Integer getNumber() 
	{
		return number;
	}
	public void setDeliverNum(Integer deliverNum) 
	{
		this.deliverNum = deliverNum;
	}

	public Integer getDeliverNum() 
	{
		return deliverNum;
	}
	public void setTotalPrict(Float totalPrict) 
	{
		this.totalPrict = totalPrict;
	}

	public Float getTotalPrict() 
	{
		return totalPrict;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
	}

	public Integer getMoq() {
		return moq;
	}

	public void setMoq(Integer moq) {
		this.moq = moq;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	public String toString() {
		return "PurchaseDetails{" +
				"id=" + id +
				", purchaseId=" + purchaseId +
				", purchaseCode='" + purchaseCode + '\'' +
				", materielCode='" + materielCode + '\'' +
				", materielName='" + materielName + '\'' +
				", materielModel='" + materielModel + '\'' +
				", price=" + price +
				", supplierCode='" + supplierCode + '\'' +
				", number=" + number +
				", deliverNum=" + deliverNum +
				", prereceiveNumber=" + prereceiveNumber +
				", totalPrict=" + totalPrict +
				", remark='" + remark + '\'' +
				", moq=" + moq +
				", supplierId=" + supplierId +
				", sign=" + sign +
				", companyId=" + companyId +
				", mrpId=" + mrpId +
				", materielId=" + materielId +
				", delNumber=" + delNumber +
				", tiaoNumber=" + tiaoNumber +
				", totalNumber=" + totalNumber +
				", lockMatNumber=" + lockMatNumber +
				'}';
	}
}
