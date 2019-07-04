package com.ruoyi.project.production.singleWork.mapper;

import com.ruoyi.project.production.singleWork.domain.SingleWork;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单工位数据 数据层
 *
 * @author sj
 * @date 2019-07-03
 */
public interface SingleWorkMapper {
    /**
     * 查询单工位数据信息
     *
     * @param id 单工位数据ID
     * @return 单工位数据信息
     */
    public SingleWork selectSingleWorkById(Integer id);

    /**
     * 查询单工位数据列表
     *
     * @param singleWork 单工位数据信息
     * @return 单工位数据集合
     */
    public List<SingleWork> selectSingleWorkList(SingleWork singleWork);

    /**
     * 新增单工位数据
     *
     * @param singleWork 单工位数据信息
     * @return 结果
     */
    public int insertSingleWork(SingleWork singleWork);

    /**
     * 修改单工位数据
     *
     * @param singleWork 单工位数据信息
     * @return 结果
     */
    public int updateSingleWork(SingleWork singleWork);

    /**
     * 删除单工位数据
     *
     * @param id 单工位数据ID
     * @return 结果
     */
    public int deleteSingleWorkById(Integer id);

    /**
     * 批量删除单工位数据
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSingleWorkByIds(String[] ids);

    /**
     * 通过车间名查询单工位信息
     *
     * @param companyId    公司id
     * @param workshopName 车间名
     * @return 结果
     */
    SingleWork selectSingleWorkByWorkshopName(@Param("companyId") Integer companyId, @Param("workshopName") String workshopName);

    /**
     * 通过父id查询单工位列表
     * @param companyId 公司id
     * @param parentId 父id
     * @return 结果
     */
    List<SingleWork> selectSingleWorkByParentId(@Param("companyId") Integer companyId, @Param("parentId") Integer parentId);
}