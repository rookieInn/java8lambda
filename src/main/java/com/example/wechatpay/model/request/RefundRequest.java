package com.example.wechatpay.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 退款请求参数
 */
@Data
@Builder
public class RefundRequest {
    
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
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    
    /**
     * 退款原因
     */
    private String reason;
    
    /**
     * 退款结果通知url
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;
    
    /**
     * 退款资金来源
     */
    @JSONField(name = "funds_account")
    private String fundsAccount;
    
    /**
     * 金额信息
     */
    private RefundAmount amount;
    
    /**
     * 退款商品
     */
    @JSONField(name = "goods_detail")
    private List<RefundGoodsDetail> goodsDetail;
    
    @Data
    @Builder
    public static class RefundAmount {
        /**
         * 退款金额，单位为分
         */
        private Integer refund;
        
        /**
         * 退款出资账户及金额
         */
        private List<RefundFrom> from;
        
        /**
         * 原订单金额，单位为分
         */
        private Integer total;
        
        /**
         * 退款币种
         */
        private String currency = "CNY";
    }
    
    @Data
    @Builder
    public static class RefundFrom {
        /**
         * 出资账户类型
         * 枚举值：
         * AVAILABLE : 可用余额
         * UNAVAILABLE : 不可用余额
         */
        private String account;
        
        /**
         * 出资金额，单位为分
         */
        private Integer amount;
    }
    
    @Data
    @Builder
    public static class RefundGoodsDetail {
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