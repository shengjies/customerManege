package com.ruoyi.project.production.singleWork.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 单工位与工单进行配置表 tab_single_work_order
 * 
 * @author sj
 * @date 2019-07-09
 */
public class SingleWorkOrder extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 工单id */
	private Integer workId;
	/** 工单编号 */
	private String workCode;
	/** 单工位id */
	private Integer singleId;
	/** 父级id */
	private Integer singleP;
	/** 配置时间 */
	private Date cTime;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setWorkId(Integer workId) 
	{
		this.workId = workId;
	}

	public Integer getWorkId() 
	{
		return workId;
	}
	public void setWorkCode(String workCode) 
	{
		this.workCode = workCode;
	}

	public String getWorkCode() 
	{
		return workCode;
	}
	public void setSingleId(Integer singleId) 
	{
		this.singleId = singleId;
	}

	public Integer getSingleId() 
	{
		return singleId;
	}
	public void setSingleP(Integer singleP) 
	{
		this.singleP = singleP;
	}

	public Integer getSingleP() 
	{
		return singleP;
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
            .append("workId", getWorkId())
            .append("workCode", getWorkCode())
            .append("singleId", getSingleId())
            .append("singleP", getSingleP())
            .append("cTime", getCTime())
            .toString();
    }
}
