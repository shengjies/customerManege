package com.ruoyi.project.erp.productBom.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * 产品BOM单表 tab_product_bom
 * 
 * @author sj
 * @date 2019-06-25
 */
public class ProductBom extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 产品BOM单主键ID */
	private Integer id;
	/** 公司主键ID */
	private Integer companyId;
	/** bom单编号 */

	private String bomCode;
	/** bom版本号 */
	private Integer bomVersion;
	/** 产品主键ID */
	private Integer productId;
	/** 产品编码 */

	private String productCode;
	/** 产品型号 */

	private String productModel;
	/** 产品名称 */

	private String productName;
	/** 备注信息 */

	private String remark;
	/** 创建者 */
	private Integer createId;
	/** 创建时间 */

	private Date createTime;
	/** 标记当前最新版本 */
	private Integer sign;
	/** 标记是否审核 */
	private Integer sSign; //0、未审核 1、已审核

	private String ver;
	private String verChange;
	/** BOM详情 */

	private List<ProductBomDetails> details;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getBomCode() {
		return bomCode;
	}


	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}

	public Integer getBomVersion() {
		return bomVersion;
	}

	public void setBomVersion(Integer bomVersion) {
		this.bomVersion = bomVersion;
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

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String getRemark() {
		return StringUtils.isEmpty(remark)?"":remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getSign() {
		return sign;
	}

	public void setSign(Integer sign) {
		this.sign = sign;
	}

	public Integer getsSign() {
		return sSign;
	}

	public void setsSign(Integer sSign) {
		this.sSign = sSign;
	}

	public List<ProductBomDetails> getDetails() {
		return details;
	}

	public void setDetails(List<ProductBomDetails> details) {
		this.details = details;
	}

	public String getVer() {
		return "v"+getBomVersion();
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getVerChange() {
		return verChange;
	}

	public void setVerChange(String verChange) {
		this.verChange = verChange;
	}

	@Override
	public String toString() {
		return "ProductBom{" +
				"id=" + id +
				", companyId=" + companyId +
				", bomCode='" + bomCode + '\'' +
				", bomVersion=" + bomVersion +
				", productId=" + productId +
				", productCode='" + productCode + '\'' +
				", productModel='" + productModel + '\'' +
				", productName='" + productName + '\'' +
				", remark='" + remark + '\'' +
				", createId=" + createId +
				", createTime=" + createTime +
				", sign=" + sign +
				", details=" + details +
				'}';
	}
}
