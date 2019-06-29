package com.ruoyi.project.erp.productBom.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.poi.ExcelUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.bomChange.domain.BomChange;
import com.ruoyi.project.erp.bomChange.mapper.BomChangeMapper;
import com.ruoyi.project.erp.materiel.domain.Materiel;
import com.ruoyi.project.erp.materiel.mapper.MaterielMapper;
import com.ruoyi.project.erp.productBom.domain.BomConfig;
import com.ruoyi.project.erp.productBom.domain.ProductBomDetails;
import com.ruoyi.project.erp.productBom.mapper.BomConfigMapper;
import com.ruoyi.project.erp.productBom.mapper.ProductBomDetailsMapper;
import com.ruoyi.project.page.pageInfo.domain.PageReal;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.system.ser.domain.Ser;
import com.ruoyi.project.system.user.domain.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.productBom.mapper.ProductBomMapper;
import com.ruoyi.project.erp.productBom.domain.ProductBom;
import com.ruoyi.project.erp.productBom.service.IProductBomService;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 产品BOM单 服务层实现
 *
 * @author sj
 * @date 2019-06-24
 */
@Service
public class ProductBomServiceImpl implements IProductBomService {
    @Autowired
    private ProductBomMapper productBomMapper;

    @Autowired
    private BomConfigMapper bomConfigMapper;

    @Autowired
    private DevProductListMapper productListMapper;

    @Autowired
    private MaterielMapper materielMapper;

    @Autowired
    private ProductBomDetailsMapper productBomDetailsMapper;

    @Autowired
    private BomChangeMapper bomChangeMapper;

    /**
     * 查询产品BOM单信息
     *
     * @param id 产品BOM单ID
     * @return 产品BOM单信息
     */
    @Override
    public ProductBom selectProductBomById(Integer id) {
        return productBomMapper.selectProductBomById(id);
    }

    /**
     * 查询产品BOM单列表
     *
     * @param productBom 产品BOM单信息
     * @return 产品BOM单集合
     */
    @Override
    public List<ProductBom> selectProductBomList(ProductBom productBom) {
        return productBomMapper.selectProductBomList(productBom);
    }

    /**
     * 根据产品id查询对应的BOM版本信息
     *
     * @param pid 产品id
     * @return
     */
    @Override
    public List<ProductBom> selectBomByPid(int pid) {
        return productBomMapper.selectBomByPid(pid);
    }

