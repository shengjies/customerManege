package com.ruoyi.project.production.productionLine.service;

import java.util.*;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 生产线 服务层实现
 *
 * @author ruoyi
 * @date 2019-04-11
 */
@Service("productionLine")
public class ProductionLineServiceImpl implements IProductionLineService {
    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DevCompanyMapper devCompanyMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private WorkstationMapper workstationMapper;

    @Autowired
    private DevListMapper devListMapper;

    /**
     * 查询生产线信息
     *
     * @param id 生产线ID
     * @return 生产线信息
     */
    @Override
    public ProductionLine selectProductionLineById(Integer id) {
        return productionLineMapper.selectProductionLineById(id);
    }

    /**
     * 查询生产线列表
     *
     * @param productionLine 生产线信息
     * @return 生产线集合
     */
    @Override
    public List<ProductionLine> selectProductionLineList(ProductionLine productionLine, HttpServletRequest request) {
        User u = JwtUtil.getTokenUser(request);
        if (u == null) return Collections.emptyList();
        if (!User.isSys(u)) {
            productionLine.setCompanyId(u.getCompanyId());
        }
        List<ProductionLine> list = productionLineMapper.selectProductionLineList(productionLine);
        for (ProductionLine line : list) {
            User user = userMapper.selectUserInfoById(line.getCreate_by() == null ? 0 : line.getCreate_by());
            if (user != null) line.setCreateName(user.getUserName());
            User user1 = userMapper.selectUserInfoById(line.getDeviceLiable() == null ? 0 : line.getDeviceLiable());
            if (user1 != null) line.setDeviceLiableName(user1.getUserName());
            DevCompany devCompany = devCompanyMapper.selectDevCompanyById(line.getCompanyId() == null ? 0 : line.getCompanyId());
            if (devCompany != null) line.setComName(devCompany.getComName());
            user = userMapper.selectUserInfoById(line.getDeviceLiableTow() == null ? 0 : line.getDeviceLiableTow());
            if (user != null) line.setDeviceLiableTowName(user.getUserName());
        }
        return list;
    }

    /**
     * 新增生产线
     *
     * @param productionLine 生产线信息
     * @return 结果
     */
    @Override
//    @DataSource(DataSourceType.SLAVE)
    public int insertProductionLine(ProductionLine productionLine, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user != null) {
            productionLine.setCompanyId(user.getCompanyId());
            productionLine.setCreate_by(user.getUserId().intValue());
        }
        productionLine.setCreateTime(new Date());
        return productionLineMapper.insertProductionLine(productionLine);
    }

    /**
     * 修改生产线
     *
     * @param productionLine 生产线信息
     * @return 结果
     */
    @Override
