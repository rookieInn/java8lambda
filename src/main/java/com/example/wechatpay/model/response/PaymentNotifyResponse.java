package com.example.wechatpay.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * 支付通知响应
 */
@Data
public class PaymentNotifyResponse {
    
    /**
     * 通知ID
     */
    private String id;
    
    /**
     * 通知创建时间
     */
    @JSONField(name = "create_time")
    private Date createTime;
    
    /**
     * 通知类型
     */
    @JSONField(name = "event_type")
    private String eventType;
    
    /**
     * 通知数据类型
     */
    @JSONField(name = "resource_type")
    private String resourceType;
    
    /**
     * 回调摘要
     */
    private String summary;
    
    /**
     * 通知数据
     */
    private Resource resource;
    
    @Data
    public static class Resource {
        /**
         * 加密算法类型
         */
        private String algorithm;
        
        /**
         * 数据密文
         */
        private String ciphertext;
        
        /**
         * 附加数据
         */
        @JSONField(name = "associated_data")
        private String associatedData;
        
        /**
         * 原始类型
         */
        @JSONField(name = "original_type")
        private String originalType;
        
        /**
         * 随机串
         */
        private String nonce;
    }
    
    /**
     * 解密后的支付结果
     */
    @Data
    public static class DecryptedResource {
        /**
         * 应用ID
         */
        private String appid;
        
        /**
         * 商户号
         */
        private String mchid;
        
        /**
         * 商户订单号
         */
        @JSONField(name = "out_trade_no")
        private String outTradeNo;
        
        /**
         * 微信支付订单号
         */
        @JSONField(name = "transaction_id")
        private String transactionId;
        
        /**
         * 交易类型
         */
        @JSONField(name = "trade_type")
        private String tradeType;
        
        /**
         * 交易状态
         */
        @JSONField(name = "trade_state")
        private String tradeState;
        
        /**
         * 交易状态描述
         */
        @JSONField(name = "trade_state_desc")
        private String tradeStateDesc;
        
        /**
         * 付款银行
         */
        @JSONField(name = "bank_type")
        private String bankType;
        
        /**
         * 附加数据
         */
        private String attach;
        
        /**
         * 支付完成时间
         */
        @JSONField(name = "success_time")
        private Date successTime;
        
        /**
         * 支付者
         */
        private Payer payer;
        
        /**
         * 订单金额
         */
        private Amount amount;
        
        /**
         * 场景信息
         */
        @JSONField(name = "scene_info")
        private SceneInfo sceneInfo;
        
        /**
         * 优惠功能
         */
        @JSONField(name = "promotion_detail")
        private PromotionDetail promotionDetail;
    }
    
    @Data
    public static class Payer {
        /**
         * 用户标识
         */
        private String openid;
    }
    
    @Data
    public static class Amount {
        /**
         * 总金额，单位为分
         */
        private Integer total;
        
        /**
         * 用户支付金额，单位为分
         */
        @JSONField(name = "payer_total")
        private Integer payerTotal;
        
        /**
         * 货币类型
         */
        private String currency;
        
        /**
         * 用户支付币种
         */
        @JSONField(name = "payer_currency")
        private String payerCurrency;
    }
    
    @Data
    public static class SceneInfo {
        /**
         * 商户端设备号
         */
        @JSONField(name = "device_id")
        private String deviceId;
    }
    
    @Data
    public static class PromotionDetail {
        /**
         * 券ID
         */
        @JSONField(name = "coupon_id")
        private String couponId;
        
        /**
         * 优惠名称
         */
        private String name;
        
        /**
         * 优惠范围
         */
        private String scope;
        
        /**
         * 优惠类型
         */
        private String type;
        
        /**
         * 优惠券面额
         */
        private Integer amount;
        
        /**
         * 活动ID
         */
        @JSONField(name = "stock_id")
        private String stockId;
        
        /**
         * 微信出资
         */
        @JSONField(name = "wechatpay_contribute")
        private Integer wechatpayContribute;
        
        /**
         * 商户出资
         */
        @JSONField(name = "merchant_contribute")
        private Integer merchantContribute;
        
        /**
         * 其他出资
         */
        @JSONField(name = "other_contribute")
        private Integer otherContribute;
        
        /**
         * 优惠币种
         */
        private String currency;
    }
}