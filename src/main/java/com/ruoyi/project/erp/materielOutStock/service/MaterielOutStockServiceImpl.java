package com.ruoyi.project.erp.materielOutStock.service;

import com.ruoyi.common.constant.MrpConstants;
import com.ruoyi.common.constant.StockConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.erpRelation.domain.ErpRelation;
import com.ruoyi.project.erp.erpRelation.mapper.ErpRelationMapper;
import com.ruoyi.project.erp.materielOutStock.domain.MaterielOutStock;
import com.ruoyi.project.erp.materielOutStock.mapper.MaterielOutStockMapper;
import com.ruoyi.project.erp.materielOutStockDetails.domain.MaterielOutStockDetails;
import com.ruoyi.project.erp.materielOutStockDetails.mapper.MaterielOutStockDetailsMapper;
import com.ruoyi.project.erp.materielStock.domain.MaterielStock;
import com.ruoyi.project.erp.materielStock.mapper.MaterielStockMapper;
import com.ruoyi.project.erp.materielSupplier.domain.MaterielSupplier;
import com.ruoyi.project.erp.materielSupplier.mapper.MaterielSupplierMapper;
import com.ruoyi.project.erp.mrp.domain.Mrp;
import com.ruoyi.project.erp.mrp.mapper.MrpMapper;
import com.ruoyi.project.erp.mrpPurchase.domain.MrpPurchase;
import com.ruoyi.project.erp.mrpPurchase.mapper.MrpPurchaseMapper;
import com.ruoyi.project.erp.orderDetails.domain.OrderDetails;
import com.ruoyi.project.erp.orderDetails.mapper.OrderDetailsMapper;
import com.ruoyi.project.erp.purchase.domain.Purchase;
import com.ruoyi.project.erp.purchase.mapper.PurchaseMapper;
import com.ruoyi.project.erp.purchaseDetails.domain.PurchaseDetails;
import com.ruoyi.project.erp.purchaseDetails.mapper.PurchaseDetailsMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 物料出库 服务层实现
 *
 * @author zqm
 * @date 2019-05-07
 */
@Service
public class MaterielOutStockServiceImpl implements IMaterielOutStockService {
    @Autowired
    private MaterielOutStockMapper materielOutStockMapper;

    @Autowired
    private MaterielStockMapper materielStockMapper;

    @Autowired
    private MaterielOutStockDetailsMapper materielOutStockDetailsMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseDetailsMapper purchaseDetailsMapper;

    @Autowired
    private MaterielSupplierMapper materielSupplierMapper;

    @Autowired
    private MrpMapper mrpMapper;

    @Autowired
    private MrpPurchaseMapper mrpPurchaseMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private ErpRelationMapper erpRelationMapper;


    /**
     * 查询物料出库信息
     *
     * @param id 物料出库ID
     * @return 物料出库信息
     */
    @Override
    public MaterielOutStock selectMaterielOutStockById(Integer id) {
        MaterielOutStock materielOutStock = materielOutStockMapper.selectMaterielOutStockById(id);
        MaterielOutStockDetails materielOutStockDetails = new MaterielOutStockDetails();
        materielOutStockDetails.setOutId(id);
        List<MaterielOutStockDetails> materielOutStockDetailsList = materielOutStockDetailsMapper.selectMaterielOutStockDetailsList(materielOutStockDetails);
        for (MaterielOutStockDetails outStockDetails : materielOutStockDetailsList) {
            // 采购单编号
            String purchaseCode = outStockDetails.getPurchaseCode();
            if (!StringUtils.isEmpty(purchaseCode) && !"-1".equals(purchaseCode)) {
                PurchaseDetails purchaseDetails = purchaseDetailsMapper.selectPurchaseDetailsByCode(null, purchaseCode, outStockDetails.getSupplierCode(),
                        outStockDetails.getMaterielCode());
                outStockDetails.setPurchaseDetails(purchaseDetails);
            }
        }
        materielOutStock.setMaterielOutStockDetailsList(materielOutStockDetailsList);
        return materielOutStock;
    }

