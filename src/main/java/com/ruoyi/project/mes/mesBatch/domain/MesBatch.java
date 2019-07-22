package com.ruoyi.project.mes.mesBatch.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * MES批准追踪表 tab_mes_batch
 * 
 * @author sj
 * @date 2019-07-22
 */
public class MesBatch extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键，自增长 */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 主码 */
	private String mesCode;
	/** 工单号 */
	private String workCode;
	/** 订单号 */
	private String orderCode;
	/** 规则id */
	private Integer ruleId;
	/** 规则名称 */
	private String ruleName;
	/** 规则物料 */
	private String ruleMateriel;
	/** 创建时间 */
	private Date cTime;
	/**  */
	private Date uTime;

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
	public void setMesCode(String mesCode) 
	{
		this.mesCode = mesCode;
	}

	public String getMesCode() 
	{
		return mesCode;
	}
	public void setWorkCode(String workCode) 
	{
		this.workCode = workCode;
	}

	public String getWorkCode() 
	{
		return workCode;
	}
	public void setOrderCode(String orderCode) 
	{
		this.orderCode = orderCode;
	}

	public String getOrderCode() 
	{
		return orderCode;
	}
	public void setRuleId(Integer ruleId) 
	{
		this.ruleId = ruleId;
	}

	public Integer getRuleId() 
	{
		return ruleId;
	}
	public void setRuleName(String ruleName) 
	{
		this.ruleName = ruleName;
	}

	public String getRuleName() 
	{
		return ruleName;
	}
	public void setRuleMateriel(String ruleMateriel) 
	{
		this.ruleMateriel = ruleMateriel;
	}

	public String getRuleMateriel() 
	{
		return ruleMateriel;
	}
	public void setCTime(Date cTime) 
	{
		this.cTime = cTime;
	}

	public Date getCTime() 
	{
		return cTime;
	}
	public void setUTime(Date uTime) 
	{
		this.uTime = uTime;
	}

	public Date getUTime() 
	{
		return uTime;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("mesCode", getMesCode())
            .append("workCode", getWorkCode())
            .append("orderCode", getOrderCode())
            .append("ruleId", getRuleId())
            .append("ruleName", getRuleName())
            .append("ruleMateriel", getRuleMateriel())
            .append("cTime", getCTime())
            .append("uTime", getUTime())
            .toString();
    }
}
