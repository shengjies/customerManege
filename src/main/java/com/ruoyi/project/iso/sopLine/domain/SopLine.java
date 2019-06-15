package com.ruoyi.project.iso.sopLine.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 作业指导书  产线 配置表 tab_sop_line
 * 
 * @author sj
 * @date 2019-06-15
 */
public class SopLine extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 作业指定数id，文件夹 */
	private Integer sopId;
	/** 产线id */
	private Integer lineId;
	/** 产品id */
	private Integer pnId;
	/** 产品编码 */
	private String pnCode;
	/** 修改者id */
	private Integer cId;
	/** 修改时间 */
	private Date cTime;

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
	public void setSopId(Integer sopId) 
	{
		this.sopId = sopId;
	}

	public Integer getSopId() 
	{
		return sopId;
	}
	public void setLineId(Integer lineId) 
	{
		this.lineId = lineId;
	}

	public Integer getLineId() 
	{
		return lineId;
	}
	public void setPnId(Integer pnId) 
	{
		this.pnId = pnId;
	}

	public Integer getPnId() 
	{
		return pnId;
	}
	public void setPnCode(String pnCode) 
	{
		this.pnCode = pnCode;
	}

	public String getPnCode() 
	{
		return pnCode;
	}
	public void setCId(Integer cId) 
	{
		this.cId = cId;
	}

	public Integer getCId() 
	{
		return cId;
	}
	public void setCTime(Date cTime) 
	{
		this.cTime = cTime;
	}

	public Date getCTime() 
	{
		return cTime;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("sopId", getSopId())
            .append("lineId", getLineId())
            .append("pnId", getPnId())
            .append("pnCode", getPnCode())
            .append("cId", getCId())
            .append("cTime", getCTime())
            .toString();
    }
}
