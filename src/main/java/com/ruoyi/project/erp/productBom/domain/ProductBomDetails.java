package com.ruoyi.project.erp.productBom.domain;

import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品BOM清单表 tab_product_bom_details
 * 
 * @author sj
 * @date 2019-06-25
 */
public class ProductBomDetails extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 产品BOM清单主键ID */
	private Integer id;
	/** 产品BOM单编号 */
	private Integer bomId;
	/** BOM类型,0、物料，1、半成品 */
	private Integer bomDetailsType;
	/** 物料/半成品id */
	private Integer detailId;
	/** 物料/半成品编码 */
	private String detailCode;
	/** 物料/半成品型号 */
	private String detailModel;
	/** 物料/半成品名称 */
	private String detailName;
	/** 单次数量 */
	private Integer oneNum;
	/** 单价(含税),保留两位，引用相关物料/半成品的含税单价 */
	private BigDecimal price;
	/** 总价(含税)，保留两位 */
	private BigDecimal totalPrice;
	/** 创建者 */
	private Integer createId;
	/** 创建时间 */
	private Date createTime;
	/** 位号 */
	private String placeNumber;
	/** 单位 */
	private String unit;
	/** 备注信息 */
	private String remark;
	/**  */
	private Integer bIndex;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBomId() {
		return bomId;
	}

	public void setBomId(Integer bomId) {
		this.bomId = bomId;
	}

	public Integer getBomDetailsType() {
		return bomDetailsType;
	}

	public void setBomDetailsType(Integer bomDetailsType) {
		this.bomDetailsType = bomDetailsType;
	}

	public Integer getDetailId() {
		return detailId;
	}

	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}

	public String getDetailCode() {
		return detailCode;
	}

	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}

	public String getDetailModel() {
		return detailModel;
	}

	public void setDetailModel(String detailModel) {
		this.detailModel = detailModel;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public Integer getOneNum() {
		return oneNum;
	}

	public void setOneNum(Integer oneNum) {
		this.oneNum = oneNum;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPlaceNumber() {
		return StringUtils.isEmpty(placeNumber)?"":placeNumber;
	}

	public void setPlaceNumber(String placeNumber) {
		this.placeNumber = placeNumber;
	}

	public String getUnit() {
		return StringUtils.isEmpty(unit)?"":unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String getRemark() {
		return StringUtils.isEmpty(remark)?"":remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getbIndex() {
		return bIndex;
	}

	public void setbIndex(Integer bIndex) {
		this.bIndex = bIndex;
	}

	@Override
	public String toString() {
		return "ProductBomDetails{" +
				"id=" + id +
				", bomId=" + bomId +
				", bomDetailsType=" + bomDetailsType +
				", detailId=" + detailId +
				", detailCode='" + detailCode + '\'' +
				", detailModel='" + detailModel + '\'' +
				", detailName='" + detailName + '\'' +
				", oneNum=" + oneNum +
				", price=" + price +
				", totalPrice=" + totalPrice +
				", createId=" + createId +
				", createTime=" + createTime +
				", placeNumber='" + placeNumber + '\'' +
				", unit='" + unit + '\'' +
				", remark='" + remark + '\'' +
				", bIndex=" + bIndex +
				'}';
	}
}
