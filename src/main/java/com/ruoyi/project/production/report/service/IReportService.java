package com.ruoyi.project.production.report.service;

import com.itextpdf.text.DocumentException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PDF 报表导出
 */
public interface IReportService {
    /**
     * 导出产线在指定时间段内已经提交的各个工单的数据
     * @param lineId 产线编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    int exportReport(int lineId,String productCode, String startTime, String endTime, HttpServletResponse response, HttpServletRequest request);

    /**
     * 产线生产报表导出
     * @param lineId 产线id
     * @param productCode 产品id
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    void lineReport(int lineId,String productCode,String startTime,String endTime) throws IOException, DocumentException;

    /**
     * 车间报表导出
     * @param singleId 车间id
     * @param productCode 产品编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    void singleReport(int singleId,String productCode,String startTime,String endTime) throws IOException, DocumentException;
}
