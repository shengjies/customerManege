package com.ruoyi.project.iso.sopLine.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 产线工位配置指导书页表 tab_sop_line_work
 * 
 * @author sj
 * @date 2019-06-15
 */
public class SopLineWork extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 产线id */
	private Integer lineId;
	/** 作业指导书id */
	private Integer sopId;
	/** 工位id */
	private Integer wId;
	/** 作业指导书页id */
	private Integer pageId;
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
	public void setLineId(Integer lineId) 
	{
		this.lineId = lineId;
	}

	public Integer getLineId() 
	{
		return lineId;
	}
	public void setSopId(Integer sopId) 
	{
		this.sopId = sopId;
	}

	public Integer getSopId() 
	{
		return sopId;
	}
	public void setWId(Integer wId) 
	{
		this.wId = wId;
	}

	public Integer getWId() 
	{
		return wId;
	}
	public void setPageId(Integer pageId) 
	{
		this.pageId = pageId;
	}

	public Integer getPageId() 
	{
		return pageId;
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
            .append("lineId", getLineId())
            .append("sopId", getSopId())
            .append("wId", getWId())
            .append("pageId", getPageId())
            .append("cId", getCId())
            .append("cTime", getCTime())
            .toString();
    }
}
