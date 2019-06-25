package com.ruoyi.project.erp.productBom.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.materiel.domain.Materiel;
import com.ruoyi.project.erp.materiel.mapper.MaterielMapper;
import com.ruoyi.project.erp.productBom.domain.BomConfig;
import com.ruoyi.project.erp.productBom.domain.ProductBomDetails;
import com.ruoyi.project.erp.productBom.mapper.BomConfigMapper;
import com.ruoyi.project.page.pageInfo.domain.PageReal;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.system.ser.domain.Ser;
import com.ruoyi.project.system.user.domain.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
public class ProductBomServiceImpl implements IProductBomService 
{
	@Autowired
	private ProductBomMapper productBomMapper;

	@Autowired
	private BomConfigMapper bomConfigMapper;

	@Autowired
	private DevProductListMapper productListMapper;

	@Autowired
	private MaterielMapper materielMapper;

	/**
     * 查询产品BOM单信息
     * 
     * @param id 产品BOM单ID
     * @return 产品BOM单信息
     */
    @Override
	public ProductBom selectProductBomById(Integer id)
	{
	    return productBomMapper.selectProductBomById(id);
	}
	
	/**
     * 查询产品BOM单列表
     * 
     * @param productBom 产品BOM单信息
     * @return 产品BOM单集合
     */
	@Override
	public List<ProductBom> selectProductBomList(ProductBom productBom)
	{
	    return productBomMapper.selectProductBomList(productBom);
	}

