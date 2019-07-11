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

	/** 以下用于查询 */

	/** 单工位 */
	private String imCode;
	/** 责任人 */
	private int liableOne;
	/** 责任人名称 */
	private String liableOneName;
	/** 工单状态 */
	private int workorderStatus;
	/** 生产数量 */
	private int productNumber;

	private int type;

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

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public String getImCode() {
		return imCode;
	}

	public void setImCode(String imCode) {
		this.imCode = imCode;
	}

	public int getLiableOne() {
		return liableOne;
	}

	public void setLiableOne(int liableOne) {
		this.liableOne = liableOne;
	}

	public String getLiableOneName() {
		return liableOneName;
	}

	public void setLiableOneName(String liableOneName) {
		this.liableOneName = liableOneName;
	}

	public int getWorkorderStatus() {
		return workorderStatus;
	}

	public void setWorkorderStatus(int workorderStatus) {
		this.workorderStatus = workorderStatus;
	}

	public int getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(int productNumber) {
		this.productNumber = productNumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("workId", getWorkId())
            .append("workCode", getWorkCode())
            .append("singleId", getSingleId())
            .append("singleP", getSingleP())
            .append("cTime", getcTime())
            .toString();
    }
}
