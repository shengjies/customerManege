package com.ruoyi.project.iso.sopLine.service;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.insmanage.instrumentManage.mapper.InstrumentManageMapper;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.domain.SopLineWork;
import com.ruoyi.project.iso.sopLine.mapper.SopLineMapper;
import com.ruoyi.project.iso.sopLine.mapper.SopLineWorkMapper;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 作业指导书  产线 配置 服务层实现
 *
 * @author sj
 * @date 2019-06-15
 */
@Service
public class SopLineServiceImpl implements ISopLineService {
    @Autowired
    private SopLineMapper sopLineMapper;

    @Autowired
    private DevProductListMapper productListMapper;

    @Autowired
    private SopLineWorkMapper sopLineWorkMapper;

    @Autowired
    private IsoMapper isoMapper;

    /**
     * 查询作业指导书  产线 配置信息
     *
     * @param id 作业指导书  产线 配置ID
     * @return 作业指导书  产线 配置信息
     */
    @Override
    public SopLine selectSopLineById(Integer id) {
        return sopLineMapper.selectSopLineById(id);
    }

    /**
     * 查询作业指导书  产线 配置列表
     *
     * @param sopLine 作业指导书  产线 配置信息
     * @return 作业指导书  产线 配置集合
     */
    @Override
    public List<SopLine> selectSopLineList(SopLine sopLine) {
        return sopLineMapper.selectSopLineList(sopLine);
    }

