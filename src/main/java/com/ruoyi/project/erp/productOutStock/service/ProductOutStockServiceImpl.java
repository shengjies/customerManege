package com.ruoyi.project.erp.productOutStock.service;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.erp.orderDetails.domain.OrderDetails;
import com.ruoyi.project.erp.orderDetails.mapper.OrderDetailsMapper;
import com.ruoyi.project.erp.orderInfo.domain.OrderInfo;
import com.ruoyi.project.erp.orderInfo.mapper.OrderInfoMapper;
import com.ruoyi.project.erp.productCustomer.domain.ProductCustomer;
import com.ruoyi.project.erp.productCustomer.mapper.ProductCustomerMapper;
import com.ruoyi.project.erp.productOutStock.domain.ProductOutStock;
import com.ruoyi.project.erp.productOutStock.mapper.ProductOutStockMapper;
import com.ruoyi.project.erp.productOutStockDetails.domain.ProductOutStockDetails;
import com.ruoyi.project.erp.productOutStockDetails.mapper.ProductOutStockDetailsMapper;
import com.ruoyi.project.erp.productStock.domain.ProductStock;
import com.ruoyi.project.erp.productStock.mapper.ProductStockMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 产品出库 服务层实现
 *
 * @author zqm
 * @date 2019-04-30
 */
@Service
public class ProductOutStockServiceImpl implements IProductOutStockService {
    @Autowired
    private ProductOutStockMapper productOutStockMapper;

    @Autowired
    private ProductOutStockDetailsMapper productOutStockDetailsMapper;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ProductCustomerMapper productCustomerMapper;

    /**
     * 查询产品出库信息
     *
     * @param id 产品出库ID
     * @return 产品出库信息
     */
    @Override
    public ProductOutStock selectProductOutStockById(Integer id,HttpServletRequest request) {
        ProductOutStock productOutStock = productOutStockMapper.selectProductOutStockById(id);
        // 出库明细
        List<ProductOutStockDetails> productOutStockDetailsList = productOutStockDetailsMapper.selectProductOutStockDetailsByOutCode(productOutStock.getOutCode());
        for (ProductOutStockDetails productOutStockDetails : productOutStockDetailsList) {
            OrderDetails orderDetails = orderDetailsMapper.selectOrderDetailByCodeAndCusId(JwtUtil.getTokenUser(request).getCompanyId(), productOutStockDetails.getOrderCode(),
                    productOutStock.getCustomerId(), productOutStockDetails.getProductCode());
            productOutStockDetails.setOrderDetails(orderDetails);
        }
        productOutStock.setProductOutStockDetailsList(productOutStockDetailsList);
        return productOutStock;
    }

