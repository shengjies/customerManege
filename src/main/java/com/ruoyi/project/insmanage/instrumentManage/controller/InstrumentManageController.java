package com.ruoyi.project.insmanage.instrumentManage.controller;

import java.util.List;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.system.user.domain.User;
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
import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import com.ruoyi.project.insmanage.instrumentManage.service.IInstrumentManageService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 仪器设备管理 信息操作处理
 * 
 * @author sj
 * @date 2019-06-19
 */
@Controller
@RequestMapping("/insmanage/instrumentManage")
public class InstrumentManageController extends BaseController
{
    private String prefix = "insmanage/instrumentManage";
	
	@Autowired
	private IInstrumentManageService instrumentManageService;
	
	@RequiresPermissions("insmanage:instrumentManage:view")
	@GetMapping()
	public String instrumentManage()
	{
	    return prefix + "/instrumentManage";
	}
	
	/**
	 * 查询仪器设备管理列表
	 */
	@RequiresPermissions("insmanage:instrumentManage:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(InstrumentManage instrumentManage, HttpServletRequest request)
	{
		startPage();
        List<InstrumentManage> list = instrumentManageService.selectInstrumentManageList(instrumentManage,request);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出仪器设备管理列表
	 */
	@RequiresPermissions("insmanage:instrumentManage:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(InstrumentManage instrumentManage,HttpServletRequest request)
    {
    	List<InstrumentManage> list = instrumentManageService.selectInstrumentManageList(instrumentManage,request);
        ExcelUtil<InstrumentManage> util = new ExcelUtil<InstrumentManage>(InstrumentManage.class);
        return util.exportExcel(list, "instrumentManage");
    }
	
	/**
	 * 新增仪器设备管理
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存仪器设备管理
	 */
	@RequiresPermissions("insmanage:instrumentManage:add")
	@Log(title = "仪器设备管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(InstrumentManage instrumentManage,HttpServletRequest request)
	{
		User user = JwtUtil.getTokenUser(request);
		if (user == null) {
		    return error("用户未登陆或登陆超时");
		}
		return toAjax(instrumentManageService.insertInstrumentManage(instrumentManage,user));
	}

	/**
	 * 修改仪器设备管理
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		InstrumentManage instrumentManage = instrumentManageService.selectInstrumentManageById(id);
		mmap.put("instrumentManage", instrumentManage);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存仪器设备管理
	 */
	@RequiresPermissions("insmanage:instrumentManage:edit")
	@Log(title = "仪器设备管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(InstrumentManage instrumentManage)
	{		
		return toAjax(instrumentManageService.updateInstrumentManage(instrumentManage));
	}
	
	/**
	 * 删除仪器设备管理
	 */
	@RequiresPermissions("insmanage:instrumentManage:remove")
	@Log(title = "仪器设备管理", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(instrumentManageService.deleteInstrumentManageByIds(ids));
	}

	/**
	 * 设备状态修改
	 */
	@RequiresPermissions("insmanage:instrumentManage:edit")
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(InstrumentManage instrumentManage,HttpServletRequest request) {
		try {
			return toAjax(instrumentManageService.changeStatus(instrumentManage,request));
		} catch (BusinessException e) {
			return error(e.getMessage());
		}
	}
	
}
