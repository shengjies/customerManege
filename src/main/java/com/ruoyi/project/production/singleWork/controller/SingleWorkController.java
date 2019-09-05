package com.ruoyi.project.production.singleWork.controller;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.service.ISopLineService;
import com.ruoyi.project.product.list.service.IDevProductListService;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.production.singleWork.domain.SingleWorkOrder;
import com.ruoyi.project.production.singleWork.service.ISingleWorkOrderService;
import com.ruoyi.project.production.singleWork.service.ISingleWorkService;
import com.ruoyi.project.system.menu.service.IMenuService;
import com.ruoyi.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单工位数据 信息操作处理
 *
 * @author sj
 * @date 2019-07-03
 */
@Controller
@RequestMapping("/production/singleWork")
public class SingleWorkController extends BaseController {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleWorkController.class);

    private String prefix = "production/singleWork";

    @Autowired
    private ISingleWorkService singleWorkService;

    @Autowired
    private IIsoService iIsoService;

    @Autowired
    private IDevProductListService productListService;

    @RequiresPermissions("production:singleWork:view")
    @GetMapping()
    public String singleWork() {
        return prefix + "/singleWork";
    }

    /**
     * 查询单工位数据列表
     */
    @RequiresPermissions("production:singleWork:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SingleWork singleWork) {
        startPage();
        List<SingleWork> list = singleWorkService.selectSingleWorkList(singleWork);
        return getDataTable(list);
    }

    /**
     * 查询单工位数据列表
     */
    @PostMapping("/jpushList")
    @ResponseBody
    public TableDataInfo jpushList(SingleWorkOrder singleWorkOrder) {
        startPage();
        List<SingleWork> list = singleWorkService.selectSingleWorkList2(singleWorkOrder);
        return getDataTable(list);
    }


    /**
     * 导出单工位数据列表
     */
    @RequiresPermissions("production:singleWork:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SingleWork singleWork) {
        List<SingleWork> list = singleWorkService.selectSingleWorkList(singleWork);
        ExcelUtil<SingleWork> util = new ExcelUtil<SingleWork>(SingleWork.class);
        return util.exportExcel(list, "singleWork");
    }

    /**
     * 新建车间
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增设备
     */
    @GetMapping("/addIm/{id}")
    public String addIm(@PathVariable("id") Integer parentId, ModelMap mmap) {
        mmap.put("parentId", parentId);
        return prefix + "/addIm";
    }


    /**
     * 新增保存单工位数据
     */
    @RequiresPermissions("production:singleWork:add")
    @Log(title = "单工位数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SingleWork singleWork) {
        try {
            return toAjax(singleWorkService.insertSingleWork(singleWork));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 修改车间数据
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        SingleWork singleWork = singleWorkService.selectSingleWorkById(id);
        mmap.put("singleWork", singleWork);
        return prefix + "/edit";
    }


    /**
     * 修改单工位信息
     */
    @GetMapping("/editIm/{id}")
    public String editIm(@PathVariable("id") Integer id, ModelMap mmap) {
        SingleWork singleWork = singleWorkService.selectSingleWorkById(id);
        mmap.put("singleWork", singleWork);
        return prefix + "/editIm";
    }

    /**
     * 修改保存单工位数据
     */
    @RequiresPermissions("production:singleWork:edit")
    @Log(title = "单工位数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SingleWork singleWork) {
        return toAjax(singleWorkService.updateSingleWork(singleWork));
    }

    /**
     * 删除单工位数据
     */
    @RequiresPermissions("production:singleWork:remove")
    @Log(title = "单工位数据", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(singleWorkService.deleteSingleWorkByIds(ids));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 校验车间名称的唯一性
     */
    @PostMapping("/checkNameUnique")
    @ResponseBody
    public String checkNameUnique(SingleWork singleWork) {
        return singleWorkService.checkNameUnique(singleWork);
    }

    /**
     * 跳转单工位设备硬件配置
     */
    @GetMapping("/configDev")
    @RequiresPermissions("production:singleWork:configDev")
    public String configDev(Integer id, ModelMap modelMap) {
        SingleWork singleWork = singleWorkService.selectSingleWorkById(id);
        modelMap.put("singleWork", singleWork);
        return prefix + "/configDev";
    }

    /**
     * 单工位设备配置保存硬件信息
     */
    @PostMapping("/saveConfigDev")
    @RequiresPermissions("production:singleWork:configDev")
    @ResponseBody
    public AjaxResult saveConfigDev(SingleWork singleWork) {
        try {
            return toAjax(singleWorkService.saveConfigDev(singleWork));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 跳转单工位配置SOP页面
     */
    @GetMapping("/configSop")
    @RequiresPermissions("production:singleWork:configSop")
    public String configSop(Integer parentId, Integer id, ModelMap modelMap) {
        modelMap.put("lineId", parentId);
        modelMap.put("wId", id);
        modelMap.put("sops", iIsoService.selectIsoByParentId(FileConstants.FOLDER_SOP));
        return prefix + "/configSop";
    }

    /**
     * 根据id查询对应的
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectById")
    public AjaxResult selectSingleById(int id) {
        try {
            return AjaxResult.success(singleWorkService.selectSingleWorkByIdGetUser(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error();
    }

    /**
     * 查询还未配置sop的车间单工位信息
     */
    @PostMapping("/selectNotConfigSop")
    @ResponseBody
    public AjaxResult selectNotConfigSop(int wId) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return error(UserConstants.NOT_LOGIN);
        }
        return AjaxResult.success("success",  productListService.selectNotConfigByWId(wId, user.getCompanyId(), FileConstants.SOP_TAG_SINGWORK));
    }

    /**
     * 通过车间id查询该车间的单工位信息
     */
    @PostMapping("/selectSingWorkByPId")
    @ResponseBody
    public AjaxResult selectSingWorkByPId(int houseId){
        User user = JwtUtil.getUser();
        if (user == null) {
            return error();
        }
        return AjaxResult.success(singleWorkService.selectSingleWorkByParentId(user.getCompanyId(),houseId));
    }


    /******************************************************************************************************
     *********************************** app端单工位业务逻辑 ***********************************************
     ******************************************************************************************************/

    @Autowired
    private ISopLineService sopLineService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private ISingleWorkOrderService singleWorkOrderService;

    /**
     * app端查询产线单工位式列表信息，首次请求parentId为0
     */
    @PostMapping("/applist")
    @ResponseBody
    public AjaxResult appSelectSingWorkList(@RequestBody SingleWork singleWork) {
        try {
            if (singleWork != null) {
                singleWork.appStartPage();
                Map<String, Object> map = new HashMap<>(16);
                if (singleWork.getUid() != null && singleWork.getmParentId() != null) {
                    map.put("menuList", menuService.selectMenuListByParentId(singleWork.getUid(), singleWork.getmParentId()));
                }
                map.put("singWorkList", singleWorkService.appSelectSingleWorkList(singleWork));
                return AjaxResult.success("请求成功", map);
            }
            return error();
        } catch (Exception e) {
            return error("请求失败");
        }
    }

    /**
     * app端单工位查询sop配置列表
     *
     * @param lineId 单工位id
     * @param sopTag sop标记
     */
    @PostMapping("/appSingWorkCfSopList")
    @ResponseBody
    public AjaxResult appSingWorkCfSopList(@RequestBody SopLine sopLine) {
        try {
            if (sopLine != null) {
                sopLine.appStartPage();
                return AjaxResult.success("请求成功", sopLineService.selectSopLineList2(sopLine));
            }
            return error();
        } catch (Exception e) {
            return error("请求失败");
        }
    }

    /**
     * app端查询单工位配置的工单信息
     *
     * @param singleId 单工位id
     */
    @PostMapping("/appSingWorkCfWorkList")
    @ResponseBody
    public AjaxResult appSingWorkCfWorkList(@RequestBody SingleWorkOrder singleWorkOrder) {
        try {
            if (singleWorkOrder != null) {
                singleWorkOrder.appStartPage();
                return AjaxResult.success("请求成功", singleWorkOrderService.selectSingleWorkOrderList(singleWorkOrder));
            }
            return error();
        } catch (Exception e) {
            return error();
        }
    }

    /**
     * app端配置单工位的硬件
     */
    @PostMapping("/appEdit")
    @ResponseBody
    public AjaxResult appUpdateSingWork(@RequestBody SingleWork singleWork) {
        try {
            return toAjax( singleWorkService.appUpdateSingWork(singleWork));
        } catch (BusinessException e) {
            LOGGER.error("单工位配置硬件出现异常：" + e.getMessage());
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("单工位配置硬件出现异常：" + e.getMessage());
            return AjaxResult.error();
        }
    }
}
