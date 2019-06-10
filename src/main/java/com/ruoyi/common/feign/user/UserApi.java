package com.ruoyi.common.feign.user;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.user.domain.User;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.HashMap;

/**
 * 用户数据操作，调用用户服务器
 */
public interface UserApi {
    /**
     * 修改用户数据信息
     * @param user 用户信息
     * @param token token 验证
     * @return
     */
    @RequestLine("POST /system/user/api/edit")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> editUserInfo(User user, @Param("token") String token);
}
