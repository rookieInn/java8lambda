package com.example.wechatpay.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 退款响应
 */
@Data
public class RefundResponse {
    
    /**
     * 微信支付退款单号
     */
    @JSONField(name = "refund_id")
    private String refundId;
    
    /**
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    
    /**
     * 微信支付订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;
    
    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    
    /**
     * 退款渠道
     */
    private String channel;
    
    /**
     * 退款入账账户
     */
    @JSONField(name = "user_received_account")
    private String userReceivedAccount;
    
    /**
     * 退款成功时间
     */
    @JSONField(name = "success_time")
    private Date successTime;
    
    /**
     * 退款创建时间
     */
    @JSONField(name = "create_time")
    private Date createTime;
    
    /**
     * 退款状态
     */
    private String status;
    
    /**
     * 资金账户
     */
    @JSONField(name = "funds_account")
    private String fundsAccount;
    
    /**
     * 金额信息
     */
    private Amount amount;
    
    /**
     * 优惠退款信息
     */
    @JSONField(name = "promotion_detail")
    private List<PromotionDetail> promotionDetail;
    
    @Data
    public static class Amount {
        /**
         * 订单金额，单位为分
         */
        private Integer total;
        
        /**
         * 退款金额，单位为分
         */
        private Integer refund;
        
        /**
         * 退款出资账户及金额
         */
        private List<From> from;
        
        /**
         * 用户支付金额，单位为分
         */
        @JSONField(name = "payer_total")
        private Integer payerTotal;
        
        /**
         * 用户退款金额，单位为分
         */
        @JSONField(name = "payer_refund")
        private Integer payerRefund;
        
        /**
         * 应结退款金额，单位为分
         */
        @JSONField(name = "settlement_refund")
        private Integer settlementRefund;
        
        /**
         * 应结订单金额，单位为分
         */
        @JSONField(name = "settlement_total")
        private Integer settlementTotal;
        
        /**
         * 优惠退款金额，单位为分
         */
        @JSONField(name = "discount_refund")
        private Integer discountRefund;
        
        /**
         * 退款币种
         */
        private String currency;
    }
    
    @Data
    public static class From {
        /**
         * 出资账户类型
         */
        private String account;
        
        /**
         * 出资金额，单位为分
         */
        private Integer amount;
    }
    
    @Data
    public static class PromotionDetail {
        /**
         * 券ID
         */
        @JSONField(name = "promotion_id")
        private String promotionId;
        
        /**
         * 优惠范围
         */
        private String scope;
        
        /**
         * 优惠类型
         */
        private String type;
        
        /**
         * 优惠券面额，单位为分
         */
        private Integer amount;
        
        /**
         * 优惠退款金额，单位为分
         */
        @JSONField(name = "refund_amount")
        private Integer refundAmount;
        
        /**
         * 商品列表
         */
        @JSONField(name = "goods_detail")
        private List<GoodsDetail> goodsDetail;
    }
    
    @Data
    public static class GoodsDetail {
        /**
         * 商户侧商品编码
         */
        @JSONField(name = "merchant_goods_id")
        private String merchantGoodsId;
        
        /**
         * 微信侧商品编码
         */
        @JSONField(name = "wechatpay_goods_id")
        private String wechatpayGoodsId;
        
        /**
         * 商品名称
         */
        @JSONField(name = "goods_name")
        private String goodsName;
        
        /**
         * 商品单价，单位为分
         */
        @JSONField(name = "unit_price")
        private Integer unitPrice;
        
        /**
         * 商品退款金额，单位为分
         */
        @JSONField(name = "refund_amount")
        private Integer refundAmount;
        
        /**
         * 商品退货数量
         */
        @JSONField(name = "refund_quantity")
        private Integer refundQuantity;
    }
}