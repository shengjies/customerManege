package com.ruoyi.project.iso.iso.mapper;

import com.ruoyi.project.iso.iso.domain.Iso;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件管理 数据层
 *
 * @author sj
 * @date 2019-06-13
 */
public interface IsoMapper {
    /**
     * 查询文件管理信息
     *
     * @param id 文件管理ID
     * @return 文件管理信息
     */
    public Iso selectIsoById(Integer id);

    /**
     * 查询文件管理列表
     *
     * @param iso 文件管理信息
     * @return 文件管理集合
     */
    public List<Iso> selectIsoList(Iso iso);

    /**
     * 新增文件管理
     *
     * @param iso 文件管理信息
     * @return 结果
     */
    public int insertIso(Iso iso);

    /**
     * 修改文件管理
     *
     * @param iso 文件管理信息
     * @return 结果
     */
    public int updateIso(Iso iso);

    /**
     * 删除文件管理
     *
     * @param id 文件管理ID
     * @return 结果
     */
    public int deleteIsoById(Integer id);

    /**
     * 批量删除文件管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteIsoByIds(String[] ids);

    /**
     * 根据父id查询对应的子目录
     *
     * @param pid
     * @return
     */
    List<Iso> selectByPid(@Param("parentId") int pid);

	/**
	 * 根据父id和产线id查询对应产线所以未配置的SOP指导书
	 * @param pid 父id
	 * @param lineId 产线id
     * @param sopTag sop配置标记状态流水线或者车间
	 * @return
	 */
	List<Iso> selectNotConfigByPidAndLineId(@Param("pid")int pid,@Param("lineId")int lineId,@Param("sopTag") int sopTag);

	/**
	 * 根据祖父id查询对应的子目录 注册生成文件夹
	 * @param grParentId 祖父id
	 * @return 结果
	 */
	List<Iso> selectByGPid(@Param("grParentId")int grParentId);

    /**
     * 通过文件名已经文件夹路径查询文件信息
     *
     * @param diskPath 文件路径
     * @param fileName 文件名
     * @return 结果
     */
    Iso selectIsoByUploadName(@Param("diskPath") String diskPath, @Param("cName") String fileName);

    /**
     * 校验随机生成的英文文件夹名是否存在
     *
     * @param randomPath 文件名
     * @return 结果
     */
    Iso selectIsoByRandomName(@Param("eName") String randomPath);

    /**
     * 查询文件名是否重复
     *
     * @param diskPath 路径
     * @param eName    文件名不加后缀
     * @return 结果
     */
    Iso selectIsoByEName(@Param("diskPath") String diskPath, @Param("eName") String eName);

    /**
     * 校验文件夹名称的唯一性
     *
     * @param iType    类型
     * @param parentId 父id
     * @param cName    文件名
     * @return
     */
    Iso selectIsoByFolderNameUnique(@Param("iType") Integer iType, @Param("parentId") Integer parentId, @Param("cName") String cName);

    /**
     * 查询所有的作业指导书
     * @param parentId 父id
     * @param companyId 公司id
     * @return 结果
     */
    List<Iso> selectASOPList(@Param("parentId") Integer parentId,@Param("companyId") Integer companyId);
}