package com.ruoyi.project.erp.materielIntoStock.service;

import com.ruoyi.common.constant.MrpConstants;
import com.ruoyi.common.constant.StockConstants;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.erpRelation.domain.ErpRelation;
import com.ruoyi.project.erp.erpRelation.mapper.ErpRelationMapper;
import com.ruoyi.project.erp.materielIntoStock.domain.MaterielIntoStock;
import com.ruoyi.project.erp.materielIntoStock.mapper.MaterielIntoStockMapper;
import com.ruoyi.project.erp.materielIntoStockDetails.domain.MaterielIntoStockDetails;
import com.ruoyi.project.erp.materielIntoStockDetails.mapper.MaterielIntoStockDetailsMapper;
import com.ruoyi.project.erp.materielStock.domain.MaterielStock;
import com.ruoyi.project.erp.materielStock.mapper.MaterielStockMapper;
import com.ruoyi.project.erp.materielStockIqc.domain.MaterielStockIqc;
import com.ruoyi.project.erp.materielStockIqc.mapper.MaterielStockIqcMapper;
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
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 物料入库 服务层实现
 *
 * @author zqm
 * @date 2019-04-30
 */
@Service
public class MaterielIntoStockServiceImpl implements IMaterielIntoStockService {
    @Autowired
    private MaterielIntoStockMapper materielIntoStockMapper;

    @Autowired
    private MaterielIntoStockDetailsMapper materielIntoStockDetailsMapper;

    @Autowired
    private MaterielStockMapper materielStockMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseDetailsMapper purchaseDetailsMapper;

    @Autowired
    private MaterielSupplierMapper materielSupplierMapper;

    @Autowired
    private MaterielStockIqcMapper materielStockIqcMapper;

    @Autowired
    private MrpPurchaseMapper mrpPurchaseMapper;

    @Autowired
    private MrpMapper mrpMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private ErpRelationMapper erpRelationMapper;

    /**
     * 查询物料入库信息
     *
     * @param id 物料入库ID
     * @return 物料入库信息
     */
    @Override
    public MaterielIntoStock selectMaterielIntoStockById(Integer id) {
        MaterielIntoStock materielIntoStock = materielIntoStockMapper.selectMaterielIntoStockById(id);
        //  查询物料入库明细
        MaterielIntoStockDetails materielIntoStockDetails = new MaterielIntoStockDetails();
        materielIntoStockDetails.setIntoId(id);
        List<MaterielIntoStockDetails> materielIntoStockDetailsList = materielIntoStockDetailsMapper.selectMaterielIntoStockDetailsList(materielIntoStockDetails);
        for (MaterielIntoStockDetails intoStockDetails : materielIntoStockDetailsList) {
            if (!StringUtils.isEmpty(intoStockDetails.getPurchaseCode()) && !"-1".equals(intoStockDetails.getPurchaseCode())) {
                // 获取采购单明细
                PurchaseDetails purchaseDetails = purchaseDetailsMapper.selectPurchaseDetailsByCode(null, intoStockDetails.getPurchaseCode(),
                        intoStockDetails.getSupplierCode(), intoStockDetails.getMaterielCode());
                intoStockDetails.setPurchaseDetails(purchaseDetails);
            }
        }
        materielIntoStock.setMaterielIntoStockDetails(materielIntoStockDetailsList);
        return materielIntoStock;
    }

