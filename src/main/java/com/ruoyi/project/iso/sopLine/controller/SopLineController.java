package com.ruoyi.project.iso.sopLine.controller;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.project.iso.sop.domain.Sop;
import com.ruoyi.project.iso.sop.service.ISopService;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
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
    private IIsoService isoService;

    @Autowired
    private IDevProductListService productListService;

    @Autowired
    private IWorkstationService workstationService;

    @Autowired
    private IProductionLineService lineService;

    @Autowired
    private ISingleWorkService singleWorkService;

    @Autowired
    private ISopService sopService;


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
     * 从文件系统配置产线SOP配置
     */
    @GetMapping("/add")
    public String add(Integer isoId, ModelMap modelMap) {
        // 查询未配置该作业指导书的所有产线信息
        modelMap.put("lineList", lineService.selectAllLineByComId());
        // 查询作业指导书的所有页信息
        modelMap.put("pages",isoService.selectIsoByParentId(isoId));
        modelMap.put("isoId", isoId);
        return prefix + "/add";
    }


    /******************    产线SOP 配置 *************************/

    @RequiresPermissions("iso:sopLine:list")
    @GetMapping("/config/{id}")
    public String sopLineConfig(@PathVariable("id") int id, ModelMap mmap) {
        mmap.put("line", id);
        //查询所以的SOP 作业指导书
        mmap.put("sops", isoService.selectIsoByParentId(FileConstants.FOLDER_SOP));
        return prefix + "/sopLineConfig";
    }

    /**
     * 从产线跳转SOP配置
     */
    @GetMapping("/addConfig")
    @RequiresPermissions("iso:sopLine:add")
    public String addConfig(int lineId, ModelMap mmap, HttpServletRequest request) {
        //查询该产线所有未配置的SOP书
        mmap.put("iso",isoService.selectASOPList(FileConstants.FOLDER_SOP));
        //根据产线id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByLineId(lineId, JwtUtil.getTokenUser(request).getCompanyId(), FileConstants.SOP_TAG_LINE));
        //查询对应产线的所有工位
        mmap.put("work", workstationService.selectAllByLineId(lineId));
        mmap.put("line", lineId);
        return prefix + "/add1";
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
     * 从单工位跳转配置作业指导书
     */
    @GetMapping("/addSop/{lineId}/{wId}")
    public String addSop(@PathVariable("lineId") int lineId, @PathVariable("wId") int wId, ModelMap mmap) {
        User user = JwtUtil.getUser();
        // 查询所有的作业指导书
        mmap.put("iso", isoService.selectASOPList(FileConstants.FOLDER_SOP));
        // 根据单工位id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByWId(wId, user.getCompanyId(), FileConstants.SOP_TAG_SINGWORK));
        mmap.put("lineId", lineId);
        mmap.put("wId", wId);
        return "production/singleWork/addSop";
    }


    /******************   SOP 单工位配置 *************************/

    /**
     * 通过指定sop查询相关的所有单工位配置信息
     */
    @RequiresPermissions("production:singleWork:configSop")
    @GetMapping("/singWorkView/{id}")
    public String singWorkView(@PathVariable("id") Integer isoId, ModelMap mmap) {
        jumpAboutSingWork(isoId, mmap);
        return prefix + "/sopSingleWork";
    }

    /**
     * 跳转 SOP 单工位配置新增页面从文件系统进入
     */
    @GetMapping("/addSingWork")
    public String addSingWork(Integer isoId,ModelMap modelMap){
        modelMap.put("isoId", isoId);
        // 查询所有的车间信息
        modelMap.put("house",singleWorkService.selectSingleWorkByParentId(JwtUtil.getUser().getCompanyId(), 0));
        // 查询作业指导书所有的页信息
        modelMap.put("pages", isoService.selectIsoByParentId(isoId));
        return prefix + "/addSingWork";
    }


    /**
     * 跳转 SOP 单工位修改页面从文件管理系统进入
     */
    @GetMapping("/editSingWork")
    public String editSingWork(Integer id,ModelMap modelMap){
        Sop sop = sopService.selectSopById(id);
        modelMap.put("sop",sop);
        // 查询所有的车间信息
        modelMap.put("house",singleWorkService.selectSingleWorkByParentId(JwtUtil.getUser().getCompanyId(), 0));
        // 查询所有的作业指导书
        modelMap.put("iso", isoService.selectASOPList(FileConstants.FOLDER_SOP));
        // 根据单工位id查询所以未配置的产品信息
        modelMap.put("pns", productListService.selectNotConfigByWId(sop.getLineId(), JwtUtil.getUser().getCompanyId(), FileConstants.SOP_TAG_SINGWORK));
        // 所有配置信息
        List<SopLine> sopLines = sopLineService.selectSopConfigProBySId(id);
        if (StringUtils.isNotEmpty(sopLines)) {
            // 配置的车间id
            modelMap.put("houseId",sopLines.get(0).getLineId());
            // 配置的车间所有的设备信息
            modelMap.put("singWorks",singleWorkService.selectSingleWorkByParentId(JwtUtil.getUser().getCompanyId(), sopLines.get(0).getLineId()));
            // 查询已经配置过的产品信息
            modelMap.put("sopLines", sopLines);
            // 已经配置过的页
            modelMap.put("sopLineWork",sopLines.get(0));
        }
        // 查询对应指导书的页信息
        modelMap.put("pages", isoService.selectIsoByParentId(sop.getSopId()));
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

    /**
     * 跳转查看单工位配置明细
     */
    @GetMapping("/showSingWorkDetail/{id}")
    public String showSingWorkDetail(@PathVariable("id") int isoId,ModelMap modelMap){
        jumpAboutSingWork(isoId, modelMap);
        return prefix + "/sopSingleWorkDetail";
    }

    /**
     * sop单工位跳转参数携带
     * @param isoId sopid
     * @param modelMap
     */
    private void jumpAboutSingWork(@PathVariable("id") int isoId, ModelMap modelMap) {
        User user = JwtUtil.getUser();
        modelMap.put("isoId", isoId);
        SingleWork singleWork = new SingleWork();
        singleWork.setCompanyId(user.getCompanyId());
        singleWork.setSign(FileConstants.SIGN_SINGWORK);
        modelMap.put("allSw", singleWorkService.selectSingleWorkList(singleWork));
    }
}