	/**
	 * 导入bom单
	 * @param file 导入文件
	 * @param pid 产品id
	 * @return 结果
	 */
	@Override
	public String insertProductBom(MultipartFile file, int pid) throws Exception {
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		User u =  JwtUtil.getTokenUser(ServletUtils.getRequest());
		int failureNum = 0;//解析错误数量
		int companyId = JwtUtil.getTokenUser(ServletUtils.getRequest()).getCompanyId();//公司id
		//查询对应bom配置是否存在
		BomConfig config = bomConfigMapper.selectBomConfigLimit1();
		if(config == null){
			failureMsg.insert(0,"很抱歉，导入失败！请先进行BOM导入配置");
			throw new Exception(failureMsg.toString());
		}
		/** 单位下标 */
		 int unit  = config.getUnit()==null?0:config.getUnit();
		/** 位号下标 */
		 int placeNumber = config.getPlaceNumber() == null?0:config.getPlaceNumber();
		/** 备注下标 */
		 int remarkIndex = config.getRemarkIndex() == null?0:config.getRemarkIndex();
		//查询对应产品是否存在
		DevProductList productList = productListMapper.selectDevProductListById(pid);
		if(productList == null){
			failureMsg.insert(0,"很抱歉，导入失败！您选择的产品不存在");
			throw new Exception(failureMsg.toString());
		}
		Workbook wb = null;
		try {
			//进行数据解析
			 wb = WorkbookFactory.create(file.getInputStream());
		}catch (Exception e){
			failureMsg.insert(0,"很抱歉，导入失败！系统出错，请联系系统管理员");
			throw new Exception(failureMsg.toString());
		}
		if(wb == null){
			failureMsg.insert(0,"很抱歉，导入失败！系统出错，请联系系统管理员");
			throw new Exception(failureMsg.toString());
		}
		Sheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		if(rows <=0 ){
			failureMsg.insert(0,"很抱歉，导入失败！导入的文件中行数为0");
			throw new Exception(failureMsg.toString());
		}
		if(rows < config.getRowIndex()){
			failureMsg.insert(0,"很抱歉，导入失败！导入的文件中行数小于BOM配置中开始解析行数");
			throw new Exception(failureMsg.toString());
		}
		Row row =null;
		ProductBomDetails details = null;
		List<ProductBomDetails> list = new ArrayList<>();
		for (int i=config.getRowIndex()-1;i<rows;i++){
			try {
				row = sheet.getRow(i);
				if(row == null){
					failureNum ++;
					failureMsg.append("<br/>第" + (i+1) + "行数据为空");
					continue;
				}
				//判断对应的物料编号是否存在
				String wCode = ExcelUtil.getCellValue1(row,config.getMaterielCode()-1).toString().trim();
				if(StringUtils.isEmpty(wCode)){
					failureNum ++;
					failureMsg.append("<br/>第" + (i+1) + "行数据,物料编码为空");
					continue;
				}
				details = new ProductBomDetails();
				//查询物料是否存在
				Materiel materiel = materielMapper.selectMaterielByMaterielCode(wCode,companyId);
				if(materiel == null){
					failureNum ++;
					failureMsg.append("<br/>第" + (i+1) + "行数据,导入的物料不存在，请先导入对应的物料信息");
					continue;
				}
				details.setBomDetailsType(0);
				details.setDetailId(materiel.getId());
				details.setDetailCode(materiel.getMaterielCode());
				//获取物料名称
				String wName = ExcelUtil.getCellValue1(row,config.getMaterielName()-1).toString().trim();
				if(StringUtils.isEmpty(wName)){
					failureNum ++;
					failureMsg.append("<br/>第" + (i+1) + "行数据,物料名称为空");
					continue;
				}
				//判断物料名称是否相等
				if(!wName.equals(materiel.getMaterielName())){
					failureNum ++;
					failureMsg.append("<br/>第" + (i+1) + "行数据,物料编码:"+wCode+",物料名称与系统中的物料名称不同");
					continue;
				}
				details.setDetailName(materiel.getMaterielName());
				//判断物料型号
				String wModel = ExcelUtil.getCellValue1(row,config.getMaterielModel() -1).toString().trim();
				if(StringUtils.isEmpty(wModel)){
					failureNum ++;
					failureMsg.append("<br/>第" + (i+1) + "行数据,物料型号为空");
					continue;
				}
				//判断物料型号是否相等
				if(!wModel.equals(materiel.getMaterielModel())){
					failureNum ++;
					failureMsg.append("<br/>第" + (i+1) + "行数据,物料编码:"+wCode+",物料名称与系统中的物料型号不同");
					continue;
				}
				details.setDetailModel(materiel.getMaterielModel());
				//判断用量
				try {
					int number = Convert.toInt(ExcelUtil.getCellValue1(row,config.getNumber() -1));
					if(number <=0 ){
						failureNum ++;
						failureMsg.append("<br/>第" + (i+1) + "行数据,物料用量必须大于0");
						continue;
					}
					details.setOneNum(number);
					details.setPrice(materiel.getPrice());
					details.setTotalPrice(new BigDecimal(materiel.getPrice().floatValue()*number));
				}catch (Exception e){
					failureNum ++;
					failureMsg.append("<br/>第" + (i+1) + "行数据,物料用量必须为数字");
					continue;
				}
				details.setCreateId(u.getUserId().intValue());
				details.setCreateTime(new Date());
				//单位
				if(unit > 0){
					String wUnit = ExcelUtil.getCellValue1(row,unit -1).toString().trim();
					details.setUnit(wUnit);
				}
				//位号
				if(placeNumber > 0){
					String wh = ExcelUtil.getCellValue1(row,placeNumber -1).toString().trim();
					details.setPlaceNumber(wh);
				}
				//备注
				if(remarkIndex > 0){
					String remark = ExcelUtil.getCellValue1(row,remarkIndex -1).toString().trim();
					details.setRemark(remark);
				}
				list.add(details);
			}catch (Exception e){
				failureNum ++;
				failureMsg.append("<br/>第" + (i+1) + "行数据导入失败");
				continue;
			}
		}
		if(failureNum > 0){
			failureMsg.insert(0,"很抱歉，导入失败！");
			return failureMsg.toString();
		}
		System.out.println(list.size());
		successMsg.insert(0,"恭喜您，数据导入成功");
		return successMsg.toString();
	}


	/**
     * 修改产品BOM单
     * 
     * @param productBom 产品BOM单信息
     * @return 结果
     */
	@Override
	public int updateProductBom(ProductBom productBom)
	{
	    return productBomMapper.updateProductBom(productBom);
	}

	/**
     * 删除产品BOM单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteProductBomByIds(String ids)
	{
		return productBomMapper.deleteProductBomByIds(Convert.toStrArray(ids));
	}

	/**
	 * 查询bom配置信息
	 * @return
	 */
	@Override
	public BomConfig selectBomConfigLimit1() {
		return bomConfigMapper.selectBomConfigLimit1();
	}

	/**
	 * 保存bom单解析配置
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
}
