package com.ruoyi.project.iso.sop.domain;

import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.iso.sopLine.domain.SopLine;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * SOP配置主表 tab_sop
 * 
 * @author sj
 * @date 2019-08-30
 */
public class Sop extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 配置名称 */
	private String cnfName;
	/** 产线id */
	private Integer lineId;
	/** 产线名称 */
	private String lineName;
	/** sop书id */
	private Integer sopId;
	/** sop书名称 */
	private String sopName;
	/** 产品id  */
	private String pId;
	/** 产品编码 */
	private String pCode;
	/** 创建时间 */
	private Date createTime;
	/** sop配置标记(默认0、流水线，1、单工位) */
	private Integer sopTag;
	/** 产品主键id */
	private Integer[] pnsId;
	/** SOP配置明细 */
	private List<SopLine> sopLines;

	public String getSopName() {
		return sopName;
	}

	public void setSopName(String sopName) {
		this.sopName = sopName;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
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

	public String getCnfName() {
		return cnfName;
	}

	public void setCnfName(String cnfName) {
		this.cnfName = cnfName;
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

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer[] getPnsId() {
		return pnsId;
	}

	public void setPnsId(Integer[] pnsId) {
		this.pnsId = pnsId;
	}

	public List<SopLine> getSopLines() {
		return sopLines;
	}

	public void setSopLines(List<SopLine> sopLines) {
		this.sopLines = sopLines;
	}

	@Override
	public String toString() {
		return "Sop{" +
				"id=" + id +
				", companyId=" + companyId +
				", cnfName='" + cnfName + '\'' +
				", lineId=" + lineId +
				", lineName='" + lineName + '\'' +
				", sopId=" + sopId +
				", sopName='" + sopName + '\'' +
				", pId='" + pId + '\'' +
				", pCode='" + pCode + '\'' +
				", createTime=" + createTime +
				", sopTag=" + sopTag +
				", pnsId=" + Arrays.toString(pnsId) +
				", sopLines=" + sopLines +
				'}';
	}
}
