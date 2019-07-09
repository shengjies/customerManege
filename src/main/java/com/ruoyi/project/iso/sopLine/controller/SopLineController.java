package com.ruoyi.project.iso.sopLine.controller;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.domain.SopLineWork;
import com.ruoyi.project.iso.sopLine.service.ISopLineService;
import com.ruoyi.project.product.list.service.IDevProductListService;
import com.ruoyi.project.production.productionLine.service.IProductionLineService;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.production.singleWork.service.ISingleWorkService;
import com.ruoyi.project.production.workstation.service.IWorkstationService;
import com.ruoyi.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 作业指导书  产线 配置 信息操作处理
 *
 * @author sj
 * @date 2019-06-15
 */
@Controller
@RequestMapping("/iso/sopLine")
public class SopLineController extends BaseController {
    private String prefix = "iso/sopLine";

    @Autowired
    private ISopLineService sopLineService;

    @Autowired
    private IIsoService iIsoService;

    @Autowired
    private IDevProductListService productListService;

    @Autowired
    private IWorkstationService workstationService;

    @Autowired
    private IProductionLineService lineService;

    @Autowired
    private ISingleWorkService singleWorkService;


    @RequiresPermissions("iso:sopLine:list")
    @GetMapping("/view/{id}")
    public String sopLine(@PathVariable("id") Integer isoId, ModelMap mmap) {
        mmap.put("isoId", isoId);
        return prefix + "/sopLine";
    }

    @RequiresPermissions("iso:sopLine:list")
    @GetMapping("/showDetail/{id}")
    public String showdetail(@PathVariable("id") Integer isoId, ModelMap mmap) {
        mmap.put("isoId", isoId);
        return prefix + "/sopLineDetail";
    }

    /**
     * 查询作业指导书  产线 配置列表
     */
    @RequiresPermissions("iso:sopLine:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SopLine sopLine) {
        startPage();
        List<SopLine> list = sopLineService.selectSopLineList(sopLine);
        return getDataTable(list);
    }


