package com.ruoyi.project.mes.mesBatchRule.controller;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.mes.mesBatchRule.domain.MesBatchRule;
import com.ruoyi.project.mes.mesBatchRule.service.IMesBatchRuleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MES批准追踪规则 信息操作处理
 * 
 * @author sj
 * @date 2019-07-22
 */
@Controller
@RequestMapping("/mes/mesBatchRule")
public class MesBatchRuleController extends BaseController
{
    private String prefix = "mes/mesBatchRule";
	
	@Autowired
	private IMesBatchRuleService mesBatchRuleService;
	
	@RequiresPermissions("mes:mesBatchRule:view")
	@GetMapping()
	public String mesBatchRule()
	{
	    return prefix + "/mesBatchRule";
	}
	
	/**
	 * 查询MES批准追踪规则列表
	 */
	@RequiresPermissions("mes:mesBatchRule:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MesBatchRule mesBatchRule)
	{
		startPage();
        List<MesBatchRule> list = mesBatchRuleService.selectMesBatchRuleList(mesBatchRule);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出MES批准追踪规则列表
	 */
	@RequiresPermissions("mes:mesBatchRule:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MesBatchRule mesBatchRule)
    {
    	List<MesBatchRule> list = mesBatchRuleService.selectMesBatchRuleList(mesBatchRule);
        ExcelUtil<MesBatchRule> util = new ExcelUtil<MesBatchRule>(MesBatchRule.class);
        return util.exportExcel(list, "mesBatchRule");
    }
	
	/**
	 * 新增MES批准追踪规则
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存MES批准追踪规则
	 */
	@RequiresPermissions("mes:mesBatchRule:add")
	@Log(title = "MES批准追踪规则", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@RequestBody MesBatchRule mesBatchRule)
	{
		try {
			return toAjax(mesBatchRuleService.insertMesBatchRule(mesBatchRule));
		} catch (BusinessException e) {
			return error(e.getMessage());
		}
	}

	/**
	 * 修改MES批准追踪规则
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		MesBatchRule mesBatchRule = mesBatchRuleService.selectMesBatchRuleById(id);
		mmap.put("mesBatchRule", mesBatchRule);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存MES批准追踪规则
	 */
	@RequiresPermissions("mes:mesBatchRule:edit")
	@Log(title = "MES批准追踪规则", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MesBatchRule mesBatchRule)
	{		
		return toAjax(mesBatchRuleService.updateMesBatchRule(mesBatchRule));
	}
	
	/**
	 * 删除MES批准追踪规则
	 */
	@RequiresPermissions("mes:mesBatchRule:remove")
	@Log(title = "MES批准追踪规则", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(mesBatchRuleService.deleteMesBatchRuleByIds(ids));
	}

	/**
	 * 校验追踪规格名称的唯一性
	 */
	@PostMapping("/checkMesRuleNameUnique")
	@ResponseBody
	public String checkMesRuleNameUnique(MesBatchRule mesBatchRule){
		return mesBatchRuleService.checkMesRuleNameUnique(mesBatchRule);
	}
	
}