    /**
     * 新增作业指导书  产线 配置
     *
     * @param sopLine 作业指导书  产线 配置信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSopLine(SopLine sopLine) {
        User user = JwtUtil.getUser();
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        if (sopLine != null) {
            //操作对应产品SOP 配置
            handleSopConfig(sopLine);
            // //操作产线 SOP 工位配置
            // handleSop(sopLine);
            return 1;
        }
        return 0;
    }

    /**
     * 修改作业指导书  产线 配置
     *
     * @param sopLine 作业指导书  产线 配置信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSopLine(SopLine sopLine) {
        User user = JwtUtil.getUser();
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        if (sopLine != null) {
            /**
             * 产线
             */
            if (FileConstants.SOP_TAG_LINE.equals(sopLine.getSopTag())) {
                //删除对应公司 对应产线 对应 SOP 配置信息
                sopLineMapper.deleteSopLine(sopLine.getCompanyId(), sopLine.getLineId(),null, sopLine.getSopId(),FileConstants.SOP_TAG_LINE);
                //删除 工位配置
                // sopLineWorkMapper.deleteSopLineWork(sopLine.getCompanyId(), sopLine.getLineId(), sopLine.getSopId(),FileConstants.SOP_TAG_LINE);
                //操作产线 SOP 工位配置
                // handleSop(sopLine);
            } else {
                //删除对应公司 对应单工位配置信息
                sopLineMapper.deleteSopLine(sopLine.getCompanyId(), sopLine.getLineId(),null, sopLine.getSopId(),FileConstants.SOP_TAG_SINGWORK);
                // 删除 车间单工位配置
                // sopLineWorkMapper.deleteSopLineWorkByWId(sopLine.getCompanyId(),sopLine.getLineId(),sopLine.getSopId(),FileConstants.SOP_TAG_SINGWORK);
                //操作产线 SOP 工位配置
                // handleSop(sopLine);

            }
            handleSopConfig(sopLine);
            return 1;
        }
        return 0;
    }

    /**
     * 操作sop配置信息
     * @param sopLine
     */
    private void handleSopConfig(SopLine sopLine) {
        if (sopLine.getPns() != null && sopLine.getPns().length > 0 && sopLine.getSopLineWorks() != null && sopLine.getSopLineWorks().size() > 0) {
            DevProductList productList = null;
            for (Integer pn : sopLine.getPns()) {
                for (SopLineWork work : sopLine.getSopLineWorks()) {
                    //获取对应产品信息
                    productList = productListMapper.selectDevProductListById(pn);
                    if (productList == null) {
                        continue;
                    }
                    sopLine.setPnId(pn);
                    sopLine.setPnCode(productList.getProductCode());
                    sopLine.setwId(work.getwId());
                    sopLine.setPageId(work.getPageId());
                    sopLine.setcTime(new Date());
                    sopLineMapper.insertSopLine(sopLine);
                }
            }
        }
    }

    /**
     * 操作单工位
     * @param sopLine sopline对象
     */
    private void handleSop(SopLine sopLine) {
        if (sopLine.getSopLineWorks() != null && sopLine.getSopLineWorks().size() > 0) {
            for (SopLineWork sopLineWork : sopLine.getSopLineWorks()) {
                sopLineWork.setcId(sopLine.getcId());
                sopLineWork.setCompanyId(sopLine.getCompanyId());
                sopLineWork.setcTime(new Date());
                sopLineWorkMapper.insertSopLineWork(sopLineWork);
            }
        }
    }

    /**
     * 删除作业指导书  产线 配置对象
     *
     * @param lineId 产线id
     * @param sopId  SOP id
     * @param sopTag sop配置标记车间或者流水线
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSopLine(int companyId, int lineId, int sopId,int sopTag) {
        try {
            //删除对应公司 对应产线 对应 SOP 配置信息
            sopLineMapper.deleteSopLine(companyId,null, lineId, sopId,FileConstants.SOP_TAG_LINE);
            //删除 工位配置
            if (sopTag == FileConstants.SOP_TAG_LINE) {
                sopLineWorkMapper.deleteSopLineWork(companyId, lineId, sopId,sopTag);
            } else {
                sopLineWorkMapper.deleteSopLineWorkByWId(companyId,lineId,sopId,sopTag);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据公司id 产线id SOP id查询所以的产线SOP 配置细心
     *
     * @param companyId 公司id
     * @param lineId    产线id
     * @param sopId     SOP id
     * @return
     */
    @Override
    public List<SopLine> selectLineAllSopConfig(int companyId, int lineId, int sopId, int sopTag) {
        return sopLineMapper.selectLineAllSopConfig(companyId, lineId, sopId, sopTag);
    }

    /**
     * 根据公司id 产线id SOP id查询所以的工位配置信息
     *
     * @param companyId 公司id
     * @param lineId    产线 id
     * @param sopId     SOP id
     * @return
     */
    @Override
    public List<SopLineWork> selectWorkstionByCompanyAndLineIdAndSopId(int companyId, int lineId, int sopId) {
        return sopLineWorkMapper.selectWorkstionByCompanyAndLineIdAndSopId(companyId, lineId, sopId);
    }

    /**
     * 查询作业指导书产线配置列表
     *
     * @param isoId     作业指导书id
     * @param companyId 公司id
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopLineListBySopId(Integer companyId, Integer isoId) {
        return sopLineMapper.selectSopLineListBySopId(companyId, isoId);
    }

    /**
     * 查询作业指导书工位配置列表
     *
     * @param companyId 公司id
     * @param pageId     作业指导书页id
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopLineListByPageId(Integer companyId, Integer pageId) {
        return sopLineMapper.selectSopLineListByPageId(companyId, pageId);
    }


    /******************************  单工位SOP配置 ***********************************************/

    @Autowired
    private InstrumentManageMapper instrumentManageMapper;
    @Autowired
    private SingleWorkMapper singleWorkMapper;

    /**
     * 查询单工位SOP配置列表
     *
     * @param sopLine sop信息
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopLineList2(SopLine sopLine) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return Collections.emptyList();
        }
        sopLine.setCompanyId(user.getCompanyId());
        List<SopLine> sopLines = sopLineMapper.selectSopLineList2(sopLine);
        for (SopLine line : sopLines) {
            Integer imId = singleWorkMapper.selectSingleWorkById(line.getLineId()).getImId();
            String imCode = instrumentManageMapper.selectInstrumentManageById(imId).getImCode();
            line.setImName(imCode);
        }
        return sopLines;
    }

    /**
     * 查询单工位的配置信息
     *
     * @param companyId 公司id
     * @param lineId    车间id
     * @param sopId     sopid
     * @param wId       工位id
     * @param sopTag    sop生产配置标记
     * @return
     */
    @Override
    public SopLineWork selectSopLineWorkInfo(int companyId, int lineId, int sopId, int wId, int sopTag) {
        SopLineWork sopLineWork = sopLineWorkMapper.selectInfoByApi(companyId, lineId, sopId, wId, sopTag);
        if (StringUtils.isNotNull(sopLineWork)) {
            sopLineWork.setcName(isoMapper.selectIsoById(sopLineWork.getPageId()).getcName());
        }
        return sopLineWork;
    }

    /**
     * 通过主表id查看配置明细
     * @param sId 主表id
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopConfigProBySId(Integer sId) {
        return sopLineMapper.selectSopConfigProBySId(sId);
    }

    /**
     * 查询所有工位配置
     * @param sId 主表id
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopConfigWorkBySId(Integer sId) {
        return sopLineMapper.selectSopConfigWorkBySId(sId);
    }
}
