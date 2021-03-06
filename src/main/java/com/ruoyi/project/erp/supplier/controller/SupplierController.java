package com.ruoyi.project.erp.supplier.controller;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.erp.supplier.domain.Supplier;
import com.ruoyi.project.erp.supplier.service.ISupplierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 供应商数据 信息操作处理
 *
 * @author zqm
 * @date 2019-04-30
 */
@Controller
@RequestMapping("/erp/supplier")
public class SupplierController extends BaseController {
    private String prefix = "erp/supplier";

    @Autowired
    private ISupplierService supplierService;

    @RequiresPermissions("erp:supplier:view")
    @GetMapping()
    public String supplier(HttpServletResponse response) {
        return prefix + "/supplier";
    }

    /**
     * 查询供应商数据列表
     */
    @RequiresPermissions("erp:supplier:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Supplier supplier, HttpServletRequest request,HttpServletResponse response) {
        startPage();
        List<Supplier> list = supplierService.selectSupplierList(supplier,request);
        return getDataTable(list);
    }


    /**
     * 导出供应商数据列表
     */
    @RequiresPermissions("erp:supplier:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Supplier supplier,HttpServletRequest request) {
        List<Supplier> list = supplierService.selectSupplierList(supplier,request);
        ExcelUtil<Supplier> util = new ExcelUtil<Supplier>(Supplier.class);
        return util.exportExcel(list, "supplier");
    }

    /**
     * 新增供应商数据
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存供应商数据
     */
    @RequiresPermissions("erp:supplier:add")
    @Log(title = "供应商数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Supplier supplier,HttpServletRequest request) {
        return toAjax(supplierService.insertSupplier(supplier,request));
    }

    /**
     * 修改供应商数据
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        Supplier supplier = supplierService.selectSupplierById(id);
        mmap.put("supplier", supplier);
        return prefix + "/edit";
    }

    /**
     * 修改保存供应商数据
     */
    @RequiresPermissions("erp:supplier:edit")
    @Log(title = "供应商数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Supplier supplier) {
        return toAjax(supplierService.updateSupplier(supplier));
    }

    /**
     * 删除供应商数据
     */
    @RequiresPermissions("erp:supplier:remove")
    @Log(title = "供应商数据", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(supplierService.deleteSupplierByIds(ids));
    }

}
