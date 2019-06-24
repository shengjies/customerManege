package com.ruoyi.project.page.pageInfoConfig.service;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.PageTypeConstants;
import com.ruoyi.common.utils.TimeUtil;
import com.ruoyi.project.device.devIo.domain.DevIo;
import com.ruoyi.project.device.devIo.mapper.DevIoMapper;
import com.ruoyi.project.page.pageInfo.domain.PageInfo;
import com.ruoyi.project.page.pageInfo.mapper.PageInfoMapper;
import com.ruoyi.project.page.pageInfo.service.IPageInfoService;
import com.ruoyi.project.page.pageInfoConfig.domain.PageInfoConfig;
import com.ruoyi.project.page.pageInfoConfig.mapper.PageInfoConfigMapper;
import com.ruoyi.project.production.devWorkData.domain.DevWorkData;
import com.ruoyi.project.production.devWorkData.mapper.DevWorkDataMapper;
import com.ruoyi.project.production.devWorkDayHour.mapper.DevWorkDayHourMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.support.Convert;
import org.springframework.util.StringUtils;

/**
 * 页面配置 服务层实现
 * 
 * @author zqm
 * @date 2019-04-13
 */
@Service
public class PageInfoConfigServiceImpl implements IPageInfoConfigService
{
	@Autowired
	private PageInfoConfigMapper pageInfoConfigMapper;

	@Autowired
	private ProductionLineMapper productionLineMapper;

	@Autowired
	private PageInfoMapper pageInfoMapper;

	@Autowired
	private DevWorkOrderMapper devWorkOrderMapper;

	@Autowired
	private DevWorkDataMapper devWorkDataMapper;

	@Autowired
	private DevIoMapper devIoMapper;

	@Autowired
	private DevWorkDayHourMapper devWorkDayHourMapper;

	@Autowired
	private IPageInfoService pageInfoService;

	/**
     * 查询页面配置信息
     * 
     * @param id 页面配置ID
     * @return 页面配置信息
     */
    @Override
	public PageInfoConfig selectPageInfoConfigById(Integer id)
	{
	    return pageInfoConfigMapper.selectPageInfoConfigById(id);
	}
	
	/**
     * 查询页面配置列表
     * 
     * @param pageInfoConfig 页面配置信息
     * @return 页面配置集合
     */
	@Override
	public List<PageInfoConfig> selectPageInfoConfigList(PageInfoConfig pageInfoConfig)
	{
	    return pageInfoConfigMapper.selectPageInfoConfigList(pageInfoConfig);
	}
	
    /**
     * 新增页面配置
     * 
     * @param pageInfoConfig 页面配置信息
     * @return 结果
     */
	@Override
	public int insertPageInfoConfig(PageInfoConfig pageInfoConfig)
	{
	    return pageInfoConfigMapper.insertPageInfoConfig(pageInfoConfig);
	}
	
	/**
     * 修改页面配置
     * 
     * @param pageInfoConfig 页面配置信息
     * @return 结果
     */
	@Override
	public int updatePageInfoConfig(PageInfoConfig pageInfoConfig)
	{
	    return pageInfoConfigMapper.updatePageInfoConfig(pageInfoConfig);
	}

	/**
     * 删除页面配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePageInfoConfigByIds(String ids)
	{
		return pageInfoConfigMapper.deletePageInfoConfigByIds(Convert.toStrArray(ids));
	}
}
