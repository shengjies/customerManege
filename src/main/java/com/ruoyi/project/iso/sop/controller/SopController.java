package com.ruoyi.project.iso.sop.controller;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
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
import com.ruoyi.project.production.singleWork.service.ISingleWorkService;
import com.ruoyi.project.production.workstation.service.IWorkstationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SOP配置主 信息操作处理
 * 
 * @author sj
 * @date 2019-08-30
 */
@Controller
@RequestMapping("/iso/sop")
public class SopController extends BaseController
{
    private String prefix = "iso/sop";
	
	@Autowired
	private ISopService sopService;

	@Autowired
	private IDevProductListService productListService;

	@Autowired
	private ISopLineService sopLineService;

	@Autowired
	private IIsoService isoService;

	@Autowired
	private IWorkstationService workstationService;

	@Autowired
	private IProductionLineService lineService;

	@Autowired
	private ISingleWorkService singleWorkService;
	
	@RequiresPermissions("iso:sopLine:list")
	@GetMapping()
	public String sop()
	{
	    return prefix + "/sop";
	}
	
	/**
	 * 查询SOP配置主列表生产线
	 */
	@RequiresPermissions("iso:sopLine:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Sop sop)
	{
		startPage();
		sop.setSopTag(FileConstants.SOP_TAG_LINE);
        List<Sop> list = sopService.selectSopList(sop);
		return getDataTable(list);
	}

	/**
	 * 查询SOP配置主列表生产线
	 */
	@RequiresPermissions("iso:sopLine:list")
	@PostMapping("/list2")
	@ResponseBody
	public TableDataInfo list2(Sop sop)
	{
		startPage();
		sop.setSopTag(FileConstants.SOP_TAG_SINGWORK);
		List<Sop> list = sopService.selectSopList2(sop);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出SOP配置主列表
	 */
	@RequiresPermissions("iso:sopLine:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Sop sop)
    {
    	List<Sop> list = sopService.selectSopList(sop);
        ExcelUtil<Sop> util = new ExcelUtil<Sop>(Sop.class);
        return util.exportExcel(list, "sop");
    }
	
	/**
	 * 新增SOP配置主
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存SOP配置主
	 */
	@RequiresPermissions("iso:sopLine:add")
	@Log(title = "SOP配置主", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@RequestBody Sop sop)
	{
		return toAjax(sopService.insertSop(sop));
	}

	/**
	 * 修改产线SOP配置从产线入口进入
	 */
	@GetMapping("/editLineSop")
	public String editLineSop(Integer id, ModelMap mmap)
	{
		Sop sop = sopService.selectSopById(id);
		// 查询所有的作业指导书
		mmap.put("iso",isoService.selectASOPList(FileConstants.FOLDER_SOP));
		// 该条产线未配置的产品信息
		mmap.put("pns", productListService.selectNotConfigByLineId(sop.getLineId(),JwtUtil.getUser().getCompanyId(), FileConstants.SOP_TAG_LINE));
		// 查询已经配置过的产品信息
		mmap.put("sopLines",sopLineService.selectSopConfigProBySId(id));
		// 查询所有工位信息
		mmap.put("work", workstationService.selectAllByLineId(sop.getLineId()));
		// 查询对应指导书所有的页
		mmap.put("pages", isoService.selectIsoByParentId(sop.getSopId()));
		// 查询所有工位配置
		mmap.put("sopLineWork",sopLineService.selectSopConfigWorkBySId(id));
		mmap.put("sop", sop);
	    return "iso/sopLine/edit1";
	}

	/**
	 * 修改产线SOP配置从文件管理入口进入
	 */
	@GetMapping("/editSopLine")
	public String editSopLine(Integer id, ModelMap mmap)
	{
		Sop sop = sopService.selectSopById(id);
		// 查询所有的产线信息
		mmap.put("lineList",lineService.selectAllLineByComId());
		// 该条产线未配置的产品信息
		mmap.put("pns", productListService.selectNotConfigByLineId(sop.getLineId(),JwtUtil.getUser().getCompanyId(), FileConstants.SOP_TAG_LINE));
		// 查询已经配置过的产品信息
		mmap.put("sopLines",sopLineService.selectSopConfigProBySId(id));
		// 查询所有工位信息
		mmap.put("work", workstationService.selectAllByLineId(sop.getLineId()));
		// 查询作业指导书的所有页信息
		mmap.put("pages",isoService.selectIsoByParentId(sop.getSopId()));
		// 查询所有工位配置
		mmap.put("sopLineWork",sopLineService.selectSopConfigWorkBySId(id));
		mmap.put("isoId",sop.getSopId());
		mmap.put("sop", sop);
		return "iso/sopLine/edit";
	}


	/**
	 * 修改单工位SOP配置从单工位入口进入
	 */
	@GetMapping("/editSingWorkSop")
	public String editSingWorkSop(Integer id,Integer houseId, ModelMap mmap){
		Sop sop = sopService.selectSopById(id);
		// 查询所有的作业指导书
		mmap.put("iso", isoService.selectASOPList(FileConstants.FOLDER_SOP));
		// 根据单工位id查询所以未配置的产品信息
		mmap.put("pns", productListService.selectNotConfigByWId(sop.getLineId(), JwtUtil.getUser().getCompanyId(), FileConstants.SOP_TAG_SINGWORK));
		mmap.put("houseId",houseId);
		List<SopLine> sopLines = sopLineService.selectSopConfigProBySId(id);
		if (StringUtils.isNotEmpty(sopLines)) {
			// 查询已经配置过的产品信息
			mmap.put("sopLines", sopLines);
			// 已经配置过的页
			mmap.put("sopLineWork",sopLines.get(0));
		}
		// 查询对应指导书的页信息
		mmap.put("pages", isoService.selectIsoByParentId(sop.getSopId()));
		mmap.put("sop",sop);
		return "production/singleWork/editSop";
	}


	/**
	 * 修改保存SOP配置主
	 */
	@RequiresPermissions("iso:sopLine:edit")
	@Log(title = "SOP配置主", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@RequestBody Sop sop)
	{
		return toAjax(sopService.updateSop(sop));
	}
	
	/**
	 * 删除SOP配置主
	 */
	@RequiresPermissions("iso:sopLine:remove")
	@Log(title = "SOP配置主", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(sopService.deleteSopByIds(ids));
	}

	/**
	 * 校验配置名称是否重复
	 */
	@PostMapping("/checkCnfNameUnique")
	@ResponseBody
	public String checkCnfNameUnique(Sop sop){
		return sopService.checkCnfNameUnique(sop);
	}

}
