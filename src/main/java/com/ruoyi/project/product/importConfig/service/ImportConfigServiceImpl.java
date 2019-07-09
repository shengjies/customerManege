package com.ruoyi.project.product.importConfig.service;


import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.product.importConfig.mapper.ImportConfigMapper;
import com.ruoyi.project.product.importConfig.domain.ImportConfig;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Target;
import java.util.Date;


/**
 * 导入配置 服务层实现
 * 
 * @author sj
 * @date 2019-07-03
 */
@Service
public class ImportConfigServiceImpl implements IImportConfigService 
{
	@Autowired
	private ImportConfigMapper importConfigMapper;

	/**
	 * 查询导入配置信息
	 * @param cType 导入配置ID
	 * @return
	 */
	@Override
	public ImportConfig selectImportConfigByType(Integer cType) {
		return importConfigMapper.selectImportConfigByType(cType);
	}

	/**
	 * 新增导入配置
	 * @param importConfig 导入配置信息
	 * @return
	 */
	@Override
	@Transactional
	public int insertImportConfig(ImportConfig importConfig) {
		//删除之前产品导入的配置
		importConfigMapper.deleteImportConfigByType(importConfig.getcType());
		importConfig.setCompanyId(JwtUtil.getTokenUser(ServletUtils.getRequest()).getCompanyId());
		importConfig.setcTime(new Date());
		return importConfigMapper.insertImportConfig(importConfig);
	}

	/**
	 * 删除导入配置信息
	 * @param cType 需要删除的数据cType
	 * @return
	 */
	@Override
	public int deleteImportConfigByType(Integer cType) {
		return importConfigMapper.deleteImportConfigByType(cType);
	}
}
