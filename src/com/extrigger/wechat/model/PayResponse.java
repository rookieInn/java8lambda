package com.extrigger.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信支付响应模型
 * @author extrigger
 */
public class PayResponse {
    
    /** 预支付交易会话标识 */
    @JsonProperty("prepay_id")
    private String prepayId;
    
    /** 错误码 */
    private String code;
    
    /** 错误信息 */
    private String message;
    
    /** 错误详情 */
    private String detail;
    
    public String getPrepayId() {
        return prepayId;
    }
    
    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
}