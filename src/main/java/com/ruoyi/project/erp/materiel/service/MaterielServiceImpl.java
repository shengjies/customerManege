package com.ruoyi.project.erp.materiel.service;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.constant.MaterielConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.fileSourceInfo.domain.FileSourceInfo;
import com.ruoyi.project.erp.fileSourceInfo.mapper.FileSourceInfoMapper;
import com.ruoyi.project.erp.materiel.domain.Materiel;
import com.ruoyi.project.erp.materiel.mapper.MaterielMapper;
import com.ruoyi.project.erp.materielStock.domain.MaterielStock;
import com.ruoyi.project.erp.materielStock.mapper.MaterielStockMapper;
import com.ruoyi.project.erp.materielSupplier.domain.MaterielSupplier;
import com.ruoyi.project.erp.materielSupplier.mapper.MaterielSupplierMapper;
import com.ruoyi.project.product.importConfig.domain.ImportConfig;
import com.ruoyi.project.product.importConfig.mapper.ImportConfigMapper;
import com.ruoyi.project.system.user.domain.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 物料 服务层实现
 *
 * @author zqm
 * @date 2019-04-30
 */
@Service("materiel")
public class MaterielServiceImpl implements IMaterielService {
    @Autowired
    private MaterielMapper materielMapper;

    @Autowired
    private MaterielSupplierMapper materielSupplierMapper; // 供应商物料关联数据层

    @Autowired
    private MaterielStockMapper materielStockMapper; // 物料库存数据层

    @Autowired
    private FileSourceInfoMapper fileSourceInfoMapper; // 文件资源数据层

    @Autowired
    private ImportConfigMapper configMapper;

    /**
     * 查询物料信息
     *
     * @param id 物料ID
     * @return 物料信息
     */
    @Override
    public Materiel selectMaterielById(Integer id) {
        return materielMapper.selectMaterielById(id);
    }

    /**
     * 查询物料列表
     *
     * @param materiel 物料信息
     * @return 物料集合
     */
    @Override
    public List<Materiel> selectMaterielList(Materiel materiel, HttpServletRequest request) {
        User sysUser = JwtUtil.getTokenUser(request);
        if (sysUser == null) return Collections.emptyList();
        if (!User.isSys(sysUser)) {
            materiel.setCompanyId(sysUser.getCompanyId());
        }
        List<Materiel> materielList = materielMapper.selectMaterielList(materiel);
        for (Materiel mat : materielList) {
            // 查询物料是否上传了文件
            List<FileSourceInfo> fileSourceInfos = fileSourceInfoMapper.selectFileSourceInfoBySaveId(sysUser.getCompanyId(), FileConstants.FILE_SAVETYPE_MAT, mat.getId());
            if (StringUtils.isNotEmpty(fileSourceInfos)) {
                mat.setFileFlag(FileConstants.FILE_SAVE_YES);
            }
        }
        return materielList;
    }

    /**
     * 新增物料
     *
     * @param materiel 物料信息
     * @return 结果
     */
    @Override
    public int insertMateriel(Materiel materiel, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        materiel.setCompanyId(user.getCompanyId());
        materiel.setCreateId(user.getUserId().intValue());
        materiel.setCreateName(user.getUserName());
        materiel.setCreateTime(new Date());
        if (materiel.getPriceImport() != null && materiel.getPriceImport() != 0.00f) {
            materiel.setPrice(new BigDecimal(materiel.getPriceImport()));
        }
        return materielMapper.insertMateriel(materiel);
    }

    /**
     * 修改物料
     *
     * @param materiel 物料信息
     * @return 结果
     */
    @Override
    public int updateMateriel(Materiel materiel) {
        if (materiel.getPriceImport() != null && materiel.getPriceImport() != 0.00f) {
            materiel.setPrice(new BigDecimal(materiel.getPriceImport()));
        }
        // 更新物料库存的物料信息
        updateMatlStockInfo(materiel);
        return materielMapper.updateMateriel(materiel);
    }


    /**
     * 更新物料库存信息
     *
     * @param materiel 物料信息
     */
    private void updateMatlStockInfo(Materiel materiel) {
        MaterielStock materielStock = materielStockMapper.selectMaterielStockByMaterielId(materiel.getId());
        if (!StringUtils.isNull(materielStock)) {
            materielStock.setMaterielCode(materiel.getMaterielCode());
            materielStock.setMaterielModel(materiel.getMaterielModel());
            materielStock.setMaterielName(materiel.getMaterielName());
            materielStockMapper.updateMaterielStock(materielStock);
        }
    }