    /**
     * 新增作业指导书  产线 配置
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") Integer isoId, ModelMap modelMap, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        // 查询未配置该作业指导书的所有产线信息
        modelMap.put("lineList", lineService.selectLineNotConfigByIsoId(isoId, user.getCompanyId()));
        // 查询该公司的所有产品信息
        modelMap.put("proList", productListService.selectProductAllByCompanyId(0));
        modelMap.put("isoId", isoId);
        return prefix + "/add";
    }

    /**
     * 新增保存作业指导书  产线 配置
     */
    @RequiresPermissions("iso:sopLine:add")
    @Log(title = "作业指导书  产线 配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody SopLine sopLine) {
        User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
        sopLine.setcId(u.getUserId().intValue());
        sopLine.setCompanyId(u.getCompanyId());
        try {
            return toAjax(sopLineService.insertSopLine(sopLine));
        } catch (BusinessException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 修改作业指导书  产线 配置
     */
    @GetMapping("/edit/{lineId}/{sopId}")
    public String edit(@PathVariable("lineId") int lineId, @PathVariable("sopId") int sopId, ModelMap mmap, HttpServletRequest request) {
        mmap.put("isoId", sopId);
        mmap.put("lineId", lineId);
        // 查询未配置该作业指导书的所有产线信息
        mmap.put("lineList", lineService.selectLineNotConfigByIsoId(sopId, JwtUtil.getTokenUser(request).getCompanyId()));
        //根据产线id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByLineId(lineId, JwtUtil.getTokenUser(request).getCompanyId(), FileConstants.SOP_TAG_LINE));
        //查询对应产线的所以工位信息
        mmap.put("work", workstationService.selectAllByLineId(lineId));
        //查询产线SOP 所以配置信息
        List<SopLine> sopLines = sopLineService.selectLineAllSopConfig(JwtUtil.getTokenUser(request).getCompanyId(), lineId, sopId,FileConstants.SOP_TAG_LINE);
        mmap.put("sopLines", sopLines);
        //查询对应的指导书的页数
        List<Iso> pages = null;
        if (sopLines != null && sopLines.size() > 0) {
            pages = iIsoService.selectIsoByParentId(sopLines.get(0).getSopId());
        }
        mmap.put("pages", pages);
        //查询所有工位配置信息
        mmap.put("sopLineWork", sopLineService.selectWorkstionByCompanyAndLineIdAndSopId(
                JwtUtil.getTokenUser(request).getCompanyId(), lineId, sopId
        ));
        return prefix + "/edit";
    }

    /**
     * 修改保存作业指导书  产线 配置
     */
    @RequiresPermissions("iso:sopLine:edit")
    @Log(title = "作业指导书  产线 配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody SopLine sopLine, HttpServletRequest request) {
        User u = JwtUtil.getTokenUser(request);
        sopLine.setCompanyId(u.getCompanyId());
        sopLine.setcId(u.getUserId().intValue());
        return toAjax(sopLineService.updateSopLine(sopLine));
    }

    /**
     * 删除作业指导书  产线 配置
     */
    @RequiresPermissions("iso:sopLine:remove")
    @Log(title = "作业指导书  产线 配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(int lineId, int sopId,
                             HttpServletRequest request) {
        return toAjax(sopLineService.deleteSopLine(JwtUtil.getTokenUser(request).getCompanyId(), lineId, sopId,FileConstants.SOP_TAG_LINE));
    }

    /******************    产线SOP 配置 *************************/

    @RequiresPermissions("iso:sopLine:list")
    @GetMapping("/config/{id}")
    public String sopLineConfig(@PathVariable("id") int id, ModelMap mmap) {
        mmap.put("line", id);
        //查询所以的SOP 作业指导书
        mmap.put("sops", iIsoService.selectIsoByParentId(FileConstants.FOLDER_SOP));
        return prefix + "/sopLineConfig";
    }


    /**
     * 新增作业指导书  产线 配置
     */
    @GetMapping("/addConfig")
    @RequiresPermissions("iso:sopLine:add")
    public String addConfig(int lineId, ModelMap mmap, HttpServletRequest request) {
        //查询该产线所有未配置的SOP书
        mmap.put("iso", iIsoService.selectNotConfigByPidAndLineId(FileConstants.FOLDER_SOP, lineId, FileConstants.SOP_TAG_LINE));
        //根据产线id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByLineId(lineId, JwtUtil.getTokenUser(request).getCompanyId(), FileConstants.SOP_TAG_LINE));
        //查询对应产线的所以工位信息
        mmap.put("work", workstationService.selectAllByLineId(lineId));
        mmap.put("line", lineId);
        return prefix + "/add1";
    }

    /**
     * 修改作业指导书 产线 配置
     *
     * @param lineId 产线 id
     * @param sopId  SOP id
     * @return
     */
    @GetMapping("/editConfig/{lineId}/{sopId}")
    @RequiresPermissions("iso:sopLine:edit")
    public String editConfig(@PathVariable("lineId") int lineId, @PathVariable("sopId") int sopId, ModelMap mmap, HttpServletRequest request) {
        mmap.put("sopId", sopId);
        mmap.put("line", lineId);
        //查询该产线所有未配置的SOP书
        mmap.put("iso", iIsoService.selectNotConfigByPidAndLineId(FileConstants.FOLDER_SOP, lineId, FileConstants.SOP_TAG_LINE));
        //根据产线id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByLineId(lineId, JwtUtil.getTokenUser(request).getCompanyId(), FileConstants.SOP_TAG_LINE));
        //查询对应产线的所以工位信息
        mmap.put("work", workstationService.selectAllByLineId(lineId));
        //查询产线SOP 所以配置信息
        List<SopLine> sopLines = sopLineService.selectLineAllSopConfig(JwtUtil.getTokenUser(request).getCompanyId(), lineId, sopId,FileConstants.SOP_TAG_LINE);
        mmap.put("sopLines", sopLines);
        //查询对应的指导书的页数
        List<Iso> pages = null;
        if (sopLines != null && sopLines.size() > 0) {
            pages = iIsoService.selectIsoByParentId(sopLines.get(0).getSopId());
        }
        mmap.put("pages", pages);
        //查询所有工位配置信息
        mmap.put("sopLineWork", sopLineService.selectWorkstionByCompanyAndLineIdAndSopId(
                JwtUtil.getTokenUser(request).getCompanyId(), lineId, sopId
        ));
        return prefix + "/edit1";
    }


    /******************   单工位  SOP配置 *************************/

    /**
     * 查询作业指导书  单工位 配置列表
     */
    @RequiresPermissions("iso:sopLine:list")
    @PostMapping("/list2")
    @ResponseBody
    public TableDataInfo list2(SopLine sopLine) {
        startPage();
        List<SopLine> list = sopLineService.selectSopLineList2(sopLine);
        return getDataTable(list);
    }



    /**
     * 单工位调转修改SOP作业指导书页面
     */
    @GetMapping("/editSop/{lineId}/{sopId}/{parentId}")
    public String editSop(@PathVariable("lineId") int lineId, @PathVariable("sopId") int sopId,
                          @PathVariable("parentId") int parentId, ModelMap mmap) {
        User user = JwtUtil.getUser();
        // 查询该单工位所有未配置的SOP书
        mmap.put("iso", iIsoService.selectNotConfigByPidAndLineId(FileConstants.FOLDER_SOP, lineId, FileConstants.SOP_TAG_SINGWORK));
        // 根据单工位id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByLineId(lineId, user.getCompanyId(), FileConstants.SOP_TAG_SINGWORK));
        // 查询单工位SOP 所有已配置信息
        List<SopLine> sopLines = sopLineService.selectLineAllSopConfig(user.getCompanyId(), lineId, sopId,FileConstants.SOP_TAG_SINGWORK);
        mmap.put("sopLines", sopLines);
        //查询对应的指导书的页数
        List<Iso> pages = null;
        if (sopLines != null && sopLines.size() > 0) {
            pages = iIsoService.selectIsoByParentId(sopLines.get(0).getSopId());
        }
        mmap.put("pages", pages);
        // 查询单工位配置的页信息
        SopLineWork sopLineWork = sopLineService.selectSopLineWorkInfo(user.getCompanyId(),parentId,sopId,lineId,FileConstants.SOP_TAG_SINGWORK);
        mmap.put("sopLineWork",sopLineWork);
        mmap.put("parentId", parentId);
        mmap.put("lineId", lineId);
        return "production/singleWork/editSop";
    }

    /******************   SOP 单工位配置 *************************/

    /**
     * 通过指定sop查询相关的所有单工位配置信息
     */
    @RequiresPermissions("production:singleWork:configSop")
    @GetMapping("/singWorkView/{id}")
    public String singWorkView(@PathVariable("id") Integer isoId, ModelMap mmap) {
        User user = JwtUtil.getUser();
        mmap.put("isoId", isoId);
        SingleWork singleWork = new SingleWork();
        singleWork.setCompanyId(user.getCompanyId());
        singleWork.setSign(FileConstants.SIGN_SINGWORK);
        mmap.put("allSw",singleWorkService.selectSingleWorkList(singleWork));
        return prefix + "/singleWork";
    }

    /**
     * 跳转 SOP 单工位配置新增页面
     */
    @GetMapping("/addSingWork/{id}")
    public String addSingWork(@PathVariable("id")Integer isoId,ModelMap modelMap){
        modelMap.put("isoId", isoId);
        SingleWork singleWork = new SingleWork();
        singleWork.setSign(FileConstants.SIGN_HOUSE);
        modelMap.put("house",singleWorkService.selectSingleWorkList(singleWork));
        modelMap.put("pns",productListService.selectProductAllByCompanyId(0));
        modelMap.put("pages", iIsoService.selectIsoByParentId(isoId));
        return prefix + "/addSingWork";
    }

    /**
     * 跳转 SOP 单工位修改页面
     */
    @GetMapping("/editSingWork/{isoId}/{lineId}")
    public String editSingWork(@PathVariable("isoId") int isoId,@PathVariable("lineId") int lineId,ModelMap modelMap){
        User user = JwtUtil.getUser();
        modelMap.put("isoId", isoId);
        // 查询所有的车间
        modelMap.put("house",singleWorkService.selectSingleWorkByParentId(user.getCompanyId(),0));
        // 查询单工位SOP 所有已配置信息
        List<SopLine> sopLines = sopLineService.selectLineAllSopConfig(user.getCompanyId(), lineId, isoId,FileConstants.SOP_TAG_SINGWORK);
        modelMap.put("sopLines", sopLines);
        SingleWork singleWork = singleWorkService.selectSingleWorkById(lineId);
        // 查询对应的指导书的页数
        List<Iso> pages = null;
        if (sopLines != null && sopLines.size() > 0) {
            pages = iIsoService.selectIsoByParentId(isoId);
        }
        modelMap.put("pages", pages);
        // 查询单工位配置的页信息
        SopLineWork sopLineWork = sopLineService.selectSopLineWorkInfo(user.getCompanyId(),singleWork.getParentId(),isoId,lineId,FileConstants.SOP_TAG_SINGWORK);
        // 查询所有的对应车间的所有单工位信息
        modelMap.put("swList",singleWorkService.selectSingleWorkByParentId(user.getCompanyId(),sopLineWork.getLineId()));
        modelMap.put("sopLineWork",sopLineWork);
        // 根据单工位id查询所以未配置的产品信息
        modelMap.put("pns", productListService.selectNotConfigByLineId(lineId, user.getCompanyId(), FileConstants.SOP_TAG_SINGWORK));
        return prefix + "/editSingWork";
    }

    /**
     * 删除sop单工位作业指导书
     */
    @RequiresPermissions("iso:sopLine:remove")
    @Log(title = "作业指导书  产线 配置", businessType = BusinessType.DELETE)
    @PostMapping("/removeSingWork")
    @ResponseBody
    public AjaxResult removeSingWork(int lineId, int sopId) {
        User user = JwtUtil.getUser();
        return toAjax(sopLineService.deleteSopLine(user.getCompanyId(), lineId, sopId,FileConstants.SOP_TAG_SINGWORK));
    }
}
