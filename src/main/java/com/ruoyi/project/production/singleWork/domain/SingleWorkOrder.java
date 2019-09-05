package com.ruoyi.project.production.singleWork.domain;

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
	/** 流水线工位极光推送标记 0未更新，1已更新 */
	private Integer jpushTag;

	private int type;

	/****** app单工位工单分配 ********/
	/** app端分配工单时单工位的计数器编码 */
	private String jsCode;

	public Integer getJpushTag() {
		return jpushTag;
	}

	public void setJpushTag(Integer jpushTag) {
		this.jpushTag = jpushTag;
	}

	public String getJsCode() {
		return jsCode;
	}

	public void setJsCode(String jsCode) {
		this.jsCode = jsCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWorkId() {
		return workId;
	}

	public void setWorkId(Integer workId) {
		this.workId = workId;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public Integer getSingleId() {
		return singleId;
	}

	public void setSingleId(Integer singleId) {
		this.singleId = singleId;
	}

	public Integer getSingleP() {
		return singleP;
	}

	public void setSingleP(Integer singleP) {
		this.singleP = singleP;
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

	@Override
	public String toString() {
		return "SingleWorkOrder{" +
				"id=" + id +
				", workId=" + workId +
				", workCode='" + workCode + '\'' +
				", singleId=" + singleId +
				", singleP=" + singleP +
				", cTime=" + cTime +
				", imCode='" + imCode + '\'' +
				", liableOne=" + liableOne +
				", liableOneName='" + liableOneName + '\'' +
				", workorderStatus=" + workorderStatus +
				", productNumber=" + productNumber +
				", type=" + type +
				'}';
	}
}
