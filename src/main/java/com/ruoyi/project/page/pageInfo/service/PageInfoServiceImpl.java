package com.ruoyi.project.page.pageInfo.service;

import com.ruoyi.common.constant.PageTypeConstants;
import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.TimeUtil;
import com.ruoyi.common.utils.spring.DevId;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devDeviceCounts.mapper.DevDataLogMapper;
import com.ruoyi.project.device.devIo.domain.DevIo;
import com.ruoyi.project.device.devIo.mapper.DevIoMapper;
import com.ruoyi.project.page.pageInfo.domain.*;
import com.ruoyi.project.page.pageInfo.mapper.PageInfoMapper;
import com.ruoyi.project.page.pageInfoConfig.domain.PageInfoConfig;
import com.ruoyi.project.page.pageInfoConfig.mapper.PageInfoConfigMapper;
import com.ruoyi.project.production.countPiece.domain.CountPiece;
import com.ruoyi.project.production.countPiece.mapper.CountPieceMapper;
import com.ruoyi.project.production.devWorkData.domain.DevWorkData;
import com.ruoyi.project.production.devWorkData.mapper.DevWorkDataMapper;
import com.ruoyi.project.production.devWorkDayHour.domain.DevWorkDayHour;
import com.ruoyi.project.production.devWorkDayHour.mapper.DevWorkDayHourMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkMapper;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkOrderMapper;
import com.ruoyi.project.production.singleWork.service.ISingleWorkService;
import com.ruoyi.project.production.workExceptionList.domain.WorkExceptionList;
import com.ruoyi.project.production.workExceptionList.mapper.WorkExceptionListMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 页面管理 服务层实现
 *
 * @author ruoyi
 * @date 2019-04-10
 */
@Service("page")
public class PageInfoServiceImpl implements IPageInfoService {
    @Autowired
    private PageInfoMapper pageInfoMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private PageInfoConfigMapper pageInfoConfigMapper;

    @Autowired
    private DevIoMapper devIoMapper;

    @Autowired
    private DevCompanyMapper devCompanyMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private DevWorkDataMapper devWorkDataMapper;

    @Autowired
    private DevWorkDayHourMapper dayHourMapper;

    @Autowired
    private WorkstationMapper workstationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkExceptionListMapper workExceptionListMapper;

    @Autowired
    private DevDataLogMapper devDataLogMapper;

    @Autowired
    private ISingleWorkService singleWorkService;

    @Autowired
    private SingleWorkMapper singleWorkMapper;

    @Autowired
    private SingleWorkOrderMapper singleWorkOrderMapper;

    @Autowired
    private CountPieceMapper countPieceMapper;

    @Value("${page.url}")
    private String pageUrl;

    /**
     * 查询页面管理信息
     *
     * @param id 页面管理ID
     * @return 页面管理信息
     */
    @Override
    public PageInfo selectPageInfoById(Integer id) {
        return pageInfoMapper.selectPageInfoById(id);
    }

    /**
     * 根据页面编号查询页面基本信息
     *
     * @param id 页面编号
     * @return
     */
    @Override
    public PageInfo selectPageInfoByPageId(Integer id) {
        return pageInfoMapper.selectPageInfoById(id);
    }

