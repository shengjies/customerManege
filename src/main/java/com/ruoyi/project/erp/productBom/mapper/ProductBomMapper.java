package com.ruoyi.project.erp.productBom.mapper;

import com.ruoyi.project.erp.productBom.domain.ProductBom;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 产品BOM单 数据层
 * 
 * @author sj
 * @date 2019-06-25
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
	 * 根据产品id查询对应的产品是否已经导入bom
	 * @param pid 产品id
	 * @return
	 */
	public ProductBom selectBomByProductId(@Param("pid")int pid);

	/**
	 * 根据产品id修改对应BOM 单的sign值为0
	 * @param pid 产品id
	 * @return
	 */
	int updateBomSignByProductId(@Param("pid")int pid);

	/**
	 * 对BOM 进行审核
	 * @param id BOM编号
	 * @return
	 */
	int updateBomSignAndSsignById(@Param("id") int id);

	/**
	 * 根据产品id查询最新版本的BOM单
	 * @param pid 产品id
	 * @return
	 */
	public ProductBom selectNewBomVersion(@Param("pid")int pid);

	/**
     * 查询产品BOM单列表
     * 
     * @param productBom 产品BOM单信息
     * @return 产品BOM单集合
     */
	public List<ProductBom> selectProductBomList(ProductBom productBom);

	/**
	 * 根据产品id查询对应的BOM版本信息
	 * @param pid
	 * @return
	 */
	List<ProductBom> selectBomByPid(@Param("pid")int pid);

	/**
     * 新增产品BOM单
     * 
     * @param productBom 产品BOM单信息
     * @return 结果
     */
	public int insertProductBom(ProductBom productBom);

	/**
	 * 通过产品id查询最新的产品bom信息
	 * @param companyId 公司id
	 * @param productId 产品id
	 * @return 结果
	 */
	ProductBom selectProBomNewVerByProId(@Param("companyId") Integer companyId,@Param("productId") Integer productId);


}