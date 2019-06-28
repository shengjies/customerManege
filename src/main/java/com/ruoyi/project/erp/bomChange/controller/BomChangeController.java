package com.ruoyi.project.erp.bomChange.controller;

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
import com.ruoyi.project.erp.bomChange.domain.BomChange;
import com.ruoyi.project.erp.bomChange.service.IBomChangeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * BOM更改记录 信息操作处理
 * 
 * @author sj
 * @date 2019-06-26
 */
@Controller
@RequestMapping("/erp/bomChange")
public class BomChangeController extends BaseController
{
    private String prefix = "erp/bomChange";
	
	@Autowired
	private IBomChangeService bomChangeService;
	
	@RequiresPermissions("erp:bomChange:view")
	@GetMapping()
	public String bomChange()
	{
	    return prefix + "/bomChange";
	}
	
	/**
	 * 查询BOM更改记录列表
	 */
	@RequiresPermissions("erp:bomChange:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(BomChange bomChange)
	{
		startPage();
        List<BomChange> list = bomChangeService.selectBomChangeList(bomChange);
		return getDataTable(list);
	}

	
	/**
	 * 审核保存BOM更改记录
	 */
	@RequiresPermissions("erp:bomChange:edit")
	@Log(title = "BOM更改记录", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(int id)
	{		
		return toAjax(bomChangeService.examineBomChange(id));
	}
	
	/**
	 * 删除BOM更改记录
	 */
	@RequiresPermissions("erp:bomChange:remove")
	@Log(title = "BOM更改记录", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(int id)
	{		
		return toAjax(bomChangeService.cancelBomChange(id));
	}
	
}
