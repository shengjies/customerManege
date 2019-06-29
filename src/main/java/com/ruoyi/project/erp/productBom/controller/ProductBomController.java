package com.ruoyi.project.erp.productBom.controller;

import java.util.List;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.poi.ExcelUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.bomChange.domain.BomChange;
import com.ruoyi.project.erp.bomChange.service.IBomChangeService;
import com.ruoyi.project.erp.productBom.domain.BomConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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
public class ProductBomController extends BaseController {
    private String prefix = "erp/productBom";

    @Autowired
    private IProductBomService productBomService;

    @Autowired
    private IBomChangeService bomChangeService;

    @RequiresPermissions("erp:productBom:view")
    @GetMapping()
    public String productBom() {
        return prefix + "/productBom";
    }

    /**
     * 查询产品BOM单列表
     */
    @RequiresPermissions("erp:productBom:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProductBom productBom) {
        startPage();
        productBom.setCompanyId(JwtUtil.getTokenUser(ServletUtils.getRequest()).getCompanyId());
        List<ProductBom> list = productBomService.selectProductBomList(productBom);
        return getDataTable(list);
    }


    /**
     * 导出产品BOM单列表
     */
    @RequiresPermissions("erp:productBom:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(int id) {
        Workbook wb = productBomService.export(id);
        String fileName = ExcelUtils.encodingFilename("BOM单");
        return ExcelUtils.getAjaxResult(wb,fileName);
    }

    /**
     * 新增产品BOM单
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存产品BOM单
     */
    @RequiresPermissions("erp:productBom:add")
    @Log(title = "产品BOM单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MultipartFile[] files) {
        String message = null;
        try {
            message = productBomService.insertProductBom(files);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success(message);
    }

    /**
     * 修改产品BOM单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        ProductBom productBom = productBomService.selectProductBomById(id);
        mmap.put("productBom", productBom);
        mmap.put("boms", productBomService.selectBomByPid(productBom.getProductId()));
        mmap.put("detail", productBomService.selectBomDetailByBomId(productBom.getId()));
        return prefix + "/edit";
    }

    /**
     * 修改保存产品BOM单
     */
    @RequiresPermissions("erp:productBom:edit")
    @Log(title = "产品BOM单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody ProductBom productBom) {
        try {
            return success(productBomService.updateProductBom(productBom));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * BOM单详情信息
     * @param bid BOM单id
     * @param mmap 缓存数据
     * @return
     */
    @RequestMapping("/detail")
    @RequiresPermissions("erp:productBom:detail")
    public String bomDetail(int bid,ModelMap mmap){
        ProductBom productBom = productBomService.selectProductBomById(bid);
        mmap.put("productBom", productBom);
        mmap.put("boms", productBomService.selectBomByPid(productBom.getProductId()));
        mmap.put("detail2", productBomService.selectBomDetailByBomId(productBom.getId()));
        //不是初始版本
        if(productBom.getBomVersion() > 1){
            //查询变更信息
            BomChange change = bomChangeService.selectBomChangeByNewBomId(bid);
            mmap.put("change",change);
            if(change != null && change.getOldBomId() != null){
                //查询上一个版本信息
                ProductBom oldBom = productBomService.selectProductBomById(change.getOldBomId());
                mmap.put("oldBom",oldBom);
                mmap.put("oldDetail",productBomService.selectBomDetailByBomId(change.getOldBomId()));
            }
        }
        return prefix+"/detail";
    }

    @ResponseBody
    @RequestMapping("/detail/info")
    @RequiresPermissions("erp:productBom:detail")
    public AjaxResult bomDetailInfo(int bid){
        try {
            AjaxResult ajaxResult = AjaxResult.success();
            ProductBom productBom = productBomService.selectProductBomById(bid);
            ajaxResult.put("productBom", productBom);
            ajaxResult.put("detail2", productBomService.selectBomDetailByBomId(productBom.getId()));
            //不是初始版本
            if(productBom.getBomVersion() > 1){
                //查询变更信息
                BomChange change = bomChangeService.selectBomChangeByNewBomId(bid);
                ajaxResult.put("change",change);
                if(change != null && change.getOldBomId() != null){
                    //查询上一个版本信息
                    ProductBom oldBom = productBomService.selectProductBomById(change.getOldBomId());
                    ajaxResult.put("oldBom",oldBom);
                    ajaxResult.put("oldDetail",productBomService.selectBomDetailByBomId(change.getOldBomId()));
                }
            }
            return ajaxResult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.error();
    }

    /**
     * 版本改变
     * @param id 版本id
     * @return
     */
    @ResponseBody
    @RequestMapping("/vchange")
    public AjaxResult versionChange(int id){
        try {
            ProductBom bom = productBomService.selectProductBomById(id);
            if(bom != null){
                bom.setDetails(productBomService.selectBomDetailByBomId(id));
            }
            return AjaxResult.success(bom);
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.error();
    }


    /**
     * bom 配置
     *
     * @return
     */
    @GetMapping("/config")
    public String config(ModelMap mmap) {
        mmap.put("config", productBomService.selectBomConfigLimit1());
        return prefix + "/config";
    }

    /**
     * 保存BOM单解析配置
     */
    @RequiresPermissions("erp:bomConfig:add")
    @Log(title = "BOM单配置", businessType = BusinessType.UPDATE)
    @PostMapping("/config/edit")
    @ResponseBody
    public AjaxResult saveConfig(BomConfig config) {
        return toAjax(productBomService.saveBomConfig(config));
    }

}
