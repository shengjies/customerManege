package com.ruoyi.project.iso.iso.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 文件管理表 tab_iso
 * 
 * @author sj
 * @date 2019-06-13
 */
public class Iso extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer isoId;
	/** 中文名称 */
	private String cName;
	/** 英文名称 */
	private String eName;
	/** 文件夹路径 */
	private String disk;
	/** 访问拼接地址 */
	private String diskPath;
	/** 访问路径 */
	private String path;
	/** 文件类型，0、文件夹 1、文件 */
	private Integer iType;
	/** 文件大小 */
	private String fileSize;
	/** 类别 0、普通文件 1、SOP 2、ECN 3、DCN */
	private Integer category;
	/** 公司id */
	private Integer companyId;
	/** 父类别id */
	private Integer parentId;
	/** 祖父类id */
	private Integer grParentId;
	/** 创建者id */
	private Integer cId;
	/** 最后更新时间 */
	private Date cTime;
	/** 是否可以删除 0、不能 1、可以 */
	private Integer defId;

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getGrParentId() {
		return grParentId;
	}

	public void setGrParentId(Integer grParentId) {
		this.grParentId = grParentId;
	}

	public Integer getIsoId() {
		return isoId;
	}

	public void setIsoId(Integer isoId) {
		this.isoId = isoId;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String getDisk() {
		return disk;
	}

	public void setDisk(String disk) {
		this.disk = disk;
	}

	public String getDiskPath() {
		return diskPath;
	}

	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getiType() {
		return iType;
	}

	public void setiType(Integer iType) {
		this.iType = iType;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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

	public Integer getDefId() {
		return defId;
	}

	public void setDefId(Integer defId) {
		this.defId = defId;
	}

	@Override
	public String toString() {
		return "Iso{" +
				"isoId=" + isoId +
				", cName='" + cName + '\'' +
				", eName='" + eName + '\'' +
				", disk='" + disk + '\'' +
				", diskPath='" + diskPath + '\'' +
				", path='" + path + '\'' +
				", iType=" + iType +
				", fileSize='" + fileSize + '\'' +
				", category=" + category +
				", companyId=" + companyId +
				", parentId=" + parentId +
				", grParentId=" + grParentId +
				", cId=" + cId +
				", cTime=" + cTime +
				", defId=" + defId +
				'}';
	}
}
