package com.ruoyi.project.production.ecnLog.controller;

import java.util.HashMap;
import java.util.List;

import com.ruoyi.project.product.list.domain.DevProductList;
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
import com.ruoyi.project.production.ecnLog.domain.EcnLog;
import com.ruoyi.project.production.ecnLog.service.IEcnLogService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * ECN 变更记录 信息操作处理
 *
 * @author zqm
 * @date 2019-05-16
 */
@Controller
@RequestMapping("/production/ecnLog")
public class EcnLogController extends BaseController {
    private String prefix = "production/ecnLog";

    @Autowired
    private IEcnLogService ecnLogService;

    @RequiresPermissions("production:ecnLog:view")
    @GetMapping()
    public String ecnLog(int ecn_type, int save_id, int ecn_status, ModelMap mmap) {
        mmap.put("ecn_type", ecn_type);
        mmap.put("save_id", save_id);
        mmap.put("ecn_status", ecn_status);
        return prefix + "/ecnLog";
    }

    /**
     * 查询ECN 变更记录列表
     */
    @RequiresPermissions("production:ecnLog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EcnLog ecnLog, HttpServletRequest request) {
        startPage();
        List<EcnLog> list = ecnLogService.selectEcnLogList(ecnLog,request);
        return getDataTable(list);
    }

    /**
     * 添加变更信息
     * @param type 变更类型
     * @param save_id 保存id
     * @param mmap 缓存数据
     * @return
     */
    @GetMapping("/add")
    public String add(int type,int save_id,ModelMap mmap){
        mmap.put("type",type);
        mmap.put("save_id",save_id);
        return prefix+"/add";
    }

    /**
     * 新增保存变更记录
     */
    @RequiresPermissions("production:ecnLog:add")
    @Log(title = "新增变更记录", businessType = BusinessType.INSERT)
    @PostMapping("/addSave")
    @ResponseBody
    public AjaxResult addSave(EcnLog ecnLog){
        return toAjax(ecnLogService.insertEcnLog(ecnLog));
    }


    /**
     * 修改保存ECN 变更记录
     */
    @RequiresPermissions("production:ecnLog:edit")
    @Log(title = "关闭ECN状态", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(int type,int save_id) {
        return toAjax(ecnLogService.updateEcnLog(type,save_id));
    }


}
