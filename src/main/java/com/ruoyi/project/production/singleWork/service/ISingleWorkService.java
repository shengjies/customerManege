package com.ruoyi.project.production.singleWork.service;

import com.ruoyi.project.production.singleWork.domain.SingleWork;
import java.util.List;

/**
 * 单工位数据 服务层
 * 
 * @author sj
 * @date 2019-07-03
 */
public interface ISingleWorkService 
{
	/**
     * 查询单工位数据信息
     * 
     * @param id 单工位数据ID
     * @return 单工位数据信息
     */
	public SingleWork selectSingleWorkById(Integer id);
	
	/**
     * 查询单工位数据列表
     * 
     * @param singleWork 单工位数据信息
     * @return 单工位数据集合
     */
	public List<SingleWork> selectSingleWorkList(SingleWork singleWork);
	
	/**
     * 新增单工位数据
     * 
     * @param singleWork 单工位数据信息
     * @return 结果
     */
	public int insertSingleWork(SingleWork singleWork);
	
	/**
     * 修改单工位数据
     * 
     * @param singleWork 单工位数据信息
     * @return 结果
     */
	public int updateSingleWork(SingleWork singleWork);
		
	/**
     * 删除单工位数据信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSingleWorkByIds(String ids);

	/**
	 * 校验车间名称的唯一性
	 * @param singleWork 单工位信息
	 * @return 结果
	 */
    String checkNameUnique(SingleWork singleWork);
}
