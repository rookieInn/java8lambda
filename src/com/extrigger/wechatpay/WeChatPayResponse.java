package com.extrigger.wechatpay;

import java.util.Map;

/**
 * 微信支付响应类
 * 统一下单接口的返回结果
 */
public class WeChatPayResponse {
    
    // 返回状态码
    private String returnCode;
    
    // 返回信息
    private String returnMsg;
    
    // 应用ID
    private String appId;
    
    // 商户号
    private String mchId;
    
    // 设备号
    private String deviceInfo;
    
    // 随机字符串
    private String nonceStr;
    
    // 签名
    private String sign;
    
    // 业务结果
    private String resultCode;
    
    // 错误代码
    private String errCode;
    
    // 错误代码描述
    private String errCodeDes;
    
    // 交易类型
    private String tradeType;
    
    // 预支付交易会话标识
    private String prepayId;
    
    // 二维码链接
    private String codeUrl;
    
    // 时间戳
    private String timeStamp;
    
    // 随机字符串（用于支付）
    private String nonceStrPay;
    
    // 签名（用于支付）
    private String paySign;
    
    // 签名类型
    private String signType;
    
    public WeChatPayResponse() {}
    
    /**
     * 从Map构造响应对象
     */
    public WeChatPayResponse(Map<String, String> responseMap) {
        this.returnCode = responseMap.get("return_code");
        this.returnMsg = responseMap.get("return_msg");
        this.appId = responseMap.get("appid");
        this.mchId = responseMap.get("mch_id");
        this.deviceInfo = responseMap.get("device_info");
        this.nonceStr = responseMap.get("nonce_str");
        this.sign = responseMap.get("sign");
        this.resultCode = responseMap.get("result_code");
        this.errCode = responseMap.get("err_code");
        this.errCodeDes = responseMap.get("err_code_des");
        this.tradeType = responseMap.get("trade_type");
        this.prepayId = responseMap.get("prepay_id");
        this.codeUrl = responseMap.get("code_url");
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode);
    }
    
    /**
     * 判断通信是否成功
     */
    public boolean isReturnSuccess() {
        return "SUCCESS".equals(returnCode);
    }
    
    /**
     * 判断业务是否成功
     */
    public boolean isResultSuccess() {
        return "SUCCESS".equals(resultCode);
    }
    
    // Getters and Setters
    public String getReturnCode() {
        return returnCode;
    }
    
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    
    public String getReturnMsg() {
        return returnMsg;
    }
    
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    
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
    
    public String getDeviceInfo() {
        return deviceInfo;
    }
    
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
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
    
    public String getResultCode() {
        return resultCode;
    }
    
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    
    public String getErrCode() {
        return errCode;
    }
    
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    
    public String getErrCodeDes() {
        return errCodeDes;
    }
    
    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }
    
    public String getTradeType() {
        return tradeType;
    }
    
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    
    public String getPrepayId() {
        return prepayId;
    }
    
    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }
    
    public String getCodeUrl() {
        return codeUrl;
    }
    
    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
    
    public String getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public String getNonceStrPay() {
        return nonceStrPay;
    }
    
    public void setNonceStrPay(String nonceStrPay) {
        this.nonceStrPay = nonceStrPay;
    }
    
    public String getPaySign() {
        return paySign;
    }
    
    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
    
    public String getSignType() {
        return signType;
    }
    
    public void setSignType(String signType) {
        this.signType = signType;
    }
}