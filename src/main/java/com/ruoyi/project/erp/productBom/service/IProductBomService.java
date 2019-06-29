package com.ruoyi.project.erp.productBom.service;

import com.ruoyi.project.erp.productBom.domain.BomConfig;
import com.ruoyi.project.erp.productBom.domain.ProductBom;
import com.ruoyi.project.erp.productBom.domain.ProductBomDetails;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 产品BOM单 服务层
 * 
 * @author sj
 * @date 2019-06-24
 */
public interface IProductBomService 
{
	/**
     * 查询产品BOM单信息
     * 
     * @param id 产品BOM单ID
     * @return 产品BOM单信息
     */
	public ProductBom selectProductBomById(Integer id);
	
	/**
     * 查询产品BOM单列表
     * 
     * @param productBom 产品BOM单信息
     * @return 产品BOM单集合
     */
	public List<ProductBom> selectProductBomList(ProductBom productBom);



	/**
	 * 根据产品id查询对应的BOM版本信息
	 * @param pid 产品id
	 * @return
	 */
	List<ProductBom> selectBomByPid(int pid);


	/**
	 * 导入bom单 多文件
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public String insertProductBom(MultipartFile[] files) throws Exception;
	
	/**
     * 修改产品BOM单
     * 
     * @param productBom 产品BOM单信息
     * @return 结果
     */
	public String updateProductBom(ProductBom productBom) throws Exception;
		


	/**
	 * 查询bom配置信息
	 * @return
	 */
	public BomConfig selectBomConfigLimit1();

	/**
	 * 保存bom单解析配置
	 * @param config 配置信息
	 * @return
	 */
	public int saveBomConfig(BomConfig config);

	/**
	 * 根据bomid查询对应的bom详情数据
	 * @param bid bom id
	 * @return
	 */
	List<ProductBomDetails> selectBomDetailByBomId(int bid);

	/**
	 * 导出BOM 单信息
	 * @param id BOM单id
	 * @return
	 */
	Workbook export(int id);
	
}
