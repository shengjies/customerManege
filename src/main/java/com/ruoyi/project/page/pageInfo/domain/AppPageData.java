package com.ruoyi.project.page.pageInfo.domain;

import java.io.Serializable;

/**
 * app端看板查询参数封装
 * @Author: Rainey
 * @Date: 2019/8/27 8:18
 * @Version: 1.0
 **/
public class AppPageData implements Serializable {
    private static final long serialVersionUID = -7414594672244950746L;
    /** 看板编码 */
    private String pageCode;
    /** 页面密码 默认1234356 */
    private String pagePwd;
    /** app设备编号 */
    private String devCode;
    /** app设备类型 */
    private String devType;

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

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getPagePwd() {
        return pagePwd;
    }

    public void setPagePwd(String pagePwd) {
        this.pagePwd = pagePwd;
    }

    @Override
    public String toString() {
        return "AppPageData{" +
                "pageCode='" + pageCode + '\'' +
                ", pagePwd='" + pagePwd + '\'' +
                ", devCode='" + devCode + '\'' +
                ", devType='" + devType + '\'' +
                '}';
    }
}
