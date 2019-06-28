package com.ruoyi.common.constant;

/**
 * mrp常量信息
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.common.constant
 * @Author: Administrator
 * @Date: 2019/6/25 14:34
 * @Description //TODO
 * @Version: 1.0
 **/
public class MrpConstants {
    /**
     * 订单是否生成mrp记录 未生成 0 </br>
     * 0、未生成 ；1、已生成
     */
    public final static Integer ORDER_NOTO_MRP = 0;
    /**
     * 订单是否生成mrp记录 已生成 1 </br>
     * 0、未生成 ；1、已生成
     */
    public final static Integer ORDER_TO_MRP = 1;

    /**
     * MRP采购状态描述 需采购 0 </br>
     * 0、需采购 1、采购中、2、采购完成 3、不采购
     */
    public final static Integer MRP_MAT_NEEDCG = 0;
    /**
     * MRP采购状态描述 采购中 1 </br>
     * 0、需采购 1、采购中、2、采购完成 3、不采购
     */
    public final static Integer MRP_MAT_CGING = 1;
    /**
     * MRP采购状态描述 采购完成 2 </br>
     * 0、需采购 1、采购中、2、采购完成 3、不采购
     */
    public final static Integer MRP_MAT_CGFINISH = 2;
    /**
     * MRP采购状态描述 不采购 3 </br>
     * 0、需采购 1、采购中、2、采购完成 3、不采购
     */
    public final static Integer MRP_MAT_NOTCG = 3;
}
