package com.ruoyi.project.insmanage.instrumentType.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 设备类型表 tab_instrument_type
 * 
 * @author sj
 * @date 2019-07-02
 */
public class InstrumentType extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 设备类型主键id */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 设备类型名称 */
	private String inType;
	/** 创建时间 */
	private Date createTime;
	/** 创建者 */
	private Integer createId;

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
	public void setInType(String inType) 
	{
		this.inType = inType;
	}

	public String getInType() 
	{
		return inType;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setCreateId(Integer createId) 
	{
		this.createId = createId;
	}

	public Integer getCreateId() 
	{
		return createId;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("inType", getInType())
            .append("createTime", getCreateTime())
            .append("createId", getCreateId())
            .toString();
    }
}
