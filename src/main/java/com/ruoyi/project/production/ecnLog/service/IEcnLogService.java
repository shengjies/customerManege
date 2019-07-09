package com.ruoyi.project.production.ecnLog.service;

import com.ruoyi.project.production.ecnLog.domain.EcnLog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ECN 变更记录 服务层
 * 
 * @author zqm
 * @date 2019-05-16
 */
public interface IEcnLogService 
{
	/**
	 * 查询ECN 变更记录信息
	 *
	 * @param id ECN 变更记录ID
	 * @return ECN 变更记录信息
	 */
	public EcnLog selectEcnLogById(Integer id);
	/**
     * 查询ECN 变更记录列表
     * 
     * @param ecnLog ECN 变更记录信息
     * @return ECN 变更记录集合
     */
	public List<EcnLog> selectEcnLogList(EcnLog ecnLog, HttpServletRequest request);
	
	/**
     * 新增ECN 变更记录
     * 
     * @param ecnLog ECN 变更记录信息
     * @return 结果
     */
	public int insertEcnLog(EcnLog ecnLog);

	/**
	 * 关闭ECN 状态变更记录
	 *
	 * @param  type 变更记录信息
	 * @return 结果
	 */
	public int updateEcnLog(int type,int save_id);

}
