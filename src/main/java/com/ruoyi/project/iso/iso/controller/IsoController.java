package com.ruoyi.project.iso.iso.controller;

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
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 文件管理 信息操作处理
 * 
 * @author sj
 * @date 2019-06-13
 */
@Controller
@RequestMapping("/iso/iso")
public class IsoController extends BaseController
{
    private String prefix = "iso/iso";
	
	@Autowired
	private IIsoService isoService;
	
	@RequiresPermissions("iso:iso:view")
	@GetMapping()
	public String iso()
	{
	    return prefix + "/iso";
	}
	
	/**
	 * 查询文件管理列表
	 */
	@RequiresPermissions("iso:iso:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Iso iso)
	{
		startPage();
        List<Iso> list = isoService.selectIsoList(iso);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出文件管理列表
	 */
	@RequiresPermissions("iso:iso:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Iso iso)
    {
    	List<Iso> list = isoService.selectIsoList(iso);
        ExcelUtil<Iso> util = new ExcelUtil<Iso>(Iso.class);
        return util.exportExcel(list, "iso");
    }
	
	/**
	 * 新增文件管理
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存文件管理
	 */
	@RequiresPermissions("iso:iso:add")
	@Log(title = "文件管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Iso iso)
	{		
		return toAjax(isoService.insertIso(iso));
	}

	/**
	 * 修改文件管理
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Iso iso = isoService.selectIsoById(id);
		mmap.put("iso", iso);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存文件管理
	 */
	@RequiresPermissions("iso:iso:edit")
	@Log(title = "文件管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Iso iso)
	{		
		return toAjax(isoService.updateIso(iso));
	}
	
	/**
	 * 删除文件管理
	 */
	@RequiresPermissions("iso:iso:remove")
	@Log(title = "文件管理", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(isoService.deleteIsoByIds(ids));
	}
	
}
