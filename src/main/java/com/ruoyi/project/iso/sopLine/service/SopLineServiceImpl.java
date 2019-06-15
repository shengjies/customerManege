package com.ruoyi.project.iso.sopLine.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.iso.sopLine.mapper.SopLineMapper;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.service.ISopLineService;
import com.ruoyi.common.support.Convert;

/**
 * 作业指导书  产线 配置 服务层实现
 * 
 * @author sj
 * @date 2019-06-15
 */
@Service
public class SopLineServiceImpl implements ISopLineService 
{
	@Autowired
	private SopLineMapper sopLineMapper;

	/**
     * 查询作业指导书  产线 配置信息
     * 
     * @param id 作业指导书  产线 配置ID
     * @return 作业指导书  产线 配置信息
     */
    @Override
	public SopLine selectSopLineById(Integer id)
	{
	    return sopLineMapper.selectSopLineById(id);
	}
	
	/**
     * 查询作业指导书  产线 配置列表
     * 
     * @param sopLine 作业指导书  产线 配置信息
     * @return 作业指导书  产线 配置集合
     */
	@Override
	public List<SopLine> selectSopLineList(SopLine sopLine)
	{
	    return sopLineMapper.selectSopLineList(sopLine);
	}
	
    /**
     * 新增作业指导书  产线 配置
     * 
     * @param sopLine 作业指导书  产线 配置信息
     * @return 结果
     */
	@Override
	public int insertSopLine(SopLine sopLine)
	{
	    return sopLineMapper.insertSopLine(sopLine);
	}
	
	/**
     * 修改作业指导书  产线 配置
     * 
     * @param sopLine 作业指导书  产线 配置信息
     * @return 结果
     */
	@Override
	public int updateSopLine(SopLine sopLine)
	{
	    return sopLineMapper.updateSopLine(sopLine);
	}

	/**
     * 删除作业指导书  产线 配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteSopLineByIds(String ids)
	{
		return sopLineMapper.deleteSopLineByIds(Convert.toStrArray(ids));
	}
	
}
