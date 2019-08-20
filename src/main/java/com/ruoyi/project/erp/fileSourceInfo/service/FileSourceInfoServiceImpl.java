package com.ruoyi.project.erp.fileSourceInfo.service;

import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.erp.fileSourceInfo.domain.FileSourceInfo;
import com.ruoyi.project.erp.fileSourceInfo.mapper.FileSourceInfoMapper;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * 文件素材管理 服务层实现
 * 
 * @author zqm
 * @date 2019-05-09
 */
@Service
public class FileSourceInfoServiceImpl implements IFileSourceInfoService 
{
	@Autowired
	private FileSourceInfoMapper fileSourceInfoMapper;

	@Autowired
	private DevListMapper devListMapper;

	@Autowired
	private DevProductListMapper productListMapper;

	/**
     * 查询文件素材管理列表
     * 
     * @param fileSourceInfo 文件素材管理信息
     * @return 文件素材管理集合
     */
	@Override
//	@DataSource(DataSourceType.ERP)
	public List<FileSourceInfo> selectFileSourceInfoList(FileSourceInfo fileSourceInfo)
	{
		if(fileSourceInfo.getCompanyId() == null) {
			User user = JwtUtil.getUser();
			if (user == null) return Collections.emptyList();
			fileSourceInfo.setCompanyId(user.getCompanyId());
		}
	    return fileSourceInfoMapper.selectFileSourceInfoList(fileSourceInfo);
	}
	
    /**
     * 新增文件素材管理
     * 
     * @param fileSourceInfo 文件素材管理信息
     * @return 结果
     */
	@Override
//	@DataSource(DataSourceType.ERP)
	public int insertFileSourceInfo(FileSourceInfo fileSourceInfo)
	{
	    return fileSourceInfoMapper.insertFileSourceInfo(fileSourceInfo);
	}

	/**
     * 删除文件素材管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
//	@DataSource(DataSourceType.ERP)
	public int deleteFileSourceInfoByIds(String ids)
	{
		FileSourceInfo info = fileSourceInfoMapper.selectFileSourceInfoById(Integer.parseInt(ids));
		if(info != null && !StringUtils.isEmpty(info.getSavePath())){
			//删除对应的文件
			File file = new File(info.getSavePath());
			file.delete();
		}
		return fileSourceInfoMapper.deleteFileSourceInfoByIds(Convert.toStrArray(ids));
	}

	/**
	 * 根据硬件编号和产品编码查询对应的产品文件信息
	 * @param dCode 硬件编码
	 * @param pCode 产品编码
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileSourceInfo> selectFileSourceByDCodeAndPCode(String dCode, String pCode) throws Exception {
		//判断对应的硬件编码是否存在
		DevList devList = devListMapper.selectDevListByCode(dCode);
		if(devList == null || devList.getCompanyId() ==null){
			throw  new Exception("硬件不存在");
		}
		//产线对应的产品是否存在
	    DevProductList productList = productListMapper.selectDevProductByCode(devList.getCompanyId(),pCode);
		if(productList == null){
			throw new Exception("产品不存在");
		}
		return fileSourceInfoMapper.selectFileSourceBySaveIdAndComId(productList.getId(),productList.getCompanyId());
	}
}
