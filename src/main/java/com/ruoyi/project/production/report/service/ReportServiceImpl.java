package com.ruoyi.project.production.report.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.poi.PdfUtil;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.erp.fileSourceInfo.domain.FileSourceInfo;
import com.ruoyi.project.erp.fileSourceInfo.mapper.FileSourceInfoMapper;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.production.countPiece.domain.CountPiece;
import com.ruoyi.project.production.countPiece.mapper.CountPieceMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.report.domain.AppReport;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkMapper;
import com.ruoyi.project.production.workExceptionList.domain.WorkExceptionList;
import com.ruoyi.project.production.workExceptionList.mapper.WorkExceptionListMapper;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ReportServiceImpl implements IReportService {
    private static PdfPTable table;//表格创建
    private static PdfPCell cell;//单元格
    private boolean isColor = false;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private DevCompanyMapper devCompanyMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private WorkExceptionListMapper workExceptionListMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SingleWorkMapper singleWorkMapper;

    @Autowired
    private CountPieceMapper countPieceMapper;

    @Autowired
    private IsoMapper isoMapper;

    @Autowired
    private FileSourceInfoMapper fileSourceInfoMapper;

    @Value("${file.iso}")
    private String fileUrl;


    private float getFloat3(float val) {
        return new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 产线生产报表导出
     *
     * @param lineId      产线id
     * @param productCode 产品id
     * @param startTime   开始时间
     * @param endTime     结束时间
     */
    @Override
    public String lineReport(int lineId, String productCode, String startTime, String endTime) throws IOException, DocumentException {
        //表头参数定义
        String titleHeader[] = {"总入库数量", "总报废数量", "总投入数量", "标准总工时", "出勤总工时", "生产总工时", "平均达成率(%)",
                "平均直通率(%)", "加班X1.5", "加班X2.0", "加班X3.0"};
        String workHeader[] = {"工单号", "产品/半成品", "工单数量", "入库数量", "报废数量", "投入数量", "达成率%", "直通率%", "出勤工时", "生产工时", "加班X1.5", "加班X2.0", "加班X3.0"};
        String eHeader[] = {"工单号", "异常分类", "发生时间", "异常描述", "处理状态", "处理者", "解决方案", "处理时间"};
        //汇总参数
        float totalWork1 = 0.0F;//加班倍率 1.5
        float totalWork2 = 0.0F;//加班倍率 2.0
        float totalWork3 = 0.0F;//加班倍率 3.0
        int totalActualWarehouseNum = 0;//总入库数量
        int totalScrapNum = 0;//总报废数量
        int totalInputNum = 0; //总投入数量
        float totalStandardHour = 0.0F;//标准总工时
        float totalWorkHour = 0.0F;//出勤总工时
        float totalProductHour = 0.0F;//生产总工时
        float totalReachRate = 0.0F;//总达成率
        float totalDirectPassRate = 0.0F;//总直通率
        int workNum = 0;//总工单数量
        StringBuffer sb = new StringBuffer();
        HttpServletResponse response = ServletUtils.getResponse();
        StringBuffer fileName = new StringBuffer();
        if (lineId > 0) {
            ProductionLine line = productionLineMapper.selectProductionLineById(lineId);
            fileName.append(line.getLineName() != null ? line.getLineName() : "");
        } else {
            fileName.append("全局");
        }
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(productCode)) {
            fileName.append("-产品：" + productCode);
        } else {
            fileName.append("-全部产品-");
        }
        fileName.append("时间从" + startTime + "至" + endTime);
        response.setHeader("content-Type", "application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName.toString() + ".pdf");
        Document document = new Document(PageSize.A4);
        Font titleFont = new Font(BaseFont.createFont("/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED));
        titleFont.setSize(14);
        PdfWriter writer;
        FileOutputStream fos = null;
        String appPath = null;
        Iso iso = isoMapper.selectIsoById(1);
        if (!StringUtils.isEmpty(iso)) {
            String savePath = iso.getDisk() + "/" + fileName.toString() + ".pdf";
            File file = new File(savePath);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(savePath);
            appPath = fileUrl + iso.getDiskPath() + "/" + fileName.toString() + ".pdf";
            writer = PdfWriter.getInstance(document, fos);
            User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
            document.open();
            writer.open();
            if (u == null) {
                document.add(new Paragraph("登录超时，请重新登录", titleFont));
                writer.close();
                document.close();
                return null;
            }

            StringBuilder dataSource = new StringBuilder();
            if (lineId > 0 && !StringUtils.isEmpty(productCode)) {
                ProductionLine line = productionLineMapper.selectProductionLineById(lineId);
                dataSource.append(line.getLineName() + ",产品/半成品：" + productCode);
            } else {
                if (lineId > 0) {
                    ProductionLine line = productionLineMapper.selectProductionLineById(lineId);
                    dataSource.append(line.getLineName());
                } else if (!StringUtils.isEmpty(productCode)) {
                    dataSource.append("产品/半成品：" + productCode);
                } else {
                    dataSource.append("全局");
                }
            }
            DevCompany company = devCompanyMapper.selectDevCompanyById(u.getCompanyId());
            if (company != null) {
                Paragraph companyTile = new Paragraph(company.getComName(), titleFont);
                companyTile.setAlignment(1);
                document.add(companyTile);
            }
            //查询产线在该时间段内已经提交的所以工单数据
            List<DevWorkOrder> orders = devWorkOrderMapper.selectOrderByLineIsSubmit(u.getCompanyId(), productCode,
                    lineId, startTime + " 00:00:00", endTime + " 23:59:59", 0);
            //数据计算
            if (orders != null && orders.size() > 0) {
                workNum = orders.size();
                for (DevWorkOrder order : orders) {
                    sb.append(order.getId());
                    sb.append(",");
                    //加班倍率
                    if (PdfUtil.floatNull(order.getOvertimeHour()) > 0 && PdfUtil.floatNull(order.getOvertimeRace()) > 0) {
                        if (order.getOvertimeRace() == 1.5) {
                            order.setWork1(order.getOvertimeRace().toString());
                            totalWork1 += order.getOvertimeRace();
                        } else if (order.getOvertimeRace() == 2) {
                            order.setWork2(order.getOvertimeRace().toString());
                            totalWork2 += order.getOvertimeRace();
                        } else if (order.getOvertimeRace() == 3) {
                            order.setWork3(order.getOvertimeHour().toString());
                            totalWork3 += order.getOvertimeHour();
                        }
                    }
                    //达成率 = 实际计数数量/(标准工时*生产用时)
                    order.setReachRate(0F);
                    if (PdfUtil.IntegerNull(order.getCumulativeNumber()) > 0) {
                        float total = order.getProductStandardHour() * PdfUtil.floatNull(order.getSignHuor());//实际标准产量
                        order.setReachRate(total == 0 ? 0 : getFloat3(((float) order.getCumulativeNumber() / total) * 100));
                    }
                    //总达成率
                    totalReachRate += order.getReachRate();
                    //总直通率
                    totalDirectPassRate += PdfUtil.floatNull(order.getDirectPassRate());
                    //标准工时
                    if (PdfUtil.IntegerNull(order.getProductStandardHour()) > 0) {
                        totalStandardHour += ((float) PdfUtil.IntegerNull(order.getProductNumber()) / (float) order.getProductStandardHour());
                    }
                    //入库总数量
                    totalActualWarehouseNum += PdfUtil.IntegerNull(order.getActualWarehouseNum());
                    //报废总数量
                    totalScrapNum += PdfUtil.IntegerNull(order.getScrapNum());
                    //投入总数量
                    totalInputNum += PdfUtil.IntegerNull(order.getPutIntoNumber());
                    //出勤总工时
                    totalWorkHour += (PdfUtil.floatNull(order.getWorkingHour()) + PdfUtil.floatNull(order.getOvertimeHour()));
                    //生产总工时
                    totalProductHour += PdfUtil.floatNull(order.getSignHuor());
                }
            }
            //查询所以的异常事件
            List<WorkExceptionList> exceptionLists = null;
            if (sb.toString().length() > 0) {
                exceptionLists = workExceptionListMapper.selectWorkExceByWorkId(sb.toString().substring(0, sb.length() - 1));
            }
            titleFont.setSize(12);
            Paragraph text = new Paragraph("生产报表", titleFont);
            text.setAlignment(1);
            document.add(text);
            //数据来源
            titleFont.setSize(10);
            Phrase phrase = new Phrase("数据来源：" + dataSource.toString() + ";  报表时间:" + startTime + "至" + endTime, titleFont);
            document.add(phrase);
            //数据汇总表
            Font bodyFont = new Font(BaseFont.createFont("/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED));
            bodyFont.setSize(8);
            PdfPTable table = new PdfPTable(titleHeader.length);
            table.setTotalWidth(580);
            table.setLockedWidth(true);
            for (String h : titleHeader) {
                table.addCell(createCell(h, bodyFont, 1, isColor));
            }
            table.addCell(createCell(PdfUtil.stringNullValue(totalActualWarehouseNum), bodyFont, 1, isColor));//总入库数量
            table.addCell(createCell(PdfUtil.stringNullValue(totalScrapNum), bodyFont, 1, isColor));//总入报废数量
            table.addCell(createCell(PdfUtil.stringNullValue(totalInputNum), bodyFont, 1, isColor));//总入投入数量
            table.addCell(createCell(PdfUtil.stringNullValue(getFloat3(totalStandardHour)), bodyFont, 1, isColor));//标准总工时
            table.addCell(createCell(PdfUtil.stringNullValue(totalWorkHour), bodyFont, 1, isColor));//出勤总工时
            table.addCell(createCell(PdfUtil.stringNullValue(totalProductHour), bodyFont, 1, isColor));//生产总工时
            if (workNum > 0) {
                table.addCell(createCell(PdfUtil.stringNullValue(getFloat3((totalReachRate / workNum))) + "%", bodyFont, 1, isColor));//平均达成率
                table.addCell(createCell(PdfUtil.stringNullValue(getFloat3((totalDirectPassRate / workNum))) + "%", bodyFont, 1, isColor));//平均直通率
            } else {
                table.addCell(createCell(0 + "%", bodyFont, 1, isColor));//平均达成率
                table.addCell(createCell(0 + "%", bodyFont, 1, isColor));//平均直通率
            }
            table.addCell(createCell(PdfUtil.stringNullValue(totalWork1), bodyFont, 1, isColor));
            table.addCell(createCell(PdfUtil.stringNullValue(totalWork2), bodyFont, 1, isColor));
            table.addCell(createCell(PdfUtil.stringNullValue(totalWork3), bodyFont, 1, isColor));
            document.add(table);
            document.add(new Chunk("\n"));
            //工单详情
            PdfPTable detailsTable = createTable(workHeader.length + 2);
            PdfPCell titleCell = createCell("工单详情", titleFont, workHeader.length + 2, isColor);
            titleCell.setVerticalAlignment(1);
            detailsTable.addCell(titleCell);
            int i = 0;
            for (; i < workHeader.length; i++) {
                if (i == 0 || i == 1) {
                    detailsTable.addCell(createCell(workHeader[i], bodyFont, 2, !isColor));
                } else {
                    detailsTable.addCell(createCell(workHeader[i], bodyFont, 1, !isColor));
                }
            }
            //循环工单信息
            if (orders != null && orders.size() > 0) {
                for (DevWorkOrder order : orders) {
                    detailsTable.addCell(createCell(order.getWorkorderNumber(), bodyFont, 2, isColor));//工单号
                    detailsTable.addCell(createCell(order.getProductCode(), bodyFont, 2, isColor));//产品编码
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getProductNumber()), bodyFont, 1, isColor));//工单数量
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getActualWarehouseNum()), bodyFont, 1, isColor));//入库数量
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getScrapNum()), bodyFont, 1, isColor));//报废数量
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getPutIntoNumber()), bodyFont, 1, isColor));//投入数量
                    detailsTable.addCell(createCell(PdfUtil.floatNullValue(order.getReachRate()) + "%", bodyFont, 1, isColor));//达成率
                    detailsTable.addCell(createCell(PdfUtil.floatNullValue(order.getDirectPassRate()) + "%", bodyFont, 1, isColor));//直通率
                    detailsTable.addCell(createCell(PdfUtil.floatNullValue(order.getWorkingHour()), bodyFont, 1, isColor));//出勤工时
                    detailsTable.addCell(createCell(PdfUtil.floatNullValue(order.getSignHuor()), bodyFont, 1, isColor));//生产工时
                    detailsTable.addCell(createCell(PdfUtil.stringNullValue(order.getWork1()), bodyFont, 1, isColor));//加班X1.5
                    detailsTable.addCell(createCell(PdfUtil.stringNullValue(order.getWork2()), bodyFont, 1, isColor));//加班X2.0
                    detailsTable.addCell(createCell(PdfUtil.stringNullValue(order.getWork3()), bodyFont, 1, isColor));//加班X3.0
                }
            }
            document.add(detailsTable);
            document.add(new Chunk("\n"));
            //工单异常
            PdfPTable eTable = createTable(eHeader.length + 7);
            PdfPCell eCell = createCell("异常事件", titleFont, eHeader.length + 7, false);
            eCell.setVerticalAlignment(1);
            eTable.addCell(eCell);
            i = 0;
            for (; i < eHeader.length; i++) {
                if (i == 0 || i == 2 || i == eHeader.length - 1) {
                    eTable.addCell(createCell(eHeader[i], bodyFont, 2, !isColor));
                } else if (i == 3 || i == 6) {
                    eTable.addCell(createCell(eHeader[i], bodyFont, 3, !isColor));
                } else {
                    eTable.addCell(createCell(eHeader[i], bodyFont, 1, !isColor));
                }
            }
            if (exceptionLists != null && exceptionLists.size() > 0) {
                for (WorkExceptionList exe : exceptionLists) {
                    eTable.addCell(createCell(exe.getWorkorderNumber(), bodyFont, 2, isColor));//工单号
                    eTable.addCell(createCell(exe.getTypeName(), bodyFont, 1, isColor));//异常分类
                    eTable.addCell(createCell(DateUtils.getDateTime(exe.getCreateTime()), bodyFont, 2, isColor));//发生时间
                    eTable.addCell(createCell(exe.getRemark(), bodyFont, 3, isColor));//异常描述
                    eTable.addCell(createCell(exe.getExce(), bodyFont, 1, isColor));//处理状态
                    eTable.addCell(createCell(exe.getHandleUser(), bodyFont, 1, isColor));//处理者
                    eTable.addCell(createCell(exe.getHandleContent(), bodyFont, 3, isColor));//解决方法
                    eTable.addCell(createCell(DateUtils.getDateTime(exe.getHandleTime()), bodyFont, 2, isColor));//解决时间
                }
            }

            /**
             * 文件系统操作
             */
            FileSourceInfo fileSourceInfo = fileSourceInfoMapper.selectFileSourceByFileName(u.getCompanyId(),null,fileName.toString() + ".pdf");
            if (fileSourceInfo != null) {
                fileSourceInfoMapper.deleteFileSourceInfoById(fileSourceInfo.getId());
                addFileInfo(fileName.toString(),1, appPath, savePath, u);
            } else {
                addFileInfo(fileName.toString(),1, appPath, savePath, u);
            }

            document.add(eTable);
            document.close();
            return appPath;
        }
        return "下载失败";
    }

    /**
     * 新增文件
     * @param fileName
     * @param appPath
     * @param savePath
     * @param u
     */
    private void addFileInfo(String fileName,Integer saveType, String appPath, String savePath, User u) {
        FileSourceInfo fileSourceInfo;
        fileSourceInfo = new FileSourceInfo();
        fileSourceInfo.setCompanyId(u.getCompanyId());
        // 报表文件类型
        fileSourceInfo.setSaveType(14);
        fileSourceInfo.setSaveId(saveType);
        fileSourceInfo.setSavePath(savePath);
        fileSourceInfo.setFilePath(appPath);
        fileSourceInfo.setFileName(fileName + ".pdf");
        fileSourceInfo.setCreateTime(new Date());
        fileSourceInfoMapper.insertFileSourceInfo(fileSourceInfo);
    }

    /**
     * 创建单元格
     *
     * @param value 对应数值
     * @param font  对应样式
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int span, boolean color) {
        cell = new PdfPCell(new Paragraph(value, font));
        cell.setHorizontalAlignment(1);
        cell.setColspan(span);
        if (color) {
            cell.setBackgroundColor(PdfUtil.headerColor);
        }
        return cell;
    }

    /**
     * 创建表格
     *
     * @param numColumns 单元格个数
     * @return
     */
    public static PdfPTable createTable(int numColumns) {
        table = new PdfPTable(numColumns);
        table.setTotalWidth(580);
        table.setLockedWidth(true);
        return table;
    }

    /**
     * 车间报表导出
     *
     * @param singleId    车间id
     * @param userId      员工id
     * @param productCode 产品编码
     * @param startTime   开始时间
     * @param endTime     结束时间
     */
    @Override
    public String singleReport(int singleId, int userId, String productCode, String startTime, String endTime) throws IOException, DocumentException {
        int totalNum = 0;//总计件数量
        int totalBadNumber = 0;//总不良品数量
        int totalInputNum = 0; //总投入数量
        float totalPrice = 0;//总工价
        float totalStandardHour = 0.0F;//标准总工时
        float totalProductHour = 0.0F;//生产总工时
        //表格头部参数定义
        String titleHeader[] = {"总计件数量", "总不良品数量", "总投入数量", "总工价", "标准总工时", "生产总工时", "标准总工时/生产总工时"};
        String workHeader[] = {"工单号", "产品/半成品", "工单数量", "计件数量", "不良品数量", "投入数量", "标准工时", "生产工时", "工价", "总工价"};
        String eHeader[] = {"工单号", "异常分类", "发生时间", "异常描述", "处理状态", "处理者", "解决方案", "处理时间"};
        HttpServletResponse response = ServletUtils.getResponse();
        response.setHeader("content-Type", "application/pdf");
        // 获取PDF文件名
        StringBuilder fileName =new StringBuilder();
        SingleWork work = null;
        if (singleId > 0 ) {
            work = singleWorkMapper.selectSingleWorkById(singleId);
            fileName.append(work.getWorkshopName());
        } else {
            fileName.append("全局");
        }
        User user = null;
        if (userId > 0) {
            user = userMapper.selectUserInfoById(userId);
            fileName.append("-" + user.getUserName());
        }
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(productCode)) {
            fileName.append("-产品:" + productCode);
        }
        fileName.append("时间从" + startTime + "到" + endTime);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName.toString() + ".pdf");
        Document document = new Document(PageSize.A4);
        Font titleFont = new Font(BaseFont.createFont("/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED));
        titleFont.setSize(14);

        PdfWriter writer;
        FileOutputStream fos = null;
        String appPath = null;

        Iso iso = isoMapper.selectIsoById(1);
        if (!StringUtils.isEmpty(iso)) {
            String savePath = iso.getDisk() + "/" + fileName + ".pdf";
            File file = new File(savePath);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(iso.getDisk() + "/" + fileName + ".pdf");
            appPath = fileUrl + iso.getDiskPath() + "/" + fileName + ".pdf";
            writer = PdfWriter.getInstance(document, fos);
            User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
            document.open();
            writer.open();
            if (u == null) {
                document.add(new Paragraph("登录超时，请重新登录", titleFont));
                writer.close();
                document.close();
                return null;
            }

            StringBuilder dataSource = new StringBuilder();
            if (singleId > 0) {
                dataSource.append(work.getWorkshopName() + ",");
            }
            if (!StringUtils.isEmpty(productCode)) {
                dataSource.append("产品/半成品:" + productCode);
            }
            if (userId > 0) {
                dataSource.append("员工:" + user.getUserName());
            }
            if (dataSource.toString().length() <= 0) {
                dataSource.append("全局");
            }

            DevCompany company = devCompanyMapper.selectDevCompanyById(u.getCompanyId());
            if (company != null) {
                Paragraph companyTile = new Paragraph(company.getComName(), titleFont);
                companyTile.setAlignment(1);
                document.add(companyTile);
            }
            //查询产线在该时间段内已经提交的所以工单数据
            StringBuffer sb = new StringBuffer();
            List<DevWorkOrder> orders = null;
            if (userId <= 0) {
                orders = devWorkOrderMapper.selectOrderByLineIsSubmit(u.getCompanyId(), productCode,
                        singleId, startTime + " 00:00:00", endTime + " 23:59:59", 1);
            } else {
                orders = devWorkOrderMapper.selectOrderBySingleIsSubmit(u.getCompanyId(), productCode, singleId,
                        startTime + " 00:00:00", endTime + " 23:59:59", 1, userId);
            }

            CountPiece piece = null;
            if (orders != null && orders.size() > 0) {
                for (DevWorkOrder order : orders) {
                    sb.append(order.getId());
                    sb.append(",");
                    //查询对应的计件数
                    order.setActualWarehouseNum(0);//计件数
                    order.setBadNumber(0);//不良品数
                    order.setWorkingHour(0F);
                    if (userId <= 0) {
                        piece = countPieceMapper.selectPieceByWorkId(order.getId(), 0);
                    } else {
                        piece = countPieceMapper.selectPieceByWorkId(order.getId(), userId);
                    }
                    if (piece != null) {
                        order.setActualWarehouseNum(piece.getCpNumber());//计件数
                        order.setBadNumber(piece.getCpBadNumber());//不良品数
                        totalNum += piece.getCpNumber();//总计件数
                        totalBadNumber += piece.getCpBadNumber();//总不良品数
                        float price = (piece.getCpNumber() - piece.getCpBadNumber()) * order.getWorkPrice();
                        //设置总工价
                        order.setWorkingHour(price);
                        totalPrice += price;//总工价
                    }
                    totalInputNum += PdfUtil.IntegerNull(order.getPutIntoNumber());//总投入数量
                    //标准工时
                    order.setOvertimeRace(0F);
                    if (PdfUtil.IntegerNull(order.getProductStandardHour()) > 0) {
                        float standardHour = ((float) PdfUtil.IntegerNull(order.getProductNumber()) / (float) order.getProductStandardHour());
                        order.setOvertimeRace(getFloat3(standardHour));
                        totalStandardHour += standardHour;
                    }
                    //总生产工时
                    totalProductHour += PdfUtil.floatNull(order.getSignHuor());
                }
            }
            //查询所以的异常事件
            List<WorkExceptionList> exceptionLists = null;
            if (sb.toString().length() > 0) {
                exceptionLists = workExceptionListMapper.selectWorkExceByWorkId(sb.toString().substring(0, sb.length() - 1));
            }
            titleFont.setSize(12);
            Paragraph text = new Paragraph("生产报表", titleFont);
            text.setAlignment(1);
            document.add(text);
            //数据来源
            titleFont.setSize(10);
            Phrase phrase = new Phrase("数据来源：" + dataSource.toString() + ";  报表时间:" + startTime + "至" + endTime, titleFont);
            document.add(phrase);
            //数据汇总表
            Font bodyFont = new Font(BaseFont.createFont("/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED));
            bodyFont.setSize(8);
            PdfPTable table = new PdfPTable(titleHeader.length);
            table.setTotalWidth(580);
            table.setLockedWidth(true);
            for (String h : titleHeader) {
                table.addCell(createCell(h, bodyFont, 1, isColor));
            }
            table.addCell(createCell(PdfUtil.stringNullValue(totalNum), bodyFont, 1, isColor));//总计件数量
            table.addCell(createCell(PdfUtil.stringNullValue(totalBadNumber), bodyFont, 1, isColor));//总不良品数量
            table.addCell(createCell(PdfUtil.stringNullValue(totalInputNum), bodyFont, 1, isColor));//总入投入数量
            table.addCell(createCell(PdfUtil.stringNullValue(totalPrice), bodyFont, 1, isColor));//总工价
            table.addCell(createCell(PdfUtil.stringNullValue(getFloat3(totalStandardHour)), bodyFont, 1, isColor));//标准总工时
            table.addCell(createCell(PdfUtil.stringNullValue(totalProductHour), bodyFont, 1, isColor));//生产总工时
            if (totalProductHour > 0) {
                table.addCell(createCell(PdfUtil.stringNullValue(getFloat3((totalStandardHour / totalProductHour) * 100) + "%"), bodyFont, 1, isColor));//生产总工时
            } else {
                table.addCell(createCell("0%", bodyFont, 1, isColor));//生产总工时
            }
            document.add(table);
            document.add(new Chunk("\n"));
            //工单详情
            PdfPTable detailsTable = createTable(workHeader.length + 2);
            PdfPCell titleCell = createCell("工单详情", titleFont, workHeader.length + 2, isColor);
            titleCell.setVerticalAlignment(1);
            detailsTable.addCell(titleCell);
            int i = 0;
            for (; i < workHeader.length; i++) {
                if (i == 0 || i == 1) {
                    detailsTable.addCell(createCell(workHeader[i], bodyFont, 2, !isColor));
                } else {
                    detailsTable.addCell(createCell(workHeader[i], bodyFont, 1, !isColor));
                }
            }
            if (orders != null && orders.size() > 0) {
                for (DevWorkOrder order : orders) {
                    detailsTable.addCell(createCell(order.getWorkorderNumber(), bodyFont, 2, isColor));//工单号
                    detailsTable.addCell(createCell(order.getProductCode(), bodyFont, 2, isColor));//产品编码
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getProductNumber()), bodyFont, 1, isColor));//工单数量
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getActualWarehouseNum()), bodyFont, 1, isColor));//计件数量
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getBadNumber()), bodyFont, 1, isColor));//不良品数量
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getPutIntoNumber()), bodyFont, 1, isColor));//投入数量
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getOvertimeRace()), bodyFont, 1, isColor));//标准工时
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getSignHuor()), bodyFont, 1, isColor));//生产用时
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getWorkPrice()), bodyFont, 1, isColor));//工价
                    detailsTable.addCell(createCell(PdfUtil.nullValue(order.getWorkingHour()), bodyFont, 1, isColor));//总工价
                }
            }
            document.add(detailsTable);
            document.add(new Chunk("\n"));
            //工单异常
            PdfPTable eTable = createTable(eHeader.length + 7);
            PdfPCell eCell = createCell("异常事件", titleFont, eHeader.length + 7, false);
            eCell.setVerticalAlignment(1);
            eTable.addCell(eCell);
            i = 0;
            for (; i < eHeader.length; i++) {
                if (i == 0 || i == 2 || i == eHeader.length - 1) {
                    eTable.addCell(createCell(eHeader[i], bodyFont, 2, !isColor));
                } else if (i == 3 || i == 6) {
                    eTable.addCell(createCell(eHeader[i], bodyFont, 3, !isColor));
                } else {
                    eTable.addCell(createCell(eHeader[i], bodyFont, 1, !isColor));
                }
            }
            if (exceptionLists != null && exceptionLists.size() > 0) {
                for (WorkExceptionList exe : exceptionLists) {
                    eTable.addCell(createCell(exe.getWorkorderNumber(), bodyFont, 2, isColor));//工单号
                    eTable.addCell(createCell(exe.getTypeName(), bodyFont, 1, isColor));//异常分类
                    eTable.addCell(createCell(DateUtils.getDateTime(exe.getCreateTime()), bodyFont, 2, isColor));//发生时间
                    eTable.addCell(createCell(exe.getRemark(), bodyFont, 3, isColor));//异常描述
                    eTable.addCell(createCell(exe.getExce(), bodyFont, 1, isColor));//处理状态
                    eTable.addCell(createCell(exe.getHandleUser(), bodyFont, 1, isColor));//处理者
                    eTable.addCell(createCell(exe.getHandleContent(), bodyFont, 3, isColor));//解决方法
                    eTable.addCell(createCell(DateUtils.getDateTime(exe.getHandleTime()), bodyFont, 2, isColor));//解决时间
                }
            }
            /**
             * 文件系统操作
             */
            FileSourceInfo fileSourceInfo = fileSourceInfoMapper.selectFileSourceByFileName(u.getCompanyId(),14,fileName + ".pdf");
            if (fileSourceInfo != null) {
                fileSourceInfoMapper.deleteFileSourceInfoById(fileSourceInfo.getId());
                addFileInfo(fileName.toString(),2, appPath, savePath, u);
            } else {
                addFileInfo(fileName.toString(),2, appPath, savePath, u);
            }
            document.add(eTable);
            document.close();
            return appPath;
        }
        return null;
    }

    /**
     * 删除app端下载缓存文件
     */
    @Override
    public int appRemoveFile(AppReport appReport) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return 0;
        }
        DevCompany company = devCompanyMapper.selectDevCompanyById(user.getCompanyId());
        if (company != null && !StringUtils.isEmpty(appReport.getFileName())) {
            String filePath = RuoYiConfig.getProfile() + company.getTotalIso() + "/" + appReport.getFileName();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
        return 1;
    }
}
