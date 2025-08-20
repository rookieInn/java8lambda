package com.extrigger.wechatpay;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信退款请求参数类
 * 用于申请退款接口
 */
public class WeChatRefundRequest {
    
    // 应用ID
    private String appId;
    
    // 商户号
    private String mchId;
    
    // 随机字符串
    private String nonceStr;
    
    // 签名
    private String sign;
    
    // 签名类型
    private String signType = "MD5";
    
    // 微信订单号
    private String transactionId;
    
    // 商户订单号
    private String outTradeNo;
    
    // 商户退款单号
    private String outRefundNo;
    
    // 订单金额（分）
    private Integer totalFee;
    
    // 退款金额（分）
    private Integer refundFee;
    
    // 货币类型
    private String refundFeeType = "CNY";
    
    // 退款原因
    private String refundDesc;
    
    // 退款资金来源
    private String refundAccount;
    
    // 退款通知地址
    private String notifyUrl;
    
    public WeChatRefundRequest() {}
    
    public WeChatRefundRequest(String outRefundNo, Integer totalFee, Integer refundFee) {
        this.outRefundNo = outRefundNo;
        this.totalFee = totalFee;
        this.refundFee = refundFee;
    }
    
    /**
     * 转换为Map，用于签名和请求
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        
        if (appId != null) map.put("appid", appId);
        if (mchId != null) map.put("mch_id", mchId);
        if (nonceStr != null) map.put("nonce_str", nonceStr);
        if (sign != null) map.put("sign", sign);
        if (signType != null) map.put("sign_type", signType);
        if (transactionId != null) map.put("transaction_id", transactionId);
        if (outTradeNo != null) map.put("out_trade_no", outTradeNo);
        if (outRefundNo != null) map.put("out_refund_no", outRefundNo);
        if (totalFee != null) map.put("total_fee", totalFee.toString());
        if (refundFee != null) map.put("refund_fee", refundFee.toString());
        if (refundFeeType != null) map.put("refund_fee_type", refundFeeType);
        if (refundDesc != null) map.put("refund_desc", refundDesc);
        if (refundAccount != null) map.put("refund_account", refundAccount);
        if (notifyUrl != null) map.put("notify_url", notifyUrl);
        
        return map;
    }
    
    // Getters and Setters
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getMchId() {
        return mchId;
    }
    
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    
    public String getNonceStr() {
        return nonceStr;
    }
    
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
    
    public String getSign() {
        return sign;
    }
    
    public void setSign(String sign) {
        this.sign = sign;
    }
    
    public String getSignType() {
        return signType;
    }
    
    public void setSignType(String signType) {
        this.signType = signType;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getOutTradeNo() {
        return outTradeNo;
    }
    
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
    
    public String getOutRefundNo() {
        return outRefundNo;
    }
    
    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }
    
    public Integer getTotalFee() {
        return totalFee;
    }
    
    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }
    
    public Integer getRefundFee() {
        return refundFee;
    }
    
    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }
    
    public String getRefundFeeType() {
        return refundFeeType;
    }
    
    public void setRefundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType;
    }
    
    public String getRefundDesc() {
        return refundDesc;
    }
    
    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }
    
    public String getRefundAccount() {
        return refundAccount;
    }
    
    public void setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount;
    }
    
    public String getNotifyUrl() {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}