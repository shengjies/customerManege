package com.ruoyi.project.production.report.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.poi.PdfUtil;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.workExceptionList.domain.WorkExceptionList;
import com.ruoyi.project.production.workExceptionList.mapper.WorkExceptionListMapper;
import com.ruoyi.project.system.user.domain.User;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private DevCompanyMapper devCompanyMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private WorkExceptionListMapper workExceptionListMapper;

    @Override
    public int exportReport(int lineId, String productCode,String startTime, String endTime,
                            HttpServletResponse response, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        String path = "/test_A4.jrxml";
        String pdfName = "全局";
        Map<String,Object> param = new HashMap<>();
        param.put("company_name","xxx");
        param.put("line","xxx");
        param.put("startTime",startTime);
        param.put("endTime", startTime);
        int totalActualNum =0;//总入库数量
        int totalScrapNum =0;//总报废数量
        float totalStandardHour =0F;//标准总工时
        float totalHour =0F;//实际总工时
        float totalProduct=0F;//生产总工时
        float totalReachRate = 0F;//总平均达成率
        float reachRateNum = 0F;//总达成率
        float ztRate = 0F;//总直通率
        float pjztRate = 0F;//平均直通率
        int totalInput =0;//总投入数量
        float totalZcHour = 0F;//正常总工时
        float totalWork1 = 0F;//1.5
        float totalWork2 = 0F;//2
        float totalWork3 = 0F;//3
        int totalCumulativeNumber = 0;//总累计产量
        StringBuffer sb = new StringBuffer();
        if(user != null){
            //查询对应的公司名称
            DevCompany company = devCompanyMapper.selectDevCompanyById(user.getCompanyId());
            if(company != null){
                param.put("company_name",company.getComName());
            }
            //查询对应产线
                if(lineId > 0){
                    ProductionLine line = productionLineMapper.selectProductionLineById(lineId);
                    pdfName = line.getLineName();
                }
                if(!StringUtils.isEmpty(productCode)){
                    pdfName = "产品："+productCode;
                }
                if(lineId >0 && !StringUtils.isEmpty(productCode)){
                    ProductionLine line = productionLineMapper.selectProductionLineById(lineId);
                    pdfName = line.getLineName()+",产品："+productCode;
                }
                param.put("line",pdfName);
                //查询产线在该时间段内已经提交的所以工单数据
               List<DevWorkOrder> orders = devWorkOrderMapper.selectOrderByLineIsSubmit(user.getCompanyId(),productCode,
                       lineId,startTime+" 00:00:00",endTime+" 23:59:59");
                for (DevWorkOrder order : orders) {
                    float wh = order.getWorkingHour() == null?0:order.getWorkingHour();//正常工时
//                    float mh = order.getManualTime()==null?0:order.getManualTime();//手动调整工时
                    float sh = order.getSignHuor() == null?0:order.getSignHuor();//生产工时
                    sb.append(order.getId());
                    sb.append(",");
                    totalActualNum += order.getActualWarehouseNum()==null?0:order.getActualWarehouseNum();//总入库数量
                    totalScrapNum += order.getScrapNum()==null?0:order.getScrapNum();
                    totalHour += wh;//正常中工时
                    totalProduct += sh;//生产总工时
                    totalInput += order.getPutIntoNumber()==null?0:order.getPutIntoNumber();//中投入数
                    totalCumulativeNumber += order.getCumulativeNumber() ==null?0:order.getCumulativeNumber();//总累计数量
                    //加班倍率
                    if(order.getOvertimeHour() != null && order.getOvertimeHour() > 0 && order.getOvertimeRace() != null && order.getOvertimeRace() >0){
                        if(order.getOvertimeRace() == 1.5){
                            order.setWork1(order.getOvertimeRace()+"");
                            totalWork1 += order.getOvertimeRace();
                        }else if(order.getOvertimeRace() == 2){
                            order.setWork2(order.getOvertimeRace()+"");
                            totalWork2 += order.getOvertimeRace();
                        }else if(order.getOvertimeRace() == 3){
                            order.setWork3(order.getOvertimeHour()+"");
                            totalWork3 += order.getOvertimeHour();
                        }
                    }
                    order.setPutIntoNumber(order.getPutIntoNumber() == null?0:order.getPutIntoNumber());
                    //直通率 = 入库数量 / 累计产量
                    ztRate +=  order.getDirectPassRate()==null?0:order.getDirectPassRate();

                    //达成率
                    order.setReachRate(0F);
                    if(order.getCumulativeNumber() != null && order.getProductStandardHour() != null){
                        float total =  order.getProductStandardHour()*(sh);//实际标准产量
                        order.setReachRate(total==0?0:getFloat3((((float)order.getCumulativeNumber())/ total))*100);
                    }
                    reachRateNum+=order.getReachRate();//总达成率
                    //标准工时
                    float standardHour = 0F;
                    if(order.getProductStandardHour() != null && order.getProductStandardHour() >0){
                        int num = order.getActualWarehouseNum() ==null?order.getProductNumber():order.getActualWarehouseNum();
                        standardHour = (float)num/order.getProductStandardHour();
                    }
                    totalStandardHour +=standardHour;//总标准工时
                }
                if(orders.size() >0){
                    totalReachRate = getFloat3(reachRateNum/orders.size());//平均达成率
                    pjztRate = getFloat3(ztRate/orders.size());//平均直通率
                }
                JRDataSource dataSource = new JRBeanCollectionDataSource(orders);
                param.put("orderDataTable",dataSource);

        }
        //查询所以的异常事件
        if(sb.toString().length()>0){
            List<WorkExceptionList> exceptionLists = workExceptionListMapper.selectWorkExceByWorkId(sb.toString().substring(0,sb.length()-1));
            JRDataSource dataSource = new JRBeanCollectionDataSource(exceptionLists);
            param.put("exceptionData",dataSource);
        }
        param.put("totalActualNum",totalActualNum);
        param.put("totalScrapNum",totalScrapNum);
        param.put("totalStandardHour",new BigDecimal(totalStandardHour).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue());
        param.put("totalHour",totalHour);
        param.put("totalReachRate",totalReachRate);
        param.put("totalZcHour",totalZcHour);
        param.put("totalInput",totalInput);
        param.put("totalWork1",totalWork1);
        param.put("totalWork2",totalWork2);
        param.put("totalWork3",totalWork3);
        param.put("totalProduct",totalProduct);//生产总工时
        //直通率
        param.put("totalDirectPassRate",pjztRate);
        PdfUtil util = new PdfUtil();
        util.exportPdf(path,pdfName,resourceLoader,response,request,param);
        return 1;
    }

    private float getFloat3(float val){
        return  new BigDecimal(val).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
