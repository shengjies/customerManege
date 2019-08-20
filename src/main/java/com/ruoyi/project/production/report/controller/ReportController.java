package com.ruoyi.project.production.report.controller;

import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.product.list.service.IDevProductListService;
import com.ruoyi.project.production.productionLine.service.IProductionLineService;
import com.ruoyi.project.production.report.domain.AppReport;
import com.ruoyi.project.production.report.service.IReportService;
import com.ruoyi.project.production.singleWork.service.ISingleWorkService;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/production/report")
public class ReportController extends BaseController {
    private String prefix = "production/report";

    @Autowired
    private IReportService reportService;


    /**
     * 查询报表数
     *
     * @return
     */
    @GetMapping
    @RequiresPermissions("production:report:view")
    public String report() {
        return prefix + "/report";
    }

    /**
     * 导出产线报表数据
     *
     * @param lineId      产线编号
     * @param productCode 产品编码
     * @param startTime   开始时间
     * @param endTime     结束时间
     */
    @ResponseBody
    @RequestMapping("/line/pdf")
    @RequiresPermissions("production:report:pdf")
    public void exportReport(int lineId, String productCode, String startTime, String endTime) {
        try {
            reportService.lineReport(lineId, productCode, startTime, endTime,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出车间报表数据
     *
     * @param singleId    车间id
     * @param userId      员工id
     * @param productCode 产品编码
     * @param startTime   开始时间
     * @param endTime     结束时间
     */
    @ResponseBody
    @RequestMapping("/single/pdf")
    @RequiresPermissions("production:report:pdf")
    public void exportSingleReport(int singleId, int userId, String productCode, String startTime, String endTime) {
        try {
            reportService.singleReport(singleId, userId, productCode, startTime, endTime,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /******************************************************************************************************
     *********************************** app端数据报表交互 *************************************************
     ******************************************************************************************************/

    @Autowired
    private IProductionLineService lineService;

    @Autowired
    private ISingleWorkService singleWorkService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDevProductListService productService;

    /**
     * 响应app产线车间产品信息
     * 首次传参 wlSign 0、流水线，1、车间
     * 在线用户 uid
     */
    @PostMapping("/appReport")
    @ResponseBody
    public AjaxResult appReport(@RequestBody AppReport appReport) {
        if (appReport != null) {
            Map<String, Object> map = new HashMap<>(16);
            if (appReport.getWlSign() != null) {
                if (appReport.getWlSign().equals(WorkConstants.SING_LINE)) {
                    // 产线
                    map.put("lineList", lineService.appSelectLineList(null));
                } else if(appReport.getWlSign().equals(WorkConstants.SING_SINGLE)){
                    // 车间
                    map.put("cjList", singleWorkService.selectSingleWorkByParentId(null, 0));
                    // 所有用户
                    map.put("userList", userService.appSelectUserInfoList(appReport.getUid()));
                }
            }
            // 所有产品
            map.put("productList", productService.selectProductAll());
            return AjaxResult.success("请求成功", map);
        }
        return error();
    }

    /**
     * app端导出数据报表
     */
    @PostMapping("/appReportData")
    @ResponseBody
    public AjaxResult appReportData(@RequestBody AppReport appReport) {
        try {
            if (appReport != null) {
                if (appReport.getWlSign().equals(WorkConstants.SING_LINE)) {
                    return AjaxResult.success("请求成功",reportService.lineReport(appReport.getLineId(), appReport.getProductCode(), appReport.getStartTime(), appReport.getEndTime(), appReport.getDevType()));
                } else if (appReport.getWlSign().equals(WorkConstants.SING_SINGLE)) {
                    return AjaxResult.success("请求成功",reportService.singleReport(appReport.getLineId(), appReport.getUserId(), appReport.getProductCode(), appReport.getStartTime(), appReport.getEndTime(), appReport.getDevType()));
                }
            }
            return error();
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }

    /**
     * 删除文件
     */
    @PostMapping("/appRemoveFile")
    @ResponseBody
    public AjaxResult appRemoveFile(@RequestBody AppReport appReport){
        try {
            if (appReport != null && StringUtils.isNotEmpty(appReport.getFileName())) {
                return toAjax(reportService.appRemoveFile(appReport));
            }
            return error();
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }
}
