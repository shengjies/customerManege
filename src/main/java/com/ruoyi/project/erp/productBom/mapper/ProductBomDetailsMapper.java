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
     * @param bid BOMid
     * @return 产品BOM清单集合
     */
	public List<ProductBomDetails> selectProductBomDetailsList(@Param("bid") int bid);
	
	/**
     * 新增产品BOM清单
     * 
     * @param productBomDetails 产品BOM清单信息
     * @return 结果
     */
	public int insertProductBomDetails(ProductBomDetails productBomDetails);

	/**
	 * 根据BOM id 详情下标查询对应的详情内容
	 * @param bid BOM id
	 * @param bType 详情类型 0、物料 1、半成品
	 * @return
	 */
	ProductBomDetails selectBomDetailByBidAndIndex(@Param("bid")int bid,@Param("bType")int bType,@Param("bIndex")int bIndex);


	/**
	 * 通过bom主表id查询bom清单
	 * @param bomId bom主表id
	 * @return 结果
	 */
	List<ProductBomDetails> selectProBomDetailsByBomId(@Param("bomId") Integer bomId);


}