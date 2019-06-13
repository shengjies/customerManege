package com.ruoyi.common.feign.user;

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
     * @return 结果
     */
    @RequestLine("POST /system/user/api/edit")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> editUserInfo(User user, @Param("token") String token);

    /**
     * 新增用户信息
     * @param user 用户信息
     * @param token token 验证
     * @return 结果
     */
    @RequestLine("POST /system/user/api/add")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> addUser(User user, @Param("token") String token);

    /**
     * 用户登录初始化设置
     * @param user 用户信息
     * @param token token 验证
     * @return 结果
     */
    @RequestLine("POST /system/user/api/changeLoginTag")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> changeLoginTag(User user, @Param("token") String token);

    /**
     * 删除用户信息
     * @param ids 用户id列表
     * @param token token 验证
     * @return 结果
     */
    @RequestLine("POST /system/user/api/remove")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> removeUser(String ids, @Param("token") String token);

    /**
     * 用户退出
     * @param token
     * @return
     */
    @RequestLine("POST /logout")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> loginOut(@Param("token")String token);


    @RequestLine("POST /system/user/api/updateUserDelFlag")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> updateUserDelFlag(User user,@Param("token") String token);
}
