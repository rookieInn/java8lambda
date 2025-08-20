package com.example.wechatpay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付配置类
 * 包含所有微信支付所需的配置参数
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat.pay")
public class WeChatPayConfig {
    
    /**
     * 应用ID (公众号ID或小程序ID)
     */
    private String appId;
    
    /**
     * 商户号
     */
    private String mchId;
    
    /**
     * 商户API密钥 (API v2使用)
     */
    private String apiKey;
    
    /**
     * API v3密钥
     */
    private String apiV3Key;
    
    /**
     * 商户证书序列号
     */
    private String mchSerialNo;
    
    /**
     * 商户私钥路径
     */
    private String privateKeyPath;
    
    /**
     * 商户私钥内容 (如果不使用文件路径)
     */
    private String privateKey;
    
    /**
     * 微信支付平台证书路径
     */
    private String wechatPayCertPath;
    
    /**
     * 支付回调地址
     */
    private String notifyUrl;
    
    /**
     * 退款回调地址
     */
    private String refundNotifyUrl;
    
    /**
     * 是否使用沙箱环境
     */
    private boolean sandboxEnabled = false;
    
    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 5000;
    
    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 10000;
}