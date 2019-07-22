package com.ruoyi.project.mes.mesBatch.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * MES批准追踪详情表 tab_mes_batch_detail
 * 
 * @author sj
 * @date 2019-07-22
 */
public class MesBatchDetail extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键，自增长 */
	private Integer id;
	/** 追踪id */
	private Integer bId;
	/** 物料编码 */
	private String materielCode;
	/** 批次号 */
	private String batchCode;
	/** 创建时间 */
	private Date cTime;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setBId(Integer bId) 
	{
		this.bId = bId;
	}

	public Integer getBId() 
	{
		return bId;
	}
	public void setMaterielCode(String materielCode) 
	{
		this.materielCode = materielCode;
	}

	public String getMaterielCode() 
	{
		return materielCode;
	}
	public void setBatchCode(String batchCode) 
	{
		this.batchCode = batchCode;
	}

	public String getBatchCode() 
	{
		return batchCode;
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
            .append("bId", getBId())
            .append("materielCode", getMaterielCode())
            .append("batchCode", getBatchCode())
            .append("cTime", getCTime())
            .toString();
    }
}
