package com.ruoyi.project.mes.mesBatch.service;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.mes.mesBatch.domain.MesBatch;
import com.ruoyi.project.mes.mesBatch.domain.MesBatchDetail;
import com.ruoyi.project.mes.mesBatch.mapper.MesBatchDetailMapper;
import com.ruoyi.project.mes.mesBatch.mapper.MesBatchMapper;
import com.ruoyi.project.mes.mesBatchRule.domain.MesBatchRule;
import com.ruoyi.project.mes.mesBatchRule.mapper.MesBatchRuleMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * MES批准追踪 服务层实现
 *
 * @author sj
 * @date 2019-07-22
 */
@Service
public class MesBatchServiceImpl implements IMesBatchService {
    @Autowired
    private MesBatchMapper mesBatchMapper;

    @Autowired
    private MesBatchRuleMapper mesBatchRuleMapper;

    @Autowired
    private MesBatchDetailMapper mesBatchDetailMapper;

    /**
     * 查询MES批准追踪信息
     *
     * @param id MES批准追踪ID
     * @return MES批准追踪信息
     */
    @Override
    public MesBatch selectMesBatchById(Integer id) {
        return mesBatchMapper.selectMesBatchById(id);
    }

    /**
     * 查询MES批准追踪列表
     *
     * @param mesBatch MES批准追踪信息
     * @return MES批准追踪集合
     */
    @Override
    public List<MesBatch> selectMesBatchList(MesBatch mesBatch) {
        String batchCode = mesBatch.getBatchCode();
        if (StringUtils.isNotEmpty(batchCode)) {
            return mesBatchMapper.selectMesBatchList2(mesBatch);
        } else {
            return mesBatchMapper.selectMesBatchList(mesBatch);
        }
    }

    /**
     * 新增MES批准追踪
     *
     * @param mesBatch MES批准追踪信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertMesBatch(MesBatch mesBatch) {
        User user = JwtUtil.getUser();
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        String mesCode = mesBatch.getMesCode();
        // 查询mes是否存在
        MesBatch mesUnique = mesBatchMapper.selectMesBatchByMesCode(user.getCompanyId(), mesCode);
        if (StringUtils.isNotNull(mesUnique)) {
            if (mesUnique.getWorkCode().equals(mesBatch.getWorkCode())) {
                // 删除之前配置
                mesBatchDetailMapper.deleteMesBatchDetailByBId(mesUnique.getId());
                mesBatchMapper.deleteMesBatchById(mesUnique.getId());
            } else {
                throw new BusinessException("存在相同的MES主码");
            }
        }
        mesBatch.setCompanyId(user.getCompanyId());
        Integer ruleId = mesBatch.getRuleId();
        MesBatchRule mesBatchRule = mesBatchRuleMapper.selectMesBatchRuleById(ruleId);
        if (StringUtils.isNotNull(mesBatchRule)) {
            mesBatch.setRuleName(mesBatchRule.getRuleName());
            mesBatch.setRuleMateriel(mesBatchRule.getMateriels());
        }
        mesBatch.setcTime(new Date());
        int i = mesBatchMapper.insertMesBatch(mesBatch);
        List<MesBatchDetail> mesBatchDetailList = mesBatch.getMesBatchDetailList();
        for (MesBatchDetail batchDetail : mesBatchDetailList) {
            batchDetail.setbId(mesBatch.getId());
            batchDetail.setcTime(new Date());
            mesBatchDetailMapper.insertMesBatchDetail(batchDetail);
        }
        return i;
    }

    /**
     * 修改MES批准追踪
     *
     * @param mesBatch MES批准追踪信息
     * @return 结果
     */
    @Override
    public int updateMesBatch(MesBatch mesBatch) {
        return mesBatchMapper.updateMesBatch(mesBatch);
    }

    /**
     * 删除MES批准追踪对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteMesBatchByIds(String ids) {
        return mesBatchMapper.deleteMesBatchByIds(Convert.toStrArray(ids));
    }

}
