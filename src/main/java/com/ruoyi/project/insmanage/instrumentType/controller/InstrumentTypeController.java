package com.ruoyi.project.insmanage.instrumentType.controller;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.insmanage.instrumentType.domain.InstrumentType;
import com.ruoyi.project.insmanage.instrumentType.service.IInstrumentTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备类型 信息操作处理
 * 
 * @author sj
 * @date 2019-07-02
 */
@Controller
@RequestMapping("/insmanage/instrumentType")
public class InstrumentTypeController extends BaseController
{
    private String prefix = "insmanage/instrumentType";
	
	@Autowired
	private IInstrumentTypeService instrumentTypeService;
	
	@RequiresPermissions("insmanage:instrumentType:view")
	@GetMapping()
	public String instrumentType()
	{
	    return prefix + "/instrumentType";
	}
	
	/**
	 * 查询设备类型列表
	 */
	@RequiresPermissions("insmanage:instrumentType:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(InstrumentType instrumentType)
	{
		startPage();
        List<InstrumentType> list = instrumentTypeService.selectInstrumentTypeList(instrumentType);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出设备类型列表
	 */
	@RequiresPermissions("insmanage:instrumentType:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(InstrumentType instrumentType)
    {
    	List<InstrumentType> list = instrumentTypeService.selectInstrumentTypeList(instrumentType);
        ExcelUtil<InstrumentType> util = new ExcelUtil<InstrumentType>(InstrumentType.class);
        return util.exportExcel(list, "instrumentType");
    }
	
	/**
	 * 新增设备类型
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存设备类型
	 */
	@RequiresPermissions("insmanage:instrumentType:add")
	@Log(title = "设备类型", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(InstrumentType instrumentType)
	{		
		return toAjax(instrumentTypeService.insertInstrumentType(instrumentType));
	}

	/**
	 * 修改设备类型
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		InstrumentType instrumentType = instrumentTypeService.selectInstrumentTypeById(id);
		mmap.put("instrumentType", instrumentType);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存设备类型
	 */
	@RequiresPermissions("insmanage:instrumentType:edit")
	@Log(title = "设备类型", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(InstrumentType instrumentType)
	{		
		return toAjax(instrumentTypeService.updateInstrumentType(instrumentType));
	}
	
	/**
	 * 删除设备类型
	 */
	@RequiresPermissions("insmanage:instrumentType:remove")
	@Log(title = "设备类型", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{
		try {
			return toAjax(instrumentTypeService.deleteInstrumentTypeByIds(ids));
		} catch (BusinessException e) {
			return error(e.getMessage());
		}
	}

	/**
	 * 校验设备类型名称的唯一性
	 */
	@PostMapping("/checkInsTypeUnique")
	@ResponseBody
	public String checkInsTypeUnique(InstrumentType instrumentType){
		return instrumentTypeService.checkInsTypeUnique(instrumentType);
	}
}
