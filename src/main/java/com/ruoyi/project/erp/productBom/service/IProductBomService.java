package com.ruoyi.project.erp.productBom.service;

import com.ruoyi.project.erp.productBom.domain.BomConfig;
import com.ruoyi.project.erp.productBom.domain.ProductBom;
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
	 * 导入bom单
	 * @param file 导入文件
	 * @param pid 产品id
	 * @return 结果
	 */
	public String insertProductBom(MultipartFile file, int pid) throws Exception;
	
	/**
     * 修改产品BOM单
     * 
     * @param productBom 产品BOM单信息
     * @return 结果
     */
	public int updateProductBom(ProductBom productBom);
		
	/**
     * 删除产品BOM单信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteProductBomByIds(String ids);

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
	
}
