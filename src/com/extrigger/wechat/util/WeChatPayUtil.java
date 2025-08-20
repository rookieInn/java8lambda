package com.extrigger.wechat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

/**
 * 微信支付工具类
 * @author extrigger
 */
public class WeChatPayUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(WeChatPayUtil.class);
    private static final String ALGORITHM = "SHA256withRSA";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 生成随机字符串
     * @return 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 获取当前时间戳（秒）
     * @return 时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
    
    /**
     * 构建签名字符串
     * @param method HTTP方法
     * @param url 请求URL
     * @param timestamp 时间戳
     * @param nonceStr 随机字符串
     * @param body 请求体
     * @return 签名字符串
     */
    public static String buildSignString(String method, String url, long timestamp, String nonceStr, String body) {
        return method + "\n" +
               url + "\n" +
               timestamp + "\n" +
               nonceStr + "\n" +
               body + "\n";
    }
    
    /**
     * 使用私钥进行RSA签名
     * @param signString 待签名字符串
     * @param privateKeyPath 私钥文件路径
     * @return 签名结果
     * @throws Exception 签名异常
     */
    public static String sign(String signString, String privateKeyPath) throws Exception {
        PrivateKey privateKey = loadPrivateKey(privateKeyPath);
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initSign(privateKey);
        signature.update(signString.getBytes(StandardCharsets.UTF_8));
        byte[] signBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signBytes);
    }
    
    /**
     * 加载私钥
     * @param privateKeyPath 私钥文件路径
     * @return 私钥对象
     * @throws Exception 加载异常
     */
    private static PrivateKey loadPrivateKey(String privateKeyPath) throws Exception {
        String privateKeyContent = new String(Files.readAllBytes(Paths.get(privateKeyPath)), StandardCharsets.UTF_8);
        privateKeyContent = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
    
    /**
     * 加载证书
     * @param certificatePath 证书文件路径
     * @return 证书对象
     * @throws Exception 加载异常
     */
    public static X509Certificate loadCertificate(String certificatePath) throws Exception {
        try (InputStream inputStream = Files.newInputStream(Paths.get(certificatePath))) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certificateFactory.generateCertificate(inputStream);
        }
    }
    
    /**
     * 构建Authorization头
     * @param mchId 商户号
     * @param serialNo 证书序列号
     * @param timestamp 时间戳
     * @param nonceStr 随机字符串
     * @param signature 签名
     * @return Authorization头值
     */
    public static String buildAuthorizationHeader(String mchId, String serialNo, long timestamp, String nonceStr, String signature) {
        return String.format("WECHATPAY2-SHA256-RSA2048 mchid=\"%s\",nonce_str=\"%s\",signature=\"%s\",timestamp=\"%d\",serial_no=\"%s\"",
                mchId, nonceStr, signature, timestamp, serialNo);
    }
    
    /**
     * 验证微信支付回调签名
     * @param timestamp 时间戳
     * @param nonceStr 随机字符串
     * @param body 回调体
     * @param signature 签名
     * @param certificatePath 证书路径
     * @return 验证结果
     */
    public static boolean verifySignature(String timestamp, String nonceStr, String body, String signature, String certificatePath) {
        try {
            String signString = timestamp + "\n" + nonceStr + "\n" + body + "\n";
            X509Certificate certificate = loadCertificate(certificatePath);
            PublicKey publicKey = certificate.getPublicKey();
            
            Signature sig = Signature.getInstance(ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(signString.getBytes(StandardCharsets.UTF_8));
            
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            logger.error("验证签名失败", e);
            return false;
        }
    }
    
    /**
     * 发送POST请求
     * @param url 请求URL
     * @param requestBody 请求体
     * @param authorizationHeader Authorization头
     * @return 响应内容
     * @throws IOException IO异常
     */
    public static String sendPostRequest(String url, String requestBody, String authorizationHeader) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Authorization", authorizationHeader);
            httpPost.setHeader("User-Agent", "wechatpay-java");
            
            if (requestBody != null) {
                StringEntity entity = new StringEntity(requestBody, StandardCharsets.UTF_8);
                httpPost.setEntity(entity);
            }
            
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
                return null;
            }
        }
    }
    
    /**
     * 发送GET请求
     * @param url 请求URL
     * @param authorizationHeader Authorization头
     * @return 响应内容
     * @throws IOException IO异常
     */
    public static String sendGetRequest(String url, String authorizationHeader) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Authorization", authorizationHeader);
            httpGet.setHeader("User-Agent", "wechatpay-java");
            
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
                return null;
            }
        }
    }
    
    /**
     * 对象转JSON字符串
     * @param obj 对象
     * @return JSON字符串
     * @throws JsonProcessingException JSON处理异常
     */
    public static String toJsonString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
    
    /**
     * JSON字符串转对象
     * @param json JSON字符串
     * @param clazz 目标类型
     * @param <T> 泛型
     * @return 对象实例
     * @throws JsonProcessingException JSON处理异常
     */
    public static <T> T fromJsonString(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }
    
    /**
     * 生成微信小程序支付参数
     * @param appId 应用ID
     * @param prepayId 预支付ID
     * @param privateKeyPath 私钥路径
     * @return 支付参数
     * @throws Exception 异常
     */
    public static WeChatPayParams generateMiniProgramPayParams(String appId, String prepayId, String privateKeyPath) throws Exception {
        long timestamp = getCurrentTimestamp();
        String nonceStr = generateNonceStr();
        String packageStr = "prepay_id=" + prepayId;
        String signType = "RSA";
        
        String signString = appId + "\n" + timestamp + "\n" + nonceStr + "\n" + packageStr + "\n";
        String paySign = sign(signString, privateKeyPath);
        
        WeChatPayParams params = new WeChatPayParams();
        params.setAppId(appId);
        params.setTimeStamp(String.valueOf(timestamp));
        params.setNonceStr(nonceStr);
        params.setPackageValue(packageStr);
        params.setSignType(signType);
        params.setPaySign(paySign);
        
        return params;
    }
    
    /**
     * 微信支付参数类
     */
    public static class WeChatPayParams {
        private String appId;
        private String timeStamp;
        private String nonceStr;
        private String packageValue;
        private String signType;
        private String paySign;
        
        // Getters and Setters
        public String getAppId() {
            return appId;
        }
        
        public void setAppId(String appId) {
            this.appId = appId;
        }
        
        public String getTimeStamp() {
            return timeStamp;
        }
        
        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }
        
        public String getNonceStr() {
            return nonceStr;
        }
        
        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }
        
        public String getPackageValue() {
            return packageValue;
        }
        
        public void setPackageValue(String packageValue) {
            this.packageValue = packageValue;
        }
        
        public String getSignType() {
            return signType;
        }
        
        public void setSignType(String signType) {
            this.signType = signType;
        }
        
        public String getPaySign() {
            return paySign;
        }
        
        public void setPaySign(String paySign) {
            this.paySign = paySign;
        }
    }
}