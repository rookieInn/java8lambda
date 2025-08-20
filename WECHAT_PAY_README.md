# 微信支付 Java 实现

这是一个完整的微信支付 Java 实现，包含支付和退款功能，支持多种支付方式。

## 功能特性

### 支付功能
- ✅ JSAPI支付（公众号/小程序支付）
- ✅ Native支付（扫码支付）
- ✅ APP支付
- ✅ H5支付
- ✅ 查询订单
- ✅ 关闭订单
- ✅ 支付通知处理

### 退款功能
- ✅ 申请退款
- ✅ 查询退款
- ✅ 退款通知处理

## 项目结构

```
src/main/java/com/example/wechatpay/
├── config/
│   └── WeChatPayConfig.java         # 配置类
├── constant/
│   └── WeChatPayConstants.java      # 常量定义
├── controller/
│   └── WeChatPayController.java     # REST控制器
├── model/
│   ├── request/
│   │   ├── UnifiedOrderRequest.java # 下单请求
│   │   └── RefundRequest.java       # 退款请求
│   └── response/
│       ├── UnifiedOrderResponse.java # 下单响应
│       ├── RefundResponse.java       # 退款响应
│       └── PaymentNotifyResponse.java # 支付通知
├── service/
│   └── WeChatPayService.java        # 核心服务
├── util/
│   ├── SignatureUtil.java           # 签名工具
│   └── HttpClientUtil.java          # HTTP工具
└── WeChatPayApplication.java        # 主应用类
```

## 快速开始

### 1. 配置要求

- Java 8 或更高版本
- Maven 3.x
- Spring Boot 2.7.x

### 2. 配置文件

修改 `src/main/resources/application.yml` 中的微信支付配置：

```yaml
wechat:
  pay:
    app-id: YOUR_APP_ID              # 应用ID
    mch-id: YOUR_MCH_ID              # 商户号
    api-key: YOUR_API_KEY            # API密钥
    api-v3-key: YOUR_API_V3_KEY      # API v3密钥
    mch-serial-no: YOUR_SERIAL_NO    # 证书序列号
    private-key: |                   # 商户私钥
      -----BEGIN PRIVATE KEY-----
      YOUR_PRIVATE_KEY_CONTENT
      -----END PRIVATE KEY-----
    notify-url: https://your-domain.com/api/wechat-pay/notify/payment
    refund-notify-url: https://your-domain.com/api/wechat-pay/notify/refund
```

### 3. 编译运行

```bash
# 安装依赖
mvn clean install

# 运行应用
mvn spring-boot:run
```

## API 接口文档

### 1. JSAPI支付（公众号/小程序）

**请求URL:** `POST /api/wechat-pay/jsapi/create`

**请求参数:**
```json
{
  "openId": "用户openId",
  "amount": 100,
  "description": "商品描述"
}
```

**响应示例:**
```json
{
  "success": true,
  "outTradeNo": "16234567890123",
  "payParams": {
    "appId": "wx123456",
    "timeStamp": "1623456789",
    "nonceStr": "abc123",
    "package": "prepay_id=wx123456",
    "signType": "RSA",
    "paySign": "signature..."
  },
  "message": "订单创建成功"
}
```

### 2. Native支付（扫码支付）

**请求URL:** `POST /api/wechat-pay/native/create`

**请求参数:**
```json
{
  "amount": 100,
  "description": "商品描述"
}
```

**响应示例:**
```json
{
  "success": true,
  "outTradeNo": "16234567890123",
  "codeUrl": "weixin://wxpay/bizpayurl?pr=xxx",
  "message": "订单创建成功"
}
```

### 3. 申请退款

**请求URL:** `POST /api/wechat-pay/refund`

**请求参数:**
```json
{
  "outTradeNo": "商户订单号",
  "refundAmount": 50,
  "totalAmount": 100,
  "reason": "退款原因"
}
```

**响应示例:**
```json
{
  "success": true,
  "outRefundNo": "R16234567890123",
  "refundId": "50000000382019052709732678859",
  "status": "SUCCESS",
  "message": "退款申请成功"
}
```