    /**
     * 查询物料出库列表
     *
     * @param materielOutStock 物料出库信息
     * @return 物料出库集合
     */
    @Override
    public List<MaterielOutStock> selectMaterielOutStockList(MaterielOutStock materielOutStock, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            return Collections.emptyList();
        }
        materielOutStock.setCompanyId(user.getCompanyId());
        return materielOutStockMapper.selectMaterielOutStockList(materielOutStock);
    }

    /**
     * 新增物料出库
     *
     * @param materielOutStock 物料出库信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertMaterielOutStock(MaterielOutStock materielOutStock, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) return 0;
        /**
         * 物料退货
         */
        // 物料出库单自动生成
        String matOutStockCode = CodeUtils.getMatOutStockCode();
        materielOutStock.setOutCode(matOutStockCode);
        materielOutStock.setCompanyId(user.getCompanyId());
        materielOutStock.setCreateId(user.getUserId().intValue());
        materielOutStock.setCreateName(user.getUserName());
        materielOutStock.setCreateTime(new Date());
        int i = materielOutStockMapper.insertMaterielOutStock(materielOutStock);
        // 存在物料退货明细
        if (!StringUtils.isEmpty(materielOutStock.getMaterielOutStockDetailsList())) {
            /**
             * 物料退货明细
             */
            BigDecimal outNumber = null;
            List<MrpPurchase> mrpPurchaseList = null;
            Mrp mrp = null;
            OrderDetails orderDetails = null;
            ErpRelation erpRelation = null;
            List<MaterielOutStockDetails> materielOutStockDetailsList = materielOutStock.getMaterielOutStockDetailsList();
            for (MaterielOutStockDetails outStockDetails : materielOutStockDetailsList) {
                /**
                 * 物料库存操作
                 */
                MaterielStock materielStock = materielStockMapper.selectMaterielStockByMatCodeAndComId(outStockDetails.getMaterielCode(), user.getCompanyId());
                // 库存中没有改物料信息
                if (StringUtils.isNull(materielStock)) {
                    throw new BusinessException("库存中没有" + outStockDetails.getMaterielCode() + "物料信息");
                } else {
                    /**
                     * 减库存不良品数量
                     */
                    // 退货数量大于仓库不良品数量
                    if (outStockDetails.getOutNumber() > materielStock.getBadNumber()) {
                        throw new BusinessException("库存不良品数量不足");
                    }
                    /**
                     * 物料退货明细
                     */
                    // 查询物料供应商关联价格信息
                    List<MaterielSupplier> materielSuppliers = materielSupplierMapper.selectMaterielSupplierListByMatIdAndSupId(outStockDetails.getMaterielId(), materielOutStock.getSupplierId());
                    // 物料供应商关联信息
                    if (!StringUtils.isEmpty(materielSuppliers)) {
                        outNumber = new BigDecimal(outStockDetails.getOutNumber());
                        MaterielSupplier materielSupplier = materielSuppliers.get(0);
                        // 设置单价
                        outStockDetails.setPrice(materielSupplier.getSupplierPrice());
                        // 设置总价格
                        outStockDetails.setTotalPrice(materielSupplier.getSupplierPrice().multiply(outNumber));
                    }
                    outStockDetails.setOutId(materielOutStock.getId());
                    outStockDetails.setOutCode(matOutStockCode);
                    outStockDetails.setCreateTime(new Date());

                    materielOutStockDetailsMapper.insertMaterielOutStockDetails(outStockDetails);
                    /**
                     * 采购单操作
                     */
                    // 物料退货当中采购单号信息
                    String purchaseCode = outStockDetails.getPurchaseCode();

                    // 是选择了采购单退货
                    if (!StringUtils.isEmpty(purchaseCode) && !"-1".equals(purchaseCode)) {
                        /**
                         * 采购单操作
                         */
                        Purchase purchase = purchaseMapper.selectPurchaseBySupIdAndPuraseCode(user.getCompanyId(), materielOutStock.getSupplierId(), outStockDetails.getPurchaseCode());
                        purchase.setDeliverTotalNum(purchase.getDeliverTotalNum() - outStockDetails.getOutNumber());
                        // 采购单总数减少，交付状态变成未交付
                        purchase.setPurchaseStatut(StockConstants.ORDER_STATUS_TWO);
                        purchaseMapper.updatePurchase(purchase);
                        /**
                         * 采购单详情，已采购数量
                         */
                        PurchaseDetails purchaseDetails = purchaseDetailsMapper.selectPurchaseDetailsByCode(purchase.getId(), purchase.getPurchaseCode(),
                                outStockDetails.getSupplierCode(), outStockDetails.getMaterielCode());
                        purchaseDetails.setDeliverNum(purchaseDetails.getDeliverNum() - outStockDetails.getOutNumber());
                        purchaseDetailsMapper.updatePurchaseDetails(purchaseDetails);
                        /**
                         * MRP相关操作
                         */
                        mrpPurchaseList = mrpPurchaseMapper.selectMrpPurchaseByPurIdAndMatCodeGtLockNum(purchase.getId(), outStockDetails.getMaterielCode());
                        // 退货数量操作mrp相关表记录，递减
                        Integer mrpOutNumber = outStockDetails.getOutNumber();
                        for (MrpPurchase mrpPurchase : mrpPurchaseList) {

                            if (mrpOutNumber <= 0) {
                                break;
                            }

                            // 新增关联记录
                            erpRelation = new ErpRelation();
                            erpRelation.setMpId(mrpPurchase.getId());
                            erpRelation.setMatDetailId(outStockDetails.getId());
                            erpRelation.setReStatus(MrpConstants.MAT_OUTSTOCK);
                            erpRelationMapper.insertErpRelation(erpRelation);

                            Integer initLockMatNumber = mrpPurchase.getInitLockMatNumber();
                            // 退货数量大于mrp采购单关联的实际锁定库存数量
                            if (mrpOutNumber >= initLockMatNumber) {
                                // mrp采购单关联信息
                                mrpPurchase.setLockMatNumber(mrpPurchase.getLockMatNumber() - initLockMatNumber);
                                mrpPurchase.setDelNumber(mrpPurchase.getDelNumber() + initLockMatNumber);
                                mrpPurchase.setTotalNumber(mrpPurchase.getTotalNumber() + initLockMatNumber);
                                mrpPurchase.setInitLockMatNumber(0);

                                // mrp信息
                                mrp = mrpMapper.selectMrpById(mrpPurchase.getMrpId());
                                // 订单操作
                                orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(), mrp.getOrderId(), mrp.getProductId());
                                orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() - initLockMatNumber);

                                mrp.setLockMatNumber(mrp.getLockMatNumber() - initLockMatNumber);
                                mrp.setDelNumber(mrp.getDelNumber() + initLockMatNumber);
                                mrp.setTotalNumber(mrp.getTotalNumber() + initLockMatNumber);
                                // 更新为采购中
                                mrp.setmStatus(MrpConstants.MRP_MAT_CGING);

                                mrpOutNumber = mrpOutNumber - initLockMatNumber;

                                // 退货数量小于mrp采购单关联的实际锁定库存数量
                            } else {
                                // mrp采购单关联信息
                                mrpPurchase.setLockMatNumber(mrpPurchase.getLockMatNumber() - mrpOutNumber);
                                mrpPurchase.setInitLockMatNumber(initLockMatNumber - mrpOutNumber);
                                mrpPurchase.setDelNumber(mrpPurchase.getDelNumber() + mrpOutNumber);
                                mrpPurchase.setTotalNumber(mrpPurchase.getTotalNumber() + mrpOutNumber);

                                // MRP以及订单明细信息操作
                                mrp = mrpMapper.selectMrpById(mrpPurchase.getMrpId());
                                orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(), mrp.getOrderId(), mrp.getProductId());
                                orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() - mrpOutNumber);
                                mrp.setTotalNumber(mrp.getTotalNumber() + mrpOutNumber);
                                mrp.setDelNumber(mrp.getDelNumber() + mrpOutNumber);
                                mrp.setLockMatNumber(mrp.getLockMatNumber() - mrpOutNumber);

                                mrpOutNumber = 0;

                            }
                            orderDetailsMapper.updateOrderDetails(orderDetails);
                            mrpPurchaseMapper.updateMrpPurchase(mrpPurchase);
                            mrpMapper.updateMrp(mrp);
                        }
                    }

                    // 物料库存操作
                    materielStock.setBadNumber(materielStock.getBadNumber() - outStockDetails.getOutNumber());
                    materielStock.setTotalNumber(materielStock.getTotalNumber() - outStockDetails.getOutNumber());
                    materielStock.setLastUpdate(new Date());
                    materielStockMapper.updateMaterielStock(materielStock);
                }

            }
        }
        return i;
    }

    /**
     * 修改物料出库
     *
     * @param materielOutStock 物料出库信息
     * @return 结果
     */
    @Override
    public int updateMaterielOutStock(MaterielOutStock materielOutStock) {
        return materielOutStockMapper.updateMaterielOutStock(materielOutStock);
    }

    /**
     * 删除物料出库对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteMaterielOutStockByIds(String ids) {
        return materielOutStockMapper.deleteMaterielOutStockByIds(Convert.toStrArray(ids));
    }

    /**
     * 作废物料退货信息
     *
     * @param id 需要作废的物料退货id
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int nullifyMaterielOutStockByIds(Integer id, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            return 0;
        }
        // 物料退货单
        MaterielOutStock materielOutStock = materielOutStockMapper.selectMaterielOutStockById(id);
        MaterielOutStockDetails materielOutStockDetails = new MaterielOutStockDetails();
        materielOutStockDetails.setOutId(materielOutStock.getId());
        // 物料退货明细
        List<MaterielOutStockDetails> materielOutStockDetailsList = materielOutStockDetailsMapper.selectMaterielOutStockDetailsList(materielOutStockDetails);
        List<MrpPurchase> mrpPurchaseList = null;
        Mrp mrp = null;
        OrderDetails orderDetails = null;
        ErpRelation erpRelation = null;
        MrpPurchase mrpPurchase = null;
        for (MaterielOutStockDetails outStockDetails : materielOutStockDetailsList) {
            /**
             * 库存数量回滚
             */
            MaterielStock materielStock = materielStockMapper.selectMaterielStockByMatCodeAndComId(outStockDetails.getMaterielCode(), user.getCompanyId());
            materielStock.setTotalNumber(materielStock.getTotalNumber() + outStockDetails.getOutNumber());
            materielStock.setBadNumber(materielStock.getBadNumber() + outStockDetails.getOutNumber());
            materielStock.setLastUpdate(new Date());
            materielStockMapper.updateMaterielStock(materielStock);
            /**
             * 采购单操作
             */
            // 物料退货当中采购单号信息
            String purchaseCode = outStockDetails.getPurchaseCode();
            // 是选择了采购单退货
            if (!StringUtils.isEmpty(purchaseCode) && !"-1".equals(purchaseCode)) {
                /**
                 * 采购单操作
                 */
                Purchase purchase = purchaseMapper.selectPurchaseBySupIdAndPuraseCode(user.getCompanyId(), materielOutStock.getSupplierId(), outStockDetails.getPurchaseCode());
                purchase.setDeliverTotalNum(purchase.getDeliverTotalNum() + outStockDetails.getOutNumber());
                purchaseMapper.updatePurchase(purchase);
                /**
                 * 采购单详情，已采购数量
                 */
                PurchaseDetails purchaseDetails = purchaseDetailsMapper.selectPurchaseDetailsByCode(purchase.getId(), purchase.getPurchaseCode(),
                        outStockDetails.getSupplierCode(), outStockDetails.getMaterielCode());
                purchaseDetails.setDeliverNum(purchaseDetails.getDeliverNum() + outStockDetails.getOutNumber());
                purchaseDetailsMapper.updatePurchaseDetails(purchaseDetails);

                /**
                 * MRP相关操作
                 */
                erpRelation = erpRelationMapper.selectErpRelationByMatDetailId(outStockDetails.getId(),MrpConstants.MAT_OUTSTOCK);
                Integer mrpOutNumber = outStockDetails.getOutNumber();
                if (StringUtils.isNotNull(erpRelation)) {
                    mrpPurchase = mrpPurchaseMapper.selectMrpPurchaseById(erpRelation.getMpId());
                    Integer totalNumber = mrpPurchase.getTotalNumber();
                    // 退货数量大于mrp采购单关联的差的物料总数量
                    if (mrpOutNumber >= totalNumber) {
                        // mrp采购单关联信息
                        mrpPurchase.setLockMatNumber(mrpPurchase.getLockMatNumber() + totalNumber);
                        mrpPurchase.setInitLockMatNumber(mrpPurchase.getInitLockMatNumber() + totalNumber);
                        mrpPurchase.setDelNumber(0);
                        mrpPurchase.setTotalNumber(0);

                        // mrp以及订单明细信息
                        mrp = mrpMapper.selectMrpById(mrpPurchase.getMrpId());
                        orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(), mrp.getOrderId(), mrp.getProductId());
                        orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() + totalNumber);
                        mrp.setLockMatNumber(mrp.getLockMatNumber() + totalNumber);
                        mrp.setTotalNumber(0);
                        mrp.setDelNumber(0);

                        mrpOutNumber = mrpOutNumber - totalNumber;

                        // 退货数量小于mrp采购单关联的差的物料总数量
                    } else {
                        // mrp采购单关联信息
                        mrpPurchase.setLockMatNumber(mrpPurchase.getLockMatNumber() + mrpOutNumber);
                        mrpPurchase.setInitLockMatNumber(mrpPurchase.getInitLockMatNumber() + mrpOutNumber);
                        mrpPurchase.setDelNumber(mrpPurchase.getDelNumber() - mrpOutNumber);
                        mrpPurchase.setTotalNumber(mrpPurchase.getTotalNumber() - mrpOutNumber);

                        // mrp以及订单明细信息
                        mrp = mrpMapper.selectMrpById(mrpPurchase.getMrpId());
                        orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(), mrp.getOrderId(), mrp.getProductId());
                        orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() + mrpOutNumber);
                        mrp.setDelNumber(mrp.getDelNumber() - mrpOutNumber);
                        mrp.setTotalNumber(mrp.getTotalNumber() - mrpOutNumber);
                        mrp.setLockMatNumber(mrp.getLockMatNumber() + mrpOutNumber);
                    }

                    orderDetailsMapper.updateOrderDetails(orderDetails);
                    mrpPurchaseMapper.updateMrpPurchase(mrpPurchase);
                    mrpMapper.updateMrp(mrp);

                }
            }
            // 物料退货明细删除状态更新为已作废
            materielOutStockDetailsMapper.deleteMaterielOutStockDetailsById(outStockDetails.getId());
        }
        return materielOutStockMapper.deleteMaterielOutStockById(id);
    }
}
