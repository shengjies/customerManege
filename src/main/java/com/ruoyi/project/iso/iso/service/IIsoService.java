package com.ruoyi.project.iso.iso.service;

import com.ruoyi.project.iso.iso.domain.Iso;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文件管理 服务层
 * 
 * @author sj
 * @date 2019-06-13
 */
public interface IIsoService 
{
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
	public List<Iso> selectIsoList(Iso iso,HttpServletRequest request);
	
	/**
     * 新增文件管理
     * 
     * @param iso 文件管理信息
     * @return 结果
     */
	public int insertIso(Iso iso, HttpServletRequest request);
	
	/**
     * 修改文件管理
     * 
     * @param iso 文件管理信息
     * @return 结果
     */
	public int updateIso(Iso iso,HttpServletRequest request) throws IOException;
		
	/**
     * 删除文件管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteIsoByIds(String ids);

	/**
	 * 删除文件管理信息
	 * @param isoId 文件管理id
	 * @return 结果
	 */
	int deleteIsoById(Integer isoId);

	/**
	 * 通过父文件id查询子文件列表
	 * @param parentId 父菜单id
	 * @return 结果
	 */
	List<Iso> selectIsoByParentId(Integer parentId);

	/**
	 * 根据父id和产线id查询对应产线所以未配置的SOP指导书
	 * @param pid 父id
	 * @param lineId 产线id
	 * @param sopTag sop配置标记状态流水线或者车间
	 * @return
	 */
	List<Iso> selectNotConfigByPidAndLineId(Integer pid,Integer lineId,Integer sopTag);

	/**
	 * 上传sop文件
	 * @param file 文件
	 * @param parentId 父id
	 * @param request 请求
	 * @return 结果
	 */
	int uploadSop(MultipartFile file, int parentId, HttpServletRequest request) throws IOException;

	/**
	 * 根据硬件编码查询对应的作业指导书
	 * @param code 硬件编码
	 * @return
	 */
	Map<String,Object> selectSopByDevCode(String code);

	/**
	 * app端查询ISO文件系统
	 * @param iso
	 * @return
	 */
	List<Iso> appSelectList(Iso iso);

	/**
	 * 查询所有的作业指导书
	 * @param parentId 父id，7为作业指导书
	 * @return 结果
	 */
	List<Iso> selectASOPList(Integer parentId);
}
