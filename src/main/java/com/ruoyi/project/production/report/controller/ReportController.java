package com.ruoyi.project.production.report.controller;

import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.poi.ExcelUtils;
import com.ruoyi.common.utils.poi.PdfUtil;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.erp.bomChange.domain.BomChange;
import com.ruoyi.project.erp.bomChange.service.IBomChangeService;
import com.ruoyi.project.erp.productBom.domain.ProductBom;
import com.ruoyi.project.erp.productBom.domain.ProductBomDetails;
import com.ruoyi.project.erp.productBom.service.IProductBomService;
import com.ruoyi.project.production.report.service.IReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/production/report")
public class ReportController extends BaseController {
    private String prefix = "production/report";

    @Autowired
    private IReportService reportService;


    /**
     * 查询报表数
     * @return
     */
    @GetMapping
    @RequiresPermissions("production:report:view")
    public String report(){
        return  prefix+"/report";
    }

    /**
     * 导出产线报表数据
     * @param lineId 产线编号
     * @param productCode 产品编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    @ResponseBody
    @RequestMapping("/line/pdf")
    @RequiresPermissions("production:report:pdf")
    public void exportReport(int lineId, String productCode,String startTime, String endTime){
        try {
            reportService.lineReport(lineId,productCode,startTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 导出车间报表数据
     * @param singleId 车间id
     * @param productCode 产品编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    @ResponseBody
    @RequestMapping("/single/pdf")
    @RequiresPermissions("production:report:pdf")
    public void exportSingleReport(int singleId, String productCode,String startTime, String endTime){
        try {
            reportService.singleReport(singleId,productCode,startTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
