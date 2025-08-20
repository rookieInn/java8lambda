# 微信支付Java实现

这是一个完整的微信支付和退款功能的Java实现，包含统一下单、查询订单、申请退款、查询退款等核心功能。

## 功能特性

- ✅ 统一下单（支持NATIVE、JSAPI、APP支付）
- ✅ 查询订单状态
- ✅ 关闭订单
- ✅ 申请退款（支持全额和部分退款）
- ✅ 查询退款状态
- ✅ 支付通知处理
- ✅ 退款通知处理
- ✅ 签名验证
- ✅ 工具方法（随机字符串、时间戳、金额转换等）

## 项目结构

```
src/com/extrigger/wechatpay/
├── WeChatPayConfig.java          # 微信支付配置类
├── WeChatPayRequest.java         # 支付请求参数类
├── WeChatPayResponse.java        # 支付响应结果类
├── WeChatRefundRequest.java      # 退款请求参数类
├── WeChatRefundResponse.java     # 退款响应结果类
├── WeChatPayUtils.java           # 工具类
├── WeChatPayService.java         # 支付服务类
├── WeChatRefundService.java      # 退款服务类
├── WeChatPayExample.java         # 使用示例
└── README.md                     # 说明文档
```

## 快速开始

### 1. 配置微信支付参数

```java
WeChatPayConfig config = new WeChatPayConfig();
config.setAppId("your_app_id");
config.setMchId("your_mch_id");
config.setMchKey("your_mch_key");
config.setNotifyUrl("https://your.domain.com/pay/notify");
config.setRefundNotifyUrl("https://your.domain.com/refund/notify");
```

### 2. 创建支付服务

```java
WeChatPayService payService = new WeChatPayService(config);
WeChatRefundService refundService = new WeChatRefundService(config);
```

### 3. 统一下单

```java
WeChatPayRequest request = new WeChatPayRequest();
request.setOutTradeNo(WeChatPayUtils.generateOutTradeNo());
request.setBody("测试商品");
request.setTotalFee(WeChatPayUtils.yuanToFen(0.01)); // 1分钱
request.setSpbillCreateIp("127.0.0.1");
request.setTradeType("NATIVE"); // 扫码支付

WeChatPayResponse response = payService.unifiedOrder(request);

if (response.isSuccess()) {
    System.out.println("二维码链接: " + response.getCodeUrl());
}
```

### 4. 申请退款

```java
WeChatRefundResponse refundResponse = refundService.refund(
    outTradeNo,           // 商户订单号
    transactionId,        // 微信订单号
    totalFee,            // 订单总金额（分）
    refundFee,           // 退款金额（分）
    "用户申请退款"        // 退款原因
);
```

## 配置说明

### 必需参数

- `appId`: 微信应用ID
- `mchId`: 微信商户号
- `mchKey`: 微信商户密钥

### 可选参数

- `notifyUrl`: 支付结果通知地址
- `refundNotifyUrl`: 退款结果通知地址
- `certPath`: 商户证书路径（退款时必需）
- `sandbox`: 是否使用沙箱环境

## 支付方式

### 1. 扫码支付（NATIVE）

适用于PC网站，生成二维码供用户扫码支付。

```java
request.setTradeType("NATIVE");
// 支付成功后获取 codeUrl 生成二维码
```

### 2. JSAPI支付

适用于微信公众号内支付。

```java
request.setTradeType("JSAPI");
request.setOpenId("user_open_id");
// 支付成功后获取 JSAPI 支付参数
```

### 3. APP支付

适用于移动应用。

```java
request.setTradeType("APP");
// 支付成功后获取 APP 支付参数
```

## 退款功能

### 全额退款

```java
refundService.fullRefund(outTradeNo, transactionId, totalFee, "全额退款");
```

### 部分退款

```java
refundService.partialRefund(outTradeNo, transactionId, totalFee, refundFee, "部分退款");
```

### 查询退款

```java
refundService.queryRefund(null, outTradeNo, null, null);
```

## 通知处理

### 支付通知

```java
// 接收微信支付通知
String notifyXml = "..."; // 从请求体获取
WeChatPayResponse notify = payService.handlePayNotify(notifyXml);

if (notify.isSuccess()) {
    // 处理支付成功逻辑
    String outTradeNo = notify.getOutTradeNo();
    String transactionId = notify.getTransactionId();
}
```

### 退款通知

```java
// 接收微信退款通知
String notifyXml = "..."; // 从请求体获取
WeChatRefundResponse notify = refundService.handleRefundNotify(notifyXml);

if (notify.isSuccess()) {
    // 处理退款成功逻辑
    String refundId = notify.getRefundId();
}
```

## 工具方法

### 金额转换

```java
// 元转分
int fen = WeChatPayUtils.yuanToFen(99.99);

// 分转元
double yuan = WeChatPayUtils.fenToYuan(9999);
```

### 生成订单号

```java
String outTradeNo = WeChatPayUtils.generateOutTradeNo();
String outRefundNo = WeChatPayUtils.generateOutRefundNo();
```

### 签名验证

```java
boolean isValid = WeChatPayUtils.verifySign(data, mchKey, sign);
```

## 注意事项

### 1. 安全性

- 商户密钥必须保密，不能泄露
- 所有请求必须验证签名
- 使用HTTPS进行通信

### 2. 证书要求

- 退款接口需要使用商户证书
- 证书文件必须安全存储
- 生产环境建议使用硬件安全模块（HSM）

### 3. 错误处理

- 必须处理所有可能的异常情况
- 记录详细的错误日志
- 实现重试机制

### 4. 通知处理

- 通知处理必须幂等
- 及时返回成功响应
- 实现通知重发机制

### 5. 金额精度

- 微信支付金额单位为分
- 避免浮点数精度问题
- 使用工具类进行金额转换

## 测试

### 沙箱环境

```java
config.setSandbox(true);
```

### 运行示例

```bash
javac -cp . com/extrigger/wechatpay/*.java
java -cp . com.extrigger.wechatpay.WeChatPayExample
```

## 依赖要求

- Java 8+
- 标准Java库（无需额外依赖）

## 扩展功能

### 1. 添加新的支付方式

继承 `WeChatPayRequest` 类，实现特定的支付参数。

### 2. 自定义通知处理

实现自定义的通知处理器，处理业务逻辑。

### 3. 数据库集成

将支付记录保存到数据库，实现订单管理。

### 4. 日志记录

集成日志框架，记录详细的支付和退款日志。

## 常见问题

### Q: 为什么退款接口调用失败？

A: 退款接口需要使用商户证书，请确保：
1. 证书文件路径正确
2. 证书文件有效
3. 证书密码正确

### Q: 支付通知没有收到？

A: 请检查：
1. 通知地址是否可访问
2. 网络连接是否正常
3. 防火墙设置是否正确

### Q: 签名验证失败？

A: 可能的原因：
1. 商户密钥错误
2. 参数顺序不正确
3. 字符编码问题

## 技术支持

如有问题，请检查：
1. 微信支付官方文档
2. 日志输出信息
3. 网络连接状态
4. 参数配置是否正确

## 版本信息

- 版本：1.0.0
- 更新日期：2024年
- 兼容性：Java 8+