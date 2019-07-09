package com.ruoyi.project.erp.productBom.mapper;

import com.ruoyi.project.erp.productBom.domain.BomConfig;
import org.apache.ibatis.annotations.Param;


/**
 * BOM导入配置 数据层
 * 
 * @author sj
 * @date 2019-06-25
 */
public interface BomConfigMapper 
{
	/**
     * 查询BOM导入配置信息
     *
     * @return BOM导入配置信息
     */
	public BomConfig selectBomConfigLimit1();

	/**
     * 新增BOM导入配置
     * 
     * @param bomConfig BOM导入配置信息
     * @return 结果
     */
	public int insertBomConfig(BomConfig bomConfig);

	/**
     * 删除BOM导入配置
     *
     * @return 结果
     */
	public int deleteBomConfigAll();

}