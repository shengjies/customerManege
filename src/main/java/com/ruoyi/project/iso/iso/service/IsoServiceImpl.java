package com.ruoyi.project.iso.iso.service;

import com.ruoyi.common.constant.DevConstants;
import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.mapper.SopLineMapper;
import com.ruoyi.project.iso.sopLine.mapper.SopLineWorkMapper;
import com.ruoyi.project.page.pageInfo.domain.SopApi;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.singleWork.domain.SingleWork;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import com.ruoyi.project.system.user.domain.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 文件管理 服务层实现
 *
 * @author sj
 * @date 2019-06-13
 */
@Service
public class IsoServiceImpl implements IIsoService {
    @Autowired
    private IsoMapper isoMapper;

    @Autowired
    private DevCompanyMapper companyMapper;

    @Autowired
    private WorkstationMapper workstationMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private SopLineMapper sopLineMapper;

    @Autowired
    private SopLineWorkMapper sopLineWorkMapper;

    @Autowired
    private DevListMapper devListMapper;

    @Autowired
    private SingleWorkMapper singleWorkMapper;

    @Value("${file.iso}")
    private String isoFileUrl;

    /**
     * 查询文件管理信息
     *
     * @param id 文件管理ID
     * @return 文件管理信息
     */
    @Override
    public Iso selectIsoById(Integer id) {
        return isoMapper.selectIsoById(id);
    }

