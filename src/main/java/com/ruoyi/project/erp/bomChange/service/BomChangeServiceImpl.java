package com.ruoyi.project.erp.bomChange.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.poi.ExcelUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.productBom.domain.ProductBom;
import com.ruoyi.project.erp.productBom.domain.ProductBomDetails;
import com.ruoyi.project.erp.productBom.mapper.ProductBomMapper;
import com.ruoyi.project.erp.productBom.service.IProductBomService;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.system.user.domain.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.bomChange.mapper.BomChangeMapper;
import com.ruoyi.project.erp.bomChange.domain.BomChange;
import com.ruoyi.project.erp.bomChange.service.IBomChangeService;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * BOM更改记录 服务层实现
 * 
 * @author sj
 * @date 2019-06-26
 */
@Service
public class BomChangeServiceImpl implements IBomChangeService 
{
	@Autowired
	private BomChangeMapper bomChangeMapper;

	@Autowired
	private ProductBomMapper productBomMapper;

	@Autowired
	private IsoMapper isoMapper;

	@Autowired
	private IProductBomService productBomService;

	@Value("${file.iso}")
	private String isoFileUrl;

	/**
     * 查询BOM更改记录信息
     * 
     * @param id BOM更改记录ID
     * @return BOM更改记录信息
     */
    @Override
	public BomChange selectBomChangeById(Integer id)
	{
	    return bomChangeMapper.selectBomChangeById(id);
	}
	
	/**
     * 查询BOM更改记录列表
     * 
     * @param bomChange BOM更改记录信息
     * @return BOM更改记录集合
     */
	@Override
	public List<BomChange> selectBomChangeList(BomChange bomChange)
	{
		bomChange.setCompanyId(JwtUtil.getTokenUser(ServletUtils.getRequest()).getCompanyId());
	    return bomChangeMapper.selectBomChangeList(bomChange);
	}
	
    /**
     * 新增BOM更改记录
     * 
     * @param bomChange BOM更改记录信息
     * @return 结果
     */
	@Override
	public int insertBomChange(BomChange bomChange)
	{
	    return bomChangeMapper.insertBomChange(bomChange);
	}

