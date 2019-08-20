package com.ruoyi.project.page.pageInfo.service;

import com.ruoyi.project.page.pageInfo.domain.PageInfo;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.singleWork.domain.SingleWork;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 页面管理 服务层
 * 
 * @author ruoyi
 * @date 2019-04-10
 */
public interface IPageInfoService 
{
	/**
     * 查询页面管理信息
     * 
     * @param id 页面管理ID
     * @return 页面管理信息
     */
	public PageInfo selectPageInfoById(Integer id);

	/**
	 * 根据页面编号查询页面基本信息
	 * @param id 页面编号
	 * @return
	 */
	PageInfo selectPageInfoByPageId(Integer id);
	
	/**
     * 查询页面管理列表
     * 
     * @param pageInfo 页面管理信息
     * @return 页面管理集合
     */
	public List<PageInfo> selectPageInfoList(PageInfo pageInfo,HttpServletRequest request);
	
	/**
     * 新增页面管理
     * 
     * @param pageInfo 页面管理信息
     * @return 结果
     */
	public int insertPageInfo(PageInfo pageInfo,HttpServletRequest request);
	
	/**
     * 修改页面管理
     * 
     * @param pageInfo 页面管理信息
     * @return 结果
     */
	public int updatePageInfo(PageInfo pageInfo);
		
	/**
     * 删除页面管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePageInfoByIds(String ids);

	/**
	 * 查询对应公司页面初始数据
	 * @return
	 */
	Map<String,Object> selectPageInitInfo(int a, Cookie[] cookies);

	/**
	 * 查询对应公司所以非轮播页面
	 * @return
	 */
	List<PageInfo> selectAllPage(int a, int p_id,Cookie[] cookies);

	/**
	 * 根据页面编辑查询对应的页面信息
	 * @param code 页面编号
	 * @return
	 */
	PageInfo selectPageByCode(String code);

	/**
	 * 重置页面密码
	 * @param info 页面信息
	 * @return
	 */
	int savePwd(PageInfo info);

	/**
	 * 根据页面id查询所有产线，判断是否需要配置
	 * @param pid
	 * @return
	 */
	List<ProductionLine> selectPageLineByPid(int pid,int companyId);

	/**
	 * 查询所以车间
	 * @param pid 页面id
	 * @return
	 */
	List<SingleWork> selectSingleWork(int pid);

	/**
	 * 校验看板名称的唯一性
	 * @param pageInfo 看板对象
	 * @return 结果
	 */
	String checkPageName(PageInfo pageInfo);

	/**
	 * app端查询看板页面列表
	 * @param pageInfo 页面信息
	 * @return 结果
	 */
	public List<PageInfo> appSelectPageInfo(PageInfo pageInfo);
}
