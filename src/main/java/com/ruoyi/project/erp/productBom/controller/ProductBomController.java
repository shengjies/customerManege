package com.ruoyi.project.erp.productBom.controller;

import java.util.List;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.project.erp.productBom.domain.BomConfig;
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
import com.ruoyi.project.erp.productBom.domain.ProductBom;
import com.ruoyi.project.erp.productBom.service.IProductBomService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 产品BOM单 信息操作处理
 * 
 * @author sj
 * @date 2019-06-24
 */
@Controller
@RequestMapping("/erp/productBom")
public class ProductBomController extends BaseController
{
    private String prefix = "erp/productBom";
	
	@Autowired
	private IProductBomService productBomService;
	
	@RequiresPermissions("erp:productBom:view")
	@GetMapping()
	public String productBom()
	{
	    return prefix + "/productBom";
	}
	
	/**
	 * 查询产品BOM单列表
	 */
	@RequiresPermissions("erp:productBom:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProductBom productBom)
	{
		startPage();
        List<ProductBom> list = productBomService.selectProductBomList(productBom);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出产品BOM单列表
	 */
	@RequiresPermissions("erp:productBom:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProductBom productBom)
    {
    	List<ProductBom> list = productBomService.selectProductBomList(productBom);
        ExcelUtil<ProductBom> util = new ExcelUtil<ProductBom>(ProductBom.class);
        return util.exportExcel(list, "productBom");
    }

	/**
	 * 新增产品BOM单
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}

	/**
	 * 新增保存产品BOM单
	 */
	@RequiresPermissions("erp:productBom:add")
	@Log(title = "产品BOM单", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MultipartFile file,int pid)
	{
		String message = null;
		try {
			message = productBomService.insertProductBom(file,pid);
		} catch (Exception e) {
			return AjaxResult.error(e.getMessage());
		}
		return AjaxResult.success(message);
	}

	/**
	 * 修改产品BOM单
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		ProductBom productBom = productBomService.selectProductBomById(id);
		mmap.put("productBom", productBom);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存产品BOM单
	 */
	@RequiresPermissions("erp:productBom:edit")
	@Log(title = "产品BOM单", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ProductBom productBom)
	{		
		return toAjax(productBomService.updateProductBom(productBom));
	}
	
	/**
	 * 删除产品BOM单
	 */
	@RequiresPermissions("erp:productBom:remove")
	@Log(title = "产品BOM单", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(productBomService.deleteProductBomByIds(ids));
	}


	/**
	 * bom 配置
	 * @return
	 */
	@GetMapping("/config")
	public String config(ModelMap mmap){
		mmap.put("config",productBomService.selectBomConfigLimit1());
		return prefix+"/config";
	}
	/**
	 * 保存BOM单解析配置
	 */
	@RequiresPermissions("erp:bomConfig:add")
	@Log(title = "BOM单配置", businessType = BusinessType.UPDATE)
	@PostMapping("/config/edit")
	@ResponseBody
	public AjaxResult saveConfig(BomConfig config){
		return toAjax(productBomService.saveBomConfig(config));
	}
	
}
