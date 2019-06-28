package com.ruoyi.project.erp.bomChange.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * BOM更改记录表 tab_bom_change
 * 
 * @author sj
 * @date 2019-06-26
 */
public class BomChange extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键自增长id */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 产品id */
	private Integer pId;
	/** 上一个版本 */
	private Integer oldVersion;
	/** 新版本 */
	private Integer newVersion;
	/** 创建时间 */
	private Date cTime;
	/** 创建者 */
	private Integer cId;
	/** 审核时间 */
	private Date shTime;
	/** 审核人 */
	private Integer shId;
	/** 审核状态(0、未审核 1、已审核) */
	private Integer shStatus;
	/** 修改内容(每行用<br/>隔开) */
	private String changeText;
	/** BOM id */
	private Integer newBomId;
	/**  */
	private Integer oldBomId;

	public Integer getId() {
		return id;
	}

	private String pCode;//产品编码
	private String oldCode;//旧版本BOM编码
	private String newCode;//新版本BOM编码
	private String cUser;//创建者
	private String sUser;//审核者

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public Integer getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(Integer oldVersion) {
		this.oldVersion = oldVersion;
	}

	public Integer getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(Integer newVersion) {
		this.newVersion = newVersion;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Integer getcId() {
		return cId;
	}

	public void setcId(Integer cId) {
		this.cId = cId;
	}

	public Date getShTime() {
		return shTime;
	}

	public void setShTime(Date shTime) {
		this.shTime = shTime;
	}

	public Integer getShId() {
		return shId;
	}

	public void setShId(Integer shId) {
		this.shId = shId;
	}

	public Integer getShStatus() {
		return shStatus;
	}

	public void setShStatus(Integer shStatus) {
		this.shStatus = shStatus;
	}

	public String getChangeText() {
		return changeText;
	}

	public void setChangeText(String changeText) {
		this.changeText = changeText;
	}

	public Integer getNewBomId() {
		return newBomId;
	}

	public void setNewBomId(Integer newBomId) {
		this.newBomId = newBomId;
	}

	public Integer getOldBomId() {
		return oldBomId;
	}

	public void setOldBomId(Integer oldBomId) {
		this.oldBomId = oldBomId;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}

	public String getNewCode() {
		return newCode;
	}

	public void setNewCode(String newCode) {
		this.newCode = newCode;
	}

	public String getcUser() {
		return cUser;
	}

	public void setcUser(String cUser) {
		this.cUser = cUser;
	}

	public String getsUser() {
		return sUser;
	}

	public void setsUser(String sUser) {
		this.sUser = sUser;
	}

	@Override
	public String toString() {
		return "BomChange{" +
				"id=" + id +
				", companyId=" + companyId +
				", pId=" + pId +
				", oldVersion=" + oldVersion +
				", newVersion=" + newVersion +
				", cTime=" + cTime +
				", cId=" + cId +
				", shTime=" + shTime +
				", shId=" + shId +
				", shStatus=" + shStatus +
				", changeText='" + changeText + '\'' +
				", newBomId=" + newBomId +
				", oldBomId=" + oldBomId +
				'}';
	}
}
