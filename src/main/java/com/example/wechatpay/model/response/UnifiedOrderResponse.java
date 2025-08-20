package com.example.wechatpay.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 统一下单响应
 */
@Data
public class UnifiedOrderResponse {
    
    /**
     * 预支付交易会话标识 (JSAPI/小程序支付)
     */
    @JSONField(name = "prepay_id")
    private String prepayId;
    
    /**
     * 二维码链接 (Native支付)
     */
    @JSONField(name = "code_url")
    private String codeUrl;
    
    /**
     * 支付跳转链接 (H5支付)
     */
    @JSONField(name = "h5_url")
    private String h5Url;
}