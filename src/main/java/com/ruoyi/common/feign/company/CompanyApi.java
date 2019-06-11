package com.ruoyi.common.feign.company;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

/**
 *
 *  用户对公司信息操作，调用用户服务器
 *
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.common.feign.company
 * @Author: Administrator
 * @Date: 2019/6/10 18:29
 * @Description //TODO
 * @Version: 1.0
 **/
public interface CompanyApi {
    /**
     * 修改公司信息
     * @param company 公司信息
     * @param token token 验证
     * @return 结果
     */
    @RequestLine("POST /company/api/edit")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> editCompanyInfo(DevCompany company, @Param("token") String token);

}
