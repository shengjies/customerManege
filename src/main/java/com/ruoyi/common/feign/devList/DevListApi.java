package com.ruoyi.common.feign.devList;

import com.ruoyi.project.system.user.domain.User;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RestController;

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
}
