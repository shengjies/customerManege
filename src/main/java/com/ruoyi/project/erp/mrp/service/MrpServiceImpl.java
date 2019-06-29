package com.ruoyi.project.erp.mrp.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.BomConstants;
import com.ruoyi.common.constant.MrpConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.materielStock.domain.MaterielStock;
import com.ruoyi.project.erp.materielStock.mapper.MaterielStockMapper;
import com.ruoyi.project.erp.orderDetails.domain.OrderDetails;
import com.ruoyi.project.erp.orderDetails.mapper.OrderDetailsMapper;
import com.ruoyi.project.erp.productBom.domain.ProductBom;
import com.ruoyi.project.erp.productBom.domain.ProductBomDetails;
import com.ruoyi.project.erp.productBom.mapper.ProductBomDetailsMapper;
import com.ruoyi.project.erp.productBom.mapper.ProductBomMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.erp.mrp.mapper.MrpMapper;
import com.ruoyi.project.erp.mrp.domain.Mrp;
import com.ruoyi.project.erp.mrp.service.IMrpService;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * MRP记录 服务层实现
 *
 * @author sj
 * @date 2019-06-24
 */
@Service("mrp")
public class MrpServiceImpl implements IMrpService {
    @Autowired
    private MrpMapper mrpMapper;

    @Autowired
    private ProductBomMapper productBomMapper;

    @Autowired
    private ProductBomDetailsMapper productBomDetailsMapper;

    @Autowired
    private MaterielStockMapper materielStockMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    /**
     * 查询MRP记录信息
     *
     * @param id MRP记录ID
     * @return MRP记录信息
     */
    @Override
    public Mrp selectMrpById(Integer id) {
        return mrpMapper.selectMrpById(id);
    }

    /**
     * 查询MRP记录列表
     *
     * @param mrp MRP记录信息
     * @return MRP记录集合
     */
    @Override
    public List<Mrp> selectMrpList(Mrp mrp) {
        Integer supplierId = mrp.getSupplierId();
        if (supplierId == null || supplierId == -1) {
            return mrpMapper.selectMrpList(mrp);
        } else {
            return mrpMapper.selectMrpListBySupId(supplierId);
        }
    }

    /**
     * 新增MRP记录
     *
     * @param mrp MRP记录信息
     * @return 结果
     */
    @Override
    public int insertMrp(Mrp mrp) {
        return mrpMapper.insertMrp(mrp);
    }

    /**
     * 修改MRP记录
     *
     * @param mrp MRP记录信息
     * @return 结果
     */
    @Override
    public int updateMrp(Mrp mrp) {
        return mrpMapper.updateMrp(mrp);
    }

