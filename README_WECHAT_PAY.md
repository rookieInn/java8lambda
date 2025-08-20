# 微信支付和退款功能 Java 实现

这是一个完整的微信支付和退款功能的Java实现，基于微信支付API v3版本开发。

## 功能特性

- ✅ 统一下单（JSAPI支付）
- ✅ 查询订单状态
- ✅ 申请退款
- ✅ 查询退款状态
- ✅ 支付结果通知处理
- ✅ 退款结果通知处理
- ✅ 签名生成和验证
- ✅ 微信小程序支付参数生成

## 项目结构

```
src/
├── main/
│   ├── java/com/extrigger/
│   │   ├── WeChatPayApplication.java          # 应用启动类
│   │   └── wechat/
│   │       ├── config/
│   │       │   └── WeChatPayConfig.java       # 微信支付配置类
│   │       ├── controller/
│   │       │   └── WeChatPayController.java   # 支付控制器
│   │       ├── model/                         # 数据模型
│   │       │   ├── PayRequest.java           # 支付请求模型
│   │       │   ├── PayResponse.java          # 支付响应模型
│   │       │   ├── RefundRequest.java        # 退款请求模型
│   │       │   ├── RefundResponse.java       # 退款响应模型
│   │       │   └── OrderQueryResponse.java   # 订单查询响应模型
│   │       ├── service/
│   │       │   └── WeChatPayService.java     # 支付服务类
│   │       └── util/
│   │           └── WeChatPayUtil.java        # 支付工具类
│   └── resources/
│       └── application.yml                   # 应用配置文件
└── pom.xml                                   # Maven依赖配置
```

## 快速开始

### 1. 配置微信支付参数

修改 `src/main/resources/application.yml` 文件中的微信支付配置：

```yaml
wechat:
  pay:
    app-id: your_app_id_here                    # 微信小程序/公众号应用ID
    mch-id: your_mch_id_here                    # 商户号
    serial-no: your_serial_no_here              # 商户证书序列号
    private-key-path: /path/to/private_key.pem  # 商户私钥文件路径
    certificate-path: /path/to/certificate.pem  # 微信支付平台证书路径
    notify-url: https://your-domain.com/api/wechat/pay/notify        # 支付通知URL
    refund-notify-url: https://your-domain.com/api/wechat/pay/refund/notify  # 退款通知URL
```

### 2. 准备证书文件

从微信商户平台下载以下文件：
- 商户私钥文件 (private_key.pem)
- 微信支付平台证书文件 (wechatpay_certificate.pem)

### 3. 运行应用

```bash
mvn spring-boot:run
```

应用将在 http://localhost:8080 启动。

## API 接口

### 创建支付订单

**POST** `/api/wechat/pay/create`

请求体：
```json
{
  "description": "商品描述",
  "outTradeNo": "商户订单号（可选，不传会自动生成）",
  "totalAmount": 100,
  "openid": "用户openid"
}
```

响应：
```json
{
  "success": true,
  "outTradeNo": "ORDER_1703123456789_abcd1234",
  "prepayId": "wx123456789abcdef",
  "payParams": {
    "appId": "wx1234567890abcdef",
    "timeStamp": "1703123456",
    "nonceStr": "randomstring",
    "package": "prepay_id=wx123456789abcdef",
    "signType": "RSA",
    "paySign": "signature"
  },
  "message": "订单创建成功"
}
```

### 查询订单状态

**GET** `/api/wechat/pay/query/{outTradeNo}`

响应：
```json
{
  "success": true,
  "data": {
    "outTradeNo": "ORDER_1703123456789_abcd1234",
    "transactionId": "4200001234567890123456789",
    "tradeState": "SUCCESS",
    "tradeStateDesc": "支付成功",
    "amount": {
      "total": 100,
      "payerTotal": 100,
      "currency": "CNY"
    }
  }
}
```

### 申请退款

**POST** `/api/wechat/pay/refund`

请求体：
```json
{
  "outTradeNo": "ORDER_1703123456789_abcd1234",
  "outRefundNo": "退款单号（可选，不传会自动生成）",
  "refundAmount": 50,
  "totalAmount": 100,
  "reason": "退款原因"
}
```

响应：
```json
{
  "success": true,
  "data": {
    "refundId": "50000123456789012345678901234567890",
    "outRefundNo": "REFUND_1703123456789_efgh5678",
    "status": "SUCCESS",
    "amount": {
      "refund": 50,
      "total": 100,
      "currency": "CNY"
    }
  },
  "message": "退款申请成功"
}
```

### 查询退款状态

**GET** `/api/wechat/pay/refund/query/{outRefundNo}`

响应：
```json
{
  "success": true,
  "data": {
    "refundId": "50000123456789012345678901234567890",
    "outRefundNo": "REFUND_1703123456789_efgh5678",
    "status": "SUCCESS",
    "amount": {
      "refund": 50,
      "total": 100
    }
  }
}
```

## 回调通知处理

### 支付结果通知

**POST** `/api/wechat/pay/notify`

微信支付会在支付完成后向此接口发送通知。

### 退款结果通知

**POST** `/api/wechat/pay/refund/notify`

微信支付会在退款完成后向此接口发送通知。

## 使用示例

### 前端调用支付

```javascript
// 1. 创建支付订单
const orderResponse = await fetch('/api/wechat/pay/create', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    description: '测试商品',
    totalAmount: 100,
    openid: 'user_openid_here'
  })
});

const orderData = await orderResponse.json();

if (orderData.success) {
  // 2. 调用微信支付
  wx.requestPayment({
    ...orderData.payParams,
    success: function(res) {
      console.log('支付成功', res);
    },
    fail: function(res) {
      console.log('支付失败', res);
    }
  });
}
```

### 查询订单状态

```javascript
const queryResponse = await fetch(`/api/wechat/pay/query/${outTradeNo}`);
const queryData = await queryResponse.json();

if (queryData.success) {
  console.log('订单状态:', queryData.data.tradeState);
}
```

### 申请退款

```javascript
const refundResponse = await fetch('/api/wechat/pay/refund', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    outTradeNo: 'ORDER_1703123456789_abcd1234',
    refundAmount: 50,
    totalAmount: 100,
    reason: '用户申请退款'
  })
});

const refundData = await refundResponse.json();

if (refundData.success) {
  console.log('退款申请成功:', refundData.data);
}
```

## 注意事项

1. **证书文件安全**：私钥文件和证书文件需要妥善保管，不要提交到代码仓库。

2. **回调URL配置**：确保回调URL可以被微信服务器访问，通常需要配置为公网域名。

3. **签名验证**：所有来自微信的回调通知都需要验证签名，确保数据安全。

4. **金额单位**：所有金额都以分为单位，例如1元 = 100分。

5. **订单号唯一性**：商户订单号和退款单号必须保证唯一性。

6. **幂等性**：支付和退款接口都具有幂等性，相同参数的重复请求不会产生副作用。

## 依赖说明

主要依赖包括：
- Spring Boot 2.7.18
- Jackson 2.15.2 (JSON处理)
- Apache HttpClient 4.5.14 (HTTP请求)
- SLF4J 1.7.36 (日志框架)

## 开发环境

- JDK 8+
- Maven 3.6+
- Spring Boot 2.7+

## 许可证

MIT License