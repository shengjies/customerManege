package com.ruoyi.project.erp.mrpPurchase.controller;

import java.util.List;
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
import com.ruoyi.project.erp.mrpPurchase.domain.MrpPurchase;
import com.ruoyi.project.erp.mrpPurchase.service.IMrpPurchaseService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * mrp采购单关联 信息操作处理
 * 
 * @author sj
 * @date 2019-06-26
 */
@Controller
@RequestMapping("/erp/mrpPurchase")
public class MrpPurchaseController extends BaseController
{
    private String prefix = "erp/mrpPurchase";
	
	@Autowired
	private IMrpPurchaseService mrpPurchaseService;
	
	@RequiresPermissions("erp:mrpPurchase:view")
	@GetMapping()
	public String mrpPurchase()
	{
	    return prefix + "/mrpPurchase";
	}
	
	/**
	 * 查询mrp采购单关联列表
	 */
	@RequiresPermissions("erp:mrpPurchase:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MrpPurchase mrpPurchase)
	{
		startPage();
        List<MrpPurchase> list = mrpPurchaseService.selectMrpPurchaseList(mrpPurchase);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出mrp采购单关联列表
	 */
	@RequiresPermissions("erp:mrpPurchase:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MrpPurchase mrpPurchase)
    {
    	List<MrpPurchase> list = mrpPurchaseService.selectMrpPurchaseList(mrpPurchase);
        ExcelUtil<MrpPurchase> util = new ExcelUtil<MrpPurchase>(MrpPurchase.class);
        return util.exportExcel(list, "mrpPurchase");
    }
	
	/**
	 * 新增mrp采购单关联
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存mrp采购单关联
	 */
	@RequiresPermissions("erp:mrpPurchase:add")
	@Log(title = "mrp采购单关联", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MrpPurchase mrpPurchase)
	{		
		return toAjax(mrpPurchaseService.insertMrpPurchase(mrpPurchase));
	}

	/**
	 * 修改mrp采购单关联
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		MrpPurchase mrpPurchase = mrpPurchaseService.selectMrpPurchaseById(id);
		mmap.put("mrpPurchase", mrpPurchase);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存mrp采购单关联
	 */
	@RequiresPermissions("erp:mrpPurchase:edit")
	@Log(title = "mrp采购单关联", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MrpPurchase mrpPurchase)
	{		
		return toAjax(mrpPurchaseService.updateMrpPurchase(mrpPurchase));
	}
	
	/**
	 * 删除mrp采购单关联
	 */
	@RequiresPermissions("erp:mrpPurchase:remove")
	@Log(title = "mrp采购单关联", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(mrpPurchaseService.deleteMrpPurchaseByIds(ids));
	}
	
}
