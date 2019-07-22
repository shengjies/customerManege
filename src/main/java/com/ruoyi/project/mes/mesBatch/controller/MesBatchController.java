package com.ruoyi.project.mes.mesBatch.controller;

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
import com.ruoyi.project.mes.mesBatch.domain.MesBatch;
import com.ruoyi.project.mes.mesBatch.service.IMesBatchService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * MES批准追踪 信息操作处理
 * 
 * @author sj
 * @date 2019-07-22
 */
@Controller
@RequestMapping("/mes/mesBatch")
public class MesBatchController extends BaseController
{
    private String prefix = "mes/mesBatch";
	
	@Autowired
	private IMesBatchService mesBatchService;
	
	@RequiresPermissions("mes:mesBatch:view")
	@GetMapping()
	public String mesBatch()
	{
	    return prefix + "/mesBatch";
	}
	
	/**
	 * 查询MES批准追踪列表
	 */
	@RequiresPermissions("mes:mesBatch:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MesBatch mesBatch)
	{
		startPage();
        List<MesBatch> list = mesBatchService.selectMesBatchList(mesBatch);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出MES批准追踪列表
	 */
	@RequiresPermissions("mes:mesBatch:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MesBatch mesBatch)
    {
    	List<MesBatch> list = mesBatchService.selectMesBatchList(mesBatch);
        ExcelUtil<MesBatch> util = new ExcelUtil<MesBatch>(MesBatch.class);
        return util.exportExcel(list, "mesBatch");
    }
	
	/**
	 * 新增MES批准追踪
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存MES批准追踪
	 */
	@RequiresPermissions("mes:mesBatch:add")
	@Log(title = "MES批准追踪", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MesBatch mesBatch)
	{		
		return toAjax(mesBatchService.insertMesBatch(mesBatch));
	}

	/**
	 * 修改MES批准追踪
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		MesBatch mesBatch = mesBatchService.selectMesBatchById(id);
		mmap.put("mesBatch", mesBatch);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存MES批准追踪
	 */
	@RequiresPermissions("mes:mesBatch:edit")
	@Log(title = "MES批准追踪", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MesBatch mesBatch)
	{		
		return toAjax(mesBatchService.updateMesBatch(mesBatch));
	}
	
	/**
	 * 删除MES批准追踪
	 */
	@RequiresPermissions("mes:mesBatch:remove")
	@Log(title = "MES批准追踪", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(mesBatchService.deleteMesBatchByIds(ids));
	}
	
}
