package com.ruoyi.project.iso.iso.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.common.support.Convert;

/**
 * 文件管理 服务层实现
 * 
 * @author sj
 * @date 2019-06-13
 */
@Service
public class IsoServiceImpl implements IIsoService 
{
	@Autowired
	private IsoMapper isoMapper;

	/**
     * 查询文件管理信息
     * 
     * @param id 文件管理ID
     * @return 文件管理信息
     */
    @Override
	public Iso selectIsoById(Integer id)
	{
	    return isoMapper.selectIsoById(id);
	}
	
	/**
     * 查询文件管理列表
     * 
     * @param iso 文件管理信息
     * @return 文件管理集合
     */
	@Override
	public List<Iso> selectIsoList(Iso iso)
	{
	    return isoMapper.selectIsoList(iso);
	}
	
    /**
     * 新增文件管理
     * 
     * @param iso 文件管理信息
     * @return 结果
     */
	@Override
	public int insertIso(Iso iso)
	{
	    return isoMapper.insertIso(iso);
	}
	
	/**
     * 修改文件管理
     * 
     * @param iso 文件管理信息
     * @return 结果
     */
	@Override
	public int updateIso(Iso iso)
	{
	    return isoMapper.updateIso(iso);
	}

	/**
     * 删除文件管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteIsoByIds(String ids)
	{
		return isoMapper.deleteIsoByIds(Convert.toStrArray(ids));
	}
	
}