    /**
     * 查询文件管理列表
     *
     * @param iso 文件管理信息
     * @return 文件管理集合
     */
    @Override
    public List<Iso> selectIsoList(Iso iso, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            return Collections.emptyList();
        }
        iso.setCompanyId(user.getCompanyId());
        if (StringUtils.isNotEmpty(iso.getcName())) { // 搜索查询

        }
        return isoMapper.selectIsoList(iso);
    }

    /**
     * 新增文件管理
     *
     * @param iso 文件管理信息
     * @return 结果
     */
    @Override
    public int insertIso(Iso iso, HttpServletRequest request) {
        User tokenUser = JwtUtil.getTokenUser(request);
        if (tokenUser == null) {
            return 0;
        }
        Iso uniqueIso = isoMapper.selectIsoByFolderNameUnique(iso.getiType(), iso.getParentId(), iso.getcName());
        if (StringUtils.isNotNull(uniqueIso)) {
            throw new BusinessException("文件名称重复，请重新输入");
        }
        // 查询父文件信息
        Iso isoParent = isoMapper.selectIsoById(iso.getParentId());
        if (StringUtils.isNotNull(isoParent)) {
            // 判断iso文件是否为作业指导书文件夹
            if (FileConstants.CATEGORY_SOP.equals(isoParent.getCategory())) {
                // 设置分类为作业指导书分类，该分类下只能创建一级文件夹
                iso.setCategory(FileConstants.CATEGORY_SOP_FOLDER);
            }
            String randomPath = CodeUtils.getRandomChar() + CodeUtils.getRandom(); // 随机路径
            // 校验随机生成的路径是否重复
            Iso randomIso = isoMapper.selectIsoByRandomName(randomPath);
            if (StringUtils.isNotNull(randomIso)) { // 存在相同的文件名
                // 重新生成随机英文名
                randomPath = CodeUtils.getRandomChar() + CodeUtils.getRandom();
            }
            String filePath = isoParent.getDisk() + "/" + randomPath;
            File myPath = new File(filePath);
            if (!myPath.exists()) {//若此目录不存在，则创建之
                myPath.mkdir();
            }
            iso.seteName(randomPath);
            iso.setDiskPath(isoParent.getDiskPath() + "/" + randomPath);
            iso.setDisk(filePath);
            iso.setcId(tokenUser.getUserId().intValue());
            iso.setcTime(new Date());
            iso.setCompanyId(tokenUser.getCompanyId());
            iso.setGrParentId(isoParent.getParentId());
            iso.setIsoId(null);
            return isoMapper.insertIso(iso);
        }
        return 0;

    }

    /**
     * 设置文件路径创建文件夹
     *
     * @param iso
     * @param company
     * @param isoParent
     */
    private void setFilePath(Iso iso, DevCompany company, Iso isoParent) {

    }

    /**
     * 修改文件管理
     *
     * @param iso 文件管理信息
     * @return 结果
     */
    @Override
    public int updateIso(Iso iso,HttpServletRequest request) throws IOException {
        User user = JwtUtil.getTokenUser(request);
        if (user == null ) {
            return 0;
        }
        Iso isoData = isoMapper.selectIsoById(iso.getIsoId());
        Iso isoUnique = null;
        if (StringUtils.isNotNull(isoData)) {
            // 获取文件的类型
            Integer iType = iso.getiType();
            if (FileConstants.ITYPE_FILE.equals(iType)) { // 为文件类型
                // 判断文件名是否重复
                isoUnique = isoMapper.selectIsoByEName(isoData.getDiskPath(),iso.geteName());
                if (StringUtils.isNotNull(isoUnique)) {
                    if (!isoUnique.getIsoId().equals(iso.getIsoId())) {
                        throw new BusinessException("文件名称重复，请重新输入");
                    }
                    if (iso.geteName().equals(isoUnique.geteName())) {
                        return isoMapper.updateIso(iso);
                    }
                } else {
                    // 获取旧文件信息
                    File file = new File(isoData.getDisk() + "/" + isoData.getcName());
                    if (file.exists()) { // 文件存在
                        String suffixName = file.getName();
                        suffixName = suffixName.substring(suffixName.lastIndexOf("."),suffixName.length()); // 文件名后缀
                        // 创建新的文件
                        File newFile = new File(isoData.getDisk() + "/" + iso.geteName() + suffixName);
                        FileUtils.copyFile(file,newFile);
                        file.delete();
                        iso.setcName(iso.geteName() + suffixName);
                        iso.setPath(isoFileUrl + isoData.getDiskPath() + "/" + iso.geteName() + suffixName);
                        iso.seteName(iso.geteName());
                    }
                }
            } else { // 文件夹类型
                isoUnique = isoMapper.selectIsoByFolderNameUnique(FileConstants.ITYPE_FOLDER,isoData.getParentId(),iso.getcName());
                if (StringUtils.isNotNull(isoUnique) && !isoUnique.getIsoId().equals(iso.getIsoId())) {
                    throw new BusinessException("文件名称重复，请重新输入");
                }
            }
            iso.setcTime(new Date()); // 更新修改时间
            return isoMapper.updateIso(iso);
        }
        return 0;
    }

    /**
     * 删除文件管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIsoByIds(String ids) {
        return isoMapper.deleteIsoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除文件管理
     *
     * @param isoId 文件管理id
     * @return 结果
     */
    @Override
    public int deleteIsoById(Integer isoId) {
        Iso iso = isoMapper.selectIsoById(isoId);
        if (StringUtils.isNotNull(iso) && StringUtils.isNotEmpty(iso.getDisk())) {
            // 判断是文件还是文件夹
            if (FileConstants.ITYPE_FOLDER.equals(iso.getiType())) { // 删除文件夹
                //删除对应的文件
                File file = new File(iso.getDisk());
                file.delete();
            } else { // 删除文件
                File file = new File(iso.getDisk() + File.separator + iso.getcName());
                file.delete();
            }
        }
        return isoMapper.deleteIsoById(isoId);
    }

    /**
     * 通过父文件id查询子文件列表
     *
     * @param parentId 父菜单id
     * @return 结果
     */
    @Override
    public List<Iso> selectIsoByParentId(Integer parentId) {
        return isoMapper.selectByPid(parentId);
    }

    /**
     * 根据父id和产线id查询对应产线所以未配置的SOP指导书
     *
     * @param pid    父id
     * @param lineId 产线id
     * @param sopTag sop配置标记状态流水线或者车间
     * @return
     */
    @Override
    public List<Iso> selectNotConfigByPidAndLineId(Integer pid, Integer lineId,Integer sopTag) {
        return isoMapper.selectNotConfigByPidAndLineId(pid, lineId,sopTag);
    }

    /**
     * 上传sop文件
     *
     * @param file     文件
     * @param parentId 父id
     * @param request  请求
     * @return 结果
     */
    @Override
    public int uploadSop(MultipartFile file, int parentId, HttpServletRequest request) throws IOException {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }

        // 子文件
        Iso iso = new Iso();
        iso.setCompanyId(user.getCompanyId());
        iso.setcId(user.getUserId().intValue());
        iso.setcTime(new Date());
        iso.setiType(FileConstants.ITYPE_FILE); // 类型为文件
        String fileSize = (file.getSize())/1024 + "Kb";
        iso.setFileSize(fileSize);
        // 查询父文件夹信息
        Iso parentIso = isoMapper.selectIsoById(parentId);
        if (StringUtils.isNotNull(parentIso)) {
            String fileName =  file.getOriginalFilename(); // 文件名
            // 判断相同文件夹下文件名是否存在相同文件
            Iso isoUnique = isoMapper.selectIsoByUploadName(parentIso.getDiskPath(),fileName);
            if (StringUtils.isNotNull(isoUnique)) { // 存在相同文件名的文件
                throw new BusinessException("文件" + fileName + "已存在");
            }
            if (FileConstants.CATEGORY_SOP_FOLDER.equals(parentIso.getCategory())) { // 为SOP作业指导书下的文件
                iso.setCategory(FileConstants.CATEGORY_SOP_FILE); // 分类为sop下的文件
            }
            if (StringUtils.isNotEmpty(parentIso.getDisk())) {
                String path = parentIso.getDisk() + "/";
                File desc = FileUploadUtils.getAbsoluteFile(path, path + fileName);
                file.transferTo(desc);
            }
            iso.setDiskPath(parentIso.getDiskPath());
            iso.setDisk(parentIso.getDisk());
            iso.setParentId(parentIso.getIsoId());
            iso.setGrParentId(parentIso.getParentId());
            iso.seteName(fileName.substring(0,fileName.lastIndexOf("."))); // 设置文件名
            iso.setcName(fileName); // 设置文件名包括后缀
            iso.setPath((isoFileUrl + parentIso.getDiskPath() + "/" + fileName));
            iso.setIsoId(null);
            return isoMapper.insertIso(iso);
        }
        return 0;
    }


    /**
     * 根据硬件编码查询对应的作业指导书
     *
     * @param code 硬件编码
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> selectSopByDevCode(String code){
        Map<String,Object> map = new HashMap<>(16);
        DevList devList = devListMapper.selectDevListByCode(code);
        if (devList == null || devList.getCompanyId() == null) {
            throw new BusinessException("硬件不存在或未归属公司");
        }
        if (devList.getDeviceStatus().equals(DevConstants.DEV_STATUS_NO)) {
            throw new BusinessException("硬件被禁用");
        }
        if (devList.getDevType() == null || devList.getSign().equals(DevConstants.DEV_SIGN_NOT_USE)) {
            throw new BusinessException("硬件未被配置");
        }
        //查询对应公司信息
        DevCompany company = companyMapper.selectDevCompanyById(devList.getCompanyId());
        if(company == null){
            throw new BusinessException("公司信息不存在");
        }
        /**
         * 生产线
         */
        if (devList.getDevType().equals(DevConstants.DEV_TYPE_LINE)) {
            //根据硬件编码查询对应的工位信息
            Workstation workstation = workstationMapper.selectByDevCode(code);
            if (workstation == null) throw new BusinessException("工位不存在");

            // 更新工位极光推送为已经更新
            workstation.setJpushTag(WorkConstants.JPUSH_UPDATED);
            workstationMapper.updateWorkstation(workstation);

            //查询对应产线
            ProductionLine line = productionLineMapper.selectProductionLineById(workstation.getLineId());
            if (line == null) throw new BusinessException("产线不存在");

            //查询正在进行的工单
            DevWorkOrder workOrder = devWorkOrderMapper.selectWorkByCompandAndLine(line.getCompanyId(), workstation.getLineId(), WorkConstants.SING_LINE);
            if (workOrder == null || StringUtils.isEmpty(workOrder.getProductCode())) throw new BusinessException("没有正在进行的工单");

            // 工位极光状态查询
            List<Workstation> workstationList = workstationMapper.selectWorkListJPushTagByLineId(workOrder.getLineId(),WorkConstants.JPUSH_NOT_UPDATED);
            if (StringUtils.isEmpty(workstationList)) {
                // 工位看板全部更新，更新工单极光推送标记
                workOrder.setJpushTag(WorkConstants.JPUSH_UPDATED);
                devWorkOrderMapper.updateDevWorkOrder(workOrder);
            }

            //查询对应SOP配置
            SopLine sopLine = sopLineMapper.selectSopByCompanyAndLineAndCode(workstation.getCompanyId(),workstation.getLineId(),workOrder.getProductCode(),workstation.getId(), FileConstants.SOP_TAG_LINE);
            if (sopLine == null) throw new BusinessException("没有配置SOP");

            //查询对应SOP
            Iso iso = isoMapper.selectIsoById(sopLine.getPageId());
            if (iso == null) throw new BusinessException("未配置作业指导书或作业指导书被删除");
            iso.setFileSize(line.getLineName()+" "+ workstation.getwName());
            iso.setcId(0);
            // 设置作业指导书看板相关信息
            SopApi sopApi = null;
            try {
                sopApi = getSopApi(map, workOrder, iso);
            } catch (Exception e) {
                // e.printStackTrace();
               throw new BusinessException("文件获取失败");
            }
            sopApi.setlName(line.getLineName());
            sopApi.setwName(workstation.getwName());
            sopApi.setCompany(company.getComName());
            map.put("data",sopApi);
            return map;

            /**
             * 车间
             */
        } else {
            // 查询单工位
            SingleWork singleWork = singleWorkMapper.selectSingleWorkByCode(0, devList.getId(), 0);
            if (StringUtils.isNull(singleWork)) {
                throw new BusinessException("单工位不存在");
            }

            // 更新单工位极光推送状态
            singleWork.setJpushTag(WorkConstants.JPUSH_UPDATED);
            singleWorkMapper.updateSingleWork1(singleWork);

            SingleWork house = singleWorkMapper.selectSingleWorkById(singleWork.getParentId());
            if (StringUtils.isNull(house)) {
                throw new BusinessException("车间不存在");
            }
            // 查询正在进行车间单工位生产的工单信息
            DevWorkOrder workOrder = devWorkOrderMapper.selectWorkInHouseBeInBySingId(devList.getCompanyId(), house.getId(), WorkConstants.SING_SINGLE, singleWork.getId(), WorkConstants.WORK_STATUS_STARTING);
            if (StringUtils.isNull(workOrder)) {
                throw new BusinessException("没有正在进行的工单");
            }

            // 查询配置过该工单的所有单工位信息
            List<SingleWork> singleWorkList = singleWorkMapper.selectSingleWorkJPushTagByPId(workOrder.getId(),house.getId(),WorkConstants.JPUSH_NOT_UPDATED);
            if (StringUtils.isEmpty(singleWorkList)) {
                // 更新工单极光推送为全部更新
                workOrder.setJpushTag(WorkConstants.JPUSH_UPDATED);
                devWorkOrderMapper.updateDevWorkOrder(workOrder);
            }

            // 查询对应的sop配置
            SopLine sopLine = sopLineMapper.selectSopByCompanyAndLineAndCode(devList.getCompanyId(), house.getId(), workOrder.getProductCode(),singleWork.getId(),FileConstants.SOP_TAG_SINGWORK);
            if (StringUtils.isNull(sopLine)) {
                throw new BusinessException("没有配置SOP");
            }
            //查询对应ISO信息
            Iso iso = isoMapper.selectIsoById(sopLine.getPageId());
            if (StringUtils.isNull(iso)) {
                throw new BusinessException("未配置作业指导书或作业指导书被删除");
            }
            iso.setFileSize(house.getWorkshopName()+" "+ singleWork.getImCode());
            iso.setcId(0);
            // 设置作业指导书看板相关信息
            SopApi sopApi = null;
            try {
                sopApi = getSopApi(map, workOrder, iso);
            } catch (Exception e) {
                // e.printStackTrace();
                throw new BusinessException("文件获取失败");
            }
            sopApi.setlName(house.getWorkshopName());
            sopApi.setwName(singleWork.getImCode());
            sopApi.setCompany(company.getComName());
            map.put("data",sopApi);
            return map;
        }

    }

    /**
     * 设置作业指导书看板相关信息
     * @param map 参数项
     * @param workOrder 工单
     * @param iso sop
     * @return 结果
     */
    private SopApi getSopApi(Map<String, Object> map, DevWorkOrder workOrder, Iso iso) throws UnsupportedEncodingException {
        int index = iso.getPath().lastIndexOf("/");
        String path = iso.getPath().substring(0,index);
        String fileName = iso.getPath().substring(index + 1);
        map.put("iso", iso);
        SopApi sopApi = new SopApi();
        sopApi.setwCode(workOrder.getWorkorderNumber());
        sopApi.setwStatus(workOrder.getOperationStatus());
        sopApi.setwNumber(workOrder.getProductNumber());
        sopApi.setpCode(workOrder.getProductCode());
        sopApi.setpName(workOrder.getProductName());
        sopApi.setIsoId(iso.getIsoId());
        sopApi.setIsoPath(iso.getPath());
        sopApi.setPath(path+"/"+ URLEncoder.encode(fileName,"UTF-8"));
        return sopApi;
    }


    /**
     * app端查询ISO文件系统
     * @param iso 文件体系对象
     * @return 结果
     */
    @Override
    public List<Iso> appSelectList(Iso iso) {
        return isoMapper.selectIsoList(iso);
    }

    /**
     * 查询所有的作业指导书
     * @param parentId 父id，7为作业指导书
     * @return 结果
     */
    @Override
    public List<Iso> selectASOPList(Integer parentId) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return Collections.emptyList();
        }
        return isoMapper.selectASOPList(parentId,user.getCompanyId());
    }
}
