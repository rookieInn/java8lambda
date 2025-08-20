package com.extrigger.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * 微信退款响应模型
 * @author extrigger
 */
public class RefundResponse {
    
    /** 微信退款单号 */
    @JsonProperty("refund_id")
    private String refundId;
    
    /** 商户退款单号 */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    
    /** 微信支付订单号 */
    @JsonProperty("transaction_id")
    private String transactionId;
    
    /** 商户订单号 */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    
    /** 退款渠道 */
    private String channel;
    
    /** 退款入账账户 */
    @JsonProperty("user_received_account")
    private String userReceivedAccount;
    
    /** 退款成功时间 */
    @JsonProperty("success_time")
    private Date successTime;
    
    /** 退款创建时间 */
    @JsonProperty("create_time")
    private Date createTime;
    
    /** 退款状态 */
    private String status;
    
    /** 资金账户 */
    @JsonProperty("funds_account")
    private String fundsAccount;
    
    /** 金额信息 */
    private Amount amount;
    
    /** 优惠退款信息 */
    @JsonProperty("promotion_detail")
    private Object promotionDetail;
    
    /** 错误码 */
    private String code;
    
    /** 错误信息 */
    private String message;
    
    public static class Amount {
        /** 订单金额 */
        private Integer total;
        
        /** 退款金额 */
        private Integer refund;
        
        /** 用户支付金额 */
        @JsonProperty("payer_total")
        private Integer payerTotal;
        
        /** 用户退款金额 */
        @JsonProperty("payer_refund")
        private Integer payerRefund;
        
        /** 应结订单金额 */
        @JsonProperty("settlement_total")
        private Integer settlementTotal;
        
        /** 应结退款金额 */
        @JsonProperty("settlement_refund")
        private Integer settlementRefund;
        
        /** 优惠退款金额 */
        @JsonProperty("discount_refund")
        private Integer discountRefund;
        
        /** 货币类型 */
        private String currency;
        
        // Getters and Setters
        public Integer getTotal() {
            return total;
        }
        
        public void setTotal(Integer total) {
            this.total = total;
        }
        
        public Integer getRefund() {
            return refund;
        }
        
        public void setRefund(Integer refund) {
            this.refund = refund;
        }
        
        public Integer getPayerTotal() {
            return payerTotal;
        }
        
        public void setPayerTotal(Integer payerTotal) {
            this.payerTotal = payerTotal;
        }
        
        public Integer getPayerRefund() {
            return payerRefund;
        }
        
        public void setPayerRefund(Integer payerRefund) {
            this.payerRefund = payerRefund;
        }
        
        public Integer getSettlementTotal() {
            return settlementTotal;
        }
        
        public void setSettlementTotal(Integer settlementTotal) {
            this.settlementTotal = settlementTotal;
        }
        
        public Integer getSettlementRefund() {
            return settlementRefund;
        }
        
        public void setSettlementRefund(Integer settlementRefund) {
            this.settlementRefund = settlementRefund;
        }
        
        public Integer getDiscountRefund() {
            return discountRefund;
        }
        
        public void setDiscountRefund(Integer discountRefund) {
            this.discountRefund = discountRefund;
        }
        
        public String getCurrency() {
            return currency;
        }
        
        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
    
    // Getters and Setters
    public String getRefundId() {
        return refundId;
    }
    
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }
    
    public String getOutRefundNo() {
        return outRefundNo;
    }
    
    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
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
    
    public String getChannel() {
        return channel;
    }
    
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    public String getUserReceivedAccount() {
        return userReceivedAccount;
    }
    
    public void setUserReceivedAccount(String userReceivedAccount) {
        this.userReceivedAccount = userReceivedAccount;
    }
    
    public Date getSuccessTime() {
        return successTime;
    }
    
    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFundsAccount() {
        return fundsAccount;
    }
    
    public void setFundsAccount(String fundsAccount) {
        this.fundsAccount = fundsAccount;
    }
    
    public Amount getAmount() {
        return amount;
    }
    
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
    
    public Object getPromotionDetail() {
        return promotionDetail;
    }
    
    public void setPromotionDetail(Object promotionDetail) {
        this.promotionDetail = promotionDetail;
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
}