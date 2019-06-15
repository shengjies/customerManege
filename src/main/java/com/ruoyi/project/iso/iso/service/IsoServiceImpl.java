package com.ruoyi.project.iso.iso.service;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    public List<Iso> selectIsoList(Iso iso,HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null ) {
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
        DevCompany company = companyMapper.selectDevCompanyById(tokenUser.getCompanyId());
        // 查询父文件信息
        Iso isoParent= isoMapper.selectIsoById(iso.getParentId());
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
            String filePath = isoParent.getDisk() + File.separator + randomPath;
            File myPath = new File(filePath);
            if (!myPath.exists()) {//若此目录不存在，则创建之
                myPath.mkdir();
            }
            iso.seteName(randomPath);
            String disPath = (isoParent.getDiskPath() + File.separator + randomPath).replace("\\", "/");
            iso.setDiskPath(disPath);
            iso.setDisk(filePath.replace("\\", "/"));
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
    public int updateIso(Iso iso) {
        iso.setcTime(new Date()); // 更新修改时间
        return isoMapper.updateIso(iso);
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
     * 上传sop文件
     * @param file 文件
     * @param parentId 父id
     * @param request 请求
     * @return 结果
     */
    @Override
    public int uploadSop(MultipartFile file, int parentId, HttpServletRequest request) throws IOException {
        User user = JwtUtil.getTokenUser(request);
        if (user == null ) {
            return 0;
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
            Iso isoUnique = isoMapper.selectIsoByName(parentIso.getDiskPath(),fileName);
            if (StringUtils.isNotNull(isoUnique)) { // 存在相同文件名的文件
                throw new BusinessException("存在相同文件名的文件");
            }
            if (FileConstants.CATEGORY_SOP_FOLDER.equals(parentIso.getCategory())) { // 为SOP作业指导书下的文件
                iso.setCategory(FileConstants.CATEGORY_SOP_FILE); // 分类为sop下的文件
            }
            if (StringUtils.isNotEmpty(parentIso.getDisk())) {
                String path = parentIso.getDisk() + File.separator ;
                File desc = FileUploadUtils.getAbsoluteFile(path, path + fileName);
                file.transferTo(desc);
            }
            iso.setDiskPath(parentIso.getDiskPath());
            iso.setDisk(parentIso.getDisk());
            iso.setParentId(parentIso.getIsoId());
            iso.setGrParentId(parentIso.getParentId());
            iso.seteName(parentIso.geteName());
            iso.setcName(fileName);
            iso.setPath((isoFileUrl + parentIso.getDiskPath() + File.separator + fileName).replace("\\","/"));
            iso.setIsoId(null);
            return isoMapper.insertIso(iso);
        }
        return 0;
    }

}