    /**
     * 删除物料对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteMaterielByIds(String ids, HttpServletRequest request) {
        Integer[] materielIds = Convert.toIntArray(ids);
        Materiel materiel = null;
        for (Integer materielId : materielIds) {
            materiel = materielMapper.selectMaterielById(materielId);
            // 校验是否有相关联的物料文件未删除
            List<FileSourceInfo> fileSourceInfos = fileSourceInfoMapper.selectFileSourceInfoBySaveIdAndComId(materielId, JwtUtil.getTokenUser(request).getCompanyId());
            if (!StringUtils.isEmpty(fileSourceInfos)) {
                throw new BusinessException("请先删除" + materiel.getMaterielCode() + "的关联文件");
            }
            // 校验是否有相关的供应商关联信息
            // List<MaterielSupplier> materielSuppliers = materielSupplierMapper.selectMaterielSupplierListByMaterielId(materielId);
            // if (!StringUtils.isEmpty(materielSuppliers)) {
            //     throw new BusinessException("请先删除" + materiel.getMaterielCode() + "的供应商关联");
            // }
            // // 查询对应物料库存记录
            // MaterielStock materielStock = materielStockMapper.selectMaterielStockByMaterielId(materielId);
            // if (!StringUtils.isNull(materielStock)) {
            //     throw new BusinessException(materiel.getMaterielCode() + "存在库存记录不允许删除");
            // }
        }
        return materielMapper.deleteMaterielByIds(Convert.toStrArray(ids));
    }

    /**
     * 导入物料列表
     *
     * @param file            物料列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return
     */
    @Override
    public String importMateriel(MultipartFile file, boolean isUpdateSupport, int type) throws Exception {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            throw new Exception("登录超时，请重新登录");
        }
        int failNum = 0;
        int inserNum = 0;
        StringBuilder failMsg = new StringBuilder();
        StringBuilder successMsg = new StringBuilder();
        //查询对应配置
        ImportConfig config = configMapper.selectImportConfigByType(type);
        if (config == null) {
            throw new Exception("很抱歉，导入失败，请先进行物料导入信息配置");
        }
        //创建excel 文档对象
        Workbook wb = null;
        Sheet sheet = null;
        try {
            wb = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            throw new Exception("很抱歉，导入失败，只支持excel文件导入");
        }
        if (wb == null) {
            throw new Exception("很抱歉，导入失败，只支持excel文件导入");
        }
        sheet = wb.getSheetAt(0);
        if (sheet == null) {
            throw new Exception("很抱歉，导入失败，只支持excel文件导入");
        }
        //获取导入行数
        int rows = sheet.getPhysicalNumberOfRows();
        if (rows <= 0) {
            throw new Exception("很抱歉，导入失败，导入excel不能为空");
        }
        if (rows < config.getRowIndex()) {
            throw new Exception("很抱歉，导入失败，导入excel行数必须大配置开始解析行数,请核对配置是否正确");
        }
        Row row = null;
        List<Materiel> materiels = new ArrayList<>();
        Materiel materiel = null;
        for (int i = config.getRowIndex() - 1; i < rows; i++) {
            row = sheet.getRow(i);
            if (row == null) {
                failNum++;
                failMsg.append("<br/>第" + (i + 1) + "行数据为空");
                continue;
            }
            materiel = new Materiel();
            //获取物料编码
            String mCode = ExcelUtil.getCellValue1(row, config.getCon1() - 1).toString().trim();
            if (StringUtils.isEmpty(mCode)) {
                failNum++;
                failMsg.append("<br/>第" + (i + 1) + "行，物料编码为空");
                continue;
            }
            materiel.setCompanyId(user.getCompanyId());
            materiel.setMaterielCode(mCode);
            //获取物料名称
            String mName = ExcelUtil.getCellValue1(row, config.getCon2() - 1).toString().trim();
            if (StringUtils.isEmpty(mName)) {
                failNum++;
                failMsg.append("<br/>第" + (i + 1) + "行，物料名称为空");
                continue;
            }
            materiel.setMaterielName(mName);
            //获取物料型号
            String mModel = ExcelUtil.getCellValue1(row, config.getCon3() - 1).toString().trim();
            if (StringUtils.isEmpty(mModel)) {
                failNum++;
                failMsg.append("<br/>第" + (i + 1) + "行，物料型号为空");
                continue;
            }
            materiel.setMaterielModel(mModel);
            //获取单价
            if (config.getPrice() > 0) {
                try {
                    float price = Float.parseFloat(ExcelUtil.getCellValue1(row, config.getPrice() - 1).toString());
                    materiel.setPriceImport(price);
                    materiel.setPrice(new BigDecimal(price));
                } catch (Exception e) {
                    failNum++;
                    failMsg.append("<br/>第" + (i + 1) + "行,物料单价解析失败");
                    continue;
                }
            }
            //获取产品单位
            if(config.getUnit() >0){
                String unit = ExcelUtil.getCellValue1(row,config.getUnit() -1).toString().trim();
                materiel.setUnit(unit);
            }
            //获取备注信息
            if(config.getCon5() > 0){
                String remark = ExcelUtil.getCellValue1(row,config.getCon5() -1).toString();
                materiel.setRemark(remark);
            }
            materiel.setCreateId(user.getUserId().intValue());
            materiel.setCreateName(user.getUserName());
            materiel.setCreateTime(new Date());
            materiels.add(materiel);
        }
        if(wb != null){
            wb.close();
        }
        if(materiels.size() <=0 || failNum > 0){
            failMsg.insert(0,"很抱歉，导入失败");
            throw new Exception(failMsg.toString());
        }
        failNum = 0;
        int a =0;
        for (Materiel ma : materiels) {
            try {
                a++;
                //查询对应物料是否存在
                Materiel m = materielMapper.selectMaterielByMaterielCode(ma.getMaterielCode(),user.getCompanyId());
                if(StringUtils.isNull(m)){
                    inserNum++;
                    materielMapper.insertMateriel(ma);
                    successMsg.append("<br/>"+a+"、物料:"+ma.getMaterielCode()+"新增成功");
                }else if(isUpdateSupport){
                    ma.setId(m.getId());
                    ma.setCreateTime(m.getCreateTime());
                    ma.setCreateId(m.getCreateId());
                    ma.setCreateName(m.getCreateName());
                    inserNum++;
                    materielMapper.updateMateriel(ma);
                    successMsg.append("<br/>、物料:"+ma.getMaterielCode()+"修改成功");
                }else {
                    failNum ++;
                    failMsg.append("<br/>"+a+"、物料:"+ma.getMaterielCode()+"已存在");
                }
            }catch (Exception e){
                failNum ++;
                failMsg.append("<br/>"+failNum+"、物料:"+ma.getMaterielCode()+"导入失败："+e.getMessage());
            }
        }
        if(failNum > 0){
            failMsg.insert(0,"很抱歉，部分导入失败！共"+failNum+"条数据:");
            throw new Exception(failMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + inserNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 通过供应商id查询和供应商关联的物料列表
     *
     * @param supplierId 供应商id
     * @return 结果
     */
    @Override
    public List<Materiel> materielListBySupplierId(Integer supplierId, HttpServletRequest request) {
        List<Materiel> materielList = new ArrayList<>();
        Materiel materiel = null;
        List<MaterielSupplier> materielSupplierList = materielSupplierMapper.selectMaterielSupplierListByMatIdAndSupId(null, supplierId);
        for (MaterielSupplier materielSupplier : materielSupplierList) {
            materiel = materielMapper.selectMaterielById(materielSupplier.getMaterielId());
            materiel.setSupplierCode(materielSupplier.getSupplierCode() == null ? "" : materielSupplier.getSupplierCode());
            materielList.add(materiel);
        }
        // 查询物料不良品数量
        for (Materiel mat : materielList) {
            mat.setBadNumber(materielStockMapper.selectMaterielStockByMatCodeAndComId(mat.getMaterielCode(), JwtUtil.getTokenUser(request).getCompanyId()).getBadNumber());
        }
        return materielList;
    }

    /**
     * 通过供应商id和物料id查询物料信息
     *
     * @param supplierId 供应商id
     * @param materielId 物料id
     * @return
     */
    @Override
    public Materiel materielListByMaterielId(Integer supplierId, Integer materielId) {
        Materiel materiel = null;
        List<MaterielSupplier> materielSuppliers = materielSupplierMapper.selectMaterielSupplierListByMatIdAndSupId(materielId, supplierId);
        if (!StringUtils.isEmpty(materielSuppliers)) {
            MaterielSupplier materielSupplier = materielSuppliers.get(0);
            materiel = materielMapper.selectMaterielById(materielId);
            materiel.setSupplierCode(materielSupplier.getSupplierCode());
        }
        return materiel;
    }

    /**
     * 根据供应商id查询对应的物料信息
     *
     * @param sid 供应商id
     * @return
     */
    @Override
//    @DataSource(DataSourceType.ERP)
    public List<Materiel> selectMaterielBySupplierId(int sid, HttpServletRequest request) {
        return materielMapper.selectMaterielBySupplierId(JwtUtil.getTokenUser(request).getCompanyId(), sid);
    }

    /**
     * 校验物料编码唯一性
     *
     * @param materiel 物料
     * @return 结果
     */
    @Override
    public String checkMaterielCodeUnique(Materiel materiel) {
        Materiel materielUnique = materielMapper.selectMaterielByMaterielCode(materiel.getMaterielCode(), materiel.getCompanyId());
        if (!StringUtils.isNull(materielUnique) && materiel.getId() != materielUnique.getId()) { // 存在相同的编码信息
            return MaterielConstants.PRODUCT_CODE_NOT_UNIQUE;
        }
        return MaterielConstants.PRODUCT_CODE_UNIQUE;
    }

    /**
     * 通过公司id查询供公司物料信息
     *
     * @return
     */
    @Override
    public List<Materiel> selectAllMatByComId() {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return Collections.emptyList();
        }
        return materielMapper.selectAllMatByComId(user.getCompanyId());
    }

    /**
     * 查询公司的物料名称信息
     *
     * @return 结果
     */
    @Override
    public List<Materiel> selectAllMatNameByComId(Cookie[] cookies) {
        User user = JwtUtil.getTokenCookie(cookies);
        if (user == null) {
            return Collections.emptyList();
        }
        return materielMapper.selectAllMatNameByComId(user.getCompanyId());
    }
}
