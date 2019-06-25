package com.ruoyi.project.erp.productBom.mapper;

import com.ruoyi.project.erp.productBom.domain.ProductBom;
import java.util.List;	

/**
 * 产品BOM单 数据层
 * 
 * @author sj
 * @date 2019-06-24
 */
public interface ProductBomMapper 
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
     * 新增产品BOM单
     * 
     * @param productBom 产品BOM单信息
     * @return 结果
     */
	public int insertProductBom(ProductBom productBom);
	
	/**
     * 修改产品BOM单
     * 
     * @param productBom 产品BOM单信息
     * @return 结果
     */
	public int updateProductBom(ProductBom productBom);
	
	/**
     * 删除产品BOM单
     * 
     * @param id 产品BOM单ID
     * @return 结果
     */
	public int deleteProductBomById(Integer id);
	
	/**
     * 批量删除产品BOM单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteProductBomByIds(String[] ids);
	
}