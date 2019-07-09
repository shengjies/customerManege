package com.ruoyi.project.erp.productBom.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * BOM导入配置表 tab_bom_config
 * 
 * @author sj
 * @date 2019-06-25
 */
public class BomConfig extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键自增长id */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 物料编码下标 */
	private Integer materielCode;
	/** 物料名称下标 */
	private Integer materielName;
	/** 物料型号下标 */
	private Integer materielModel;
	/** 用量下标 */
	private Integer number;
	/** 单位下标 */
	private Integer unit;
	/** 位号下标 */
	private Integer placeNumber;
	/** 备注下标 */
	private Integer remarkIndex;
	/** 创建时间 */
	private Date createTime;
	/** 从第几行开始解析 */
	private Integer rowIndex;

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
	public void setMaterielCode(Integer materielCode) 
	{
		this.materielCode = materielCode;
	}

	public Integer getMaterielCode() 
	{
		return materielCode;
	}
	public void setMaterielName(Integer materielName) 
	{
		this.materielName = materielName;
	}

	public Integer getMaterielName() 
	{
		return materielName;
	}
	public void setMaterielModel(Integer materielModel) 
	{
		this.materielModel = materielModel;
	}

	public Integer getMaterielModel() 
	{
		return materielModel;
	}
	public void setNumber(Integer number) 
	{
		this.number = number;
	}

	public Integer getNumber() 
	{
		return number;
	}
	public void setUnit(Integer unit) 
	{
		this.unit = unit;
	}

	public Integer getUnit() 
	{
		return unit;
	}
	public void setPlaceNumber(Integer placeNumber) 
	{
		this.placeNumber = placeNumber;
	}

	public Integer getPlaceNumber() 
	{
		return placeNumber;
	}
	public void setRemarkIndex(Integer remarkIndex) 
	{
		this.remarkIndex = remarkIndex;
	}

	public Integer getRemarkIndex() 
	{
		return remarkIndex;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setRowIndex(Integer rowIndex) 
	{
		this.rowIndex = rowIndex;
	}

	public Integer getRowIndex() 
	{
		return rowIndex;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("materielCode", getMaterielCode())
            .append("materielName", getMaterielName())
            .append("materielModel", getMaterielModel())
            .append("number", getNumber())
            .append("unit", getUnit())
            .append("placeNumber", getPlaceNumber())
            .append("remarkIndex", getRemarkIndex())
            .append("createTime", getCreateTime())
            .append("rowIndex", getRowIndex())
            .toString();
    }
}
