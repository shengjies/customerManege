package com.ruoyi.project.page.pageInfo.domain;

import com.ruoyi.project.production.workExceptionList.domain.WorkExceptionList;

import java.io.Serializable;
import java.util.List;

/**
 * 看板车间实体类封装
 * @Author: Rainey
 * @Date: 2019/7/9 17:42
 * @Version: 1.0
 **/
public class PageHouse implements Serializable {
    private static final long serialVersionUID = 7766218546762055117L;
    /** 设备责任人id **/
    private Integer liableOneId;
    /** 设备责任人名称 **/
    private String userName;
    /** 计数数量 **/
    private Integer countNum;
    /** 车间id **/
    private Integer parentId;
    /** 工单id **/
    private Integer workId;
    /** 工单编号 **/
    private String workCode;
    /** 工单生产产品编码 **/
    private String productCode;
    /** 工单生产状态 **/
    private Integer workOrderStatus;
    /** 工单操作状态 **/
    private Integer operationStatus;
    /** 工单所属订单号 **/
    private String orderCode;
    /** 对应工单数量 **/
    private Integer workTotalNum;
    /** 责任人负责的设备总数 **/
    private Integer imTotalNum;
    /** 工单异常信息列表 **/
    private List<WorkExceptionList> wexcList;
    /** 异常状态标记 0、还有待处理异常，1、异常正在处理，2、异常已处理完 **/
    private Integer wexStatus;

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public Integer getWexStatus() {
        return wexStatus;
    }

    public void setWexStatus(Integer wexStatus) {
        this.wexStatus = wexStatus;
    }

    public List<WorkExceptionList> getWexcList() {
        return wexcList;
    }

    public void setWexcList(List<WorkExceptionList> wexcList) {
        this.wexcList = wexcList;
    }

    public Integer getLiableOneId() {
        return liableOneId;
    }

    public void setLiableOneId(Integer liableOneId) {
        this.liableOneId = liableOneId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(Integer workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getWorkTotalNum() {
        return workTotalNum;
    }

    public void setWorkTotalNum(Integer workTotalNum) {
        this.workTotalNum = workTotalNum;
    }

    public Integer getImTotalNum() {
        return imTotalNum;
    }

    public void setImTotalNum(Integer imTotalNum) {
        this.imTotalNum = imTotalNum;
    }

    @Override
    public String toString() {
        return "PageHouse{" +
                "liableOneId=" + liableOneId +
                ", userName='" + userName + '\'' +
                ", countNum=" + countNum +
                ", parentId=" + parentId +
                ", workId=" + workId +
                ", workCode='" + workCode + '\'' +
                ", productCode='" + productCode + '\'' +
                ", workOrderStatus=" + workOrderStatus +
                ", operationStatus=" + operationStatus +
                ", orderCode='" + orderCode + '\'' +
                ", workTotalNum=" + workTotalNum +
                ", imTotalNum=" + imTotalNum +
                ", wexcList=" + wexcList +
                ", wexStatus=" + wexStatus +
                '}';
    }
}
