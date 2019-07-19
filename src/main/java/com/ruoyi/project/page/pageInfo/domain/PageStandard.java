package com.ruoyi.project.page.pageInfo.domain;

import com.ruoyi.common.utils.TimeUtil;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;

import java.util.Date;

/**
 * 工单标准产量
 */
public class PageStandard {
    //正在进行工单
    private DevWorkOrder devWorkOrder;
    private int s7;
    private int s8;
    private int s9;
    private int s10;
    private int s11;
    private int s12;
    private int s13;
    private int s14;
    private int s15;
    private int s16;
    private int s17;
    private int s18;
    private int s19;
    private int s20;
    private int s21;
    private int s22;
    private int s23;

    public DevWorkOrder getDevWorkOrder() {
        return devWorkOrder;
    }

    public void setDevWorkOrder(DevWorkOrder devWorkOrder) {
        this.devWorkOrder = devWorkOrder;
    }

    public PageStandard() {
    }

    public PageStandard(DevWorkOrder devWorkOrder) {
        this.devWorkOrder = devWorkOrder;
        this.s7 = getData(7);
        this.s8 = getData(8);
        this.s9 = getData(9);
        this.s10 = getData(10);
        this.s11 = getData(11);
        this.s12 = getData(12);
        this.s13 = getData(13);
        this.s14 = getData(14);
        this.s15 = getData(15);
        this.s16 = getData(16);
        this.s17 = getData(17);
        this.s18 = getData(18);
        this.s19 = getData(19);
        this.s20 = getData(20);
        this.s21 = getData(21);
        this.s22 = getData(22);
        this.s23 = getData(23);
    }

    public int getS7() {
        return s7;
    }

    public int getS8() {
        return s8;
    }

    public int getS9() {
        return s9;
    }

    public int getS10() {
        return s10;
    }

    public int getS11() {
        return s11;
    }

    public int getS12() {
        return s12;
    }

    public int getS13() {
        return s13;
    }

    public int getS14() {
        return s14;
    }

    public int getS15() {
        return s15;
    }

    public int getS16() {
        return s16;
    }

    public int getS17() {
        return s17;
    }

    public int getS18() {
        return s18;
    }

    public int getS19() {
        return s19;
    }

    public int getS20() {
        return s20;
    }

    public int getS21() {
        return s21;
    }

    public int getS22() {
        return s22;
    }

    public int getS23() {
        return s23;
    }

    /**
     * 设置相关数据
     * @param date 时间
     * @return
     */
    private int getData(int date){
        if(this.getDevWorkOrder() == null){
            return  0;
        }
        int startHour = TimeUtil.getHour(this.getDevWorkOrder().getStartTime());
        if(startHour > date){
            return 0;
        }

        if(startHour == date){
            int nowDay = TimeUtil.getHour(new Date());
            //实时计算
            float val = TimeUtil.getDateDel(this.getDevWorkOrder().getStartTime(),nowDay == startHour?new Date():TimeUtil.getEndHour(this.getDevWorkOrder().getStartTime()));
            int a = (int) (this.getDevWorkOrder().getProductStandardHour() *val);
            return a;
        }
        //标记时间是否与当前系统
        //获取当前系统小时数
        int hour = TimeUtil.getHour(new Date());
        if(hour == date){
            //实时计算
           float val = TimeUtil.getDateDel(TimeUtil.getSystemDate(),new Date());
            return (int) (this.getDevWorkOrder().getProductStandardHour() *val);
        }else if(date < hour){
            return this.getDevWorkOrder().getProductStandardHour();
        }else{
            return 0;
        }
    }

}
