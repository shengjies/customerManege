package com.ruoyi.project.erp.productBom.mapper;

import com.ruoyi.project.erp.productBom.domain.ProductBomDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品BOM清单 数据层
 * 
 * @author sj
 * @date 2019-06-25
 */
public interface ProductBomDetailsMapper 
{
	/**
     * 查询产品BOM清单信息
     * 
     * @param id 产品BOM清单ID
     * @return 产品BOM清单信息
     */
	public ProductBomDetails selectProductBomDetailsById(Integer id);
	
	/**
     * 查询产品BOM清单列表
     * 
     * @param productBomDetails 产品BOM清单信息
     * @return 产品BOM清单集合
     */
	public List<ProductBomDetails> selectProductBomDetailsList(ProductBomDetails productBomDetails);
	
	/**
     * 新增产品BOM清单
     * 
     * @param productBomDetails 产品BOM清单信息
     * @return 结果
     */
	public int insertProductBomDetails(ProductBomDetails productBomDetails);
	
	/**
     * 修改产品BOM清单
     * 
     * @param productBomDetails 产品BOM清单信息
     * @return 结果
     */
	public int updateProductBomDetails(ProductBomDetails productBomDetails);
	
	/**
     * 删除产品BOM清单
     * 
     * @param id 产品BOM清单ID
     * @return 结果
     */
	public int deleteProductBomDetailsById(Integer id);
	
	/**
     * 批量删除产品BOM清单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteProductBomDetailsByIds(String[] ids);

	/**
	 * 通过bom主表id查询bom清单
	 * @param bomId bom主表id
	 * @return 结果
	 */
    List<ProductBomDetails> selectProBomDetailsByBomId(@Param("bomId") Integer bomId);
}