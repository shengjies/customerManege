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
	private Integer id;
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
	/** 类别 0、普通文件 1、SOP 2、ECN 3、DCN */
	private Integer category;
	/** 公司id */
	private Integer companyId;
	/** 父类别id */
	private Integer pId;
	/** 创建者id */
	private Integer cId;
	/** 创建时间 */
	private Date cTime;
	/** 是否可以删除 0、不能 1、可以 */
	private Integer defId;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setCName(String cName) 
	{
		this.cName = cName;
	}

	public String getCName() 
	{
		return cName;
	}
	public void setEName(String eName) 
	{
		this.eName = eName;
	}

	public String getEName() 
	{
		return eName;
	}
	public void setDisk(String disk) 
	{
		this.disk = disk;
	}

	public String getDisk() 
	{
		return disk;
	}
	public void setDiskPath(String diskPath) 
	{
		this.diskPath = diskPath;
	}

	public String getDiskPath() 
	{
		return diskPath;
	}
	public void setPath(String path) 
	{
		this.path = path;
	}

	public String getPath() 
	{
		return path;
	}
	public void setIType(Integer iType) 
	{
		this.iType = iType;
	}

	public Integer getIType() 
	{
		return iType;
	}
	public void setCategory(Integer category) 
	{
		this.category = category;
	}

	public Integer getCategory() 
	{
		return category;
	}
	public void setCompanyId(Integer companyId) 
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId() 
	{
		return companyId;
	}
	public void setPId(Integer pId) 
	{
		this.pId = pId;
	}

	public Integer getPId() 
	{
		return pId;
	}
	public void setCId(Integer cId) 
	{
		this.cId = cId;
	}

	public Integer getCId() 
	{
		return cId;
	}
	public void setCTime(Date cTime) 
	{
		this.cTime = cTime;
	}

	public Date getCTime() 
	{
		return cTime;
	}
	public void setDefId(Integer defId) 
	{
		this.defId = defId;
	}

	public Integer getDefId() 
	{
		return defId;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cName", getCName())
            .append("eName", getEName())
            .append("disk", getDisk())
            .append("diskPath", getDiskPath())
            .append("path", getPath())
            .append("iType", getIType())
            .append("category", getCategory())
            .append("companyId", getCompanyId())
            .append("pId", getPId())
            .append("cId", getCId())
            .append("cTime", getCTime())
            .append("defId", getDefId())
            .toString();
    }
}
