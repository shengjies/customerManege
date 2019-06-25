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
	
	/** 主键自增长id */
	private Integer id;
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
	public void setMCode(String mCode) 
	{
		this.mCode = mCode;
	}

	public String getMCode() 
	{
		return mCode;
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
	public void setMaterielId(Integer materielId) 
	{
		this.materielId = materielId;
	}

	public Integer getMaterielId() 
	{
		return materielId;
	}
	public void setSupplierId(Integer supplierId) 
	{
		this.supplierId = supplierId;
	}

	public Integer getSupplierId() 
	{
		return supplierId;
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
	public void setProductName(String productName) 
	{
		this.productName = productName;
	}

	public String getProductName() 
	{
		return productName;
	}
	public void setNeedNumber(Integer needNumber) 
	{
		this.needNumber = needNumber;
	}

	public Integer getNeedNumber() 
	{
		return needNumber;
	}
	public void setDelNumber(Integer delNumber) 
	{
		this.delNumber = delNumber;
	}

	public Integer getDelNumber() 
	{
		return delNumber;
	}
	public void setTiaoNumber(Integer tiaoNumber) 
	{
		this.tiaoNumber = tiaoNumber;
	}

	public Integer getTiaoNumber() 
	{
		return tiaoNumber;
	}
	public void setTotalNumber(Integer totalNumber) 
	{
		this.totalNumber = totalNumber;
	}

	public Integer getTotalNumber() 
	{
		return totalNumber;
	}
	public void setMStatus(Integer mStatus) 
	{
		this.mStatus = mStatus;
	}

	public Integer getMStatus() 
	{
		return mStatus;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("mCode", getMCode())
            .append("materielCode", getMaterielCode())
            .append("materielName", getMaterielName())
            .append("materielModel", getMaterielModel())
            .append("materielId", getMaterielId())
            .append("supplierId", getSupplierId())
            .append("orderId", getOrderId())
            .append("orderCode", getOrderCode())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("needNumber", getNeedNumber())
            .append("delNumber", getDelNumber())
            .append("tiaoNumber", getTiaoNumber())
            .append("totalNumber", getTotalNumber())
            .append("mStatus", getMStatus())
            .toString();
    }
}
