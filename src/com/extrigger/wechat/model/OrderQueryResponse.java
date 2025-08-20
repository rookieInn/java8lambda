package com.extrigger.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * 订单查询响应模型
 * @author extrigger
 */
public class OrderQueryResponse {
    
    /** 应用ID */
    private String appid;
    
    /** 商户号 */
    private String mchid;
    
    /** 商户订单号 */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    
    /** 微信支付订单号 */
    @JsonProperty("transaction_id")
    private String transactionId;
    
    /** 交易类型 */
    @JsonProperty("trade_type")
    private String tradeType;
    
    /** 交易状态 */
    @JsonProperty("trade_state")
    private String tradeState;
    
    /** 交易状态描述 */
    @JsonProperty("trade_state_desc")
    private String tradeStateDesc;
    
    /** 付款银行 */
    private String bank_type;
    
    /** 附加数据 */
    private String attach;
    
    /** 支付完成时间 */
    @JsonProperty("success_time")
    private Date successTime;
    
    /** 支付者 */
    private Payer payer;
    
    /** 订单金额 */
    private Amount amount;
    
    /** 场景信息 */
    @JsonProperty("scene_info")
    private Object sceneInfo;
    
    /** 优惠功能 */
    @JsonProperty("promotion_detail")
    private Object promotionDetail;
    
    public static class Payer {
        private String openid;
        
        public String getOpenid() {
            return openid;
        }
        
        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }
    
    public static class Amount {
        /** 总金额 */
        private Integer total;
        
        /** 用户支付金额 */
        @JsonProperty("payer_total")
        private Integer payerTotal;
        
        /** 货币类型 */
        private String currency;
        
        /** 用户支付币种 */
        @JsonProperty("payer_currency")
        private String payerCurrency;
        
        public Integer getTotal() {
            return total;
        }
        
        public void setTotal(Integer total) {
            this.total = total;
        }
        
        public Integer getPayerTotal() {
            return payerTotal;
        }
        
        public void setPayerTotal(Integer payerTotal) {
            this.payerTotal = payerTotal;
        }
        
        public String getCurrency() {
            return currency;
        }
        
        public void setCurrency(String currency) {
            this.currency = currency;
        }
        
        public String getPayerCurrency() {
            return payerCurrency;
        }
        
        public void setPayerCurrency(String payerCurrency) {
            this.payerCurrency = payerCurrency;
        }
    }
    
    // Getters and Setters
    public String getAppid() {
        return appid;
    }
    
    public void setAppid(String appid) {
        this.appid = appid;
    }
    
    public String getMchid() {
        return mchid;
    }
    
    public void setMchid(String mchid) {
        this.mchid = mchid;
    }
    
    public String getOutTradeNo() {
        return outTradeNo;
    }
    
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getTradeType() {
        return tradeType;
    }
    
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    
    public String getTradeState() {
        return tradeState;
    }
    
    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }
    
    public String getTradeStateDesc() {
        return tradeStateDesc;
    }
    
    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }
    
    public String getBank_type() {
        return bank_type;
    }
    
    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }
    
    public String getAttach() {
        return attach;
    }
    
    public void setAttach(String attach) {
        this.attach = attach;
    }
    
    public Date getSuccessTime() {
        return successTime;
    }
    
    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }
    
    public Payer getPayer() {
        return payer;
    }
    
    public void setPayer(Payer payer) {
        this.payer = payer;
    }
    
    public Amount getAmount() {
        return amount;
    }
    
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
    
    public Object getSceneInfo() {
        return sceneInfo;
    }
    
    public void setSceneInfo(Object sceneInfo) {
        this.sceneInfo = sceneInfo;
    }
    
    public Object getPromotionDetail() {
        return promotionDetail;
    }
    
    public void setPromotionDetail(Object promotionDetail) {
        this.promotionDetail = promotionDetail;
    }
}