package com.ruoyi.project.erp.orderDetails.domain;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 订单详情表 tab_order_details
 * 
 * @author zqm
 * @date 2019-05-08
 */
public class OrderDetails extends BaseEntity
{
	/**  */
	private Integer id;
	/**  */
	private Integer orderId;
	/**  */
	private String orderCode;
	/**  */
	private String prodectCode;
	/**
	 * 产品库存数量
	 */
	private Integer productNumber;
	/**  */
	private String productModel;
	/**  */
	private String customerCode;
	/**  */
	private String productName;
	/**  */
	private Float productPrict;
	/**  */
	private Integer number;
	/**  */
	private Float totalPrict;
	/**  */
	private Integer deliverNum;
	/**
	 * 订单明细锁定库存数量
	 */
	private Integer lockNumber;
	/**  */
	private Date createTime;
	/**
	 * 客户主键ID
	 */
	private Integer customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 公司主键ID
	 */
	private Integer companyId;

	private String remark;

	private Integer productId;
	/**
	 * 锁定库存数量
	 */
	private Integer lockMatNumber;
	/**
	 * 物料满足状态</br>
	 * 0、物料充足可生产，1、物料不足不可生产
	 */
	private Integer matStatus;
	/**
	 * 调整数量
	 */
	private Integer tiaoNumber;
	/**
	 * 是否生成mrp状态（0、未生成，1、已生成）
	 */
	private Integer mrpStatus;

	public Integer getMrpStatus() {
		return mrpStatus;
	}

	public void setMrpStatus(Integer mrpStatus) {
		this.mrpStatus = mrpStatus;
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

	public Integer getMatStatus() {
		return matStatus;
	}

	public void setMatStatus(Integer matStatus) {
		this.matStatus = matStatus;
	}

	public Integer getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(Integer productNumber) {
		this.productNumber = productNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getLockNumber() {
		return lockNumber;
	}

	public void setLockNumber(Integer lockNumber) {
		this.lockNumber = lockNumber;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	private int sign;//标记详情 默认为 0


	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setOrderId(Integer orderId) 
	{
		this.orderId = orderId;
	}

	public Integer getOrderId() 
	{
		return orderId;
	}
	public void setOrderCode(String orderCode) 
	{
		this.orderCode = orderCode;
	}

	public String getOrderCode() 
	{
		return orderCode;
	}
	public void setProdectCode(String prodectCode) 
	{
		this.prodectCode = prodectCode;
	}

	public String getProdectCode() 
	{
		return prodectCode;
	}
	public void setProductModel(String productModel) 
	{
		this.productModel = productModel;
	}

	public String getProductModel() 
	{
		return productModel;
	}
	public void setCustomerCode(String customerCode) 
	{
		this.customerCode = customerCode;
	}

	public String getCustomerCode() 
	{
		return customerCode;
	}
	public void setProductName(String productName) 
	{
		this.productName = productName;
	}

	public String getProductName() 
	{
		return productName;
	}
	public void setProductPrict(Float productPrict) 
	{
		this.productPrict = productPrict;
	}

	public Float getProductPrict() 
	{
		return productPrict;
	}
	public void setNumber(Integer number) 
	{
		this.number = number;
	}

	public Integer getNumber() 
	{
		return number;
	}
	public void setTotalPrict(Float totalPrict) 
	{
		this.totalPrict = totalPrict;
	}

	public Float getTotalPrict() 
	{
		return totalPrict;
	}
	public void setDeliverNum(Integer deliverNum) 
	{
		this.deliverNum = deliverNum;
	}

	public Integer getDeliverNum() 
	{
		return deliverNum;
	}
	@Override
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}
	@Override
	public Date getCreateTime() 
	{
		return createTime;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "OrderDetails{" +
				"id=" + id +
				", orderId=" + orderId +
				", orderCode='" + orderCode + '\'' +
				", prodectCode='" + prodectCode + '\'' +
				", productNumber=" + productNumber +
				", productModel='" + productModel + '\'' +
				", customerCode='" + customerCode + '\'' +
				", productName='" + productName + '\'' +
				", productPrict=" + productPrict +
				", number=" + number +
				", totalPrict=" + totalPrict +
				", deliverNum=" + deliverNum +
				", lockNumber=" + lockNumber +
				", createTime=" + createTime +
				", customerId=" + customerId +
				", customerName='" + customerName + '\'' +
				", companyId=" + companyId +
				", remark='" + remark + '\'' +
				", productId=" + productId +
				", lockMatNumber=" + lockMatNumber +
				", matStatus=" + matStatus +
				", tiaoNumber=" + tiaoNumber +
				", sign=" + sign +
				'}';
	}
}
