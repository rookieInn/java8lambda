package com.extrigger.wechat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置类
 * @author extrigger
 */
@Component
@ConfigurationProperties(prefix = "wechat.pay")
public class WeChatPayConfig {
    
    /** 应用ID */
    private String appId;
    
    /** 商户号 */
    private String mchId;
    
    /** 商户API密钥 */
    private String apiKey;
    
    /** 商户证书序列号 */
    private String serialNo;
    
    /** 商户私钥文件路径 */
    private String privateKeyPath;
    
    /** 微信支付平台证书文件路径 */
    private String certificatePath;
    
    /** 支付回调通知URL */
    private String notifyUrl;
    
    /** 退款回调通知URL */
    private String refundNotifyUrl;
    
    /** 统一下单URL */
    private String unifiedOrderUrl = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
    
    /** 查询订单URL */
    private String queryOrderUrl = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no";
    
    /** 申请退款URL */
    private String refundUrl = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";
    
    /** 查询退款URL */
    private String queryRefundUrl = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";
    
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
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getSerialNo() {
        return serialNo;
    }
    
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    
    public String getPrivateKeyPath() {
        return privateKeyPath;
    }
    
    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }
    
    public String getCertificatePath() {
        return certificatePath;
    }
    
    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }
    
    public String getNotifyUrl() {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    
    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }
    
    public void setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
    }
    
    public String getUnifiedOrderUrl() {
        return unifiedOrderUrl;
    }
    
    public void setUnifiedOrderUrl(String unifiedOrderUrl) {
        this.unifiedOrderUrl = unifiedOrderUrl;
    }
    
    public String getQueryOrderUrl() {
        return queryOrderUrl;
    }
    
    public void setQueryOrderUrl(String queryOrderUrl) {
        this.queryOrderUrl = queryOrderUrl;
    }
    
    public String getRefundUrl() {
        return refundUrl;
    }
    
    public void setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
    }
    
    public String getQueryRefundUrl() {
        return queryRefundUrl;
    }
    
    public void setQueryRefundUrl(String queryRefundUrl) {
        this.queryRefundUrl = queryRefundUrl;
    }
}