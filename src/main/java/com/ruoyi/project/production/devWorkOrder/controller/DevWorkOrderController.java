package com.ruoyi.project.production.devWorkOrder.controller;

import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.TimeUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.mes.mesBatch.domain.MesBatch;
import com.ruoyi.project.mes.mesBatch.domain.MesBatchDetail;
import com.ruoyi.project.mes.mesBatch.service.IMesBatchDetailService;
import com.ruoyi.project.mes.mesBatch.service.IMesBatchService;
import com.ruoyi.project.mes.mesBatchRule.service.IMesBatchRuleDetailService;
import com.ruoyi.project.product.importConfig.domain.ImportConfig;
import com.ruoyi.project.product.importConfig.service.IImportConfigService;
import com.ruoyi.project.production.devWorkOrder.domain.AppWorkOrder;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.service.IDevWorkOrderService;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.service.IProductionLineService;
import com.ruoyi.project.production.singleWork.domain.SingleWorkOrder;
import com.ruoyi.project.production.singleWork.service.ISingleWorkOrderService;
import com.ruoyi.project.system.menu.domain.MenuApi;
import com.ruoyi.project.system.menu.service.IMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单 信息操作处理
 *
 * @author zqm
 * @date 2019-04-12
 */
@Controller
@RequestMapping("/device/devWorkOrder")
public class DevWorkOrderController extends BaseController {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DevWorkOrderController.class);

    private String prefix = "production/devWorkOrder";

    @Autowired
    private IDevWorkOrderService devWorkOrderService;

    @Autowired
    private IProductionLineService productionLineService;

    @Autowired
    private IImportConfigService configService;

    @RequiresPermissions("device:devWorkOrder:view")
    @GetMapping("/lineWorkOrder")
    public String devWorkOrder() {
        return prefix + "/devWorkOrder";
    }

    @RequiresPermissions("device:devWorkOrder:view")
    @GetMapping("/singWorkOrder")
    public String singWorkOrder() {
        return prefix + "/singWorkOrder";
    }

    /**
     * 查询流水线工单列表
     */
    @RequiresPermissions("device:devWorkOrder:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DevWorkOrder devWorkOrder) {
        startPage();
        devWorkOrder.setWlSign(WorkConstants.SING_LINE);
        List<DevWorkOrder> list = devWorkOrderService.selectDevWorkOrderList(devWorkOrder);
        return getDataTable(list);
    }

    /**
     * 查询单工位工单列表
     */
    @RequiresPermissions("device:devWorkOrder:list")
    @PostMapping("/list1")
    @ResponseBody
    public TableDataInfo list1(DevWorkOrder devWorkOrder) {
        startPage();
        devWorkOrder.setWlSign(WorkConstants.SING_SINGLE);
        List<DevWorkOrder> list = devWorkOrderService.selectDevWorkOrderList(devWorkOrder);
        return getDataTable(list);
    }

    /**
     * 导出工单列表
     */
    @RequiresPermissions("device:devWorkOrder:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DevWorkOrder devWorkOrder) {
        List<DevWorkOrder> list = devWorkOrderService.selectDevWorkOrderList(devWorkOrder);
        ExcelUtil<DevWorkOrder> util = new ExcelUtil<DevWorkOrder>(DevWorkOrder.class);
        return util.exportExcel(list, "devWorkOrder");
    }

    /**
     * 新增工单
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("workorderNumber", CodeUtils.getWorkOrderCode());
        return prefix + "/add";
    }

    /**
     * 获取工单编号
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/getWorkOrderCode")
    public String getWorkOrderCode() {
        return CodeUtils.getWorkOrderCode();
    }

    /**
     * 新增保存工单
     */
    @RequiresPermissions("device:devWorkOrder:add")
    @Log(title = "工单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DevWorkOrder devWorkOrder) {
        return toAjax(devWorkOrderService.insertDevWorkOrder(devWorkOrder));
    }

    /**
     * 修改工单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        DevWorkOrder devWorkOrder = devWorkOrderService.selectDevWorkOrderById(id);
        mmap.put("devWorkOrder", devWorkOrder);
        return prefix + "/edit";
    }

    /**
     * 修改保存工单
     */
    @RequiresPermissions("device:devWorkOrder:edit")
    @Log(title = "工单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DevWorkOrder devWorkOrder) {
        try {
            return toAjax(devWorkOrderService.updateDevWorkOrder(devWorkOrder));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 删除工单
     */
    @RequiresPermissions("device:devWorkOrder:remove")
    @Log(title = "工单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(devWorkOrderService.deleteDevWorkOrderByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 校验工单号是否存在
     *
     * @return
     */
    @PostMapping("/checkWorkOrderNumber")
    @ResponseBody
    public String checkWorkOrderNumber(DevWorkOrder devWorkOrder, HttpServletRequest request) {
        return devWorkOrderService.checkWorkOrderNumber(devWorkOrder, request);
    }

    /**
     * 工单状态控制 <br>
     * 工单开始暂停状态修改，第一次点击开始初始化数据
     *
     * @return
     */
    @PostMapping("/editWorkerOrderById")
    @ResponseBody
    public AjaxResult editWorkerOrderById(Integer id) {
        try {
            return toAjax(devWorkOrderService.editWorkerOrderById(id, null));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 提交工单确认工单
     *
     * @param id 工单id
     * @return
     */
    @PostMapping("/submitWorkOrder")
    @ResponseBody
    public AjaxResult submitWorkOrder(Integer id) {
        try {
            return toAjax(devWorkOrderService.submitWorkOrder(id, null));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }

    }

    /**
     * 结束工单
     *
     * @param id 工单id
     */
    @PostMapping("/finishWorkerOrder")
    @ResponseBody
    public AjaxResult finishWorkerOrder(Integer id) {
        try {
            return toAjax(devWorkOrderService.finishWorkerOrder(id));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }

    }

    /**
     * 通过产线Id查询该产线正在生产的工单
     */
    @PostMapping("/selectWorkOrderBeInByLineId")
    @ResponseBody
    public AjaxResult selectWorkOrderBeInByLineId(Integer lineId) {
        DevWorkOrder workOrder = devWorkOrderService.selectWorkOrderBeInByLineId(lineId);
        return AjaxResult.success("success", workOrder);
    }

    /**
     * 根据工单信息查询对应的工单信息
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/findWorkInfoById")
    public Map<String, Object> findWorkInfoById(int work_id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", devWorkOrderService.findWorkInfoById(work_id));
        return map;
    }

    /**
     * 跳转工单详情
     */
    @GetMapping("/showWorkOrderDetail/{id}")
    public String showWorkOrderDetail(@PathVariable("id") Integer id, ModelMap mmap) {
        DevWorkOrder devWorkOrder = devWorkOrderService.selectDevWorkOrderById(id);
        /**
         * 生产流水线
         */
        if (devWorkOrder.getWlSign().equals(WorkConstants.SING_LINE)) {
            if (devWorkOrder.getWorkorderStatus() == 1) {
                //达成率 = 累计产量/标准工时*(生产用时) *100
                devWorkOrder.setReachRate(0.00F);
                if (devWorkOrder.getCumulativeNumber() != 0) {
                    float standardTotal = devWorkOrder.getProductStandardHour() * (TimeUtil.getDateDel(devWorkOrder.getSignTime()) + devWorkOrder.getSignHuor());
                    devWorkOrder.setReachRate(standardTotal == 0 ? 0.0F : new BigDecimal(((float) devWorkOrder.getCumulativeNumber() / standardTotal) * 100).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());
                }
            }
            if (devWorkOrder.getWorkorderStatus() == 2) {
                devWorkOrder.setReachRate(0.00F);
                if (devWorkOrder.getCumulativeNumber() != 0) {
                    float standardTotal = devWorkOrder.getProductStandardHour() * devWorkOrder.getSignHuor();
                    devWorkOrder.setReachRate(standardTotal == 0 ? 0.0F : new BigDecimal(((float) devWorkOrder.getCumulativeNumber() / standardTotal) * 100).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());
                }
            }
            ProductionLine productionLine = productionLineService.selectProductionLineById(devWorkOrder.getLineId());
            mmap.put("line", productionLine);
            // 生产车间
        } else {

        }
        mmap.put("devWorkOrder", devWorkOrder);
        return prefix + "/workOrderDetail";
    }

    /**
     * 通过产线id查询已经提交的工单列表
     *
     * @param lineId 产线
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectWorkOrderFinishByLineId")
    public TableDataInfo selectWorkOrderFinishByLineId(int lineId, HttpServletRequest request) {
        return getDataTable(devWorkOrderService.selectWorkOrderFinishByLineId(lineId, request));
    }

    /**
     * 根据产线id和工单id查询对应工单信息
     *
     * @param lineId      产线id
     * @param workOrderId 工单id
     * @return 结果
     */
    @ResponseBody
    @RequestMapping("/selectWorkOrderFinishByLineIdAndWorkOrderId")
    public AjaxResult selectWorkOrderFinishByLineIdAndWorkOrderId(int lineId, int workOrderId, HttpServletRequest request) {
        return AjaxResult.success("sucess", devWorkOrderService.selectWorkOrderFinishByLineIdAndWorkOrderId(lineId, workOrderId, request));
    }

    /**
     * 工单变更
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping("/chang")
    public AjaxResult changeOrder(DevWorkOrder order) {
        try {
            return toAjax(devWorkOrderService.changeOrder(order));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }

    @RequestMapping("/workecn")
    public String workEcn(int workId, ModelMap mmap) {
        DevWorkOrder order = devWorkOrderService.selectWorkOrderEcn(workId);
        mmap.put("work", order);
        return prefix + "/workecn";
    }

    /**
     * 工单合并
     *
     * @param workids 需要合并的id
     * @return
     */
    @ResponseBody
    @RequestMapping("/merge/verif")
    public AjaxResult workMergeVerif(@RequestParam(value = "workids[]") int[] workids, int type) {
        try {
            return AjaxResult.success(devWorkOrderService.workMergeVerif(workids, type));
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 进行工单合并信息确认
     *
     * @param workids 需要合并工单id
     * @param mmap
     * @return
     */
    @RequestMapping("/merge/page")
    public String workMergePage(String workids, ModelMap mmap) {
        mmap.put("data", devWorkOrderService.workMergePage(workids));
        mmap.put("workids", workids);
        return prefix + "/merge";
    }

    /**
     * 工单合并
     *
     * @param order 工单信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/merge")
    public AjaxResult workMerge(DevWorkOrder order) {
        try {
            return toAjax(devWorkOrderService.workMerge(order));
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }

    /**
     * 拆单
     *
     * @return
     */
    @RequestMapping("/dismantle")
    public String workDismantle(int id, ModelMap mmap) {
        mmap.put("order", devWorkOrderService.selectDevWorkOrderById(id));
        return prefix + "/dismantle";
    }

    /**
     * 工单拆除
     *
     * @param list 拆单详情
     * @return
     */
    @ResponseBody
    @RequestMapping("/workDismantle")
    public AjaxResult workDismantleInfo(@RequestBody List<DevWorkOrder> list) {
        try {
            devWorkOrderService.workDismantleInfo(list);
            return success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error();
    }

    /****************************工单OCR操作*****************************/
    /**
     * 工单OCR 操作
     *
     * @return
     */
    @GetMapping("/ocr")
    @RequiresPermissions("device:devWorkOrder:ocr")
    public String ocr(ModelMap modelMap) {
        modelMap.put("config", configService.selectImportConfigByType(3));
        return prefix + "/workOcr";
    }

    /**
     * ocr 图片解析
     *
     * @param file 图片文件
     * @return
     */
    @PostMapping("/ocr")
    @ResponseBody
    @RequiresPermissions("device:devWorkOrder:ocr")
    public AjaxResult ocrFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return AjaxResult.error();
        }
        try {
            return AjaxResult.success(devWorkOrderService.ocrFile(file));
        } catch (Exception e) {
            return AjaxResult.error("解析异常:" + e.getMessage());
        }

    }

    /**
     * 初始化 OCR 配置
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/initOcr")
    @RequiresPermissions("device:devWorkOrder:ocrInit")
    public AjaxResult initOcr() {
        return toAjax(devWorkOrderService.initOcrConfig());
    }

    /**
     * 保存匹配配置
     *
     * @param config
     * @return
     */
    @ResponseBody
    @RequestMapping("/initOcrSave")
    @RequiresPermissions("device:devWorkOrder:ocrInit")
    public AjaxResult saveInitOcrConfig(ImportConfig config) {
        return toAjax(devWorkOrderService.saveInitOcrConfig(config));
    }

    /**
     * 保存解析的工单信息
     *
     * @param order 工单信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/ocrSaveWork")
    @RequiresPermissions("device:devWorkOrder:ocrInit")
    public AjaxResult saveOcrWork(DevWorkOrder order) {
        try {
            return toAjax(devWorkOrderService.saveOcrWork(order));
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /************************** 工单MES操作 ********************************/
    /**
     * 配料
     */
    @RequiresPermissions("device:devWorkOrder:configMes")
    @GetMapping("/woConfigMes")
    public String woConfigMes(int id, ModelMap map) {
        map.put("mesCode", CodeUtils.getMesCode());
        map.put("workOrder", devWorkOrderService.selectWorkOrderMesByWId(id));
        return prefix + "/mesConfig";
    }

    /**
     * 生产
     */
    @RequiresPermissions("device:devWorkOrder:produceMes")
    @GetMapping("/mesProduce")
    public String mesProduce(int id, ModelMap map) {
        map.put("workOrder", devWorkOrderService.selectWorkMesOrderByWorkId(id));
        return prefix + "/mesProduce";
    }

    /**
     * 流水线工单跳转查看看板更新明细
     */
    @GetMapping("/showJPush")
    public String showJPush(Integer lineId,ModelMap map){
        map.put("lineId",lineId);
        return "production/workstation/jpushDetail";
    }


    /**
     * 车间工单跳转查看看板更新明细
     */
    @GetMapping("/showJPush2")
    public String showJPush2(Integer id,Integer lineId,ModelMap map){
        map.put("lineId",lineId);
        map.put("workId",id);
        return "production/singleWork/jpushDetail";
    }


    /******************************************************************************************************
     *********************************** app工单业务接口 ***************************************************
     ******************************************************************************************************/

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IMesBatchService mesBatchService;

    @Autowired
    private IMesBatchDetailService mesBatchDetailService;

    @Autowired
    private IMesBatchRuleDetailService ruleDetailService;

    @Autowired
    private ISingleWorkOrderService singleWorkOrderService;

    /**
     * app端查询工单列表信息
     * wlSign  标记工单属于流水线还是单工位，0为流水线，1为单工位
     * uid      用户id
     * mParentId 菜单父id
     */
    @PostMapping("/applist")
    @ResponseBody
    public AjaxResult appSelectWorkOrder(@RequestBody DevWorkOrder workOrder) {
        try {
            if (workOrder != null) {
                workOrder.appStartPage();
                Map<String, Object> map = new HashMap<>(16);
                List<MenuApi> menuApiList = menuService.selectMenuListByParentId(workOrder.getUid(), workOrder.getMenuList());
                if (workOrder.getUid() != null && workOrder.getMenuList() != null) {
                    map.put("menuList", menuApiList);
                }
                if (workOrder.getUid() != null && workOrder.getmParentId() != null) {
                    menuApiList.addAll(menuService.selectMenuListByParentId(workOrder.getUid(), workOrder.getmParentId()));
                    map.put("menuList", menuApiList);
                }
                map.put("workOrderList",devWorkOrderService.appSelectDevWorkOrderList(workOrder));
                return AjaxResult.success("请求成功", map);
            }
           return error();
        } catch (Exception e) {
            return AjaxResult.error("请求失败");
        }
    }

    /**
     * app端修改流水线工单信息
     */
    @PostMapping("/appEditWorkInfo")
    @ResponseBody
    public AjaxResult appEditWorkInfo(@RequestBody DevWorkOrder workOrder) {
        try {
            if (workOrder != null && workOrder.getId() != null) {
                return toAjax(devWorkOrderService.appEditWorkInfo(workOrder));
            }
            return error("修改失败");
        } catch (BusinessException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            return error();
        }
    }


    /**
     * app开始暂停结束工单逻辑
     */
    @PostMapping("/appEditWorkStatus")
    @ResponseBody
    public AjaxResult appEditWorkStatus(@RequestBody DevWorkOrder workOrder) {
        try {
            if (workOrder != null) {
                if (workOrder.getId() != null && workOrder.getUid() != null) {
                    if (workOrder.getWorkSign() != null && workOrder.getWorkSign().equals(WorkConstants.WORK_SIGN_YES)) {
                        // 提交工单
                        return toAjax(devWorkOrderService.submitWorkOrder(workOrder.getId(), workOrder.getUid()));
                    } else if (workOrder.getWorkorderStatus() != null && workOrder.getWorkorderStatus().equals(WorkConstants.WORK_STATUS_END)) {
                        // 结束工单
                        return toAjax(devWorkOrderService.appFinishWorkOrder(workOrder.getId(), workOrder.getUid(), workOrder.getWorkorderStatus()));
                    } else {
                        return toAjax(devWorkOrderService.editWorkerOrderById(workOrder.getId(), workOrder.getUid()));
                    }
                }
            }
            return error();
        } catch (BusinessException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            return error("请求失败");
        }
    }

    /**
     * 删除工单
     */
    @PostMapping("/appRemove")
    @ResponseBody
    public AjaxResult appRemoveWorkOrder(@RequestBody DevWorkOrder workOrder) {
        try {
            if (workOrder != null && workOrder.getId() != null && workOrder.getUid() != null) {
                return toAjax(devWorkOrderService.deleteDevWorkOrderById(workOrder.getId(), workOrder.getUid()));
            }
            return error();
        } catch (BusinessException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            return error();
        }
    }


    /**
     * 工单查询MES数据列表
     * workCode 工单号
     */
    @PostMapping("/appWorkMesList")
    @ResponseBody
    public AjaxResult appSelectWorkMesList(@RequestBody MesBatch mesBatch) {
        try {
            if (mesBatch != null) {
                mesBatch.appStartPage();
                return AjaxResult.success(mesBatchService.selectMesBatchList(mesBatch));
            }
            return error();
        } catch (Exception e) {
            return error();
        }
    }

    /**
     * 查询MES数据明细列表
     * bId 数据主表id
     */
    @PostMapping("/appWorkMesDetailList")
    @ResponseBody
    public AjaxResult appSelectWorkMesDetaiList(@RequestBody MesBatchDetail mesBatchDetail) {
        try {
            if (mesBatchDetail != null) {
                mesBatchDetail.appStartPage();
                return AjaxResult.success(mesBatchDetailService.selectMesBatchDetailList(mesBatchDetail));
            }
            return error();
        } catch (Exception e) {
            return error();
        }
    }

    /**
     * app端查看工单ECN信息
     */
    @PostMapping("/appWorkOrderEcn")
    @ResponseBody
    public AjaxResult appSelectWorkOrderEcn(@RequestBody DevWorkOrder workOrder){
        if (workOrder != null && workOrder.getId() != null) {
            return AjaxResult.success(devWorkOrderService.selectWorkOrderEcn(workOrder.getId()));
        }
        return error();
    }

    /**
     * app端通过工单id查询工单信息
     */
    @PostMapping("/appSelectById")
    @ResponseBody
    public AjaxResult appSelectWorkById(@RequestBody DevWorkOrder workOrder){
        if (workOrder != null && workOrder.getId() != null) {
            return AjaxResult.success(devWorkOrderService.selectDevWorkOrderById(workOrder.getId()));
        }
        return error();
    }

    /**
     * app端工单配置MES模块
     */
    @PostMapping("/appWorkMes")
    @ResponseBody
    public AjaxResult appWorkMes(@RequestBody AppWorkOrder appWork){
        try {
            if (appWork != null && appWork.getWorkId() != null) {
                return AjaxResult.success(devWorkOrderService.appSelectWorkMes(appWork));
            }
            return error();
        } catch (BusinessException e) {
            LOGGER.error("app端查询工单配置MES模块出现异常：" + e.getMessage());
            return error(e.getMessage());
        } catch (Exception e){
            LOGGER.error("app端查询工单配置MES模块出现异常：" + e.getMessage());
            return error();
        }
    }

    /**
     * app端仓库添加MES追溯明细
     */
    @PostMapping("/appAddMesRule")
    @ResponseBody
    public AjaxResult appAddMesRule(@RequestBody AppWorkOrder appWork){
        try {
            if (appWork != null && appWork.getRuleId() > 0) {
                Map<String,Object> map = new HashMap<>(16);
                map.put("ruleList",ruleDetailService.selectMesBatchRuleByRuleId(appWork.getRuleId()));
                map.put("mesCode", CodeUtils.getMesCode());
                return AjaxResult.success(map);
            }
            return error();
        } catch (Exception e) {
            LOGGER.error("app端仓库添加MES追溯明细出现异常：" + e.getMessage());
            return error();
        }
    }

    /**
     * app端仓库删除mes配置信息
     */
    @PostMapping("/appRemoveMesData")
    @ResponseBody
    public AjaxResult appRemoveMesData(@RequestBody MesBatch mesBatch){
        try {
            if (mesBatch != null && mesBatch.getId() != null) {
                return toAjax(mesBatchService.removeMesData(mesBatch.getId()));
            }
            return error();
        } catch (Exception e) {
            LOGGER.error("app端仓库删除MES配料出现异常：" + e.getMessage());
            return error();
        }
    }

    /**
     * 查看app端单工位分配工单列表
     */
    @PostMapping("/appShareWorkList")
    @ResponseBody
    public AjaxResult appShareWorkList(@RequestBody SingleWorkOrder singleWorkOrder){
        if (singleWorkOrder != null && singleWorkOrder.getWorkId() != null) {
            //根据工单查询
            singleWorkOrder.setType(1);
            return AjaxResult.success("请求成功", singleWorkOrderService.selectSingleWorkOrderList(singleWorkOrder));
        }
        return AjaxResult.error();
    }

    /**
     * app端单工位工单分配保存
     * 传参jsCode workId
     */
    @PostMapping("/appSaveShareWork")
    @ResponseBody
    public AjaxResult appSaveShareWork(@RequestBody SingleWorkOrder singleWorkOrder){
        try {
            if (singleWorkOrder != null && singleWorkOrder.getJsCode() != null && singleWorkOrder.getWorkId() != null) {
                return toAjax(singleWorkOrderService.appSaveShareWork(singleWorkOrder));
            }
            return error();
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }
}
