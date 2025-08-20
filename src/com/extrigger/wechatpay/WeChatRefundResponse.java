package com.extrigger.wechatpay;

import java.util.Map;

/**
 * 微信退款响应类
 * 申请退款接口的返回结果
 */
public class WeChatRefundResponse {
    
    // 返回状态码
    private String returnCode;
    
    // 返回信息
    private String returnMsg;
    
    // 应用ID
    private String appId;
    
    // 商户号
    private String mchId;
    
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
    
    // 微信订单号
    private String transactionId;
    
    // 商户订单号
    private String outTradeNo;
    
    // 商户退款单号
    private String outRefundNo;
    
    // 微信退款单号
    private String refundId;
    
    // 退款渠道
    private String refundChannel;
    
    // 退款金额（分）
    private Integer refundFee;
    
    // 订单金额（分）
    private Integer totalFee;
    
    // 货币类型
    private String feeType;
    
    // 现金支付金额（分）
    private Integer cashFee;
    
    // 现金退款金额（分）
    private Integer cashRefundFee;
    
    // 代金券退款金额（分）
    private Integer couponRefundFee;
    
    // 代金券退款数量
    private Integer couponRefundCount;
    
    public WeChatRefundResponse() {}
    
    /**
     * 从Map构造响应对象
     */
    public WeChatRefundResponse(Map<String, String> responseMap) {
        this.returnCode = responseMap.get("return_code");
        this.returnMsg = responseMap.get("return_msg");
        this.appId = responseMap.get("appid");
        this.mchId = responseMap.get("mch_id");
        this.nonceStr = responseMap.get("nonce_str");
        this.sign = responseMap.get("sign");
        this.resultCode = responseMap.get("result_code");
        this.errCode = responseMap.get("err_code");
        this.errCodeDes = responseMap.get("err_code_des");
        this.transactionId = responseMap.get("transaction_id");
        this.outTradeNo = responseMap.get("out_trade_no");
        this.outRefundNo = responseMap.get("out_refund_no");
        this.refundId = responseMap.get("refund_id");
        this.refundChannel = responseMap.get("refund_channel");
        this.refundFee = parseInteger(responseMap.get("refund_fee"));
        this.totalFee = parseInteger(responseMap.get("total_fee"));
        this.feeType = responseMap.get("fee_type");
        this.cashFee = parseInteger(responseMap.get("cash_fee"));
        this.cashRefundFee = parseInteger(responseMap.get("cash_refund_fee"));
        this.couponRefundFee = parseInteger(responseMap.get("coupon_refund_fee"));
        this.couponRefundCount = parseInteger(responseMap.get("coupon_refund_count"));
    }
    
    /**
     * 解析整数
     */
    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
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
    
    public String getRefundId() {
        return refundId;
    }
    
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }
    
    public String getRefundChannel() {
        return refundChannel;
    }
    
    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }
    
    public Integer getRefundFee() {
        return refundFee;
    }
    
    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }
    
    public Integer getTotalFee() {
        return totalFee;
    }
    
    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }
    
    public String getFeeType() {
        return feeType;
    }
    
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    
    public Integer getCashFee() {
        return cashFee;
    }
    
    public void setCashFee(Integer cashFee) {
        this.cashFee = cashFee;
    }
    
    public Integer getCashRefundFee() {
        return cashRefundFee;
    }
    
    public void setCashRefundFee(Integer cashRefundFee) {
        this.cashRefundFee = cashRefundFee;
    }
    
    public Integer getCouponRefundFee() {
        return couponRefundFee;
    }
    
    public void setCouponRefundFee(Integer couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }
    
    public Integer getCouponRefundCount() {
        return couponRefundCount;
    }
    
    public void setCouponRefundCount(Integer couponRefundCount) {
        this.couponRefundCount = couponRefundCount;
    }
}