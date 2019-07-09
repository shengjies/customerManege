package com.ruoyi.project.page.pageInfo.domain;

import com.ruoyi.common.utils.TimeUtil;
import com.ruoyi.project.production.devWorkData.domain.DevWorkData;
import com.ruoyi.project.production.devWorkDayHour.domain.DevWorkDayHour;

import java.util.Date;

/**
 * 实时产量
 */
public class PageReal {
    private DevWorkDayHour hour;//保存历史数据
    private int r;//实时统计的数据
    private int r7;
    private int r8;
    private int r9;
    private int r10;
    private int r11;
    private int r12;
    private int r13;
    private int r14;
    private int r15;
    private int r16;
    private int r17;
    private int r18;
    private int r19;
    private int r20;
    private int r21;
    private int r22;
    private int r23;

    public PageReal() {
    }

    public PageReal(DevWorkDayHour hour, int r) {
        this.hour = hour;
        this.r = r;
        this.r7 = hour == null?0:getData(7,hour.getHour7());
        this.r8 = hour == null?0:getData(8,hour.getHour8());
        this.r9 = hour == null?0:getData(9,hour.getHour9());
        this.r10 = hour == null?0:getData(10,hour.getHour10());
        this.r11 = hour == null?0:getData(11,hour.getHour11());
        this.r12 = hour == null?0:getData(12,hour.getHour12());
        this.r13 = hour == null?0:getData(13,hour.getHour13());
        this.r14 = hour == null?0:getData(14,hour.getHour14());
        this.r15 = hour == null?0:getData(15,hour.getHour15());
        this.r16 = hour == null?0:getData(16,hour.getHour16());
        this.r17 = hour == null?0:getData(17,hour.getHour17());
        this.r18 = hour == null?0:getData(18,hour.getHour18());
        this.r19 = hour == null?0:getData(19,hour.getHour19());
        this.r20 = hour == null?0:getData(20,hour.getHour20());
        this.r21 = hour == null?0:getData(21,hour.getHour21());
        this.r22 = hour == null?0:getData(22,hour.getHour22());
        this.r23 = hour == null?0:getData(23,hour.getHour23());
    }

    public DevWorkDayHour getHour() {
        return hour;
    }

    public void setHour(DevWorkDayHour hour) {
        this.hour = hour;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getR7() {
        return r7;
    }

    public void setR7(int date) {

        this.r7 = r7;
    }

    public int getR8() {
        return r8;
    }

    public void setR8(int r8) {
        this.r8 = r8;
    }

    public int getR9() {
        return r9;
    }

    public void setR9(int r9) {
        this.r9 = r9;
    }

    public int getR10() {
        return r10;
    }

    public void setR10(int r10) {
        this.r10 = r10;
    }

    public int getR11() {
        return r11;
    }

    public void setR11(int r11) {
        this.r11 = r11;
    }

    public int getR12() {
        return r12;
    }

    public void setR12(int r12) {
        this.r12 = r12;
    }

    public int getR13() {
        return r13;
    }

    public void setR13(int r13) {
        this.r13 = r13;
    }

    public int getR14() {
        return r14;
    }

    public void setR14(int r14) {
        this.r14 = r14;
    }

    public int getR15() {
        return r15;
    }

    public void setR15(int r15) {
        this.r15 = r15;
    }

    public int getR16() {
        return r16;
    }

    public void setR16(int r16) {
        this.r16 = r16;
    }

    public int getR17() {
        return r17;
    }

    public void setR17(int r17) {
        this.r17 = r17;
    }

    public int getR18() {
        return r18;
    }

    public void setR18(int r18) {
        this.r18 = r18;
    }

    public int getR19() {
        return r19;
    }

    public void setR19(int r19) {
        this.r19 = r19;
    }

    public int getR20() {
        return r20;
    }

    public void setR20(int r20) {
        this.r20 = r20;
    }

    public int getR21() {
        return r21;
    }

    public void setR21(int r21) {
        this.r21 = r21;
    }

    public int getR22() {
        return r22;
    }

    public void setR22(int r22) {
        this.r22 = r22;
    }

    public int getR23() {
        return r23;
    }

    public void setR23(int r23) {
        this.r23 = r23;
    }

    /**
     * 设置相关数据
     * @param date 时间
     * @return
     */
    private int getData(int date,int val){
        //获取当前系统小时数
        int hour = TimeUtil.getHour(new Date());
        if(hour == date){
            return r;
        }else{
            return val;
        }
    }
}