    /**
     * 查询页面管理列表
     *
     * @param pageInfo 页面管理信息
     * @return 页面管理集合
     */
    @Override
    public List<PageInfo> selectPageInfoList(PageInfo pageInfo, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) return Collections.emptyList();
        if (!User.isSys(user)) {
            pageInfo.setCompanyId(user.getCompanyId());
        }
        List<PageInfo> infos = pageInfoMapper.selectPageInfoList(pageInfo);
        for (PageInfo info : infos) {
            info.setDevCompany(devCompanyMapper.selectDevCompanyById(info.getCompanyId()));
        }
        return infos;
    }

    /**
     * 新增页面管理
     *
     * @param pageInfo 页面管理信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPageInfo(PageInfo pageInfo, HttpServletRequest request) {
        //获取对应的公司
        User user = JwtUtil.getTokenUser(request);
        if (user == null) return 0;
        pageInfo.setCompanyId(user.getCompanyId());
        pageInfo.setCreateBy(user.getUserName());
        //获取对应的页面编号
        String pageCode = DevId.getPageCode();
        if (StringUtils.isEmpty(pageCode)) return 0;
        //查询对应的页面编号是否存在
        PageInfo info = pageInfoMapper.selectPageInfoByCode(pageCode);
        if (info == null) {
            pageInfo.setPageId(pageCode);
            pageInfo.setPageUrl(pageUrl + pageCode);
            pageInfo.setCreateTime(new Date());
            pageInfoMapper.insertPageInfo(pageInfo);
            //添加对应看板产线信息
            if (pageInfo.getConfigs() != null && pageInfo.getConfigs().size() > 0) {
                for (PageInfoConfig config : pageInfo.getConfigs()) {
                    config.setPId(pageInfo.getId());
                    pageInfoConfigMapper.insertPageInfoConfig(config);
                }
            }
            return 1;
        }
        return 0;
    }

    /**
     * 修改页面管理
     *
     * @param pageInfo 页面管理信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePageInfo(PageInfo pageInfo) {
        //删除也能相关的配置信息
        String[] ids = {pageInfo.getId() + ""};
        pageInfoConfigMapper.deletePageInfoConfigByPIds(ids);
        //更新页面信息
        pageInfoMapper.updatePageInfo(pageInfo);
        //添加对应看板产线信息
        if (pageInfo.getConfigs() != null && pageInfo.getConfigs().size() > 0) {
            for (PageInfoConfig config : pageInfo.getConfigs()) {
                config.setPId(pageInfo.getId());
                pageInfoConfigMapper.insertPageInfoConfig(config);
            }
        }
        return 1;
    }

    /**
     * 删除页面管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePageInfoByIds(String ids) {
        String[] idAttray = Convert.toStrArray(ids);
        pageInfoConfigMapper.deletePageInfoConfigByPIds(idAttray);
        return pageInfoMapper.deletePageInfoByIds(idAttray);
    }

    /**
     * 查询对应公司页面初始数据
     *
     * @return
     */
    @Override
    public Map<String, Object> selectPageInitInfo(int a, Cookie[] cookies) {
        Map<String, Object> map = new HashMap<>();
        User user = JwtUtil.getTokenCookie(cookies);
        if (user == null) return null;
        //查询对应的公司下的所以的产线
        List<ProductionLine> lines = productionLineMapper.selectAllDef0(user.getCompanyId());
        map.put("line", lines);
        if (lines != null && lines.size() >= 1 && a == 0) {
            List<DevIo> ios = devIoMapper.selectLineDevIO(lines.get(0).getId());
            map.put("ios", ios);
        } else if (lines != null && lines.size() >= 1 && a == 1) {
            for (ProductionLine line : lines) {
                List<DevIo> ios = devIoMapper.selectLineDevIO(line.getId());
                map.put(line.getId() + "", ios);
            }
        }
        return map;
    }

    /**
     * 查询对应公司所以非轮播页面
     *
     * @return
     */
    @Override
    public List<PageInfo> selectAllPage(int a, int p_id, Cookie[] cookies) {
        User user = JwtUtil.getTokenCookie(cookies);
        if (user == null) return null;
        List<PageInfo> infos = pageInfoMapper.selectAllPage(user.getCompanyId());
        if (a == 1) {
            for (PageInfo info : infos) {
                //查询对应轮播的页面是否配置该页面
                PageInfoConfig config = pageInfoConfigMapper.selectPageConfigOtherPage(p_id, info.getId());
                if (config != null) info.setExist(true);
            }
        }
        return infos;
    }

    /**
     * 根据页面编辑查询对应的页面信息
     *
     * @param code 页面编号
     * @return
     */
    @Override
    public PageInfo selectPageByCode(String code) {
        PageInfo info = pageInfoMapper.selectPageInfoByCode(code);
        if (info != null) {
            //查询对应公司
            info.setDevCompany(devCompanyMapper.selectDevCompanyById(info.getCompanyId()));
            if (info.getPageType() == PageTypeConstants.PAGE_TYPE_HZ) {
                return selectLineHz(info);
            } else if (info.getPageType() == PageTypeConstants.PAGE_TYPE_XQ) {
                return selectLineDetail(info);
            } else if (info.getPageType() == PageTypeConstants.PAGE_TYPE_CJ) {
                return selectWorkshopDetail(info);
            }
        }
        return null;
    }

    /**
     * 车间汇总看板
     *
     * @param info info对象
     * @return 结果
     */
    private PageInfo selectWorkshopDetail(PageInfo info) {
        PageInfoConfig pageInfoConfig = pageInfoConfigMapper.selectHousePageConfigByPageId(info.getId());
        if (pageInfoConfig == null) {
            return null;
        }
        // 查询所有的单工位明细
        List<PageHouse> pageHouseList = singleWorkMapper.selectHouseDetailByParentId(info.getCompanyId(), pageInfoConfig.getLineId(), WorkConstants.WORK_STATUS_STARTING);
        if (pageHouseList == null) {
            return null;
        }
        List<WorkExceptionList> wexcList = null;
        for (PageHouse pageHouse : pageHouseList) {
            if (pageHouse != null) {
                // 查询工单异常信息
                wexcList = workExceptionListMapper.selectWorkExceAllByWorkId(pageHouse.getWorkId());
                Integer wexStatus = 0;
                Integer wexStatusTag = 0;
                if (wexcList != null) {
                    for (WorkExceptionList wex : wexcList) {
                        if (wex.getExceStatut() == 0) {
                            wexStatusTag = 1;
                        }
                        wexStatus += wex.getExceStatut();
                    }
                    if (wexStatus == wexcList.size() * WorkConstants.WORKEXCE_STATUT_FINISH) {
                        pageHouse.setWexStatus(null);
                    } else {
                        pageHouse.setWexStatus(WorkConstants.WORKEXCE_STATUT_HANDLE);
                    }
                    if (wexStatusTag == 1) {
                        pageHouse.setWexStatus(WorkConstants.WORKEXCE_STATUT_NOHANDLE);
                    }
                }
                pageHouse.setCountNum(0);
                // 查询个人计件数量信息
                CountPiece countPiece = countPieceMapper.selectPieceByWorkIdAndUid(pageHouse.getWorkId(), info.getCompanyId(), pageHouse.getLiableOneId(), TimeUtil.getNYR());
                if (countPiece != null) {
                    pageHouse.setCountNum(countPiece.getCpNumber());
                }
            }
        }
        info.setPageHouseList(pageHouseList);
        return info;
    }

    /**
     * 产线汇总看板
     *
     * @param info
     * @return
     */
    private PageInfo selectLineHz(PageInfo info) {
        //查询产线配置
        List<ProductionLine> lines = productionLineMapper.selectLineByPageId(info.getId());
        if (lines == null) return null;
        List<PageTem> tems = new ArrayList<>();
        PageTem tem = null;
        for (ProductionLine line : lines) {
            tem = new PageTem();
            tem.setLineName(line.getLineName());
            tem.setInputNum(0);
            tem.setStandardNum(0);
            tem.setOutputNum(0);
            tem.setAchievementRate(0.00F);
            tem.setWorkCode("-");
            tem.setProductCode("-");
            tem.setWorkStatus(0);
            tem.setWorkNum(0);
            tem.setLineManual(line.getManual());
            tem.setEx(0);
            //查询责任人
            User user = userMapper.selectUserInfoById(line.getDeviceLiable());
            if (user != null) {
                tem.setPersonLiable(user.getUserName());
            }
            //查询各个产线正在进行的工单
            DevWorkOrder order = devWorkOrderMapper.selectWorkByCompandAndLine(line.getCompanyId(), line.getId(), WorkConstants.SING_LINE);
            if (order != null) {
                tem.setWorkCode(order.getWorkorderNumber());
                tem.setProductCode(order.getProductCode());
                tem.setWorkNum(order.getProductNumber());
                tem.setWorkStatus(order.getOperationStatus());
                tem.setNumber(order.getPeopleNumber() == null ? 0 : order.getPeopleNumber());
                tem.setInputNum(order.getPutIntoNumber() == null ? 0 : order.getPutIntoNumber());
                tem.setOutputNum(order.getCumulativeNumber() == null ? 0 : order.getCumulativeNumber());
                //查询产线数据标识工位
                Workstation workstation = workstationMapper.selectWorkstationSignByLineId(line.getId(), line.getCompanyId());
                if (workstation != null) {
                    DevWorkData workData = devWorkDataMapper.selectWorkDataByCompanyLineWorkDev(line.getCompanyId(),
                            line.getId(), order.getId(), workstation.getDevId(), workstation.getId(), WorkConstants.SING_LINE);
                    if (workData != null) tem.setOutputNum(workData.getCumulativeNum());
                }
                //查询对应工单是否出现未处理异常情况
                WorkExceptionList workExceptionList = workExceptionListMapper.selectWorkExceNotByWorkId(order.getId());
                if (workExceptionList != null) {
                    tem.setEx(1);
                }
                float totalHour = order.getSignHuor();
                //计算标准产量
                if (order.getOperationStatus() == WorkConstants.OPERATION_STATUS_STARTING) {
                    totalHour += TimeUtil.getDateDel(order.getSignTime(), new Date());
                }
                tem.setStandardNum((int) (totalHour * order.getProductStandardHour()));
                //计数达成率
                if (tem.getOutputNum() > 0 && tem.getStandardNum() > 0) {
                    tem.setAchievementRate(new BigDecimal(((float) tem.getOutputNum() / (float) tem.getStandardNum()) * 100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                }
            }
            tems.add(tem);
        }
        info.setTems(tems);
        return info;
    }

    /**
     * 产线详情
     *
     * @param info
     * @return
     */
    private PageInfo selectLineDetail(PageInfo info) {
        ProductionLine line = productionLineMapper.selectLineDetailByPageId(info.getId());
        if (line == null) {
            return null;
        }
        //查询相关人员
        if (line.getEdUser() != null && line.getEdUser() > 0) {
            line.setEdUserInfo(userMapper.selectUserInfoById(line.getEdUser()));
        }
        if (line.getIpqcUser() != null && line.getIpqcUser() > 0) {
            line.setIpqcUserInfo(userMapper.selectUserInfoById(line.getIpqcUser()));
        }
        if (line.getMeUser() != null && line.getMeUser() > 0) {
            line.setMeUserInfo(userMapper.selectUserInfoById(line.getMeUser()));
        }
        if (line.getTeUser() != null && line.getTeUser() > 0) {
            line.setTeUserInfo(userMapper.selectUserInfoById(line.getTeUser()));
        }
        //设置产线
        info.setLine(line);
        //查询正在进行工单
        DevWorkOrder devWorkOrder = devWorkOrderMapper.selectWorkByCompandAndLine(line.getCompanyId(), line.getId(), WorkConstants.SING_LINE);
        if (devWorkOrder != null) {
            info.setWork(devWorkOrder);
            //查询正在进行工单所有异常
            info.setExs(workExceptionListMapper.selectWorkExceAllByWorkId(devWorkOrder.getId()));
        }
        //标准产量
        PageStandard standard = new PageStandard(devWorkOrder);
        info.setStandard(standard);
        //查询产线数据标识工位
        Workstation workstation = workstationMapper.selectWorkstationSignByLineId(line.getId(), line.getCompanyId());
        //统计该时间段的数据
        int r = 0;
        DevWorkDayHour hour = null;
        if (workstation != null && devWorkOrder != null) {
            int devId = workstation.getDevId() == null ? 0 : workstation.getDevId();
            r = devDataLogMapper.selectLineWorkSysTemData(line.getCompanyId(), line.getId(),
                    devWorkOrder.getId(), devId, workstation.getId(), WorkConstants.SING_LINE);
            hour = dayHourMapper.selectInfoByCompanyLineWorkDevIo(line.getCompanyId(), line.getId(), devWorkOrder.getId(), devId, workstation.getId());
        }
        //实际产量
        PageReal real = new PageReal(hour, r);
        info.setReal(real);
        //查询当天工单
        info.setWorkOrder(devWorkOrderMapper.selectDayWorkOrder(WorkConstants.SING_LINE, line.getCompanyId(), line.getId()));
        return info;
    }

    /**
     * 重置页面密码
     *
     * @param info 页面信息
     * @return
     */
    @Override
    public int savePwd(PageInfo info) {
        return pageInfoMapper.updatePageInfo(info);
    }

    /**
     * 根据页面id查询所有产线，判断是否需要配置
     *
     * @param pid
     * @return
     */
    @Override
    public List<ProductionLine> selectPageLineByPid(int pid, int companyId) {
        List<ProductionLine> lines = productionLineMapper.selectAllDef0(companyId);
        if (lines != null && lines.size() > 0) {
            for (ProductionLine line : lines) {
                //查询对应产线是否配置
                line.setManual(0);//表示未配置
                //查询对应产线是否在该页面配置
                PageInfoConfig config = pageInfoConfigMapper.selectPageConfigByPidAndLineId(pid, line.getId());
                if (config != null) {
                    line.setManual(1);//该产线在对应的看板中存在配置信息
                }
            }
            return lines;
        }
        return null;
    }

    /**
     * 查询所以车间
     *
     * @param pid 页面id
     * @return
     */
    @Override
    public List<SingleWork> selectSingleWork(int pid) {
        List<SingleWork> works = singleWorkService.selectSingleWorkListSign0();
        if (works != null && works.size() > 0) {
            for (SingleWork work : works) {
                work.setParentId(0);
                PageInfoConfig config = pageInfoConfigMapper.selectPageConfigByPidAndLineId(pid, work.getId());
                if (config != null) {
                    work.setParentId(1);
                }
            }
            return works;
        }
        return null;
    }

    /**
     * 校验看板名称的唯一性
     *
     * @param pageInfo 看板对象
     * @return 结果
     */
    @Override
    public String checkPageName(PageInfo pageInfo) {
        PageInfo uniquePageInfo = pageInfoMapper.selectPageInfoByPageName(JwtUtil.getUser().getCompanyId(), pageInfo.getPageName());
        if (com.ruoyi.common.utils.StringUtils.isNotNull(uniquePageInfo) && !pageInfo.getId().equals(uniquePageInfo.getId())) {
            return PageTypeConstants.PAGE_NAME_NOT_UNIQUE;
        }
        return PageTypeConstants.PAGE_NAME_UNIQUE;
    }

    /**
     * app端查询页面列表信息
     *
     * @param pageInfo 页面信息
     * @return 结果
     */
    @Override
    public List<PageInfo> appSelectPageInfo(PageInfo pageInfo) {
        return pageInfoMapper.selectPageInfoList(pageInfo);
    }
}
