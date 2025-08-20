package com.example.wechatpay.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 统一下单请求参数
 */
@Data
@Builder
public class UnifiedOrderRequest {
    
    /**
     * 应用ID
     */
    @JSONField(name = "appid")
    private String appId;
    
    /**
     * 直连商户号
     */
    @JSONField(name = "mchid")
    private String mchId;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    
    /**
     * 交易结束时间
     */
    @JSONField(name = "time_expire")
    private String timeExpire;
    
    /**
     * 附加数据
     */
    private String attach;
    
    /**
     * 通知地址
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;
    
    /**
     * 订单优惠标记
     */
    @JSONField(name = "goods_tag")
    private String goodsTag;
    
    /**
     * 订单金额信息
     */
    private Amount amount;
    
    /**
     * 支付者信息 (JSAPI支付必填)
     */
    private Payer payer;
    
    /**
     * 优惠功能
     */
    private Detail detail;
    
    /**
     * 场景信息
     */
    @JSONField(name = "scene_info")
    private SceneInfo sceneInfo;
    
    /**
     * 结算信息
     */
    @JSONField(name = "settle_info")
    private SettleInfo settleInfo;
    
    @Data
    @Builder
    public static class Amount {
        /**
         * 总金额，单位为分
         */
        private Integer total;
        
        /**
         * 货币类型
         */
        private String currency = "CNY";
    }
    
    @Data
    @Builder
    public static class Payer {
        /**
         * 用户在直连商户appid下的唯一标识
         */
        private String openid;
    }
    
    @Data
    @Builder
    public static class Detail {
        /**
         * 订单原价
         */
        @JSONField(name = "cost_price")
        private Integer costPrice;
        
        /**
         * 商品小票ID
         */
        @JSONField(name = "invoice_id")
        private String invoiceId;
        
        /**
         * 商品详情
         */
        @JSONField(name = "goods_detail")
        private List<GoodsDetail> goodsDetail;
    }
    
    @Data
    @Builder
    public static class GoodsDetail {
        /**
         * 商户侧商品编码
         */
        @JSONField(name = "merchant_goods_id")
        private String merchantGoodsId;
        
        /**
         * 微信侧商品编码
         */
        @JSONField(name = "wechatpay_goods_id")
        private String wechatpayGoodsId;
        
        /**
         * 商品名称
         */
        @JSONField(name = "goods_name")
        private String goodsName;
        
        /**
         * 商品数量
         */
        private Integer quantity;
        
        /**
         * 商品单价
         */
        @JSONField(name = "unit_price")
        private Integer unitPrice;
    }
    
    @Data
    @Builder
    public static class SceneInfo {
        /**
         * 用户终端IP
         */
        @JSONField(name = "payer_client_ip")
        private String payerClientIp;
        
        /**
         * 商户端设备号
         */
        @JSONField(name = "device_id")
        private String deviceId;
        
        /**
         * 商户门店信息
         */
        @JSONField(name = "store_info")
        private StoreInfo storeInfo;
        
        /**
         * H5场景信息
         */
        @JSONField(name = "h5_info")
        private H5Info h5Info;
    }
    
    @Data
    @Builder
    public static class StoreInfo {
        /**
         * 门店编号
         */
        private String id;
        
        /**
         * 门店名称
         */
        private String name;
        
        /**
         * 地区编码
         */
        @JSONField(name = "area_code")
        private String areaCode;
        
        /**
         * 详细地址
         */
        private String address;
    }
    
    @Data
    @Builder
    public static class H5Info {
        /**
         * 场景类型
         */
        private String type;
        
        /**
         * 应用名称
         */
        @JSONField(name = "app_name")
        private String appName;
        
        /**
         * 网站URL
         */
        @JSONField(name = "app_url")
        private String appUrl;
        
        /**
         * iOS平台BundleID
         */
        @JSONField(name = "bundle_id")
        private String bundleId;
        
        /**
         * Android平台PackageName
         */
        @JSONField(name = "package_name")
        private String packageName;
    }
    
    @Data
    @Builder
    public static class SettleInfo {
        /**
         * 是否指定分账
         */
        @JSONField(name = "profit_sharing")
        private Boolean profitSharing;
    }
}