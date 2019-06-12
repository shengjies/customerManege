package com.ruoyi.common.feign.devList;

import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.domain.DevListResult;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.HashMap;

/**
 * 用户硬件操作接口
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.common.feign.devList
 * @Author: Administrator
 * @Date: 2019/6/11 12:14
 * @Description //TODO
 * @Version: 1.0
 **/

public interface DevListApi {
    /**
     * 校验硬件编码信息
     * @param code 硬件编码
     * @param token token 验证
     * @return 结果
     */
    @RequestLine("POST /dev/api/validateCode")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> devValidateCode(String code, @Param("token") String token);

    /**
     * 保存硬件
     * @param devListResult 硬件信息
     * @param token token 验证
     * @return 结果
     */
    @RequestLine("POST /dev/api/addSave")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> addSaveDevList(DevListResult devListResult, @Param("token") String token);

    /**
     * 修改硬件信息
     * @param devListResult 硬件信息
     * @param token token 验证
     * @return 结果
     */
    @RequestLine("POST /dev/api/edit")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> editDevList(DevListResult devListResult, @Param("token") String token);

    /**
     * 删除硬件信息
     * @param ids 硬件id
     * @param token token 验证
     * @return 结果
     */
    @RequestLine("POST /dev/api/remove")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> remove(String ids, @Param("token") String token);
}
