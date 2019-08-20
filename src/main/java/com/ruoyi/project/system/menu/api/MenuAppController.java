package com.ruoyi.project.system.menu.api;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.devCompany.service.IDevCompanyService;
import com.ruoyi.project.device.devNotice.service.IDevNoticeService;
import com.ruoyi.project.production.devWorkOrder.service.IDevWorkOrderService;
import com.ruoyi.project.system.menu.domain.MenuApi;
import com.ruoyi.project.system.menu.service.IMenuService;
import com.ruoyi.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * api 拉取菜单交互接口
 * @Author: Rainey
 * @Date: 2019/7/27 9:03
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/api/m")
public class MenuAppController extends BaseController {
    @Autowired
    private IMenuService menuService;

    @Autowired
    private IDevCompanyService companyService;

    @Autowired
    private IDevNoticeService noticeService;

    @Autowired
    private IDevWorkOrderService workOrderService;

    @Autowired
    private IUserService userService;

    /**
     * 一开始进入首页
     */
    @PostMapping("/index")
    @ResponseBody
    public AjaxResult selectIndexData(@RequestBody MenuApi menuApi){
        if (StringUtils.isNotNull(menuApi)) {
            HashMap<String,Object> map = new HashMap<>(16);
            // 查询菜单
            map.put("menuList",menuService.selectMenuListByParentId(menuApi.getUid(),menuApi.getParentId()));
            // 查询公司轮播图片
            map.put("company",companyService.appSelectComPicList(menuApi.getUid()));
            // 查询消息最新一条
            map.put("notice",noticeService.appSelectNoticeByOne(menuApi.getUid()));
            // 查询今日工单
            map.put("workList",workOrderService.appSelectWorkListTwo());
            // 用户头像
            map.put("userPhoto",userService.selectUserInfo(menuApi.getUid()));
            return AjaxResult.success("请求成功",map);
        }
        return AjaxResult.error("请求失败");
    }

    /**
     * 拉取菜单
     */
    @PostMapping("/list")
    @ResponseBody
    public AjaxResult selectMenuListByParentId(@RequestBody MenuApi menuApi){
        if (StringUtils.isNotNull(menuApi)) {
            return AjaxResult.success("请求成功",menuService.selectMenuListByParentId(menuApi.getUid(),menuApi.getParentId()));
        }
        return AjaxResult.error("请求失败");
    }
}
