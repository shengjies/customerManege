package com.ruoyi.project.page.pageInfo.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

public class PageTem extends BaseEntity {
    private String lineName;//产线名称
    private int standardNum;//标准产量
    private int inputNum ;//投入数量
    private int outputNum;//产出数量
    private float achievementRate;//达成率
    private String productCode;//产品编码
    private String workCode;//
    private int lineManual;//是否为手动 1、是 0、否
    private int workStatus;//工单操作状态
    private int workNum;//工单数量
    private int number;//用工人数
    private int ex;//异常情况 0、不存在 1、存在
    private String personLiable;//责任人

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getStandardNum() {
        return standardNum;
    }

    public void setStandardNum(int standardNum) {
        this.standardNum = standardNum;
    }

    public int getInputNum() {
        return inputNum;
    }

    public void setInputNum(int inputNum) {
        this.inputNum = inputNum;
    }

    public int getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(int outputNum) {
        this.outputNum = outputNum;
    }

    public float getAchievementRate() {
        return achievementRate;
    }

    public void setAchievementRate(float achievementRate) {
        this.achievementRate = achievementRate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public int getWorkNum() {
        return workNum;
    }

    public void setWorkNum(int workNum) {
        this.workNum = workNum;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPersonLiable() {
        return personLiable;
    }

    public void setPersonLiable(String personLiable) {
        this.personLiable = personLiable;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public int getLineManual() {
        return lineManual;
    }

    public void setLineManual(int lineManual) {
        this.lineManual = lineManual;
    }

    public int getEx() {
        return ex;
    }

    public void setEx(int ex) {
        this.ex = ex;
    }
}