//    @DataSource(value = DataSourceType.SLAVE)
    public int updateProductionLine(ProductionLine productionLine, HttpServletRequest request) {
        ProductionLine line = productionLineMapper.selectProductionLineById(productionLine.getId());
        User sysUser = JwtUtil.getTokenUser(request); // 在线用户
        checkDeviceLiable(sysUser, line);
        return productionLineMapper.updateProductionLine(productionLine);
    }

    /**
     * 修改产线作业指导书配置
     *
     * @param line 产线信息
     * @return
     */
    @Override
    public int editProductionLineSop(ProductionLine line) {
        return productionLineMapper.updateProductionLine(line);
    }

    /**
     * 删除生产线对象
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteProductionLineById(Integer id, HttpServletRequest request) {
        User sysUser = JwtUtil.getTokenUser(request); // 在线用户
        ProductionLine  productionLine = productionLineMapper.selectProductionLineById(id);
        if (productionLine != null && !User.isSys(sysUser) && !sysUser.getLoginName().equals(sysUser.getCreateBy())) { // 非系统用户或者非注册用户
            // 删除的时候判断是否为该工单的负责人
             // 查询产线信息
            checkDeviceLiable(sysUser, productionLine);
        }
        if(productionLine != null){
            //查询是否有工单信息
            DevWorkOrder workOrder = devWorkOrderMapper.selectWorkByLineId(id);
            if(workOrder != null){
                throw new BusinessException("该产线有未完成工单，不能删除...");
            }
        }
        //查询对应产线的工位信息
        List<Workstation> workstations = workstationMapper.selectAllByLineId(id);
        if(workstations != null && workstations.size() >0){
            DevList devList = null;
            for (Workstation workstation : workstations) {
                //将对应硬件设置为未配置
                if(workstation.getDevId() >0){
                    devList = devListMapper.selectDevListById(workstation.getDevId());
                    if(devList != null){
                        devList.setSign(0);
                        devListMapper.updateDevSign(devList);
                    }
                }
                if(workstation.getcId() >0){
                    devList = devListMapper.selectDevListById(workstation.getcId());
                    if(devList != null){
                        devList.setSign(0);
                        devListMapper.updateDevSign(devList);
                    }
                }
                if(workstation.geteId() >0){
                    devList = devListMapper.selectDevListById(workstation.geteId());
                    if(devList != null){
                        devList.setSign(0);
                        devListMapper.updateDevSign(devList);
                    }
                }
                //删除工位
                workstationMapper.deleteWorkstationById(workstation.getId());
            }
        }
        return productionLineMapper.deleteProductionLineById(id);
    }

    @Override
    public List<Map<String, Object>> selectLineDev(int id) {
        return productionLineMapper.selectLineDev(id);
    }

    /**
     * 保存相关产线IO口配置
     *
     * @param line
     * @return
     */
    @Override
    public int updateLineConfigClear(ProductionLine line, HttpServletRequest request) {
        User sysUser = JwtUtil.getTokenUser(request); // 在线用户
        ProductionLine productionLine = productionLineMapper.selectProductionLineById(line.getId()); // 查询产线信息
        checkDeviceLiable(sysUser, productionLine);
        try {
            if (line == null || line.getId() == null) return 0;
            if (line.getDevIo() != null && line.getDevIo().length > 0) {
                //将其产线修改为自动
                line.setManual(0);
                productionLineMapper.updateProductionLine(line);
            } else {
                //将其修改为手动
                line.setManual(1);
                productionLineMapper.updateProductionLine(line);
            }
            //将其产线配置清空
            productionLineMapper.updateLineConfigClear(line.getId());
            if (line.getDevIo() != null && line.getDevIo().length > 0) {
                for (Integer ioId : line.getDevIo()) {
                    //修改对应IO口的配置相关产线
                    productionLineMapper.updateLineConfig(ioId, line.getId());
                }
                if (line.getIsSign() > 0) {
                    productionLineMapper.updateIoSignLine(line.getIsSign());
                } else {
                    //获取第一个默认为警戒线判断依据
                    productionLineMapper.updateIoSignLine(line.getDevIo()[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 校验登录用户是否为生产线的责任人
     *
     * @param sysUser
     * @param productionLine
     */
    private void checkDeviceLiable(User sysUser, ProductionLine productionLine) {
        if (!User.isSys(sysUser) && !sysUser.getLoginName().equals(sysUser.getCreateBy())) { // 非系统用户或者非注册用户
            //不属于第一或者第二责任人不允许修改产线
            if (productionLine.getDeviceLiable() != null && productionLine.getDeviceLiableTow() != null) {
                if (sysUser.getUserId().longValue() != productionLine.getDeviceLiable() && sysUser.getUserId().longValue() != productionLine.getDeviceLiableTow()) {
                    throw new BusinessException("不是产线责任人，没有权限");
                }
            } else {
                throw new BusinessException("请先配置全产线责任人");
            }
        }
    }

    /**
     * 查询对应公司的所有生产线
     *
     * @return
     */
    @Override
//    @DataSource(value = DataSourceType.SLAVE)
    public List<ProductionLine> selectAllProductionLineByCompanyId(Cookie[] cookies) {
        User user = JwtUtil.getTokenCookie(cookies);
        if (user == null) return Collections.emptyList();
        return productionLineMapper.selectAllProductionLineByCompanyId(user.getCompanyId());
    }

    /**
     * 通过生产线id查询责任人名称
     *
     * @param lineId
     * @return
     */
    @Override
//    @DataSource(value = DataSourceType.SLAVE)
    public Map findDeviceLiableByLineId(Integer lineId) {
        ProductionLine productionLine = productionLineMapper.selectProductionLineById(lineId);
        Map<String, Object> map = new HashMap<>(16);
        map.put("user1", findName(productionLine.getDeviceLiable()));
        map.put("user2", findName(productionLine.getDeviceLiableTow()));
        return map;
    }

    /**
     * 通过用户id查询用户名称（责任人）
     *
     * @param userId
     * @return deviceLiable
     */
    private User findName(Integer userId) {
        return userMapper.selectUserInfoById(userId);
    }

    /**
     * 查询对应公司的所有生产线信息
     *
     * @return
     */
    public List<ProductionLine> selectProductionLineAll(Cookie[] cookies) {
        User user = JwtUtil.getTokenCookie(cookies);
        Integer companyId = null;
        if (!User.isSys(user)) { // 非系统用户
            companyId = user.getCompanyId();
        }
        return productionLineMapper.selectAllProductionLineByCompanyId(companyId);
    }
}
