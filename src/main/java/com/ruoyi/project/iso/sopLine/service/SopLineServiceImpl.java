package com.ruoyi.project.iso.sopLine.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.project.iso.sopLine.domain.SopLineWork;
import com.ruoyi.project.iso.sopLine.mapper.SopLineWorkMapper;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.iso.sopLine.mapper.SopLineMapper;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.service.ISopLineService;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

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

	@Autowired
	private DevProductListMapper productListMapper;

	@Autowired
	private SopLineWorkMapper sopLineWorkMapper;

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
	@Transactional
	public int insertSopLine(SopLine sopLine)
	{
		if(sopLine != null){
			//操作对应产品SOP 配置
			if(sopLine.getPns() != null && sopLine.getPns().length >0){
				DevProductList productList =null;
				for (Integer pn : sopLine.getPns()) {
					//获取对应产品信息
					productList = productListMapper.selectDevProductListById(pn);
					if(productList == null){
						continue;
					}
					sopLine.setPnId(pn);
					sopLine.setPnCode(productList.getProductCode());
					sopLine.setcTime(new Date());
					sopLineMapper.insertSopLine(sopLine);
				}
			}
			//操作产线 SOP 工位配置
			if(sopLine.getSopLineWorks() != null && sopLine.getSopLineWorks().size() >0){
				for (SopLineWork sopLineWork : sopLine.getSopLineWorks()) {
					sopLineWork.setcId(sopLine.getcId());
					sopLineWork.setCompanyId(sopLine.getCompanyId());
					sopLineWork.setcTime(new Date());
					sopLineWorkMapper.insertSopLineWork(sopLineWork);
				}
			}
			return  1;
		}
		return  0;
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
