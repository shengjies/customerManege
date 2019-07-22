package com.ruoyi.project.mes.mesBatch.service;

import java.util.List;

import com.ruoyi.project.mes.mesBatch.domain.MesBatchDetail;
import com.ruoyi.project.mes.mesBatch.mapper.MesBatchDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.support.Convert;

/**
 * MES批准追踪详情 服务层实现
 * 
 * @author sj
 * @date 2019-07-22
 */
@Service
public class MesBatchDetailServiceImpl implements IMesBatchDetailService 
{
	@Autowired
	private MesBatchDetailMapper mesBatchDetailMapper;

	/**
     * 查询MES批准追踪详情信息
     * 
     * @param id MES批准追踪详情ID
     * @return MES批准追踪详情信息
     */
    @Override
	public MesBatchDetail selectMesBatchDetailById(Integer id)
	{
	    return mesBatchDetailMapper.selectMesBatchDetailById(id);
	}

	/**
     * 查询MES批准追踪详情列表
     * 
     * @param mesBatchDetail MES批准追踪详情信息
     * @return MES批准追踪详情集合
     */
	@Override
	public List<MesBatchDetail> selectMesBatchDetailList(MesBatchDetail mesBatchDetail)
	{
	    return mesBatchDetailMapper.selectMesBatchDetailList(mesBatchDetail);
	}
	
    /**
     * 新增MES批准追踪详情
     * 
     * @param mesBatchDetail MES批准追踪详情信息
     * @return 结果
     */
	@Override
	public int insertMesBatchDetail(MesBatchDetail mesBatchDetail)
	{
	    return mesBatchDetailMapper.insertMesBatchDetail(mesBatchDetail);
	}
	
	/**
     * 修改MES批准追踪详情
     * 
     * @param mesBatchDetail MES批准追踪详情信息
     * @return 结果
     */
	@Override
	public int updateMesBatchDetail(MesBatchDetail mesBatchDetail)
	{
	    return mesBatchDetailMapper.updateMesBatchDetail(mesBatchDetail);
	}

	/**
     * 删除MES批准追踪详情对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMesBatchDetailByIds(String ids)
	{
		return mesBatchDetailMapper.deleteMesBatchDetailByIds(Convert.toStrArray(ids));
	}
	
}
