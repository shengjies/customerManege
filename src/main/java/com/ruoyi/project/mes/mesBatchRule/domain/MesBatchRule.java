package com.ruoyi.project.mes.mesBatchRule.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * MES批准追踪规则表 tab_mes_batch_rule
 * 
 * @author sj
 * @date 2019-07-22
 */
public class MesBatchRule extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键，自增长 */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 规则名称 */
	private String ruleName;
	/** 类型区分 */
	private Integer pType;
	/** 物料编码 */
	private String materiels;
	/** 创建时间 */
	private Date cTime;
	/** 修改时间 */
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
	public void setRuleName(String ruleName) 
	{
		this.ruleName = ruleName;
	}

	public String getRuleName() 
	{
		return ruleName;
	}
	public void setPType(Integer pType) 
	{
		this.pType = pType;
	}

	public Integer getPType() 
	{
		return pType;
	}
	public void setMateriels(String materiels) 
	{
		this.materiels = materiels;
	}

	public String getMateriels() 
	{
		return materiels;
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
            .append("ruleName", getRuleName())
            .append("pType", getPType())
            .append("materiels", getMateriels())
            .append("cTime", getCTime())
            .append("uTime", getUTime())
            .toString();
    }
}
