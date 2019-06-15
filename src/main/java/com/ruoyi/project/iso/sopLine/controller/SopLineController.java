package com.ruoyi.project.iso.sopLine.controller;

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
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.service.ISopLineService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 作业指导书  产线 配置 信息操作处理
 * 
 * @author sj
 * @date 2019-06-15
 */
@Controller
@RequestMapping("/iso/sopLine")
public class SopLineController extends BaseController
{
    private String prefix = "iso/sopLine";
	
	@Autowired
	private ISopLineService sopLineService;
	
	@RequiresPermissions("iso:sopLine:view")
	@GetMapping()
	public String sopLine()
	{
	    return prefix + "/sopLine";
	}
	
	/**
	 * 查询作业指导书  产线 配置列表
	 */
	@RequiresPermissions("iso:sopLine:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SopLine sopLine)
	{
		startPage();
        List<SopLine> list = sopLineService.selectSopLineList(sopLine);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出作业指导书  产线 配置列表
	 */
	@RequiresPermissions("iso:sopLine:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SopLine sopLine)
    {
    	List<SopLine> list = sopLineService.selectSopLineList(sopLine);
        ExcelUtil<SopLine> util = new ExcelUtil<SopLine>(SopLine.class);
        return util.exportExcel(list, "sopLine");
    }
	
	/**
	 * 新增作业指导书  产线 配置
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存作业指导书  产线 配置
	 */
	@RequiresPermissions("iso:sopLine:add")
	@Log(title = "作业指导书  产线 配置", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SopLine sopLine)
	{		
		return toAjax(sopLineService.insertSopLine(sopLine));
	}

	/**
	 * 修改作业指导书  产线 配置
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		SopLine sopLine = sopLineService.selectSopLineById(id);
		mmap.put("sopLine", sopLine);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存作业指导书  产线 配置
	 */
	@RequiresPermissions("iso:sopLine:edit")
	@Log(title = "作业指导书  产线 配置", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SopLine sopLine)
	{		
		return toAjax(sopLineService.updateSopLine(sopLine));
	}
	
	/**
	 * 删除作业指导书  产线 配置
	 */
	@RequiresPermissions("iso:sopLine:remove")
	@Log(title = "作业指导书  产线 配置", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(sopLineService.deleteSopLineByIds(ids));
	}
	
}
