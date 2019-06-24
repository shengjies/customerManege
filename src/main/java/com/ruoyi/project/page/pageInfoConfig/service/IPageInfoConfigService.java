package com.ruoyi.project.page.pageInfoConfig.service;

import com.ruoyi.project.page.pageInfoConfig.domain.PageInfoConfig;

import java.util.List;

/**
 * 页面配置 服务层
 * 
 * @author zqm
 * @date 2019-04-13
 */
public interface IPageInfoConfigService 
{
	/**
     * 查询页面配置信息
     * 
     * @param id 页面配置ID
     * @return 页面配置信息
     */
	public PageInfoConfig selectPageInfoConfigById(Integer id);
	
	/**
     * 查询页面配置列表
     * 
     * @param pageInfoConfig 页面配置信息
     * @return 页面配置集合
     */
	public List<PageInfoConfig> selectPageInfoConfigList(PageInfoConfig pageInfoConfig);
	
	/**
     * 新增页面配置
     * 
     * @param pageInfoConfig 页面配置信息
     * @return 结果
     */
	public int insertPageInfoConfig(PageInfoConfig pageInfoConfig);
	
	/**
     * 修改页面配置
     * 
     * @param pageInfoConfig 页面配置信息
     * @return 结果
     */
	public int updatePageInfoConfig(PageInfoConfig pageInfoConfig);
		
	/**
     * 删除页面配置信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePageInfoConfigByIds(String ids);

	
}