    /**
     * 删除MRP记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteMrpByIds(String ids) {
        return mrpMapper.deleteMrpByIds(Convert.toStrArray(ids));
    }


    /**
     * 将选中的订单明细生成mrp
     *
     * @param mrps
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addMrpByOrDeIds(String mrps) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return 0;
        }
        List<OrderDetails> orderDetailsList = JSON.parseArray(mrps, OrderDetails.class);
        ProductBom newProBom = null;
        List<ProductBomDetails> proBomDetailList = null;
        int proNeedTotalNumber = 0;
        int matNeedTotalNumber = 0;
        Mrp mrp = null;
        MaterielStock materielStock = null;
        // mrp编号
        String mrpCode = CodeUtils.getMrpCode();
        // 勾选中需生成mrp的订单明细列表
        for (OrderDetails orderDetails : orderDetailsList) {
            if (MrpConstants.ORDER_TO_MRP.equals(orderDetails.getMrpStatus())) {
                throw new BusinessException("订单" + orderDetails.getOrderCode() + "已生成MRP，请勿重复操作");
            }
            // 获取订单所差的产品数量
            proNeedTotalNumber = (orderDetails.getNumber() - orderDetails.getDeliverNum() - orderDetails.getLockNumber()) + orderDetails.getTiaoNumber();
            // 查询出最新版本的产品bom
            newProBom = productBomMapper.selectProBomNewVerByProId(user.getCompanyId(), orderDetails.getProductId());
            if (StringUtils.isNull(newProBom)) {
                throw new BusinessException("请先导入产品BOM信息");
            }
            // 通过产品bomId查询最新的物料组成信息、bom清单
            proBomDetailList = productBomDetailsMapper.selectProBomDetailsByBomId(newProBom.getId());
            for (ProductBomDetails bomDetails : proBomDetailList) {
                mrp = new Mrp();
                mrp.setCompanyId(user.getCompanyId());
                mrp.setmCode(mrpCode);
                mrp.setProductId(orderDetails.getProductId());
                mrp.setProductCode(orderDetails.getProdectCode());
                mrp.setProductName(orderDetails.getProductName());
                mrp.setOrderId(orderDetails.getOrderId());
                mrp.setOrderCode(orderDetails.getOrderCode());

                // bom清单为物料类型
                if (BomConstants.BOM_TYPE_MAT.equals(bomDetails.getBomDetailsType())) {
                    mrp.setMaterielId(bomDetails.getDetailId());
                    mrp.setMaterielCode(bomDetails.getDetailCode());
                    mrp.setMaterielName(bomDetails.getDetailName());
                    mrp.setMaterielModel(bomDetails.getDetailModel());
                    // 物料一共需要的数量
                    matNeedTotalNumber = bomDetails.getOneNum() * proNeedTotalNumber;
                    // 查询物料库存
                    materielStock = materielStockMapper.selectMaterielStockByMaterielId(bomDetails.getDetailId());
                    Integer goodNumber = materielStock.getGoodNumber();
                    // 库存足够
                    if (matNeedTotalNumber <= goodNumber) {
                        mrp.setNeedNumber(matNeedTotalNumber);
                        mrp.setLockMatNumber(matNeedTotalNumber);
                        mrp.setDelNumber(0);
                        // 不采购
                        mrp.setmStatus(MrpConstants.MRP_MAT_NOTCG);
                        // 订单明细锁定物料库存
                        orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() + matNeedTotalNumber);
                        materielStock.setGoodNumber(goodNumber - matNeedTotalNumber);
                        materielStock.setLockNumber(materielStock.getLockNumber() + matNeedTotalNumber);
                        // 库存不够
                    } else {
                        mrp.setNeedNumber(matNeedTotalNumber);
                        mrp.setLockMatNumber(goodNumber);
                        mrp.setDelNumber(matNeedTotalNumber - goodNumber);
                        // 需采购
                        mrp.setmStatus(MrpConstants.MRP_MAT_NEEDCG);
                        orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() + goodNumber);
                        materielStock.setGoodNumber(0);
                        materielStock.setLockNumber(materielStock.getLockNumber() + goodNumber);
                    }
                    materielStock.setLastUpdate(new Date());
                    materielStockMapper.updateMaterielStock(materielStock);
                }
                mrpMapper.insertMrp(mrp);
            }
            // 更新订单为已生成mrp记录
            orderDetails.setMrpStatus(MrpConstants.ORDER_TO_MRP);
            orderDetailsMapper.updateOrderDetails(orderDetails);
        }
        return 1;
    }

    /**
     * 取消MRP信息
     * @param mrp mrp信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelMrp(Mrp mrp) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return 0;
        }
        // 获取MRP批次编号信息,通过编号查询mrp列表
        String mCode = mrp.getmCode();
        List<Mrp> mrpList = mrpMapper.selectMrpListByMcode(user.getCompanyId(),mCode);
        OrderDetails orderDetails = null;
        MaterielStock materielStock = null;
        for (Mrp m : mrpList) {
            if (MrpConstants.MRP_MAT_CGING.equals(m.getmStatus())) {
                throw new BusinessException("MRP编号"+ m.getmCode() + "批次已有采购信息，勿取消操作");
            }
            // mrp对应订单操作
            orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(),m.getOrderId(),m.getProductId());
            orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() - m.getLockMatNumber());
            orderDetails.setMrpStatus(MrpConstants.ORDER_NOTO_MRP);
            orderDetailsMapper.updateOrderDetails(orderDetails);
            // 物料库存操作
            materielStock = materielStockMapper.selectMaterielStockByMaterielId(m.getMaterielId());
            materielStock.setLockNumber(materielStock.getLockNumber() - m.getLockMatNumber());
            materielStock.setGoodNumber(materielStock.getGoodNumber() + m.getLockMatNumber());
            materielStock.setLastUpdate(new Date());
            materielStockMapper.updateMaterielStock(materielStock);
        }
        return mrpMapper.deleteMrpByMcode(mCode);
    }

    /**
     * mrp的所有订单信息
     * @return 结果
     */
    @Override
    public List<Mrp> selectAllOrderCode() {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return Collections.emptyList();
        }
        return mrpMapper.selectAllOrderCode(user.getCompanyId());
    }

    /**
     * mrp的所有物料信息
     * @return 结果
     */
    @Override
    public List<Mrp> selectAllMatCode() {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return Collections.emptyList();
        }
        return mrpMapper.selectAllMatCode(user.getCompanyId());
    }

    /**
     * 锁定物料的mrp信息
     * @param mrp mrp信息
     * @return 结果
     */
    @Override
    public List<Mrp> selectMrpLockMatList(Mrp mrp) {
        return mrpMapper.selectMrpLockMatList(mrp);
    }

    /**
     * 查看订单锁定的物料信息
     * @param mrp mrp信息
     * @return 结果
     */
    @Override
    public List<Mrp> selectMrpListByPIdAndOId(Mrp mrp) {
        return mrpMapper.selectMrpListByPIdAndOId(mrp);
    }
}