    /**
     * 查询物料入库列表
     *
     * @param materielIntoStock 物料入库信息
     * @return 物料入库集合
     */
    @Override
    public List<MaterielIntoStock> selectMaterielIntoStockList(MaterielIntoStock materielIntoStock, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            return Collections.emptyList();
        }
        materielIntoStock.setCompanyId(user.getCompanyId());
        return materielIntoStockMapper.selectMaterielIntoStockList(materielIntoStock);
    }

    /**
     * 新增物料入库
     *
     * @param materielIntoStock 物料入库信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertMaterielIntoStock(MaterielIntoStock materielIntoStock, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) return 0;
        /**
         * 物料入库
         */
        String matIntoStockCode = CodeUtils.getMatIntoStockCode();
        materielIntoStock.setIntoCode(matIntoStockCode);
        materielIntoStock.setCompanyId(user.getCompanyId());
        materielIntoStock.setCreateId(user.getUserId().intValue());
        materielIntoStock.setCreateTime(new Date());
        materielIntoStock.setCreateName(user.getUserName());
        int i = materielIntoStockMapper.insertMaterielIntoStock(materielIntoStock);
        if (!StringUtils.isEmpty(materielIntoStock.getMaterielIntoStockDetails())) {
            BigDecimal intoNumber = null;
            List<MrpPurchase> mrpPurchaseList = null;
            Mrp mrp = null;
            OrderDetails orderDetails = null;
            ErpRelation erpRelation = null;
            List<MaterielIntoStockDetails> materielIntoStockDetails = materielIntoStock.getMaterielIntoStockDetails();
            for (MaterielIntoStockDetails materielIntoStockDetail : materielIntoStockDetails) {
                /**
                 * 物料入库详情
                 */
                // 查询物料对应供应商价格信息
                List<MaterielSupplier> materielSuppliers = materielSupplierMapper.selectMaterielSupplierListByMatIdAndSupId(materielIntoStockDetail.getMaterielId(), materielIntoStock.getSupplierId());
                // 物料供应商关联信息
                if (!StringUtils.isEmpty(materielSuppliers)) {
                    MaterielSupplier materielSupplier = materielSuppliers.get(0);
                    intoNumber = new BigDecimal(materielIntoStockDetail.getIntoNumber());
                    materielIntoStockDetail.setPrice(materielSupplier.getSupplierPrice());
                    materielIntoStockDetail.setTotalPrice(materielSupplier.getSupplierPrice().multiply(intoNumber));
                }
                materielIntoStockDetail.setIntoId(materielIntoStock.getId());
                materielIntoStockDetail.setIntoCode(matIntoStockCode);
                materielIntoStockDetail.setCreateTime(new Date());

                materielIntoStockDetailsMapper.insertMaterielIntoStockDetails(materielIntoStockDetail);

                MaterielStockIqc materielStockIqc = materielStockIqcMapper.selectMaterielStockIqcByComId(user.getCompanyId());
                // 物料库存记录
                MaterielStock materielStock = materielStockMapper.selectMaterielStockByMatCodeAndComId(materielIntoStockDetail.getMaterielCode(), user.getCompanyId());
                /**
                 * 库存操作
                 * 判断公司IQC的开启状态，开启，物料全部录入临时仓库
                 */
                // 开启物料IQC
                if (!StringUtils.isNull(materielStockIqc) && StockConstants.IQC_YES.equals(materielStockIqc.getStockIqc())) {
                    /**
                     * 物料录入到临时仓库
                     */
                    materielStock.setTotalNumber(materielStock.getTotalNumber() + materielIntoStockDetail.getIntoNumber());
                    materielStock.setTemporaryNumber(materielStock.getTemporaryNumber() + materielIntoStockDetail.getIntoNumber());
                    /**
                     * 采购单明细预收仓库新增数量
                     * 采购单总数不变
                     */
                    PurchaseDetails purchaseDetails = purchaseDetailsMapper.selectPurchaseDetailsByCode(null, materielIntoStockDetail.getPurchaseCode(),
                            materielIntoStockDetail.getSupplierCode(), materielIntoStockDetail.getMaterielCode());
                    purchaseDetails.setPrereceiveNumber(purchaseDetails.getPrereceiveNumber() + materielIntoStockDetail.getIntoNumber());
                    purchaseDetailsMapper.updatePurchaseDetails(purchaseDetails);
                    // 没有开启IQC，物料直接录入到良品仓库
                } else {
                    // 获取采购单号
                    String purchaseCode = materielIntoStockDetail.getPurchaseCode();
                    // 选择了采购单
                    if (!StringUtils.isNull(purchaseCode) && !purchaseCode.equals("-1")) {
                        /**
                         * 采购单增加总数
                         */
                        // 获取采购单信息
                        Purchase purchase = purchaseMapper.selectPurchaseBySupIdAndPuraseCode(user.getCompanyId(), materielIntoStock.getSupplierId(), purchaseCode);
                        purchase.setDeliverTotalNum(purchase.getDeliverTotalNum() + materielIntoStockDetail.getIntoNumber());
                        purchaseMapper.updatePurchase(purchase);
                        /**
                         * 采购单详情增加已采购数量
                         */
                        PurchaseDetails purchaseDetails = purchaseDetailsMapper.selectPurchaseDetailsByCode(purchase.getId(), purchase.getPurchaseCode(),
                                materielIntoStockDetail.getSupplierCode(), materielIntoStockDetail.getMaterielCode());
                        purchaseDetails.setDeliverNum(purchaseDetails.getDeliverNum() + materielIntoStockDetail.getIntoNumber());
                        purchaseDetailsMapper.updatePurchaseDetails(purchaseDetails);

                        /**
                         * MRP相关数据操作
                         */
                        // 查询mrp采购单关联信息(mrp还差总数大于0)
                        mrpPurchaseList = mrpPurchaseMapper.selectMrpPurchaseByPurIdAndMatCodeGtNum(purchase.getId(), materielIntoStockDetail.getMaterielCode());
                        // mrp判断依据入库数量，递减
                        Integer mrpNumber = materielIntoStockDetail.getIntoNumber();
                        for (MrpPurchase mrpPurchase : mrpPurchaseList) {
                            if (mrpNumber <= 0) {
                                break;
                            }

                            // 新增erp出入库关联记录
                            erpRelation = new ErpRelation();
                            erpRelation.setMpId(mrpPurchase.getId());
                            erpRelation.setMatDetailId(materielIntoStockDetail.getId());
                            // 物料入库类型
                            erpRelation.setReStatus(MrpConstants.MAT_INTOSTOCK);
                            erpRelationMapper.insertErpRelation(erpRelation);

                            // 差的总数大于本次入库数量
                            if (mrpPurchase.getTotalNumber() >= mrpNumber) {
                                // mrp采购单关联表
                                mrpPurchase.setTotalNumber(mrpPurchase.getTotalNumber() - mrpNumber);
                                mrpPurchase.setDelNumber(mrpPurchase.getDelNumber() - mrpNumber <= 0 ? 0 : mrpPurchase.getDelNumber() - mrpNumber);
                                mrpPurchase.setLockMatNumber(mrpPurchase.getLockMatNumber() + mrpNumber);
                                mrpPurchase.setInitLockMatNumber(mrpPurchase.getInitLockMatNumber() + mrpNumber);
                                // mrp更新
                                mrp = mrpMapper.selectMrpById(mrpPurchase.getMrpId());
                                mrp.setDelNumber(mrp.getDelNumber() - mrpNumber <= 0 ? 0 : mrp.getDelNumber() - mrpNumber);
                                mrp.setTotalNumber(mrp.getTotalNumber() - mrpNumber);
                                mrp.setLockMatNumber(mrp.getLockMatNumber() + mrpNumber);
                                // 订单操作
                                orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(), mrp.getOrderId(), mrp.getProductId());
                                orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() + mrpNumber);
                                mrpNumber = 0;

                                // 差的总数小于本次入库数量
                            } else {
                                // mrp采购单关联表
                                mrpNumber = (mrpNumber - (mrpPurchase.getTotalNumber()));
                                mrpPurchase.setLockMatNumber(mrpPurchase.getLockMatNumber() + mrpPurchase.getDelNumber() + mrpPurchase.getTiaoNumber());
                                mrpPurchase.setInitLockMatNumber(mrpPurchase.getInitLockMatNumber() + mrpPurchase.getTotalNumber());
                                mrpPurchase.setTotalNumber(0);
                                mrpPurchase.setDelNumber(0);
                                // mrp更新
                                mrp = mrpMapper.selectMrpById(mrpPurchase.getMrpId());
                                // 订单操作
                                orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(), mrp.getOrderId(), mrp.getProductId());
                                orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() + mrp.getTotalNumber());
                                mrp.setLockMatNumber(mrp.getLockMatNumber() + mrp.getDelNumber() + mrp.getTiaoNumber());
                                mrp.setTotalNumber(0);
                                mrp.setDelNumber(0);
                            }
                            orderDetailsMapper.updateOrderDetails(orderDetails);
                            mrpPurchaseMapper.updateMrpPurchase(mrpPurchase);
                            mrpMapper.updateMrp(mrp);
                        }
                        materielStock.setTotalNumber(materielStock.getTotalNumber() + materielIntoStockDetail.getIntoNumber());
                        materielStock.setGoodNumber(materielStock.getGoodNumber() + mrpNumber);
                        materielStock.setLockNumber(materielStock.getLockNumber() + (materielIntoStockDetail.getIntoNumber() - mrpNumber));
                        // 未选择采购单直接操作物料库存
                    } else {
                        materielStock.setTotalNumber(materielStock.getTotalNumber() + materielIntoStockDetail.getIntoNumber());
                        materielStock.setGoodNumber(materielStock.getGoodNumber() + materielIntoStockDetail.getIntoNumber());
                    }
                }
                materielStock.setLastUpdate(new Date());
                materielStockMapper.updateMaterielStock(materielStock);
            }
        }
        return i;
    }

    /**
     * 修改物料入库
     *
     * @param materielIntoStock 物料入库信息
     * @return 结果
     */
    @Override
    public int updateMaterielIntoStock(MaterielIntoStock materielIntoStock) {
        return materielIntoStockMapper.updateMaterielIntoStock(materielIntoStock);
    }

    /**
     * 删除物料入库对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteMaterielIntoStockByIds(String ids) {
        return materielIntoStockMapper.deleteMaterielIntoStockByIds(Convert.toStrArray(ids));
    }

    /**
     * 作废物料入库单
     *
     * @param id 物料入库id
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int nullifyMaterielIntoStockByIds(Integer id, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            return 0;
        }
        MaterielIntoStock materielIntoStock = materielIntoStockMapper.selectMaterielIntoStockById(id);
        // 采购单数据回滚
        // 查询作废入库单详情信息
        MaterielIntoStockDetails materielIntoStockDetails = new MaterielIntoStockDetails();
        materielIntoStockDetails.setIntoId(id);
        List<MaterielIntoStockDetails> materielIntoStockDetailsList = materielIntoStockDetailsMapper.selectMaterielIntoStockDetailsList(materielIntoStockDetails);
        // 物料iqc状态
        MaterielStockIqc materielStockIqc = materielStockIqcMapper.selectMaterielStockIqcByComId(user.getCompanyId());
        MaterielStock materielStock = null;
        MrpPurchase mrpPurchase = null;
        ErpRelation erpRelation = null;
        Mrp mrp = null;
        OrderDetails orderDetails = null;
        for (MaterielIntoStockDetails intoStockDetails : materielIntoStockDetailsList) {
            // 库存数据回滚
            materielStock = materielStockMapper.selectMaterielStockByMatCodeAndComId(intoStockDetails.getMaterielCode(), user.getCompanyId());
            /**
             * 开启iqc的情况，库存回滚临时仓库数量
             */
            if (!StringUtils.isNull(materielStockIqc) && StockConstants.IQC_YES.equals(materielStockIqc.getStockIqc())) {
                materielStock.setTemporaryNumber(materielStock.getTemporaryNumber() - intoStockDetails.getIntoNumber());
                /**
                 * 采购单明细数据回滚，回滚预收仓库数量
                 */
                // 采购单明细数据回滚
                PurchaseDetails purchaseDetails = purchaseDetailsMapper.selectPurchaseDetailsByCode(null, intoStockDetails.getPurchaseCode(),
                        intoStockDetails.getSupplierCode(), intoStockDetails.getMaterielCode());
                purchaseDetails.setPrereceiveNumber(purchaseDetails.getPrereceiveNumber() - intoStockDetails.getIntoNumber());
                purchaseDetailsMapper.updatePurchaseDetails(purchaseDetails);
                /**
                 * 未开启IQC物料检验
                 */
            } else {
                // 选择了采购单入库
                if (!StringUtils.isEmpty(intoStockDetails.getPurchaseCode()) && !"-1".equals(intoStockDetails.getPurchaseCode())) {
                    // 采购单数据回滚
                    Purchase purchase = purchaseMapper.selectPurchaseBySupIdAndPuraseCode(user.getCompanyId(), materielIntoStock.getSupplierId(), intoStockDetails.getPurchaseCode());
                    purchase.setDeliverTotalNum(purchase.getDeliverTotalNum() - intoStockDetails.getIntoNumber());
                    purchaseMapper.updatePurchase(purchase);

                    // 采购单明细数据回滚
                    PurchaseDetails purchaseDetails = purchaseDetailsMapper.selectPurchaseDetailsByCode(purchase.getId(), purchase.getPurchaseCode(),
                            intoStockDetails.getSupplierCode(), intoStockDetails.getMaterielCode());
                    purchaseDetails.setDeliverNum(purchaseDetails.getDeliverNum() - intoStockDetails.getIntoNumber());
                    purchaseDetailsMapper.updatePurchaseDetails(purchaseDetails);
                    /**
                     * MRP关联操作
                     */
                    erpRelation = erpRelationMapper.selectErpRelationByMatDetailId(intoStockDetails.getId(), MrpConstants.MAT_INTOSTOCK);
                    Integer mrpNumber = intoStockDetails.getIntoNumber();
                    if (StringUtils.isNotNull(erpRelation)) {
                        mrpPurchase = mrpPurchaseMapper.selectMrpPurchaseById(erpRelation.getMpId());
                        // 实际库存锁定数量
                        int realNumber = mrpPurchase.getInitLockMatNumber();
                        // 作废入库单的数量小于实际库存锁定数量
                        if (mrpNumber <= realNumber) {
                            // mrp采购单关联
                            mrpPurchase.setTotalNumber(mrpPurchase.getTotalNumber() + mrpNumber);
                            mrpPurchase.setDelNumber(mrpPurchase.getDelNumber() + mrpNumber);
                            mrpPurchase.setLockMatNumber(mrpPurchase.getLockMatNumber() - mrpNumber);
                            mrpPurchase.setInitLockMatNumber(realNumber - mrpNumber);
                            // mrp表记录
                            mrp = mrpMapper.selectMrpById(mrpPurchase.getMrpId());
                            // 订单记录
                            orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(), mrp.getOrderId(), mrp.getProductId());
                            orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() - mrpNumber);

                            mrp.setTotalNumber(mrp.getTotalNumber() + mrpNumber);
                            mrp.setDelNumber(mrp.getDelNumber() + mrpNumber);
                            mrp.setLockMatNumber(mrp.getLockMatNumber() - mrpNumber);

                            mrpNumber = 0;

                            // 作废入库单的订单数量大于实际库存锁定数量
                        } else {
                            // mrp表记录
                            mrp = mrpMapper.selectMrpById(mrpPurchase.getMrpId());
                            // 订单记录
                            orderDetails = orderDetailsMapper.selectOrderDetailsListByOIdAndPid(user.getCompanyId(), mrp.getOrderId(), mrp.getProductId());
                            orderDetails.setLockMatNumber(orderDetails.getLockMatNumber() - realNumber);

                            mrp.setLockMatNumber(mrp.getLockMatNumber() - realNumber);
                            mrp.setDelNumber((mrp.getDelNumber() + (mrp.getTotalNumber() + realNumber)) - mrp.getTiaoNumber());
                            mrp.setTotalNumber(mrp.getTotalNumber() + realNumber);
                            // mrp采购单关联
                            mrpPurchase.setTotalNumber(mrpPurchase.getTotalNumber() + realNumber);
                            mrpPurchase.setDelNumber((mrpPurchase.getDelNumber() + (mrpPurchase.getTotalNumber() + realNumber)) - mrpPurchase.getTiaoNumber());
                            mrpPurchase.setLockMatNumber(mrpPurchase.getLockMatNumber() - realNumber);
                            mrpPurchase.setInitLockMatNumber(0);

                            mrpNumber = (mrpNumber - realNumber);
                        }

                        orderDetailsMapper.updateOrderDetails(orderDetails);
                        mrpPurchaseMapper.updateMrpPurchase(mrpPurchase);
                        mrpMapper.updateMrp(mrp);
                        // mrp表记录以及订单明细操作
                    }
                    materielStock.setTotalNumber(materielStock.getTotalNumber() - intoStockDetails.getIntoNumber());
                    materielStock.setGoodNumber(materielStock.getGoodNumber() - mrpNumber);
                    materielStock.setLockNumber(materielStock.getLockNumber() - (intoStockDetails.getIntoNumber() - mrpNumber));
                    // 没有选择采购单入库
                } else {
                    materielStock.setTotalNumber(materielStock.getTotalNumber() - intoStockDetails.getIntoNumber());
                    materielStock.setGoodNumber(materielStock.getGoodNumber() - intoStockDetails.getIntoNumber());
                }
            }
            materielStock.setLastUpdate(new Date());
            materielStockMapper.updateMaterielStock(materielStock);

            // 物料入库明细删除状态更新
            materielIntoStockDetailsMapper.deleteMaterielIntoStockDetailsById(intoStockDetails.getId());
        }
        return materielIntoStockMapper.deleteMaterielIntoStockById(id);
    }
}