    /**
     * 导入bom单 多文件
     * @param files
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertProductBom(MultipartFile[] files) throws Exception {
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
        int failureNum = 0;//解析错误数量
        int companyId = JwtUtil.getTokenUser(ServletUtils.getRequest()).getCompanyId();//公司id
        //查询对应bom配置是否存在
        BomConfig config = bomConfigMapper.selectBomConfigLimit1();
        if (config == null) {
            failureMsg.insert(0, "很抱歉，导入失败！请先进行BOM导入配置");
            throw new Exception(failureMsg.toString());
        }
        /** 单位下标 */
        int unit = config.getUnit() == null ? 0 : config.getUnit();
        /** 位号下标 */
        int placeNumber = config.getPlaceNumber() == null ? 0 : config.getPlaceNumber();
        /** 备注下标 */
        int remarkIndex = config.getRemarkIndex() == null ? 0 : config.getRemarkIndex();
        //文件名称
        String fileName = null;
        //产品编码
        String code =null;
        //excel 文档对象
        Workbook wb = null;
        // sheet 对象
        Sheet sheet = null;
        //行对象
        Row row = null;
        //BOM 详情对象
        ProductBomDetails details = null;
        //详情集合对象
        List<ProductBomDetails> list = null;
        //BOM对象
        ProductBom bom = null;
        //BOM 对象集合
        List<ProductBom> boms = new ArrayList<>();
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename();
            code = fileName.substring(0,fileName.lastIndexOf("."));
            //查询对应产品是否存在
            DevProductList productList = productListMapper.selectDevProductByCode(companyId,code);
            if (productList == null) {
                failureNum++;
                failureMsg.append("很抱歉，导入"+fileName+"文件失败！产品不存在");
                continue;
            }
            //查询对应产品是否已经导入bom
            ProductBom productBom = productBomMapper.selectBomByProductId(productList.getId());
            if (productBom != null) {
                failureNum++;
                failureMsg.append( "很抱歉，导入"+fileName+"文件失败！该产品:"+code+"已经导入BOM单");
                continue;
            }
            try {
                //进行数据解析
                wb = WorkbookFactory.create(file.getInputStream());
            } catch (Exception e) {
                failureNum++;
                failureMsg.append( "很抱歉，系统解析"+fileName+"文件失败！");
                continue;
            }
            if (wb == null) {
                failureNum++;
                failureMsg.append( "很抱歉，系统解析"+fileName+"文件失败！");
                continue;
            }
             sheet = wb.getSheetAt(0);
            if(sheet == null){
                failureNum++;
                failureMsg.append( "很抱歉，系统解析"+fileName+"文件失败！");
                continue;
            }
            int rows = sheet.getPhysicalNumberOfRows();
            if (rows <= 0) {
                failureNum++;
                failureMsg.append("很抱歉，导入失败！导入文件"+fileName+"中行数为0");
                continue;
            }
            if (rows < config.getRowIndex()) {
                failureNum++;
                failureMsg.append("很抱歉，导入失败！导入文件"+fileName+"中行数小于BOM配置中开始解析行数");
                continue;
            }
            //创建详情对象集合
            list = new ArrayList<>();
            for (int i = config.getRowIndex() - 1; i < rows; i++) {
                try {
                    row = sheet.getRow(i);
                    if (row == null) {
                        failureNum++;
                        failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据为空");
                        continue;
                    }
                    //判断对应的物料编号是否存在
                    String wCode = ExcelUtil.getCellValue1(row, config.getMaterielCode() - 1).toString().trim();
                    if (StringUtils.isEmpty(wCode)) {
                        failureNum++;
                        failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据,物料编码为空");
                        continue;
                    }
                    //创建详情对象
                    details = new ProductBomDetails();
                    //查询物料是否存在
                    Materiel materiel = materielMapper.selectMaterielByMaterielCode(wCode, companyId);
                    if (materiel == null) {
                        failureNum++;
                        failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据,导入的物料不存在，请先导入对应的物料信息");
                        continue;
                    }
                    details.setBomDetailsType(0);
                    details.setDetailId(materiel.getId());
                    details.setDetailCode(materiel.getMaterielCode());
                    //获取物料名称
                    String wName = ExcelUtil.getCellValue1(row, config.getMaterielName() - 1).toString().trim();
                    if (StringUtils.isEmpty(wName)) {
                        failureNum++;
                        failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据,物料名称为空");
                        continue;
                    }
                    //判断物料名称是否相等
                    if (!wName.equals(materiel.getMaterielName())) {
                        failureNum++;
                        failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据,物料编码:" + wCode + ",物料名称与系统中的物料名称不同");
                        continue;
                    }
                    details.setDetailName(materiel.getMaterielName());
                    //判断物料型号
                    String wModel = ExcelUtil.getCellValue1(row, config.getMaterielModel() - 1).toString().trim();
                    if (StringUtils.isEmpty(wModel)) {
                        failureNum++;
                        failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据,物料型号为空");
                        continue;
                    }
                    //判断物料型号是否相等
                    if (!wModel.equals(materiel.getMaterielModel())) {
                        failureNum++;
                        failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据,物料编码:" + wCode + ",物料型号与系统中的物料型号不同");
                        continue;
                    }
                    details.setDetailModel(materiel.getMaterielModel());
                    //判断用量
                    try {
                        int number = Convert.toInt(ExcelUtil.getCellValue1(row, config.getNumber() - 1));
                        if (number <= 0) {
                            failureNum++;
                            failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据,物料用量必须大于0");
                            continue;
                        }
                        details.setOneNum(number);
                        details.setPrice(materiel.getPrice());
                        details.setTotalPrice(new BigDecimal(materiel.getPrice().floatValue() * number));
                    } catch (Exception e) {
                        failureNum++;
                        failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据,物料用量必须为数字");
                        continue;
                    }
                    details.setCreateId(u.getUserId().intValue());
                    details.setCreateTime(new Date());
                    //单位
                    if (unit > 0) {
                        String wUnit = ExcelUtil.getCellValue1(row, unit - 1).toString().trim();
                        details.setUnit(wUnit);
                    }
                    //位号
                    if (placeNumber > 0) {
                        String wh = ExcelUtil.getCellValue1(row, placeNumber - 1).toString().trim();
                        details.setPlaceNumber(wh);
                    }
                    //备注
                    if (remarkIndex > 0) {
                        String remark = ExcelUtil.getCellValue1(row, remarkIndex - 1).toString().trim();
                        details.setRemark(remark);
                    }
                    list.add(details);
                } catch (Exception e) {
                    failureNum++;
                    failureMsg.append("<br/>在文件"+fileName+"的第" + (i + 1) + "行数据导入失败");
                    continue;
                }
            }
            //创建主表
            bom = new ProductBom();
            bom.setCompanyId(companyId);
            bom.setBomCode(CodeUtils.getBomCode());
            bom.setBomVersion(1);
            bom.setProductId(productList.getId());
            bom.setProductCode(productList.getProductCode());
            bom.setProductName(productList.getProductName());
            bom.setProductModel(productList.getProductModel());
            bom.setCreateId(u.getUserId().intValue());
            bom.setCreateTime(new Date());
            bom.setsSign(1);
            bom.setRemark("初始版本");
            bom.setDetails(list);
            boms.add(bom);
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "<br/>很抱歉，导入失败！");
            throw new Exception(failureMsg.toString());
        }
        if (boms.size() <= 0) {
            failureMsg.insert(0, "<br/>很抱歉，系统导入失败！");
            throw new Exception(failureMsg.toString());
        } else {
            for (ProductBom productBom : boms) {
                try {
                    //新增BOM
                    productBomMapper.insertProductBom(productBom);
                    //新增bom详情
                    int i = 1;
                    for (ProductBomDetails bomDetails : productBom.getDetails()) {
                        bomDetails.setBomId(productBom.getId());
                        bomDetails.setbIndex(i);
                        productBomDetailsMapper.insertProductBomDetails(bomDetails);
                        i++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    failureNum++;
                    failureMsg.append( "<br/>很抱歉，系统出错,在导入产品:"+productBom.getProductCode()+" BOM时出错!");
                    throw new Exception(failureMsg.toString());
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "很抱歉，导入失败！");
                throw new Exception(failureMsg.toString());
            }
            successMsg.insert(0, "恭喜您，数据导入成功");
            return successMsg.toString();
        }
    }


    /**
     * 修改产品BOM单
     *
     * @param productBom 产品BOM单信息
     * @return 结果
     */
    @Override
    @Transactional
    public String updateProductBom(ProductBom productBom) throws Exception {
        User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
        int failNum = 0;
        StringBuilder failMsg = new StringBuilder();
        StringBuilder changeText = new StringBuilder();
        StringBuilder msg = null;
        if (productBom.getDetails() == null && productBom.getDetails().size() <= 0) {
            throw new Exception("很抱歉，更改失败！BOM详情内容不能为空");
        }
        //查询对应产品是否存在
        DevProductList productList = productListMapper.selectDevProductListById(productBom.getProductId());
        if (productList == null) {
            failMsg.insert(0, "很抱歉，更改失败！您选择的产品不存在");
            throw new Exception(failMsg.toString());
        }
        //查询修改BOM 是否存在
        ProductBom bom = productBomMapper.selectProductBomById(productBom.getId());
        if (bom == null) throw new Exception("很抱歉，更改失败！BOM单号:" + productBom.getBomCode() + "不存在");
        //判断是否有变化
        for (ProductBomDetails detail : productBom.getDetails()) {
            //查询对应的物料是否存在
            Materiel materiel = materielMapper.selectMaterielById(detail.getDetailId());
            if (materiel == null) {
                failNum++;
                failMsg.append("<br/>物料:" + detail.getDetailCode() + "不存在");
                continue;
            }
            //根据BOM id 详情标识查询对应详情是否存在
            ProductBomDetails bomDetails = productBomDetailsMapper.selectBomDetailByBidAndIndex(bom.getId(), 0, detail.getbIndex());
            if (bomDetails == null) {
                changeText.append("<br/>新增 物料编码:" + materiel.getMaterielCode() + ",物料名称:" + materiel.getMaterielName() + ",物料型号:" + materiel.getMaterielModel() + ",用量:" + detail.getOneNum());
                if (!StringUtils.isEmpty(detail.getUnit())) {
                    changeText.append(",单位:" + detail.getUnit());
                }
                if (!StringUtils.isEmpty(detail.getPlaceNumber())) {
                    changeText.append(",位号:" + detail.getPlaceNumber());
                }
                if (!StringUtils.isEmpty(detail.getRemark())) {
                    changeText.append(",备注:" + detail.getRemark());
                }
            } else {
                //信息对比
                msg = new StringBuilder();
                //对比物料编号
                if (!materiel.getMaterielCode().equals(bomDetails.getDetailCode())) {
                    msg.append("物料编号由：" + bomDetails.getDetailCode() + "，修改为：" + materiel.getMaterielCode() + ",");
                }
                //对比物料名称
                if (!materiel.getMaterielName().equals(bomDetails.getDetailName())) {
                    msg.append("物料名称由：" + bomDetails.getDetailName() + "，修改为：" + materiel.getMaterielName() + ",");
                }
                //对比物料型号
                if (!materiel.getMaterielModel().equals(bomDetails.getDetailModel())) {
                    msg.append("物料型号由：" + bomDetails.getDetailModel() + "，修改为：" + materiel.getMaterielModel() + ",");
                }
                //对比用量
                if (detail.getOneNum() != bomDetails.getOneNum()) {
                    msg.append("用量由：" + bomDetails.getOneNum() + "，修改为：" + detail.getOneNum() + ",");
                }
                //对比位号
                if (!detail.getPlaceNumber().equals(bomDetails.getPlaceNumber()) && bomDetails.getPlaceNumber() != null && !detail.getPlaceNumber().equals("")) {
                    msg.append("位号由：" + bomDetails.getPlaceNumber() + "，修改为：" + detail.getPlaceNumber() + ",");
                }
                //对比单位
                if (!detail.getUnit().equals(bomDetails.getUnit()) && bomDetails.getUnit() != null && !detail.getUnit().equals("")) {
                    msg.append("单位由：" + bomDetails.getUnit() + "，修改为：" + detail.getUnit() + ",");
                }
                //对比备注
                if (!detail.getRemark().equals(bomDetails.getRemark())) {
                    msg.append("备注由：" + bomDetails.getRemark() + "，修改为：" + detail.getRemark() + ",");
                }
                //判断是否添加修改信息
                if (!StringUtils.isEmpty(msg.toString())) {
                    changeText.append("<br/>物料编码：" + materiel.getMaterielCode() + "，修改点：" + msg.toString());
                }
            }
            detail.setDetailCode(materiel.getMaterielCode());
            detail.setDetailName(materiel.getMaterielName());
            detail.setDetailModel(materiel.getMaterielModel());
            detail.setBomDetailsType(0);
            detail.setPrice(materiel.getPrice());
            detail.setTotalPrice(new BigDecimal(materiel.getPrice().floatValue() * detail.getOneNum()));
            detail.setCreateId(u.getUserId().intValue());
            detail.setCreateTime(new Date());
        }
        if (failNum > 0) {
            failMsg.insert(0, "更改失败：");
            throw new Exception(failMsg.toString());
        }
        //新增BOM信息
        ProductBom newBom = new ProductBom();
        newBom.setCompanyId(u.getCompanyId());
        newBom.setBomCode(CodeUtils.getBomCode());
        newBom.setBomVersion(1);
        newBom.setProductId(productList.getId());
        newBom.setProductCode(productList.getProductCode());
        newBom.setProductName(productList.getProductName());
        newBom.setProductModel(productList.getProductModel());
        newBom.setRemark(productBom.getRemark());
        newBom.setCreateId(u.getUserId().intValue());
        newBom.setCreateTime(new Date());
        newBom.setSign(0);
        //查询对应产品最新版本BOM单
        ProductBom bom1 = productBomMapper.selectNewBomVersion(productList.getId());
        if (bom1 != null) {
            newBom.setBomVersion(bom1.getBomVersion() + 1);
        }
        //新增BOM单
        productBomMapper.insertProductBom(newBom);
        //新增详情
        for (ProductBomDetails detail : productBom.getDetails()) {
            detail.setBomId(newBom.getId());
            productBomDetailsMapper.insertProductBomDetails(detail);
        }
        //查询上一个修改BOM单详情信息
        List<ProductBomDetails> details = productBomDetailsMapper.selectProductBomDetailsList(bom.getId());
        if (details == null) {
            throw new Exception("更改失败,BOM单：" + bom.getBomCode() + ",详情信息不存在");
        }
        //判断是否被删除
        for (ProductBomDetails detail : details) {
            ProductBomDetails bomDetails = productBomDetailsMapper.selectBomDetailByBidAndIndex(newBom.getId(), 0, detail.getbIndex());
            if (bomDetails == null) {
                changeText.append("<br/>删除 物料编码：" + detail.getDetailCode());
            }
        }
        if (!StringUtils.isEmpty(changeText.toString())) {
            changeText.insert(0, "更改内容：");
        }
        //保存更改信息
        BomChange change = new BomChange();
        change.setCompanyId(u.getCompanyId());
        change.setcId(u.getUserId().intValue());
        change.setpId(productList.getId());
        change.setOldVersion(bom.getBomVersion());
        change.setNewVersion(newBom.getBomVersion());
        change.setcTime(new Date());
        change.setShStatus(0);//未审核
        change.setNewBomId(newBom.getId());
        change.setOldBomId(bom.getId());
        change.setChangeText(changeText.toString());
        bomChangeMapper.insertBomChange(change);
        return "更改成功";
    }


    /**
     * 查询bom配置信息
     *
     * @return
     */
    @Override
    public BomConfig selectBomConfigLimit1() {
        return bomConfigMapper.selectBomConfigLimit1();
    }

    /**
     * 保存bom单解析配置
     *
     * @param config 配置信息
     * @return
     */
    @Override
    @Transactional
    public int saveBomConfig(BomConfig config) {
        //删除以前配置信息
        bomConfigMapper.deleteBomConfigAll();
        config.setCompanyId(JwtUtil.getTokenCookie(ServletUtils.getRequest().getCookies()).getCompanyId());
        config.setCreateTime(new Date());
        //新增新配置
        return bomConfigMapper.insertBomConfig(config);
    }

    /**
     * 根据bomid查询对应的bom详情数据
     *
     * @param bid bom id
     * @return
     */
    @Override
    public List<ProductBomDetails> selectBomDetailByBomId(int bid) {
        return productBomDetailsMapper.selectProductBomDetailsList(bid);
    }

    /**
     * 导出BOM 单信息
     *
     * @param id BOM单id
     * @return
     */
    @Override
    public Workbook export(int id) {
        String[] titel = {"当前版本", "BOM编号", "产品编码", "产品名称", "产品型号", "备注信息"};
        String[] detailTitel = {"物料编码", "物料名称", "物料型号", "用量", "单价", "总价", "单位", "位号", "备注"};
        //查询BOM信息
        ProductBom bom = productBomMapper.selectProductBomById(id);
        //详情信息
        List<ProductBomDetails> details = productBomDetailsMapper.selectProductBomDetailsList(id);
        // 创建工作簿对象
        Workbook wb = ExcelUtils.createWorkbook();
        Sheet sheet = ExcelUtils.createSheet(wb);
        int cellLength = 9;
        for (int i = 0; i < cellLength; i++) {
            sheet.setColumnWidth(i, 252 * 14 + 323);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellLength - 1));
        //设置表头
        // 表头，公司名称
        Row row = sheet.createRow(0);
        row.setHeight((short) (14 * 36)); // 设置行高
        //表头
        Cell cell = ExcelUtils.creatCell(row, 0, "产品 " + bom.getProductCode() + " 的BOM单");
        cell.setCellStyle(ExcelUtils.createCellStyle(wb, 18, true));
        //主信息
        CellStyle style = ExcelUtils.createCellStyle(wb, 12, false);
        row = sheet.createRow(1);
        int index = 0;
        for (String val : titel) {
            Cell version = ExcelUtils.creatCell(row, index, val);
            version.setCellStyle(style);
            index++;
        }
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, cellLength - 1));
        //主信息
        row = sheet.createRow(2);
        Cell zCell = ExcelUtils.creatCell(row, 0, "v" + bom.getBomVersion());
        zCell.setCellStyle(style);
        zCell = ExcelUtils.creatCell(row, 1, bom.getBomCode());
        zCell.setCellStyle(style);
        zCell = ExcelUtils.creatCell(row, 2, bom.getProductCode());
        zCell.setCellStyle(style);
        zCell = ExcelUtils.creatCell(row, 3, bom.getProductName());
        zCell.setCellStyle(style);
        zCell = ExcelUtils.creatCell(row, 4, bom.getProductModel());
        zCell.setCellStyle(style);
        ExcelUtils.creatCell(row, 5, bom.getRemark());
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, cellLength - 1));
        //详情信息
        index = 0;
        row = sheet.createRow(3);
        for (String val : detailTitel) {
            Cell detail = ExcelUtils.creatCell(row, index, val);
            detail.setCellStyle(style);
            index++;
        }
        index = 4;
        for (ProductBomDetails detail : details) {
            row = sheet.createRow(index);
            ExcelUtils.creatCell(row, 0, detail.getDetailCode());
            ExcelUtils.creatCell(row, 1, detail.getDetailName());
            ExcelUtils.creatCell(row, 2, detail.getDetailModel());
            ExcelUtils.creatCell(row, 3, detail.getOneNum() + "");
            ExcelUtils.creatCell(row, 4, detail.getPrice() + "");
            ExcelUtils.creatCell(row, 5, detail.getTotalPrice() + "");
            ExcelUtils.creatCell(row, 6, detail.getUnit() + "");
            ExcelUtils.creatCell(row, 7, detail.getPlaceNumber());
            ExcelUtils.creatCell(row, 8, detail.getRemark());
            index++;
        }
        return wb;
    }
}
