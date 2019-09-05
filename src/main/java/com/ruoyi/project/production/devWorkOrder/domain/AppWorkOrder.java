package com.ruoyi.project.production.devWorkOrder.domain;

import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.mes.mesBatch.domain.MesBatch;
import com.ruoyi.project.mes.mesBatchRule.domain.MesBatchRuleDetail;

import java.util.List;

/**
 * app端工单封装
 * @Author: Rainey
 * @Date: 2019/8/24 11:11
 * @Version: 1.0
 **/
public class AppWorkOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 工单主键ID */
    private Integer workId;
    /** 工单配置标记 0、仓库配置，1、生产配置 */
    private Integer configTag;
    /** 工单号 */
    private String workCode;
    /** 工单生产状态 0 未开始，1 进行中，2 已完成 */
    private Integer workStatus;
    /** 工单提交状态 0、未提交，1、已提交 */
    private Integer workSign;
    /** 订单号 */
    private String orderCode;
    /** 工单产品 */
    private String productCode;
    /** 产品规则id */
    private Integer ruleId;
    /** mes主码信息 */
    private String mesCode;
    /** MES数据信息 */
    private List<MesBatch> mesDataList;
    /** 生产工单的产品规则列表 */
    private List<MesBatchRuleDetail> mesRulelList;

    public Integer getWorkSign() {
        return workSign;
    }

    public void setWorkSign(Integer workSign) {
        this.workSign = workSign;
    }

    public Integer getConfigTag() {
        return configTag;
    }

    public void setConfigTag(Integer configTag) {
        this.configTag = configTag;
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

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getMesCode() {
        return mesCode;
    }

    public void setMesCode(String mesCode) {
        this.mesCode = mesCode;
    }

    public List<MesBatch> getMesDataList() {
        return mesDataList;
    }

    public void setMesDataList(List<MesBatch> mesDataList) {
        this.mesDataList = mesDataList;
    }

    public List<MesBatchRuleDetail> getMesRulelList() {
        return mesRulelList;
    }

    public void setMesRulelList(List<MesBatchRuleDetail> mesRulelList) {
        this.mesRulelList = mesRulelList;
    }

    @Override
    public String toString() {
        return "AppWorkOrder{" +
                "workId=" + workId +
                ", configTag=" + configTag +
                ", workCode='" + workCode + '\'' +
                ", workStatus=" + workStatus +
                ", workSign=" + workSign +
                ", orderCode='" + orderCode + '\'' +
                ", productCode='" + productCode + '\'' +
                ", ruleId=" + ruleId +
                ", mesCode='" + mesCode + '\'' +
                ", mesDataList=" + mesDataList +
                ", mesRulelList=" + mesRulelList +
                '}';
    }
}
