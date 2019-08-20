package com.ruoyi.project.system.notice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * @Author: Rainey
 * @Date: 2019/7/27 10:53
 * @Version: 1.0
 **/
public class NoticeApp extends BaseEntity {
    private Integer uid;   // 用户id
    private String text; // 消息内容
    private Integer status; // 消息状态
    private Integer cid;// 创建者id
    private String cPicture; // 创建者头像
    private String cName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cTime;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getcPicture() {
        return cPicture;
    }

    public void setcPicture(String cPicture) {
        this.cPicture = cPicture;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    @Override
    public String toString() {
        return "NoticeApp{" +
                "uid=" + uid +
                ", text='" + text + '\'' +
                ", status=" + status +
                ", cid=" + cid +
                ", cPicture='" + cPicture + '\'' +
                '}';
    }
}
