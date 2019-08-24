package com.ruoyi.project.insmanage.instrumentManage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 仪器设备管理表 tab_instrument_manage
 * 
 * @author sj
 * @date 2019-06-19
 */
public class InstrumentManage extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 公司主键id */
	private Integer companyId;
	/** 仪器编号 */
	@Excel(name = "仪器编号",type= Excel.Type.ALL)
	private String imCode;
	/** 仪器名称 */
	@Excel(name = "仪器名称",type= Excel.Type.ALL)
	private String imName;
	/** 仪器型号 */
	@Excel(name = "仪器型号",type= Excel.Type.ALL)
	private String imModel;
	/**
	 * 仪器设备的类型id
	 */
	private Integer inType;
	/**
	 * 仪器设备名称
	 */
	@Excel(name ="设备类型",type = Excel.Type.ALL)
	private String inTypeName;
	/** 使用部门 */
	@Excel(name = "使用部门",type= Excel.Type.ALL)
	private String imDepartment;
	/** 设备责任人 */
	private Integer imLiable;
	/** 设备责任人名称 */
	@Excel(name = "责任人",type= Excel.Type.EXPORT)
	private String imLiableName;
	/** 设备状态(0、停用，1、启用) */
	private String imStatus;
	/** 校验证书 */
	@Excel(name = "校验证书",type= Excel.Type.ALL)
	private String imCheckBook;
	/** 校验证书号码 */
	@Excel(name = "校验证书号码",type= Excel.Type.ALL)
	private String imCheckNumber;

	private String imCheckPeriod;
	/** 有效期开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date imStartDate;
	/** 有效期结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date imEndDate;
	/** 校验机构 */
	@Excel(name = "校验机构",type= Excel.Type.ALL)
	private String imCheckMechanism;
	/** 供应商 */
	@Excel(name = "供应商",type= Excel.Type.ALL)
	private String imSupplier;
	/** 启用日期 */
	private Date imStartTime;
	/** 创建者 */
	private Integer createId;
	/** 创建时间 */
	private Date createTime;
	/** 设备标记状态(0、未配置过，1、配置过了) */
	private Integer imTag;

	public Date getImStartDate() {
		return imStartDate;
	}

	public void setImStartDate(Date imStartDate) {
		this.imStartDate = imStartDate;
	}

	public Date getImEndDate() {
		return imEndDate;
	}

	public void setImEndDate(Date imEndDate) {
		this.imEndDate = imEndDate;
	}

	public Integer getImTag() {
		return imTag;
	}

	public void setImTag(Integer imTag) {
		this.imTag = imTag;
	}

	public Integer getInType() {
		return inType;
	}

	public void setInType(Integer inType) {
		this.inType = inType;
	}

	public String getInTypeName() {
		return inTypeName;
	}

	public void setInTypeName(String inTypeName) {
		this.inTypeName = inTypeName;
	}

	public String getImLiableName() {
		return imLiableName;
	}

	public void setImLiableName(String imLiableName) {
		this.imLiableName = imLiableName;
	}

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
	public void setImCode(String imCode) 
	{
		this.imCode = imCode;
	}

	public String getImCode() 
	{
		return imCode;
	}
	public void setImName(String imName) 
	{
		this.imName = imName;
	}

	public String getImName() 
	{
		return imName;
	}
	public void setImModel(String imModel) 
	{
		this.imModel = imModel;
	}

	public String getImModel() 
	{
		return imModel;
	}
	public void setImDepartment(String imDepartment) 
	{
		this.imDepartment = imDepartment;
	}

	public String getImDepartment() 
	{
		return imDepartment;
	}
	public void setImLiable(Integer imLiable) 
	{
		this.imLiable = imLiable;
	}

	public Integer getImLiable() 
	{
		return imLiable;
	}
	public void setImStatus(String imStatus) 
	{
		this.imStatus = imStatus;
	}

	public String getImStatus() 
	{
		return imStatus;
	}
	public void setImCheckNumber(String imCheckNumber) 
	{
		this.imCheckNumber = imCheckNumber;
	}

	public String getImCheckNumber() 
	{
		return imCheckNumber;
	}
	public void setImCheckPeriod(String imCheckPeriod) 
	{
		this.imCheckPeriod = imCheckPeriod;
	}

	public String getImCheckPeriod() 
	{
		return imCheckPeriod;
	}
	public void setImCheckBook(String imCheckBook) 
	{
		this.imCheckBook = imCheckBook;
	}

	public String getImCheckBook() 
	{
		return imCheckBook;
	}
	public void setImCheckMechanism(String imCheckMechanism) 
	{
		this.imCheckMechanism = imCheckMechanism;
	}

	public String getImCheckMechanism() 
	{
		return imCheckMechanism;
	}
	public void setImSupplier(String imSupplier) 
	{
		this.imSupplier = imSupplier;
	}

	public String getImSupplier() 
	{
		return imSupplier;
	}
	public void setImStartTime(Date imStartTime) 
	{
		this.imStartTime = imStartTime;
	}

	public Date getImStartTime() 
	{
		return imStartTime;
	}
	public void setCreateId(Integer createId) 
	{
		this.createId = createId;
	}

	public Integer getCreateId() 
	{
		return createId;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}

	@Override
	public String toString() {
		return "InstrumentManage{" +
				"id=" + id +
				", companyId=" + companyId +
				", imCode='" + imCode + '\'' +
				", imName='" + imName + '\'' +
				", imModel='" + imModel + '\'' +
				", inType=" + inType +
				", imDepartment='" + imDepartment + '\'' +
				", imLiable=" + imLiable +
				", imLiableName='" + imLiableName + '\'' +
				", imStatus='" + imStatus + '\'' +
				", imCheckNumber='" + imCheckNumber + '\'' +
				", imCheckPeriod='" + imCheckPeriod + '\'' +
				", imCheckBook='" + imCheckBook + '\'' +
				", imCheckMechanism='" + imCheckMechanism + '\'' +
				", imSupplier='" + imSupplier + '\'' +
				", imStartTime=" + imStartTime +
				", createId=" + createId +
				", createTime=" + createTime +
				'}';
	}
}
