package com.ruoyi.project.production.report.domain;

import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.system.user.domain.User;

import java.util.List;

/**
 * app端实体封装
 * @Author: Rainey
 * @Date: 2019/8/6 14:17
 * @Version: 1.0
 **/
public class AppReport extends BaseEntity {
    private Integer uid; // app在线用户id
    private String devCode; //设备编号
    private String devType; //设备类型
    private Integer reportTag; // 报表保存下载标记 0 保存，1 下载
    /**
     * 导出报表参数
     */
    private Integer lineId; // 产线或者车间id
    private Integer wlSign; // 流水线车间标记 0、流水线，1、车间
    private String productCode; // 产品半成品编码
    private Integer userId; // 计件人id
    private String startTime; // 开始时间
    private String endTime; // 结束时间
    private String fileName; // pdf文件名

    public Integer getReportTag() {
        return reportTag;
    }

    public void setReportTag(Integer reportTag) {
        this.reportTag = reportTag;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 产线集合
     */
    private List<ProductionLine> lineList;
    /**
     * 车间列表
     */
    private List<SingleWork> singleWorkList;
    /**
     * 产品列表
     */
    private List<DevProductList> productList;
    /**
     * 用户列表
     */
    private List<User> userList;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public Integer getWlSign() {
        return wlSign;
    }

    public void setWlSign(Integer wlSign) {
        this.wlSign = wlSign;
    }

    public List<ProductionLine> getLineList() {
        return lineList;
    }

    public void setLineList(List<ProductionLine> lineList) {
        this.lineList = lineList;
    }

    public List<SingleWork> getSingleWorkList() {
        return singleWorkList;
    }

    public void setSingleWorkList(List<SingleWork> singleWorkList) {
        this.singleWorkList = singleWorkList;
    }

    public List<DevProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<DevProductList> productList) {
        this.productList = productList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "AppReport{" +
                "uid=" + uid +
                ", devCode='" + devCode + '\'' +
                ", devType='" + devType + '\'' +
                ", lineId=" + lineId +
                ", wlSign=" + wlSign +
                ", productCode='" + productCode + '\'' +
                ", userId=" + userId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", lineList=" + lineList +
                ", singleWorkList=" + singleWorkList +
                ", productList=" + productList +
                ", userList=" + userList +
                '}';
    }
}
