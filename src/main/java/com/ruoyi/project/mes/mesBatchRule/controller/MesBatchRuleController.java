package com.ruoyi.project.mes.mesBatchRule.controller;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.mes.mesBatchRule.domain.MesBatchRule;
import com.ruoyi.project.mes.mesBatchRule.service.IMesBatchRuleService;
import com.ruoyi.project.product.list.service.IDevProductListService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Autowired
	private IDevProductListService productListService;

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
	public AjaxResult addSave(MesBatchRule mesBatchRule)
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
		try {
			return toAjax(mesBatchRuleService.deleteMesBatchRuleByIds(ids));
		} catch (BusinessException e) {
			return error(e.getMessage());
		}
	}

	/**
	 * 校验追踪规格名称的唯一性
	 */
	@PostMapping("/checkMesRuleNameUnique")
	@ResponseBody
	public String checkMesRuleNameUnique(MesBatchRule mesBatchRule){
		return mesBatchRuleService.checkMesRuleNameUnique(mesBatchRule);
	}

	/**
	 * 查看规则配置明细
	 */
	@GetMapping("/showCfDetail")
	public String showCfDetail(int id,int pType,ModelMap map){
		map.put("ruleId",id);
		map.put("pType",pType);
		map.put("proList",productListService.selectProductAllByCompanyId(pType));
		return prefix + "/cfDetail";
	}

	/**
	 * 查询规则明细
	 */
	@PostMapping("/selectMesBatchRuleById")
	@ResponseBody
	public AjaxResult selectMesBatchRuleById(int id){
		Map<String,Object> map = new HashMap<>(16);
		map.put("mesList",mesBatchRuleService.selectMesBatchRuleById(id));
		map.put("mesCode", CodeUtils.getMesCode());
		return AjaxResult.success(map);
	}
}