### 4. 查询订单

**请求URL:** `GET /api/wechat-pay/order/query?outTradeNo=xxx`

**响应示例:**
```json
{
  "success": true,
  "data": {
    "appid": "wx123456",
    "mchid": "1234567890",
    "out_trade_no": "16234567890123",
    "transaction_id": "4200000000000000000",
    "trade_state": "SUCCESS",
    "trade_state_desc": "支付成功"
  },
  "message": "查询成功"
}
```

### 5. 查询退款

**请求URL:** `GET /api/wechat-pay/refund/query?outRefundNo=xxx`

**响应示例:**
```json
{
  "success": true,
  "data": {
    "refund_id": "50000000382019052709732678859",
    "out_refund_no": "R16234567890123",
    "status": "SUCCESS",
    "amount": {
      "refund": 50,
      "total": 100
    }
  },
  "message": "查询成功"
}
```

## 前端集成示例

### 小程序支付

```javascript
// 1. 调用后端创建订单接口
const res = await wx.request({
  url: 'https://your-domain.com/api/wechat-pay/jsapi/create',
  method: 'POST',
  data: {
    openId: wx.getStorageSync('openId'),
    amount: 100,
    description: '商品描述'
  }
});

// 2. 调起支付
if (res.data.success) {
  const payParams = res.data.payParams;
  wx.requestPayment({
    timeStamp: payParams.timeStamp,
    nonceStr: payParams.nonceStr,
    package: payParams.package,
    signType: payParams.signType,
    paySign: payParams.paySign,
    success(res) {
      console.log('支付成功');
    },
    fail(res) {
      console.log('支付失败');
    }
  });
}
```

### H5支付

```javascript
// 1. 调用后端创建订单接口
fetch('https://your-domain.com/api/wechat-pay/h5/create', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    amount: 100,
    description: '商品描述',
    clientIp: '127.0.0.1'
  })
})
.then(res => res.json())
.then(data => {
  if (data.success) {
    // 2. 跳转到微信支付页面
    window.location.href = data.h5Url;
  }
});
```

## 注意事项

### 1. 证书配置
- 商户私钥需要从微信商户平台下载
- API v3密钥需要在商户平台设置
- 证书序列号可以在商户平台查看

### 2. 安全建议
- 生产环境中不要将敏感信息硬编码
- 使用环境变量或配置中心管理密钥
- 启用HTTPS保护API接口
- 验证支付通知的签名

### 3. 回调地址
- 支付回调地址必须是公网可访问的HTTPS地址
- 回调处理要做幂等性处理
- 及时返回成功响应，避免微信重复通知

### 4. 金额处理
- 所有金额单位都是分（人民币）
- 注意金额的精度问题

## 测试建议

1. **使用沙箱环境测试**
   - 设置 `sandbox-enabled: true`
   - 使用沙箱环境的测试号

2. **本地测试工具**
   - 使用内网穿透工具（如ngrok）暴露本地服务
   - 使用Postman测试API接口

3. **日志监控**
   - 查看 `logs/wechat-pay.log` 文件
   - 监控支付成功率和退款成功率

## 常见问题

### Q1: 签名验证失败
- 检查商户私钥是否正确
- 确认证书序列号是否匹配
- 验证API密钥是否正确

### Q2: 支付通知没有收到
- 确认回调地址是否可访问
- 检查防火墙设置
- 查看微信商户平台的通知记录

### Q3: 退款失败
- 确认商户账户余额充足
- 检查退款金额是否超过原订单金额
- 验证退款单号是否重复

## 扩展功能

如需添加更多功能，可以扩展以下模块：

1. **分账功能** - 添加分账相关接口
2. **对账功能** - 实现账单下载和对账
3. **优惠券** - 集成营销功能
4. **企业付款** - 实现转账到零钱功能

## 技术支持

如有问题，请参考：
- [微信支付官方文档](https://pay.weixin.qq.com/wiki/doc/apiv3/index.shtml)
- [微信支付API v3接口规则](https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay2_0.shtml)

## License

MIT License