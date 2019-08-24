package com.ruoyi.project.device.devList.service;

import com.ruoyi.common.constant.DevConstants;
import com.ruoyi.common.feign.FeignUtils;
import com.ruoyi.common.feign.devList.DevListApi;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.DevId;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devIo.domain.DevIo;
import com.ruoyi.project.device.devIo.mapper.DevIoMapper;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.domain.DevListResult;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.device.devModel.domain.DevModel;
import com.ruoyi.project.device.devModel.mapper.DevModelMapper;
import com.ruoyi.project.system.user.domain.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 硬件 服务层实现
 *
 * @author ruoyi
 * @date 2019-04-08
 */
@Service("devList")
public class DevListServiceImpl implements IDevListService {
    @Autowired
    private DevListMapper devListMapper;

    @Autowired
    private DevModelMapper devModelMapper;

    @Autowired
    private DevIoMapper devIoMapper;

    /**
     * 查询硬件信息
     *
     * @param id 硬件ID
     * @return 硬件信息
     */
    @Override
    public DevList selectDevListById(Integer id) {
        return devListMapper.selectDevListById(id);
    }

    /**
     * 查询硬件列表
     *
     * @param devList 硬件信息
     * @return 硬件集合
     */
    @Override
    public List<DevList> selectDevListList(DevList devList, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            return Collections.emptyList();
        }
        if (!User.isSys(user)) {
            devList.setCompanyId(user.getCompanyId());
        }
        return devListMapper.selectDevListList(devList);
    }

    /**
     * 新增硬件
     *
     * @param
     * @return 结果
     */
    @Override
    public int insertDevList(int devModelId) {

        DevModel devModel = devModelMapper.selectDevModelById(devModelId);
        if (devModel == null) return 0;
        DevList devList = null;
        DevIo devIo = null;
        int i = 1;
        while (i <= 30) {
            String devId = DevId.getPageCode();
            if (StringUtils.isEmpty(devId)) {
                continue;
            }
            devList = new DevList();
            devList.setDeviceId(devId);
            devList.setDeviceStatus(1);
            devList.setDefId(0);
            devList.setDeviceUploadTime(15);
            devList.setDevModelId(devModelId);
            devList.setCreateDate(new Date());
            DevList dev = devListMapper.selectDevListByDevId(devId);
            if (dev == null) {
                devListMapper.insertDevList(devList);
                for (int j = 1; j <= 4; j++) {
                    devIo = new DevIo();
                    devIo.setDevId(devList.getId());
                    devIo.setIoOrder(j);
                    devIo.setIsSign(0);
                    devIo.setLineId(0);
                    devIo.setRemark("上传数据");
                    devIo.setCreateTime(new Date());
                    devIoMapper.insertDevIo(devIo);
                }
                i++;
            }
        }
        return 1;
    }

    /**
     * 修改硬件
     *
     * @param devList 硬件信息
     * @return 结果
     */
    @Override
    public int updateDevList(DevList devList, HttpServletRequest request) {
        DevListApi devListApi = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(DevListApi.class, FeignUtils.MAIN_PATH);
        DevListResult devListResult = new DevListResult();
        devListResult.setId(devList.getId() != null ? devList.getId() : null);
		devListResult.setRemark(devList.getRemark() != null ? devList.getRemark() : null);
		devListResult.setDeviceName(devList.getDeviceName() != null ? devList.getDeviceName() : null);
		devListResult.setDeviceStatus(devList.getDeviceStatus() != null ? devList.getDeviceStatus() : null);
        HashMap<String, Object> result = devListApi.editDevList(devListResult, JwtUtil.getToken(request));
        if (Double.valueOf(result.get("code").toString()) == 0) {
            return devListMapper.updateDevList(devList);
        }
        return 0;
    }

    /**
     * 用户添加硬件信息
     *
     * @param devList 硬件信息
     * @return
     */
    @Override
    public int addSave(DevList devList, HttpServletRequest request) {
        // 调用主服务器硬件接口
        DevListApi devListApi = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(DevListApi.class, FeignUtils.MAIN_PATH);
        DevListResult devListResult = new DevListResult();
        devListResult.setCompanyId(devList.getCompanyId());
        devListResult.setDeviceId(devList.getDeviceId());
        devListResult.setDeviceName(devList.getDeviceName());
        devListResult.setRemark(devList.getRemark());
        HashMap<String, Object> result = devListApi.addSaveDevList(devListResult, JwtUtil.getToken(request));
        devList.setCreateDate(new Date());
        if (Double.valueOf(result.get("code").toString()) == 0) {
            String data = result.get("data").toString();
            int id = Integer.parseInt(data.substring(0, data.lastIndexOf(".")));
            devList.setId(id);
            return devListMapper.insertDevList(devList);
        }
        return 0;
    }

    /**
     * 删除硬件对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDevListByIds(String ids,HttpServletRequest request) {
        DevListApi devListApi = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(DevListApi.class, FeignUtils.MAIN_PATH);
        HashMap<String, Object> result = devListApi.remove(ids, JwtUtil.getToken(request));
        if (Double.valueOf(result.get("code").toString()) == 0) {
            return devListMapper.deleteDevListByIds(Convert.toStrArray(ids));
        }
        return 0;
    }

    /**
     * 查询对应的硬件信息和对应的IO数据
     *
     * @param id
     * @return
     */
    @Override
    public DevList selectDevListAndIoById(Integer id) {
        return devListMapper.selectDevListAndIoById(id);
    }

    /**
     * 获取前20个未配置的硬件编号
     *
     * @return
     */
    @Override
    public List<String> selectNoConfigDevice() {
        return devListMapper.selectNoConfigDevice();
    }

    @Override
    public List<DevList> selectAll(Cookie[] cookies) {
        User user = JwtUtil.getTokenCookie(cookies);
        if (user == null) {
            return Collections.emptyList();
        }
        return devListMapper.selectAll(user.getCompanyId());
    }

    /**
     * 根据硬件编号验证对应的硬件是否存在
     *
     * @param code 硬件编号
     * @return
     */
    @Override
    public int deviceValidate(String code, HttpServletRequest request) {
        DevListApi devListApi = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(DevListApi.class, FeignUtils.MAIN_PATH);
        HashMap<String, Object> result = devListApi.devValidateCode(code, JwtUtil.getToken(request));
        if (Double.valueOf(result.get("code").toString()) == 0) {
            return DevConstants.DEV_VALIDATE_TRUE;
        } else {
            return  1;//硬件不存在或者真正使用
        }
    }

    /**
     * 主服务器修改硬件信息
     * @param devList 硬件信息
     * @return 结果
     */
    @Override
    public int apiEdit(DevList devList) {
        return devListMapper.updateDevList(devList);
    }

    /**
     * 查询所以未配置的计数器硬件
     * @return
     */
    @Override
    public List<DevList> selectJSDevNotConfig() {
        return devListMapper.selectJSDevNotConfig();
    }

    /**
     * 查询所以未配置的看板硬件
     * @return
     */
    @Override
    public List<DevList> selectKBDevNotConfig() {
        return devListMapper.selectKBDevNotConfig();
    }

    /**
     * app端查询硬件信息
     * @param devList 硬件信息
     * @return 结果
     */
    @Override
    public List<DevList> appSelectDevList(DevList devList) {
        return devListMapper.selectDevListList(devList);
    }
}
