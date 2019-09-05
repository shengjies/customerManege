package com.ruoyi.project.iso.iso.controller;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.project.iso.sop.service.ISopService;
import com.ruoyi.project.iso.sopLine.service.ISopLineService;
import com.ruoyi.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 文件管理 信息操作处理
 *
 * @author sj
 * @date 2019-06-13
 */
@Controller
@RequestMapping("/iso/iso")
public class IsoController extends BaseController {
    private String prefix = "iso/iso";

    @Autowired
    private IIsoService isoService;

    @Autowired
    private ISopLineService sopLineService;

    @Autowired
    private ISopService sopService;


    @RequiresPermissions("iso:iso:view")
    @GetMapping("/{id}")
    public String iso(@PathVariable("id")int id,ModelMap mmap)
    {
        mmap.put("id",id);
        return prefix + "/iso";
    }


    /**
     * 查询文件管理列表
     */
    @RequiresPermissions("iso:iso:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Iso iso,HttpServletRequest request) {
        startPage();
        List<Iso> isoList = isoService.selectIsoList(iso,request);
        return getDataTable(isoList);
    }


    /**
     * 导出文件管理列表
     */
    @RequiresPermissions("iso:iso:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Iso iso,HttpServletRequest request) {
        List<Iso> list = isoService.selectIsoList(iso,request);
        ExcelUtil<Iso> util = new ExcelUtil<Iso>(Iso.class);
        return util.exportExcel(list, "iso");
    }

    /**
     * 跳转新增文件管理
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") Integer parentId, ModelMap mmap) {
        mmap.put("parentId", parentId);
        return prefix + "/add";
    }

    /**
     * 新增保存文件管理
     */
    @RequiresPermissions("iso:iso:add")
    @Log(title = "文件管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Iso iso, HttpServletRequest request) {
        try {
            return toAjax(isoService.insertIso(iso,request));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 修改文件管理
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        Iso iso = isoService.selectIsoById(id);
        mmap.put("iso", iso);
        return prefix + "/edit";
    }

    /**
     * 修改保存文件管理
     */
    @RequiresPermissions("iso:iso:edit")
    @Log(title = "文件管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Iso iso,HttpServletRequest request) {
        try {
            return toAjax(isoService.updateIso(iso,request));
        } catch (BusinessException e) {
            return error(e.getMessage());
        } catch (IOException e) {
            return error("修改失败");
        }
    }

    /**
     * 删除文件管理
     */
    @RequiresPermissions("iso:iso:remove")
    @Log(title = "文件管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove/{isoId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("isoId") Integer isoId,HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        Iso iso = isoService.selectIsoById(isoId);
        // sop文件夹判断是否被配置过
        if (FileConstants.CATEGORY_SOP_FOLDER.equals(iso.getCategory())) {
            if (sopService.selectSopListBySopId(user.getCompanyId(),isoId).size() > 0) {
                return error(1, "存在ASOP配置,不允许删除");
            }
        }
        // sop文件夹下指导书页是否被配置过
        if (FileConstants.CATEGORY_SOP_FILE.equals(iso.getCategory())) {
            if (sopLineService.selectSopLineListByPageId(user.getCompanyId(),isoId).size() > 0) {
                return error(1, "存在ASOP配置,不允许删除");
            }
        }
        if (isoService.selectIsoByParentId(isoId).size() > 0) {
            return error(1, "存在子文件,不允许删除");
        }
        return toAjax(isoService.deleteIsoById(isoId));
    }

    /**
     * 跳转上传文件
     */
    @GetMapping("/fileupload/{id}")
    public String upload(@PathVariable("id") Integer parentId, ModelMap mmap) {
        mmap.put("parentId", parentId);
        return prefix + "/fileupload";
    }

    /**
     * 上传sop文件
     */
    @RequiresPermissions("iso:iso:add")
    @PostMapping("/uploadSop")
    @ResponseBody
    public AjaxResult uploadSop(@RequestParam("file") MultipartFile file,HttpServletRequest request){
        try {
            int parentId = Integer.parseInt(request.getParameter("parentId"));
            FileUploadUtils.assertAllowed(file);
            isoService.uploadSop(file,parentId,request);
            return success();
        } catch (BusinessException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return error("上传失败");
        }
    }

    @PostMapping("/findBackId")
    @ResponseBody
    public AjaxResult findBackId(Integer isoId){
        return AjaxResult.success("success",isoService.selectIsoById(isoId));
    }

    /**
     * 根据文件id查询对应子文件
     * @param pid
     * @return
     */
    @ResponseBody
    @PostMapping("/findByPid")
    @RequiresPermissions("iso:iso:list")
    public AjaxResult findIsoByPid(int pid){
        try {
            return AjaxResult.success(isoService.selectIsoByParentId(pid));
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.error();
    }


    /******************************************************************************************************
     *********************************** app端ISO文件系统交互 **********************************************
     ******************************************************************************************************/

    /**
     * app端查看ISO文件系统
     */
    @PostMapping("/applist")
    @ResponseBody
    public AjaxResult appSelectList(@RequestBody Iso iso){
        try {
            if (StringUtils.isNotNull(iso)) {
                iso.appStartPage();
                return AjaxResult.success("请求成功",isoService.appSelectList(iso));
            }
            return error();
        } catch (Exception e) {
            return error("请求失败");
        }
    }
}
