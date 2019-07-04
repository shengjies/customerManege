package com.ruoyi.project.production.singleWork.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 单工位数据表 tab_single_work
 * 
 * @author sj
 * @date 2019-07-03
 */
public class SingleWork extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 单工位主键id */
	private Integer id;
	/** 公司主键id */
	private Integer companyId;
	/** 车间名称 */
	private String workshopName;
	/** 设备主键id */
	private Integer imId;
	/** 设备名称 */
	private String imCode;
	/** 标记(0、车间 1、设备) */
	private Integer sign;
	/** 责任人1(如果是车间则是责任人，是设备则是员工) */
	private Integer liableOne;
	/** 责任人1名称 */
	private String liableOneName;
	/** 责任人2 */
	private Integer liableTwo;
	private String liableTwoName;
	/** 硬件id(默认是0、未配置) */
	private Integer devId;
	/** 硬件编号 */
	private String devCode;
	/**  */
	private Date cTime;
	/** 上一级id(父id) */
	private Integer parentId;

	public String getLiableTwoName() {
		return liableTwoName;
	}

	public void setLiableTwoName(String liableTwoName) {
		this.liableTwoName = liableTwoName;
	}

	public String getImCode() {
		return imCode;
	}

	public void setImCode(String imCode) {
		this.imCode = imCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getWorkshopName() {
		return workshopName;
	}

	public void setWorkshopName(String workshopName) {
		this.workshopName = workshopName;
	}

	public Integer getImId() {
		return imId;
	}

	public void setImId(Integer imId) {
		this.imId = imId;
	}

	public Integer getSign() {
		return sign;
	}

	public void setSign(Integer sign) {
		this.sign = sign;
	}

	public Integer getLiableOne() {
		return liableOne;
	}

	public void setLiableOne(Integer liableOne) {
		this.liableOne = liableOne;
	}

	public String getLiableOneName() {
		return liableOneName;
	}

	public void setLiableOneName(String liableOneName) {
		this.liableOneName = liableOneName;
	}

	public Integer getLiableTwo() {
		return liableTwo;
	}

	public void setLiableTwo(Integer liableTwo) {
		this.liableTwo = liableTwo;
	}

	public Integer getDevId() {
		return devId;
	}

	public void setDevId(Integer devId) {
		this.devId = devId;
	}

	public String getDevCode() {
		return devCode;
	}

	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "SingleWork{" +
				"id=" + id +
				", companyId=" + companyId +
				", workshopName='" + workshopName + '\'' +
				", imId=" + imId +
				", sign=" + sign +
				", liableOne=" + liableOne +
				", liableOneName='" + liableOneName + '\'' +
				", liableTwo=" + liableTwo +
				", devId=" + devId +
				", devCode='" + devCode + '\'' +
				", cTime=" + cTime +
				", parentId=" + parentId +
				'}';
	}
}
