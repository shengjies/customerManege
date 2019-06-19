package com.ruoyi.project.production.workstation.service;

import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 工位配置 服务层实现
 * 
 * @author sj
 * @date 2019-06-13
 */
@Service
public class WorkstationServiceImpl implements IWorkstationService 
{
	@Autowired
	private WorkstationMapper workstationMapper;

	@Autowired
    private DevListMapper devListMapper;

	@Autowired
	private ProductionLineMapper productionLineMapper;

	/**
     * 查询工位配置信息
     * 
     * @param id 工位配置ID
     * @return 工位配置信息
     */
    @Override
	public Workstation selectWorkstationById(Integer id)
	{
	    return workstationMapper.selectWorkstationById(id);
	}
	
	/**
     * 查询工位配置列表
     * 
     * @param workstation 工位配置信息
     * @return 工位配置集合
     */
	@Override
	public List<Workstation> selectWorkstationList(Workstation workstation)
	{
	    return workstationMapper.selectWorkstationList(workstation);
	}
	
    /**
     * 新增工位配置
     * 
     * @param workstation 工位配置信息
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertWorkstation(Workstation workstation) throws Exception
	{
	    //查询计数器硬件
        if(workstation.getDevId() != null && workstation.getDevId() > 0){
            DevList devList = devListMapper.selectDevListById(workstation.getDevId());
            if(devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0){
                workstation.setDevCode(devList.getDeviceId());
                devList.setSign(1);
                devListMapper.updateDevSign(devList);
            }else {
                throw new Exception("计数器硬件编码配置错误");
            }
        }
        //查询看板器硬件
        if(workstation.getcId() != null && workstation.getcId() > 0){
            DevList devList = devListMapper.selectDevListById(workstation.getcId());
            if(devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0){
                workstation.setcCode(devList.getDeviceId());
				devList.setSign(1);
				devListMapper.updateDevSign(devList);
            }else {
                throw new Exception("看板硬件编码配置错误");
            }
        }
		//查询MES
		if(workstation.geteId() != null && workstation.geteId() > 0){
			DevList devList = devListMapper.selectDevListById(workstation.geteId());
			if(devList != null && devList.getDefId() == 0 && devList.getDeviceStatus() == 1 && devList.getSign() == 0){
				workstation.seteCode(devList.getDeviceId());
				devList.setSign(1);
				devListMapper.updateDevSign(devList);
			}else {
				throw new Exception("MES硬件编码配置错误");
			}
		}
		//查询对应产线是否存在工位
        Workstation work = workstationMapper.selectWorkstationByLineId(workstation.getLineId(),workstation.getCompanyId());
        if(work ==null){
            workstation.setSign(1);
        }else if(work != null && workstation.getSign() == 1){
            //修改其他工位的数据标识
            workstationMapper.editWorkstationSign(workstation.getLineId(),workstation.getCompanyId(),0);
        }
        workstation.setcTime(new Date());
        int row = workstationMapper.insertWorkstation(workstation);
        //查询是否存在标记数据
		Workstation w = workstationMapper.selectWorkstationSignByLineId(workstation.getLineId(),workstation.getCompanyId());
		if(w != null && w.getDevId() !=null && w.getDevId() >0){
			//将产线修改为自动
			ProductionLine line = productionLineMapper.selectProductionLineById(workstation.getLineId());
			if(line != null){
				line.setManual(0);
				productionLineMapper.updateProductionLine(line);
			}
		}else{
			ProductionLine line = productionLineMapper.selectProductionLineById(workstation.getLineId());
			if(line != null){
				line.setManual(1);
				productionLineMapper.updateProductionLine(line);
			}
		}
	    return row;
	}
	
	/**
     * 修改工位配置
     * 
     * @param workstation 工位配置信息
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateWorkstation(Workstation workstation) throws Exception
	{
		Workstation work = workstationMapper.selectWorkstationById(workstation.getId());
		if(work == null){
			throw new Exception("工位被删除...");
		}
		//操作硬件
		DevList devList =null;
		//计数器
		if(work.getDevId() != workstation.getDevId()){
			workstation.setDevCode(null);
			if(work.getDevId() >0){
				//将原来的硬件修改为未配置
				devList = devListMapper.selectDevListById(work.getDevId());
				if(devList != null){
					devList.setSign(0);
					devListMapper.updateDevSign(devList);
				}
			}
			if(workstation.getDevId() > 0){
				//将现在的硬件修改为已配置
				devList = devListMapper.selectDevListById(workstation.getDevId());
				if(devList != null){
					devList.setSign(1);
					devListMapper.updateDevSign(devList);
					workstation.setDevCode(devList.getDeviceId());
				}
			}
		}
		//看板
		if(work.getcId() != workstation.getcId()){
			workstation.seteCode(null);
			if(work.getcId() > 0){
				devList = devListMapper.selectDevListById(work.getcId());
				if(devList != null){
					devList.setSign(0);
					devListMapper.updateDevSign(devList);
				}
			}
			if(workstation.getcId() >0){
				devList = devListMapper.selectDevListById(workstation.getcId());
				if(devList != null){
					devList.setSign(1);
					devListMapper.updateDevSign(devList);
					workstation.setcCode(devList.getDeviceId());
				}
			}
		}
		//MES
		if(work.geteId() != workstation.geteId()){
			workstation.seteCode(null);
			if(work.geteId() > 0){
				devList = devListMapper.selectDevListById(work.geteId());
				if(devList != null){
					devList.setSign(0);
					devListMapper.updateDevSign(devList);
				}
			}
			if(workstation.geteId() > 0){
				devList = devListMapper.selectDevListById(workstation.geteId());
				if(devList != null){
					devList.setSign(1);
					devListMapper.updateDevSign(devList);
					workstation.seteCode(devList.getDeviceId());
				}
			}
		}
		//判断数据标识
		if(workstation.getSign() == 1){//是
			//将其他工位全部修改为不是唯一标识
			workstationMapper.editWorkstationSign(work.getLineId(),work.getCompanyId(),0);
		}else if(work.getSign() == 1 && workstation.getSign() == 0){
			//将第一个工位修改为数据唯一标识
			workstationMapper.editFirstWorkstionSign(work.getLineId(),work.getCompanyId());
		}
		int row = workstationMapper.updateWorkstation(workstation);
		//查询是否存在标记数据
		Workstation w = workstationMapper.selectWorkstationSignByLineId(work.getLineId(),work.getCompanyId());
		if(w != null && w.getDevId() !=null && w.getDevId() >0){
			//将产线修改为自动
			ProductionLine line = productionLineMapper.selectProductionLineById(work.getLineId());
			if(line != null){
				line.setManual(0);
				productionLineMapper.updateProductionLine(line);
			}
		}else{
			ProductionLine line = productionLineMapper.selectProductionLineById(work.getLineId());
			if(line != null){
				line.setManual(1);
				productionLineMapper.updateProductionLine(line);
			}
		}
	    return row;
	}

	/**
     * 删除工位配置对象
     * 
     * @param id 需要删除的数据ID
     * @return 结果
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteWorkstationById(Integer id)
	{
		Workstation work = workstationMapper.selectWorkstationById(id);
		int row = workstationMapper.deleteWorkstationById(id);
		if(work.getSign() == 1){
			workstationMapper.editFirstWorkstionSign(work.getLineId(),work.getCompanyId());
		}
		return row;
	}

	/**
	 * 根据产线查询所以工位信息
	 * @param lineId 产线id
	 * @return
	 */
	@Override
	public List<Workstation> selectAllByLineId(Integer lineId) {
		return workstationMapper.selectAllByLineId(lineId);
	}
}