	/**
	 * 审核BOM改变内容
	 * @param id 编号
	 * @return
	 */
	@Override
	@Transactional
	public int examineBomChange(int id) {
		User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
		BomChange bomChange = bomChangeMapper.selectBomChangeById(id);
		if(bomChange == null || bomChange.getNewBomId() == null)
		return 0;
		//修改对应BOM版本
		ProductBom bom = productBomMapper.selectProductBomById(bomChange.getNewBomId());
		if(bom == null)return  0;
		Iso iso = isoMapper.selectIsoById(9);
		if(iso == null)return 0;
		//将对应产品所以的BOM 标记修改为0
		productBomMapper.updateBomSignByProductId(bomChange.getpId());
		productBomMapper.updateBomSignAndSsignById(bom.getId());
		BomChange change = new BomChange();
		change.setId(bomChange.getId());
		change.setShStatus(1);//审核通过
		change.setShTime(new Date());
		change.setShId(JwtUtil.getTokenUser(ServletUtils.getRequest()).getUserId().intValue());
		bomChangeMapper.updateBomChange(change);
		change = bomChangeMapper.selectBomChangeById(bomChange.getId());
		//查询详情
		List<ProductBomDetails> newDetailBom = productBomService.selectBomDetailByBomId(bom.getId());
		//查询变更信息;
		ProductBom oldBom = productBomService.selectProductBomById(change.getOldBomId());
		List<ProductBomDetails> oldDetailBom = productBomService.selectBomDetailByBomId(oldBom.getId());
		// 创建工作簿对象
		Workbook wb = ExcelUtils.createWorkbook();
		Sheet sheet = ExcelUtils.createSheet(wb);
		String[] titel = {"当前版本", "BOM编号", "产品编码", "产品名称", "产品型号" ,"备注信息"};
		String[] detailTitel = {"物料编码", "物料名称", "物料型号", "用量", "单价", "总价", "单位", "位号", "备注"};
		int cellLength = 9;
		for (int i = 0; i < cellLength; i++) {
			sheet.setColumnWidth(i, 252 * 14 + 323);
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellLength - 1));
		Row row = sheet.createRow(0);
		row.setHeight((short) (14 * 36)); // 设置行高
		//表头
		Cell cell = ExcelUtils.creatCell(row, 0, "产品 " + bom.getProductCode() + " 的BOM单更变记录");
		cell.setCellStyle(ExcelUtils.createCellStyle(wb, 18, true));
		row = sheet.createRow(1);
		ExcelUtils.creatCell(row, 0, "由版本:v" + oldBom.getBomVersion() +
				",BOM编号:"+oldBom.getBomCode()+"变更为版本:v"+bom.getBomVersion()+",BOM编号:"+bom.getBomCode());
		CellStyle style = ExcelUtils.createCellStyle(wb, 12, false);
		row = sheet.createRow(2);
		int index = 0;
		for (String val : titel) {
			Cell version = ExcelUtils.creatCell(row, index, val);
			version.setCellStyle(style);
			index++;
		}
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, cellLength - 1));
		//主信息
		row = sheet.createRow(3);
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
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 5, cellLength - 1));
		//详情信息
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, cellLength - 1));
		row = sheet.createRow(5);
		ExcelUtils.creatCell(row, 0, "变更前,版本:v"+oldBom.getBomVersion()+"，BOM编号:"+oldBom.getBomCode());
		index = 0;
		row = sheet.createRow(6);
		Cell det = null;
		for (String val : detailTitel) {
			det = ExcelUtils.creatCell(row, index, val);
			det.setCellStyle(style);
			index++;
		}
		index = 7;
		for (ProductBomDetails detail : oldDetailBom) {
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

		sheet.addMergedRegion(new CellRangeAddress(index+2, index+2, 0, cellLength - 1));
		row = sheet.createRow(index+2);
		ExcelUtils.creatCell(row, 0, "变更后,版本:v"+bom.getBomVersion()+"，BOM编号:"+bom.getBomCode());
		row = sheet.createRow(index+3);
		int b =0;
		for (String val : detailTitel) {
			det = ExcelUtils.creatCell(row, b, val);
			det.setCellStyle(style);
			b++;
		}
		index = index+4;
		for (ProductBomDetails detail : newDetailBom) {
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

		sheet.addMergedRegion(new CellRangeAddress(index+2, index+2, 0, cellLength - 1));
		row = sheet.createRow(index+2);
		row.setHeight((short) (14 * 300)); // 设置行高
		ExcelUtils.creatCell(row, 0, change.getChangeText().replaceAll("<br/>",";")).setCellStyle(style);
		String fileName = bom.getBomCode();
		OutputStream out =null;
		File file = null;
		try {
			file = new File(iso.getDisk()+File.separator+fileName+".xlsx");
			file.createNewFile();
			out = new FileOutputStream(file);
			wb.write(out);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(wb != null){
					wb.close();
				}
				if(out != null){
					out.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		Iso ex = new Iso();
		ex.setcName(fileName+".xlsx");
		ex.seteName(fileName);
		ex.setDisk(iso.getDisk());
		ex.setDiskPath(iso.getDiskPath());
		ex.setPath(isoFileUrl+iso.getDiskPath()+"/"+fileName+".xlsx");
		ex.setiType(1);
		ex.setFileSize(file != null?((file.length())/1024)+"kb":"0kb");
		ex.setCategory(0);
		ex.setCompanyId(u.getCompanyId());
		ex.setParentId(iso.getIsoId());
		ex.setGrParentId(iso.getParentId());
		ex.setcId(u.getUserId().intValue());
		ex.setcTime(new Date());
		ex.setDefId(0);
		isoMapper.insertIso(ex);
		return  1;
	}



	/**
	 * 取消BOM更改信息
	 * @param id id
	 * @return
	 */
	@Override
	public int cancelBomChange(int id) {
		return bomChangeMapper.cancelBomChange(id);
	}

	/**
	 * 根据新版本的BOM id查询变更记录信息
	 * @param bid BOM id
	 * @return
	 */
	@Override
	public BomChange selectBomChangeByNewBomId(int bid) {
		return bomChangeMapper.selectBomChangeByNewBomId(bid);
	}
}
