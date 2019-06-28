package com.ruoyi.project.erp.bomChange.service;

import com.ruoyi.project.erp.bomChange.domain.BomChange;
import java.util.List;

/**
 * BOM更改记录 服务层
 * 
 * @author sj
 * @date 2019-06-26
 */
public interface IBomChangeService 
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
	 * 审核BOM改变内容
	 * @param id 编号
	 * @return
	 */
	int examineBomChange(int id);


	/**
	 * 取消BOM更改信息
	 * @param id id
	 * @return
	 */
	int cancelBomChange(int id);

	/**
	 * 根据新版本的BOM id查询变更记录信息
	 * @param bid BOM id
	 * @return
	 */
	BomChange selectBomChangeByNewBomId(int bid);
	
}
