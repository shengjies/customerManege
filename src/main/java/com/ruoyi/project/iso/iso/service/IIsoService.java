package com.ruoyi.project.iso.iso.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.iso.iso.domain.Iso;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
	public int updateIso(Iso iso);
		
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
	 * 上传sop文件
	 * @param file 文件
	 * @param parentId 父id
	 * @param request 请求
	 * @return 结果
	 */
	int uploadSop(MultipartFile file, int parentId, HttpServletRequest request) throws IOException;
}