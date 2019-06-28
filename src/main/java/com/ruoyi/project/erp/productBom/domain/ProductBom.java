package com.ruoyi.project.erp.productBom.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 产品BOM单表 tab_product_bom
 * 
 * @author sj
 * @date 2019-06-24
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

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setCompanyId(Integer companyId) 
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId() 
	{
		return companyId;
	}
	public void setBomCode(String bomCode) 
	{
		this.bomCode = bomCode;
	}

	public String getBomCode() 
	{
		return bomCode;
	}

	public Integer getBomVersion() {
		return bomVersion;
	}

	public void setBomVersion(Integer bomVersion) {
		this.bomVersion = bomVersion;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}

	public Integer getProductId() 
	{
		return productId;
	}
	public void setProductCode(String productCode) 
	{
		this.productCode = productCode;
	}

	public String getProductCode() 
	{
		return productCode;
	}
	public void setProductModel(String productModel) 
	{
		this.productModel = productModel;
	}

	public String getProductModel() 
	{
		return productModel;
	}
	public void setProductName(String productName) 
	{
		this.productName = productName;
	}

	public String getProductName() 
	{
		return productName;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
	}
	public void setCreateId(Integer createId) 
	{
		this.createId = createId;
	}

	public Integer getCreateId() 
	{
		return createId;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("bomCode", getBomCode())
            .append("bomVersion", getBomVersion())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productModel", getProductModel())
            .append("productName", getProductName())
            .append("remark", getRemark())
            .append("createId", getCreateId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
