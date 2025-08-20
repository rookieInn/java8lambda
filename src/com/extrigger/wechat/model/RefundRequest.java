package com.extrigger.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信退款请求模型
 * @author extrigger
 */
public class RefundRequest {
    
    /** 微信支付订单号 */
    @JsonProperty("transaction_id")
    private String transactionId;
    
    /** 商户订单号 */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    
    /** 商户退款单号 */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    
    /** 退款原因 */
    private String reason;
    
    /** 退款金额 */
    private Amount amount;
    
    /** 退款商品 */
    @JsonProperty("goods_detail")
    private Object goodsDetail;
    
    /** 退款通知URL */
    @JsonProperty("notify_url")
    private String notifyUrl;
    
    public static class Amount {
        /** 退款金额，单位为分 */
        private Integer refund;
        
        /** 原订单金额，单位为分 */
        private Integer total;
        
        /** 货币类型 */
        private String currency = "CNY";
        
        public Amount() {}
        
        public Amount(Integer refund, Integer total) {
            this.refund = refund;
            this.total = total;
        }
        
        public Integer getRefund() {
            return refund;
        }
        
        public void setRefund(Integer refund) {
            this.refund = refund;
        }
        
        public Integer getTotal() {
            return total;
        }
        
        public void setTotal(Integer total) {
            this.total = total;
        }
        
        public String getCurrency() {
            return currency;
        }
        
        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
    
    // Getters and Setters
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
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Amount getAmount() {
        return amount;
    }
    
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
    
    public Object getGoodsDetail() {
        return goodsDetail;
    }
    
    public void setGoodsDetail(Object goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
    
    public String getNotifyUrl() {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}