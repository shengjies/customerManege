package com.ruoyi.project.production.workExceptionList.service;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.workExceptionList.domain.WorkExceptionList;
import com.ruoyi.project.production.workExceptionList.mapper.WorkExceptionListMapper;
import com.ruoyi.project.production.workExceptionType.domain.WorkExceptionType;
import com.ruoyi.project.production.workExceptionType.mapper.WorkExceptionTypeMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 各个工单异常情况记录 服务层实现
 *
 * @author zqm
 * @date 2019-04-16
 */
@Service("workOrderExc")
public class WorkExceptionListServiceImpl implements IWorkExceptionListService {
    @Autowired
    private WorkExceptionListMapper workExceptionListMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper; // 工单

    @Autowired
    private WorkExceptionTypeMapper workExceptionTypeMapper; // 异常类型

    /**
     * 查询各个工单异常情况记录信息
     *
     * @param id 各个工单异常情况记录ID
     * @return 各个工单异常情况记录信息
     */
    @Override
    public WorkExceptionList selectWorkExceptionListById(Integer id) {
        return workExceptionListMapper.selectWorkExceptionListById(id);
    }

    /**
     * 查询各个工单异常情况记录列表
     *
     * @param workExceptionList 各个工单异常情况记录信息
     * @return 各个工单异常情况记录集合
     */
    @Override
    public List<WorkExceptionList> selectWorkExceptionListList(WorkExceptionList workExceptionList, HttpServletRequest request) {
        User u = JwtUtil.getTokenUser(request);
        if (User.isSys(u) == false) { // 非系统用户，只能查看本公司的异常列表
            workExceptionList.setCompanyId(u.getCompanyId());
        }
        List<WorkExceptionList> workExceptionLists = workExceptionListMapper.selectWorkExceptionListList(workExceptionList);
        DevWorkOrder workOrder = null;
        WorkExceptionType exceptionType = null;
        for (WorkExceptionList exceptionList : workExceptionLists) {
            // 添加工单信息
            workOrder = devWorkOrderMapper.selectDevWorkOrderById(exceptionList.getWorkId());
            exceptionList.setDevWorkOrder(workOrder);
            // 添加异常类型信息
            exceptionType = workExceptionTypeMapper.selectWorkExceptionTypeById(exceptionList.getExceType());
            exceptionList.setWorkExceptionType(exceptionType);
        }
        return workExceptionLists;
    }

    /**
     * 新增各个工单异常情况记录
     *
     * @param workExceptionList 各个工单异常情况记录信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertWorkExceptionList(WorkExceptionList workExceptionList) {
        User user = JwtUtil.getUser();
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        String typeName = workExceptionList.getTypeName();
        if (StringUtils.isNotEmpty(typeName)) {
            WorkExceptionType workExcType = workExceptionTypeMapper.selectByCompanyAndTypeName(user.getCompanyId(), typeName);
            if (workExcType == null) {
                workExcType = new WorkExceptionType();
                workExcType.setCompanyId(user.getCompanyId());
                workExcType.setTypeName(typeName);
                workExcType.setCreateTime(new Date());
                workExceptionTypeMapper.insertWorkExceptionType(workExcType);
            }
            workExceptionList.setExceType(workExcType.getId());
            workExceptionList.setCompanyId(JwtUtil.getUser().getCompanyId());
            workExceptionList.setExceStatut(WorkConstants.WORKEXCE_STATUT_NOHANDLE);
            workExceptionList.setCreateTime(new Date());
            return workExceptionListMapper.insertWorkExceptionList(workExceptionList);
        }
        return 0;
    }

    /**
     * 修改各个工单异常情况记录
     *
     * @param workExceptionList 各个工单异常情况记录信息
     * @return 结果
     */
    @Override
    public int updateWorkExceptionList(WorkExceptionList workExceptionList) {
        return workExceptionListMapper.updateWorkExceptionList(workExceptionList);
    }

    /**
     * 删除各个工单异常情况记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteWorkExceptionListByIds(String ids) {
        String[] workExcIds = Convert.toStrArray(ids);
        for (String workExcId : workExcIds) {
            //未处理的异常工单不能删除
            WorkExceptionList workExceptionList = workExceptionListMapper.selectWorkExceptionListById(Integer.parseInt(workExcId));
            if (!workExceptionList.getExceStatut().equals(WorkConstants.WORKEXCE_STATUT_FINISH)) {
                throw new BusinessException("未处理完的异常不允许删除");
            }
        }

        return workExceptionListMapper.deleteWorkExceptionListByIds(Convert.toStrArray(ids));
    }

    /**
     * 执行工单异常操作
     *
     * @return
     */
    @Override
    public int handleWorkExcp(WorkExceptionList workExceptionList, HttpServletRequest request) {
        workExceptionList.setHandleUser(JwtUtil.getTokenUser(request).getUserName()); // 处理者
        workExceptionList.setHandleTime(new Date()); // 处理时间
        return workExceptionListMapper.updateWorkExceptionList(workExceptionList);
    }

    /**
     * 解决完工单异常操作
     *
     * @return
     */
    @Override
    public int finishWorkExcp(Integer id) {
        //修改异常工单的异常状态变成处理完成
        WorkExceptionList workExceptionList = workExceptionListMapper.selectWorkExceptionListById(id);
        workExceptionList.setExceStatut(WorkConstants.WORKEXCE_STATUT_FINISH); // 正在处理
        return workExceptionListMapper.updateWorkExceptionList(workExceptionList);
    }

    /**
     * 查看当前公司当天的异常记录
     *
     * @return
     */
    public List<WorkExceptionList> selectWorkExcToday(Cookie[] cookies) {
        User user = JwtUtil.getTokenCookie(cookies);
        List<WorkExceptionList> workExceptionLists = workExceptionListMapper.selectWorkExcToday(user.getCompanyId());
        for (WorkExceptionList workException : workExceptionLists) {
            workException.setDevWorkOrder(devWorkOrderMapper.selectDevWorkOrderById(workException.getWorkId())); // 异常对应工单信息
            workException.setWorkExceptionType(workExceptionTypeMapper.selectWorkExceptionTypeById(workException.getExceType())); // 异常对应类型信息
        }
        return workExceptionLists;
    }

    /**
     * app端查看工单异常记录=列表
     * @param workExceptionList 异常对象
     * @return 结果
     */
    @Override
    public List<WorkExceptionList> appSelectWorkExcList(WorkExceptionList workExceptionList) {
        return workExceptionListMapper.appSelectWorkExcList(workExceptionList);
    }
}
