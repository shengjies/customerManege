package com.ruoyi.project.mes.mesBatch.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * MES批准追踪详情表 tab_mes_batch_detail
 * 
 * @author sj
 * @date 2019-07-22
 */
public class MesBatchDetail extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键，自增长 */
	private Integer id;
	/** 追踪id */
	private Integer bId;
	/** 物料编码 */
	private String materielCode;
	/** 批次号 */
	private String batchCode;
	/** 创建时间 */
	private Date cTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getbId() {
		return bId;
	}

	public void setbId(Integer bId) {
		this.bId = bId;
	}

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	@Override
	public String toString() {
		return "MesBatchDetail{" +
				"id=" + id +
				", bId=" + bId +
				", materielCode='" + materielCode + '\'' +
				", batchCode='" + batchCode + '\'' +
				", cTime=" + cTime +
				'}';
	}
}
