package com.ruoyi.project.insmanage.instrumentManage.controller;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.insmanage.instrumentManage.domain.InstrumentManage;
import com.ruoyi.project.insmanage.instrumentManage.service.IInstrumentManageService;
import com.ruoyi.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        return util.exportExcel(list, "仪器设备");
    }

	/**
	 * 导出仪器设备模板
	 */
	@RequiresPermissions("insmanage:instrumentManage:view")
	@GetMapping("/importTemplate")
	@ResponseBody
	public AjaxResult importTemplate() {
		ExcelUtil<InstrumentManage> imListExcelUtil = new ExcelUtil<>(InstrumentManage.class);
		return imListExcelUtil.importTemplateExcel("仪器设备");
	}

	/**
	 * 导入仪器设备
	 */
	@RequiresPermissions("insmanage:instrumentManage:import")
	@PostMapping("/importData")
	@ResponseBody
	public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
		ExcelUtil<InstrumentManage> util = new ExcelUtil<>(InstrumentManage.class);
		List<InstrumentManage> imList = util.importExcel(file.getInputStream());
		String message = null;
		try {
			message = instrumentManageService.importInstrumentManageList(imList, updateSupport);
		} catch (BusinessException e) {
			return AjaxResult.error(e.getMessage());
		}
		return AjaxResult.success(message);
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
	public AjaxResult addSave(InstrumentManage instrumentManage)
	{
		User user = JwtUtil.getUser();
		if (user == null) {
		    return error(UserConstants.NOT_LOGIN);
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
	 * 查看设备明细
	 */
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Integer id, ModelMap mmap)
	{
		InstrumentManage instrumentManage = instrumentManageService.selectInstrumentManageById(id);
		mmap.put("instrumentManage", instrumentManage);
		return prefix + "/detail";
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
		try {
			return toAjax(instrumentManageService.updateInstrumentManage(instrumentManage));
		} catch (BusinessException e) {
			return error(e.getMessage());
		}
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
		try {
			return toAjax(instrumentManageService.deleteInstrumentManageByIds(ids));
		} catch (BusinessException e) {
			return error(e.getMessage());
		}
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

	/**
	 * 校验设备编码唯一性
	 */
	@PostMapping("/checkImCodeUnique")
	@ResponseBody
	public String checkImCodeUnique(InstrumentManage instrumentManage){
		return instrumentManageService.checkImCodeUnique(instrumentManage);
	}



	/******************************************************************************************************
	 *********************************** app仪器设备管理交互 ***********************************************
	 ******************************************************************************************************/

	/**
	 * app端查询设备列表
	 */
	@PostMapping("/applist")
	@ResponseBody
	public AjaxResult appSelectList(@RequestBody InstrumentManage instrumentManage){
		try {
			if (instrumentManage != null) {
			    instrumentManage.appStartPage();
				return AjaxResult.success("请求成功",instrumentManageService.appSelectList(instrumentManage));
			}
			return error();
		} catch (Exception e) {
			return error();
		}
	}
}
