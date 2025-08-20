package com.example.wechatpay.constant;

/**
 * 微信支付常量类
 */
public class WeChatPayConstants {
    
    /**
     * 微信支付API域名
     */
    public static final String DOMAIN_API = "https://api.mch.weixin.qq.com";
    
    /**
     * 微信支付API v3域名
     */
    public static final String DOMAIN_API_V3 = "https://api.mch.weixin.qq.com/v3";
    
    /**
     * 统一下单接口 (Native支付)
     */
    public static final String NATIVE_PAY_URL = "/pay/transactions/native";
    
    /**
     * 统一下单接口 (JSAPI支付)
     */
    public static final String JSAPI_PAY_URL = "/pay/transactions/jsapi";
    
    /**
     * 统一下单接口 (APP支付)
     */
    public static final String APP_PAY_URL = "/pay/transactions/app";
    
    /**
     * 统一下单接口 (H5支付)
     */
    public static final String H5_PAY_URL = "/pay/transactions/h5";
    
    /**
     * 查询订单接口
     */
    public static final String QUERY_ORDER_BY_ID = "/pay/transactions/id/{transaction_id}";
    public static final String QUERY_ORDER_BY_OUT_TRADE_NO = "/pay/transactions/out-trade-no/{out_trade_no}";
    
    /**
     * 关闭订单接口
     */
    public static final String CLOSE_ORDER_URL = "/pay/transactions/out-trade-no/{out_trade_no}/close";
    
    /**
     * 申请退款接口
     */
    public static final String REFUND_URL = "/refund/domestic/refunds";
    
    /**
     * 查询退款接口
     */
    public static final String QUERY_REFUND_URL = "/refund/domestic/refunds/{out_refund_no}";
    
    /**
     * 交易账单接口
     */
    public static final String TRADE_BILL_URL = "/bill/tradebill";
    
    /**
     * 资金账单接口
     */
    public static final String FUND_FLOW_BILL_URL = "/bill/fundflowbill";
    
    /**
     * 支付成功返回码
     */
    public static final String SUCCESS = "SUCCESS";
    
    /**
     * 支付失败返回码
     */
    public static final String FAIL = "FAIL";
    
    /**
     * 交易类型
     */
    public static class TradeType {
        public static final String JSAPI = "JSAPI";
        public static final String NATIVE = "NATIVE";
        public static final String APP = "APP";
        public static final String MWEB = "MWEB";
    }
    
    /**
     * 交易状态
     */
    public static class TradeState {
        public static final String SUCCESS = "SUCCESS";           // 支付成功
        public static final String REFUND = "REFUND";             // 转入退款
        public static final String NOTPAY = "NOTPAY";             // 未支付
        public static final String CLOSED = "CLOSED";             // 已关闭
        public static final String REVOKED = "REVOKED";           // 已撤销（付款码支付）
        public static final String USERPAYING = "USERPAYING";     // 用户支付中（付款码支付）
        public static final String PAYERROR = "PAYERROR";         // 支付失败
    }
    
    /**
     * 退款状态
     */
    public static class RefundStatus {
        public static final String SUCCESS = "SUCCESS";           // 退款成功
        public static final String CLOSED = "CLOSED";             // 退款关闭
        public static final String PROCESSING = "PROCESSING";     // 退款处理中
        public static final String ABNORMAL = "ABNORMAL";         // 退款异常
    }
    
    /**
     * 签名类型
     */
    public static class SignType {
        public static final String MD5 = "MD5";
        public static final String HMAC_SHA256 = "HMAC-SHA256";
        public static final String RSA = "RSA";
    }
}