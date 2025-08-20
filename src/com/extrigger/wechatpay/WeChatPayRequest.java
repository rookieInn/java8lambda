package com.extrigger.wechatpay;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付请求参数类
 * 用于统一下单接口
 */
public class WeChatPayRequest {
    
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
    
    // 签名类型
    private String signType = "MD5";
    
    // 商品描述
    private String body;
    
    // 商品详情
    private String detail;
    
    // 附加数据
    private String attach;
    
    // 商户订单号
    private String outTradeNo;
    
    // 货币类型
    private String feeType = "CNY";
    
    // 总金额（分）
    private Integer totalFee;
    
    // 终端IP
    private String spbillCreateIp;
    
    // 交易起始时间
    private String timeStart;
    
    // 交易结束时间
    private String timeExpire;
    
    // 商品标记
    private String goodsTag;
    
    // 通知地址
    private String notifyUrl;
    
    // 交易类型
    private String tradeType = "NATIVE"; // NATIVE, JSAPI, APP
    
    // 商品ID
    private String productId;
    
    // 用户标识（JSAPI支付必填）
    private String openId;
    
    // 场景信息
    private String sceneInfo;
    
    public WeChatPayRequest() {}
    
    public WeChatPayRequest(String outTradeNo, String body, Integer totalFee, String spbillCreateIp) {
        this.outTradeNo = outTradeNo;
        this.body = body;
        this.totalFee = totalFee;
        this.spbillCreateIp = spbillCreateIp;
    }
    
    /**
     * 转换为Map，用于签名和请求
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        
        if (appId != null) map.put("appid", appId);
        if (mchId != null) map.put("mch_id", mchId);
        if (deviceInfo != null) map.put("device_info", deviceInfo);
        if (nonceStr != null) map.put("nonce_str", nonceStr);
        if (sign != null) map.put("sign", sign);
        if (signType != null) map.put("sign_type", signType);
        if (body != null) map.put("body", body);
        if (detail != null) map.put("detail", detail);
        if (attach != null) map.put("attach", attach);
        if (outTradeNo != null) map.put("out_trade_no", outTradeNo);
        if (feeType != null) map.put("fee_type", feeType);
        if (totalFee != null) map.put("total_fee", totalFee.toString());
        if (spbillCreateIp != null) map.put("spbill_create_ip", spbillCreateIp);
        if (timeStart != null) map.put("time_start", timeStart);
        if (timeExpire != null) map.put("time_expire", timeExpire);
        if (goodsTag != null) map.put("goods_tag", goodsTag);
        if (notifyUrl != null) map.put("notify_url", notifyUrl);
        if (tradeType != null) map.put("trade_type", tradeType);
        if (productId != null) map.put("product_id", productId);
        if (openId != null) map.put("openid", openId);
        if (sceneInfo != null) map.put("scene_info", sceneInfo);
        
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
    
    public String getSignType() {
        return signType;
    }
    
    public void setSignType(String signType) {
        this.signType = signType;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public String getAttach() {
        return attach;
    }
    
    public void setAttach(String attach) {
        this.attach = attach;
    }
    
    public String getOutTradeNo() {
        return outTradeNo;
    }
    
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
    
    public String getFeeType() {
        return feeType;
    }
    
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    
    public Integer getTotalFee() {
        return totalFee;
    }
    
    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }
    
    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }
    
    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }
    
    public String getTimeStart() {
        return timeStart;
    }
    
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }
    
    public String getTimeExpire() {
        return timeExpire;
    }
    
    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }
    
    public String getGoodsTag() {
        return goodsTag;
    }
    
    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }
    
    public String getNotifyUrl() {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    
    public String getTradeType() {
        return tradeType;
    }
    
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getOpenId() {
        return openId;
    }
    
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    
    public String getSceneInfo() {
        return sceneInfo;
    }
    
    public void setSceneInfo(String sceneInfo) {
        this.sceneInfo = sceneInfo;
    }
}