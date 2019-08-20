package com.ruoyi.project.system.user.api;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.domain.UserApp;
import com.ruoyi.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 用户app交互接口
 * @Author: Rainey
 * @Date: 2019/7/27 9:42
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/api/u")
public class UserAppController extends BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IIsoService isoService;

    @Value("${file.iso}")
    private String fileUrl;

    @PostMapping("/list")
    @ResponseBody
    public AjaxResult appSelectUserList(@RequestBody UserApp userApp){
        try {
            if (userApp != null) {
                userApp.appStartPage();
                return AjaxResult.success("请求成功",userService.appSelectUserList(userApp));
            }
            return AjaxResult.error();
        } catch (Exception e) {
            return AjaxResult.error("请求失败");
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult appUpload(@RequestParam MultipartFile file, HttpServletRequest request){
        try {
            String uid = request.getHeader("uid");
            String token = request.getHeader("token");
            if (StringUtils.isNotEmpty(uid) && StringUtils.isNotEmpty(token)) {
                User user = userService.selectUserInfo(Long.valueOf(uid));
                if (user != null) {
                    String fileName = file.getOriginalFilename();
                    Iso iso = isoService.selectIsoById(1);
                    if (StringUtils.isNotNull(iso)) {
                        String path = iso.getDisk() + "/";
                        File desc = FileUploadUtils.getAbsoluteFile(path, path + fileName);
                        file.transferTo(desc);
                        path = fileUrl + iso.getDiskPath() + "/" + fileName;
                        user.setAvatar(path);
                        if (userService.updateUserInfo1(user, token) > 0) {
                            return success(path);
                        }
                    }
                }
            }
            return AjaxResult.error("上传头像失败");
        } catch (Exception e) {
            return AjaxResult.error("上传头像失败");
        }
    }
}
