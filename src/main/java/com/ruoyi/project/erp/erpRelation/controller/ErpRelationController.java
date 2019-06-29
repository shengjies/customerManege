package com.ruoyi.project.erp.erpRelation.controller;

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
import com.ruoyi.project.erp.erpRelation.domain.ErpRelation;
import com.ruoyi.project.erp.erpRelation.service.IErpRelationService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 库存出入库MRP操作关联 信息操作处理
 * 
 * @author sj
 * @date 2019-06-28
 */
@Controller
@RequestMapping("/erp/erpRelation")
public class ErpRelationController extends BaseController
{
    private String prefix = "erp/erpRelation";
	
	@Autowired
	private IErpRelationService erpRelationService;
	
	@RequiresPermissions("erp:erpRelation:view")
	@GetMapping()
	public String erpRelation()
	{
	    return prefix + "/erpRelation";
	}
	
	/**
	 * 查询库存出入库MRP操作关联列表
	 */
	@RequiresPermissions("erp:erpRelation:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ErpRelation erpRelation)
	{
		startPage();
        List<ErpRelation> list = erpRelationService.selectErpRelationList(erpRelation);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出库存出入库MRP操作关联列表
	 */
	@RequiresPermissions("erp:erpRelation:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ErpRelation erpRelation)
    {
    	List<ErpRelation> list = erpRelationService.selectErpRelationList(erpRelation);
        ExcelUtil<ErpRelation> util = new ExcelUtil<ErpRelation>(ErpRelation.class);
        return util.exportExcel(list, "erpRelation");
    }
	
	/**
	 * 新增库存出入库MRP操作关联
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存库存出入库MRP操作关联
	 */
	@RequiresPermissions("erp:erpRelation:add")
	@Log(title = "库存出入库MRP操作关联", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ErpRelation erpRelation)
	{		
		return toAjax(erpRelationService.insertErpRelation(erpRelation));
	}

	/**
	 * 修改库存出入库MRP操作关联
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		ErpRelation erpRelation = erpRelationService.selectErpRelationById(id);
		mmap.put("erpRelation", erpRelation);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存库存出入库MRP操作关联
	 */
	@RequiresPermissions("erp:erpRelation:edit")
	@Log(title = "库存出入库MRP操作关联", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ErpRelation erpRelation)
	{		
		return toAjax(erpRelationService.updateErpRelation(erpRelation));
	}
	
	/**
	 * 删除库存出入库MRP操作关联
	 */
	@RequiresPermissions("erp:erpRelation:remove")
	@Log(title = "库存出入库MRP操作关联", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(erpRelationService.deleteErpRelationByIds(ids));
	}
	
}
