package com.extrigger.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信支付请求模型
 * @author extrigger
 */
public class PayRequest {
    
    /** 商品描述 */
    private String description;
    
    /** 商户订单号 */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    
    /** 订单金额（分） */
    private Amount amount;
    
    /** 支付者信息 */
    private Payer payer;
    
    /** 附加数据 */
    private String attach;
    
    /** 订单优惠标记 */
    @JsonProperty("goods_tag")
    private String goodsTag;
    
    /** 通知地址 */
    @JsonProperty("notify_url")
    private String notifyUrl;
    
    public static class Amount {
        /** 总金额，单位为分 */
        private Integer total;
        
        /** 货币类型，CNY：人民币 */
        private String currency = "CNY";
        
        public Amount() {}
        
        public Amount(Integer total) {
            this.total = total;
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
    
    public static class Payer {
        /** 用户标识 */
        private String openid;
        
        public Payer() {}
        
        public Payer(String openid) {
            this.openid = openid;
        }
        
        public String getOpenid() {
            return openid;
        }
        
        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }
    
    // Getters and Setters
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOutTradeNo() {
        return outTradeNo;
    }
    
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
    
    public Amount getAmount() {
        return amount;
    }
    
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
    
    public Payer getPayer() {
        return payer;
    }
    
    public void setPayer(Payer payer) {
        this.payer = payer;
    }
    
    public String getAttach() {
        return attach;
    }
    
    public void setAttach(String attach) {
        this.attach = attach;
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
}