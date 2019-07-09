package com.ruoyi.project.iso.sopLine.domain;

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
	/**
	 * 作业指导书页名
	 */
	private String cName;
	/** 修改者id */
	private Integer cId;
	/** 修改时间 */
	private Date cTime;
	/**
	 * sop配置标记(默认0、流水线，1、车间单工位)
	 */
	private Integer sopTag;

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public Integer getSopTag() {
		return sopTag;
	}

	public void setSopTag(Integer sopTag) {
		this.sopTag = sopTag;
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

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Integer getSopId() {
		return sopId;
	}

	public void setSopId(Integer sopId) {
		this.sopId = sopId;
	}

	public Integer getwId() {
		return wId;
	}

	public void setwId(Integer wId) {
		this.wId = wId;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public Integer getcId() {
		return cId;
	}

	public void setcId(Integer cId) {
		this.cId = cId;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	@Override
	public String toString() {
		return "SopLineWork{" +
				"id=" + id +
				", companyId=" + companyId +
				", lineId=" + lineId +
				", sopId=" + sopId +
				", wId=" + wId +
				", pageId=" + pageId +
				", cId=" + cId +
				", cTime=" + cTime +
				", sopTag=" + sopTag +
				'}';
	}
}
