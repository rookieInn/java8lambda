package com.extrigger.wechatpay;

/**
 * 微信支付配置类
 * 包含微信支付所需的所有配置参数
 */
public class WeChatPayConfig {
    
    // 微信支付商户号
    private String mchId;
    
    // 微信支付商户密钥
    private String mchKey;
    
    // 微信支付AppId
    private String appId;
    
    // 微信支付证书路径
    private String certPath;
    
    // 微信支付API地址
    private String apiUrl = "https://api.mch.weixin.qq.com";
    
    // 支付通知回调地址
    private String notifyUrl;
    
    // 退款通知回调地址
    private String refundNotifyUrl;
    
    // 是否使用沙箱环境
    private boolean sandbox = false;
    
    public WeChatPayConfig() {}
    
    public WeChatPayConfig(String mchId, String mchKey, String appId) {
        this.mchId = mchId;
        this.mchKey = mchKey;
        this.appId = appId;
    }
    
    // Getters and Setters
    public String getMchId() {
        return mchId;
    }
    
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    
    public String getMchKey() {
        return mchKey;
    }
    
    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getCertPath() {
        return certPath;
    }
    
    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
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
    
    public boolean isSandbox() {
        return sandbox;
    }
    
    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }
}