    /**
     * 查询产品出库列表
     *
     * @param productOutStock 产品出库信息
     * @return 产品出库集合
     */
    @Override
    public List<ProductOutStock> selectProductOutStockList(ProductOutStock productOutStock, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null ) {
            return Collections.emptyList();
        }
        productOutStock.setCompanyId(user.getCompanyId());
        return productOutStockMapper.selectProductOutStockList(productOutStock);
    }

    /**
     * 新增产品出库
     *
     * @param productOutStock 产品出库信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertProductOutStock(ProductOutStock productOutStock,HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            return 0;
        }
        // 自动生产产品出库单号
        String proOutStockCode = CodeUtils.getProOutStockCode();
        /**
         * 产品出库单操作
         */
        productOutStock.setOutCode(proOutStockCode);
        productOutStock.setCompanyId(user.getCompanyId());
        productOutStock.setCreateId(user.getUserId().intValue());
        productOutStock.setCreateName(user.getUserName());
        productOutStock.setCreateTime(new Date());
        int i = productOutStockMapper.insertProductOutStock(productOutStock);

        /**
         * 更新明细、库存、订单、订单明细信息
         */
        BigDecimal bigIntoNumber = null;
        if (!StringUtils.isEmpty(productOutStock.getDetails())) {
            List<ProductOutStockDetails> productOutStockDetails = JSON.parseArray(productOutStock.getDetails(), ProductOutStockDetails.class);
            for (ProductOutStockDetails productOutStockDetail : productOutStockDetails) {
                /**
                 * 产品库存操作
                 */
                // 更新产品库存
                ProductStock productStock = productStockMapper.selectProductStockByProCode(user.getCompanyId(), productOutStockDetail.getProductCode());
                if (StringUtils.isNull(productStock)) {
                    throw new BusinessException("产品" + productOutStockDetail.getProductCode() + "没有库存记录");
                }
                // 产品出库数量
                Integer outNumber = productOutStockDetail.getOutNumber();
                /**
                 * 产品出库出锁定库存数量
                 */
                // 产品良品库存数量
                Integer lockNumber = productStock.getLockNumber();
                if (outNumber > lockNumber) {
                    throw new BusinessException("产品" + productOutStockDetail.getProductCode() + "库存不足");
                }
                // 总库存
                productStock.setTotalNumber(productStock.getTotalNumber() - outNumber);
                productStock.setLockNumber(lockNumber - outNumber);
                productStock.setLastUpdate(new Date());
                // 更新库存
                productStockMapper.updateProductStock(productStock);
                /**
                 * 产品出库清单操作
                 */
                // 查询对应客户对应产品的价格信息
                List<ProductCustomer> productCustomers = productCustomerMapper.selectProductCustomerByProIdOrCusId(productOutStockDetail.getProductId(), productOutStock.getCustomerId());
                // 设置产品出库明细价格
                if (!StringUtils.isEmpty(productCustomers)) {
                    ProductCustomer productCustomer = productCustomers.get(0);
                    bigIntoNumber = new BigDecimal(productOutStockDetail.getOutNumber());
                    // 设置单价
                    productOutStockDetail.setPrice(productCustomer.getCustomerPrice());
                    productOutStockDetail.setTotalPrice(productCustomer.getCustomerPrice().multiply(bigIntoNumber));
                }
                productOutStockDetail.setOutId(productOutStock.getId());
                productOutStockDetail.setOutCode(proOutStockCode);
                productOutStockDetail.setCreateTime(new Date());
                productOutStockDetail.setPaymentStatus(productOutStock.getPaymentStatus());
                productOutStockDetailsMapper.insertProductOutStockDetails(productOutStockDetail);

                /**
                 * 订单明细操作
                 */
                // 是否选择了订单
                // 订单编号
                String orderCode = productOutStockDetail.getOrderCode();
                // 选择了订单
                if (!StringUtils.isEmpty(orderCode) && !"-1".equals(orderCode)) {
                    // 订单信息
                    OrderInfo orderInfo = orderInfoMapper.selectOrderInfoListByOrderCode(user.getCompanyId(),orderCode);
                    if(orderInfo != null){
                        // 更新订单总数
                        orderInfo.setOrderDeliverNum(orderInfo.getOrderDeliverNum() + productOutStockDetail.getOutNumber());
                        // 更新订单锁定总数
                        orderInfo.setLockNumber(orderInfo.getLockNumber() - productOutStockDetail.getOutNumber());
                        // 更新订单
                        orderInfoMapper.updateOrderInfo(orderInfo);
                    }
                    // 客户id
                    Integer customerId = productOutStock.getCustomerId();
                    // 产品编码
                    String productCode = productOutStockDetail.getProductCode();
                    // 查询订单明细
                    OrderDetails orderDetails = orderDetailsMapper.selectOrderDetailByCodeAndCusId(user.getCompanyId(),orderCode,customerId,productCode);
                    // 更新订单明细已交付数量
                    orderDetails.setDeliverNum(orderDetails.getDeliverNum()+productOutStockDetail.getOutNumber());
                    orderDetails.setLockNumber(orderDetails.getLockNumber() - productOutStockDetail.getOutNumber());
                    // 更新订单明细
                    orderDetailsMapper.updateOrderDetails(orderDetails);
                }
            }
        }

        return i;
    }


    /**
     * 修改产品出库
     *
     * @param productOutStock 产品出库信息
     * @return 结果
     */
    @Override
    public int updateProductOutStock(ProductOutStock productOutStock) {
        return productOutStockMapper.updateProductOutStock(productOutStock);
    }

    /**
     * 删除产品出库对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteProductOutStockByIds(String ids) {
        return productOutStockMapper.deleteProductOutStockByIds(Convert.toStrArray(ids));
    }

    /**
     * 作废产品出库对象
     *
     * @param id 产品出库主表id
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int nullifyProductOutStockById(Integer id,HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        if (user == null ) {
             return 0;
        }
        // 出库主表对象
        ProductOutStock productOutStock = productOutStockMapper.selectProductOutStockById(id);
        ProductOutStockDetails productOutStockDetails = new ProductOutStockDetails();
        productOutStockDetails.setOutId(id);
        // 产品出库明细信息
        List<ProductOutStockDetails> productOutStockDetailsList = productOutStockDetailsMapper.selectProductOutStockDetailsList(productOutStockDetails);
        if (!StringUtils.isEmpty(productOutStockDetailsList)) {
            for (ProductOutStockDetails outStockDetails : productOutStockDetailsList) {
                /**
                 * 产品库存数据回滚
                 */
                ProductStock productStock = productStockMapper.selectProductStockByProId(outStockDetails.getProductId());
                productStock.setTotalNumber(productStock.getTotalNumber() + outStockDetails.getOutNumber());
                productStock.setLastUpdate(new Date());
                /**
                 * 订单以及订单明细数据回滚
                 */
                // 订单编号
                String orderCode = outStockDetails.getOrderCode();
                // 选择了订单出库
                if (!StringUtils.isEmpty(orderCode) && !"-1".equals(orderCode)) {
                    // 订单数据回滚
                    OrderInfo orderInfo = orderInfoMapper.selectOrderInfoListByOrderCode(user.getCompanyId(), orderCode);
                    orderInfo.setOrderDeliverNum(orderInfo.getOrderDeliverNum() - outStockDetails.getOutNumber());
                    // 订单主表锁定库存
                    orderInfo.setLockNumber(orderInfo.getLockNumber() + outStockDetails.getOutNumber());
                    orderInfoMapper.updateOrderInfo(orderInfo);
                    // 订单明细数据回滚
                    OrderDetails orderDetails = orderDetailsMapper.selectOrderDetailByCodeAndCusId(user.getCompanyId(),
                            orderCode, productOutStock.getCustomerId(), outStockDetails.getProductCode());
                    orderDetails.setDeliverNum(orderDetails.getDeliverNum() - outStockDetails.getOutNumber());
                    productStock.setLockNumber(productStock.getLockNumber() + outStockDetails.getOutNumber());
                    // 锁定库存
                    orderDetails.setLockNumber(orderDetails.getLockNumber() + outStockDetails.getOutNumber());
                    orderDetailsMapper.updateOrderDetails(orderDetails);
                }
                productStockMapper.updateProductStock(productStock);
                // 产品出库明细删除状态更新为已作废
                productOutStockDetailsMapper.deleteProductOutStockDetailsById(outStockDetails.getId());
            }
        }
        return productOutStockMapper.deleteProductOutStockById(id);
    }

}
