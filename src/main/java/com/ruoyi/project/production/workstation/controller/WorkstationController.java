package com.ruoyi.project.production.workstation.controller;

import java.util.List;

import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.service.IProductionLineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.service.IWorkstationService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 工位配置 信息操作处理
 * 
 * @author sj
 * @date 2019-06-13
 */
@Controller
@RequestMapping("/production/workstation")
public class WorkstationController extends BaseController
{
    private String prefix = "production/workstation";

	@Autowired
	private IWorkstationService workstationService;

	@Autowired
	private IProductionLineService productionLineService;

	@GetMapping("/{id}")
	public String workstation(@PathVariable("id") int id, ModelMap mmap)
	{
		mmap.put("line",productionLineService.selectProductionLineById(id));
	    return prefix + "/workstation";
	}
	
	/**
	 * 查询工位配置列表
	 */
	@RequiresPermissions("production:workstation:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Workstation workstation)
	{
		startPage();
        List<Workstation> list = workstationService.selectWorkstationList(workstation);
		return getDataTable(list);
	}

	/**
	 * 新增工位配置
	 */
	@GetMapping("/add")
	public String add(int line,int sopid,ModelMap mmap)
	{
		mmap.put("line",line);
		mmap.put("sopid",sopid);
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存工位配置
	 */
	@RequiresPermissions("production:workstation:add")
	@Log(title = "工位配置", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Workstation workstation,HttpServletRequest request)
	{
	    try {
	    	workstation.setCompanyId(JwtUtil.getTokenUser(request).getCompanyId());
            workstationService.insertWorkstation(workstation);
            return AjaxResult.success();
        }catch (Exception e){
	        return AjaxResult.error(e.getMessage());
        }

	}

	/**
	 * 修改工位配置
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Workstation workstation = workstationService.selectWorkstationById(id);
		mmap.put("workstation", workstation);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存工位配置
	 */
	@RequiresPermissions("production:workstation:edit")
	@Log(title = "工位配置", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Workstation workstation)
	{
		try {
			workstationService.updateWorkstation(workstation);
			return AjaxResult.success();
		}catch (Exception e){
			return AjaxResult.error(e.getMessage());
		}
	}
	
	/**
	 * 删除工位配置
	 */
	@RequiresPermissions("production:workstation:remove")
	@Log(title = "工位配置", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(Integer id)
	{
		return toAjax(workstationService.deleteWorkstationById(id));
	}
	
}
