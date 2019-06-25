package com.ruoyi.project.erp.mrp.controller;

import java.util.List;

import com.ruoyi.common.exception.BusinessException;
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
import com.ruoyi.project.erp.mrp.domain.Mrp;
import com.ruoyi.project.erp.mrp.service.IMrpService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * MRP记录 信息操作处理
 * 
 * @author sj
 * @date 2019-06-24
 */
@Controller
@RequestMapping("/erp/mrp")
public class MrpController extends BaseController
{
    private String prefix = "erp/mrp";
	
	@Autowired
	private IMrpService mrpService;
	
	@RequiresPermissions("erp:mrp:view")
	@GetMapping()
	public String mrp()
	{
	    return prefix + "/mrp";
	}
	
	/**
	 * 查询MRP记录列表
	 */
	@RequiresPermissions("erp:mrp:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Mrp mrp)
	{
		startPage();
        List<Mrp> list = mrpService.selectMrpList(mrp);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出MRP记录列表
	 */
	@RequiresPermissions("erp:mrp:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Mrp mrp)
    {
    	List<Mrp> list = mrpService.selectMrpList(mrp);
        ExcelUtil<Mrp> util = new ExcelUtil<Mrp>(Mrp.class);
        return util.exportExcel(list, "mrp");
    }
	
	/**
	 * 跳转选择订单明细页面，选择订单生产MRP
	 */
	@GetMapping("/addMrp")
	public String addMrp()
	{
	    return prefix + "/checkOrder";
	}
	
	/**
	 * 新增保存MRP记录
	 */
	@RequiresPermissions("erp:mrp:add")
	@Log(title = "MRP记录", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Mrp mrp)
	{		
		return toAjax(mrpService.insertMrp(mrp));
	}

	/**
	 * 修改MRP记录
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Mrp mrp = mrpService.selectMrpById(id);
		mmap.put("mrp", mrp);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存MRP记录
	 */
	@RequiresPermissions("erp:mrp:edit")
	@Log(title = "MRP记录", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Mrp mrp)
	{		
		return toAjax(mrpService.updateMrp(mrp));
	}
	
	/**
	 * 删除MRP记录
	 */
	@RequiresPermissions("erp:mrp:remove")
	@Log(title = "MRP记录", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(mrpService.deleteMrpByIds(ids));
	}

	/**
	 * 将选中的订单生成Mrp
	 */
	@RequiresPermissions("erp:mrp:add")
	@PostMapping( "/addMrpByOrDeIds")
	@ResponseBody
	public AjaxResult addMrpByOrDeIds(String mrps)
	{
		try {
			return toAjax(mrpService.addMrpByOrDeIds(mrps));
		} catch (BusinessException e) {
			return error(e.getMessage());
		}
	}
}
