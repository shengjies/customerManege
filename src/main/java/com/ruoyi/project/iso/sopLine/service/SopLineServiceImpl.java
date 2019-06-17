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
	@Transactional
	public int updateSopLine(SopLine sopLine)
	{
		if(sopLine != null){
			//删除对应公司 对应产线 对应 SOP 配置信息
			sopLineMapper.deleteSopLine(sopLine.getCompanyId(),sopLine.getLineId(),sopLine.getSopId());
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
			//删除 工位配置
			sopLineWorkMapper.deleteSopLineWork(sopLine.getCompanyId(),sopLine.getLineId(),sopLine.getSopId());
			//操作产线 SOP 工位配置
			if(sopLine.getSopLineWorks() != null && sopLine.getSopLineWorks().size() >0){
				for (SopLineWork sopLineWork : sopLine.getSopLineWorks()) {
					sopLineWork.setcId(sopLine.getcId());
					sopLineWork.setCompanyId(sopLine.getCompanyId());
					sopLineWork.setcTime(new Date());
					sopLineWorkMapper.insertSopLineWork(sopLineWork);
				}
			}
			return 1;
		}
	    return 0;
	}

	/**
	 * 删除作业指导书  产线 配置对象
	 * @param lineId 产线id
	 * @param sopId SOP id
	 * @return
	 */
	@Override
	@Transactional
	public int deleteSopLine(int companyId,int lineId, int sopId) {
		try {
			//删除对应公司 对应产线 对应 SOP 配置信息
			sopLineMapper.deleteSopLine(companyId,lineId,sopId);
			//删除 工位配置
			sopLineWorkMapper.deleteSopLineWork(companyId,lineId,sopId);
			return  1;
		}catch (Exception e){
			e.printStackTrace();
		}
		return  0;
	}

	/**
	 * 根据公司id 产线id SOP id查询所以的产线SOP 配置细心
	 * @param companyId 公司id
	 * @param lineId 产线id
	 * @param sopId SOP id
	 * @return
	 */
	@Override
	public List<SopLine> selectLineAllSopConfig(int companyId, int lineId, int sopId) {
		return sopLineMapper.selectLineAllSopConfig(companyId,lineId,sopId);
	}

	/**
	 * 根据公司id 产线id SOP id查询所以的工位配置信息
	 * @param companyId 公司id
	 * @param lineId 产线 id
	 * @param sopId SOP id
	 * @return
	 */
	@Override
	public List<SopLineWork> selectWorkstionByCompanyAndLineIdAndSopId(int companyId, int lineId, int sopId) {
		return sopLineWorkMapper.selectWorkstionByCompanyAndLineIdAndSopId(companyId,lineId,sopId);
	}
}
