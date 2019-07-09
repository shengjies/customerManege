package com.ruoyi.project.production.singleWork.controller;

import java.util.List;

import com.ruoyi.project.production.singleWork.domain.SingleWorkOrder;
import com.ruoyi.project.production.singleWork.service.ISingleWorkOrderService;
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
 * 单工位与工单进行配置 信息操作处理
 * 
 * @author sj
 * @date 2019-07-09
 */
@Controller
@RequestMapping("/production/singleWorkOrder")
public class SingleWorkOrderController extends BaseController
{
    private String prefix = "production/singleWorkOrder";
	
	@Autowired
	private ISingleWorkOrderService singleWorkOrderService;

	/**
	 *
	 * @param type 0、单工位配置  1、工单配置
	 * @param orderId 工单id
	 * @param singleId 单工位id
	 * @param modelMap
	 * @return
	 */
	@GetMapping()
	public String singleWorkOrder(int type,int orderId,int singleId,ModelMap modelMap)
	{
		modelMap.put("type",type);
		modelMap.put("orderId",orderId);
		modelMap.put("singleId",singleId);
		return prefix + "/singleWorkOrder";
	}
	
	/**
	 * 查询单工位与工单进行配置列表
	 */
	@RequiresPermissions("production:singleWorkOrder:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SingleWorkOrder singleWorkOrder)
	{
		startPage();
        List<SingleWorkOrder> list = singleWorkOrderService.selectSingleWorkOrderList(singleWorkOrder);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出单工位与工单进行配置列表
	 */
	@RequiresPermissions("production:singleWorkOrder:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SingleWorkOrder singleWorkOrder)
    {
    	List<SingleWorkOrder> list = singleWorkOrderService.selectSingleWorkOrderList(singleWorkOrder);
        ExcelUtil<SingleWorkOrder> util = new ExcelUtil<SingleWorkOrder>(SingleWorkOrder.class);
        return util.exportExcel(list, "singleWorkOrder");
    }

	/**
	 * 新增单工位与工单进行配置
	 * @param type 0、单工位配置  1、工单配置
	 * @param orderId 工单id
	 * @param singleId 单工位id
	 * @return
	 */
	@GetMapping("/add")
	public String add(int type,int orderId,int singleId,ModelMap modelMap)
	{
		modelMap.put("type",type);
		modelMap.put("orderId",orderId);
		modelMap.put("singleId",singleId);
		return prefix + "/add";
	}
	
	/**
	 * 新增保存单工位与工单进行配置
	 */
	@RequiresPermissions("production:singleWorkOrder:add")
	@Log(title = "单工位与工单进行配置", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SingleWorkOrder singleWorkOrder)
	{
		try {
			return toAjax(singleWorkOrderService.insertSingleWorkOrder(singleWorkOrder));
		}catch (Exception e){
			return AjaxResult.error(e.getMessage());
		}
	}

	/**
	 * 修改单工位与工单进行配置
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		SingleWorkOrder singleWorkOrder = singleWorkOrderService.selectSingleWorkOrderById(id);
		mmap.put("singleWorkOrder", singleWorkOrder);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存单工位与工单进行配置
	 */
	@RequiresPermissions("production:singleWorkOrder:edit")
	@Log(title = "单工位与工单进行配置", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SingleWorkOrder singleWorkOrder)
	{		
		return toAjax(singleWorkOrderService.updateSingleWorkOrder(singleWorkOrder));
	}
	
	/**
	 * 删除单工位与工单进行配置
	 */
	@RequiresPermissions("production:singleWorkOrder:remove")
	@Log(title = "单工位与工单进行配置", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(singleWorkOrderService.deleteSingleWorkOrderByIds(ids));
	}
	
}
