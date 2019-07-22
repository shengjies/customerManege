package com.ruoyi.project.mes.mesBatch.controller;

import java.util.List;

import com.ruoyi.project.mes.mesBatch.domain.MesBatchDetail;
import com.ruoyi.project.mes.mesBatch.service.IMesBatchDetailService;
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
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * MES批准追踪详情 信息操作处理
 * 
 * @author sj
 * @date 2019-07-22
 */
@Controller
@RequestMapping("/mes/mesBatchDetail")
public class MesBatchDetailController extends BaseController
{
    private String prefix = "mes/mesBatchDetail";
	
	@Autowired
	private IMesBatchDetailService mesBatchDetailService;
	
	@RequiresPermissions("mes:mesBatchDetail:view")
	@GetMapping()
	public String mesBatchDetail()
	{
	    return prefix + "/mesBatchDetail";
	}
	
	/**
	 * 查询MES批准追踪详情列表
	 */
	@RequiresPermissions("mes:mesBatchDetail:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MesBatchDetail mesBatchDetail)
	{
		startPage();
        List<MesBatchDetail> list = mesBatchDetailService.selectMesBatchDetailList(mesBatchDetail);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出MES批准追踪详情列表
	 */
	@RequiresPermissions("mes:mesBatchDetail:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MesBatchDetail mesBatchDetail)
    {
    	List<MesBatchDetail> list = mesBatchDetailService.selectMesBatchDetailList(mesBatchDetail);
        ExcelUtil<MesBatchDetail> util = new ExcelUtil<MesBatchDetail>(MesBatchDetail.class);
        return util.exportExcel(list, "mesBatchDetail");
    }
	
	/**
	 * 新增MES批准追踪详情
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存MES批准追踪详情
	 */
	@RequiresPermissions("mes:mesBatchDetail:add")
	@Log(title = "MES批准追踪详情", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MesBatchDetail mesBatchDetail)
	{		
		return toAjax(mesBatchDetailService.insertMesBatchDetail(mesBatchDetail));
	}

	/**
	 * 修改MES批准追踪详情
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		MesBatchDetail mesBatchDetail = mesBatchDetailService.selectMesBatchDetailById(id);
		mmap.put("mesBatchDetail", mesBatchDetail);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存MES批准追踪详情
	 */
	@RequiresPermissions("mes:mesBatchDetail:edit")
	@Log(title = "MES批准追踪详情", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MesBatchDetail mesBatchDetail)
	{		
		return toAjax(mesBatchDetailService.updateMesBatchDetail(mesBatchDetail));
	}
	
	/**
	 * 删除MES批准追踪详情
	 */
	@RequiresPermissions("mes:mesBatchDetail:remove")
	@Log(title = "MES批准追踪详情", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(mesBatchDetailService.deleteMesBatchDetailByIds(ids));
	}
	
}
