package com.ruoyi.project.erp.bomChange.mapper;

import com.ruoyi.project.erp.bomChange.domain.BomChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BOM更改记录 数据层
 * 
 * @author sj
 * @date 2019-06-26
 */
public interface BomChangeMapper 
{
	/**
     * 查询BOM更改记录信息
     * 
     * @param id BOM更改记录ID
     * @return BOM更改记录信息
     */
	public BomChange selectBomChangeById(Integer id);
	
	/**
     * 查询BOM更改记录列表
     * 
     * @param bomChange BOM更改记录信息
     * @return BOM更改记录集合
     */
	public List<BomChange> selectBomChangeList(BomChange bomChange);
	
	/**
     * 新增BOM更改记录
     * 
     * @param bomChange BOM更改记录信息
     * @return 结果
     */
	public int insertBomChange(BomChange bomChange);
	
	/**
     * 修改BOM更改记录
     * 
     * @param bomChange BOM更改记录信息
     * @return 结果
     */
	public int updateBomChange(BomChange bomChange);

	/**
	 * 取消BOM更改信息
	 * @param id
	 * @return
	 */
	public int cancelBomChange(@Param("id")int id );

	/**
	 * 根据新版本的BOM id查询变更记录信息
	 * @param bid BOM id
	 * @return
	 */
	BomChange selectBomChangeByNewBomId(@Param("bid")int bid);
	
}