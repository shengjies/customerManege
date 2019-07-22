package com.ruoyi.project.mes.mesBatch.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.mes.mesBatch.mapper.MesBatchMapper;
import com.ruoyi.project.mes.mesBatch.domain.MesBatch;
import com.ruoyi.project.mes.mesBatch.service.IMesBatchService;
import com.ruoyi.common.support.Convert;

/**
 * MES批准追踪 服务层实现
 * 
 * @author sj
 * @date 2019-07-22
 */
@Service
public class MesBatchServiceImpl implements IMesBatchService 
{
	@Autowired
	private MesBatchMapper mesBatchMapper;

	/**
     * 查询MES批准追踪信息
     * 
     * @param id MES批准追踪ID
     * @return MES批准追踪信息
     */
    @Override
	public MesBatch selectMesBatchById(Integer id)
	{
	    return mesBatchMapper.selectMesBatchById(id);
	}
	
	/**
     * 查询MES批准追踪列表
     * 
     * @param mesBatch MES批准追踪信息
     * @return MES批准追踪集合
     */
	@Override
	public List<MesBatch> selectMesBatchList(MesBatch mesBatch)
	{
	    return mesBatchMapper.selectMesBatchList(mesBatch);
	}
	
    /**
     * 新增MES批准追踪
     * 
     * @param mesBatch MES批准追踪信息
     * @return 结果
     */
	@Override
	public int insertMesBatch(MesBatch mesBatch)
	{
	    return mesBatchMapper.insertMesBatch(mesBatch);
	}
	
	/**
     * 修改MES批准追踪
     * 
     * @param mesBatch MES批准追踪信息
     * @return 结果
     */
	@Override
	public int updateMesBatch(MesBatch mesBatch)
	{
	    return mesBatchMapper.updateMesBatch(mesBatch);
	}

	/**
     * 删除MES批准追踪对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMesBatchByIds(String ids)
	{
		return mesBatchMapper.deleteMesBatchByIds(Convert.toStrArray(ids));
	}
	
}
