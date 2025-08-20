package com.example.wechatpay.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Random;

/**
 * 签名工具类
 * 处理微信支付的签名生成和验证
 */
public class SignatureUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(SignatureUtil.class);
    
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();
    
    /**
     * 生成签名
     * 
     * @param message 待签名的消息
     * @param privateKey 私钥
     * @return 签名结果（Base64编码）
     */
    public static String sign(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(signature.sign());
    }
    
    /**
     * 验证签名
     * 
     * @param message 原始消息
     * @param signature 签名（Base64编码）
     * @param publicKey 公钥
     * @return 验证结果
     */
    public static boolean verify(String message, String signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(message.getBytes(StandardCharsets.UTF_8));
        return sig.verify(Base64.decodeBase64(signature));
    }
    
    /**
     * 构建签名串
     * 
     * @param method HTTP方法
     * @param url URL路径
     * @param timestamp 时间戳
     * @param nonceStr 随机串
     * @param body 请求体
     * @return 签名串
     */
    public static String buildSignMessage(String method, String url, long timestamp, 
                                         String nonceStr, String body) {
        return method + "\n"
                + url + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }
    
    /**
     * 生成Authorization头
     * 
     * @param mchId 商户号
     * @param serialNo 证书序列号
     * @param nonceStr 随机串
     * @param timestamp 时间戳
     * @param signature 签名
     * @return Authorization头值
     */
    public static String getAuthorization(String mchId, String serialNo, String nonceStr,
                                         long timestamp, String signature) {
        return String.format("WECHATPAY2-SHA256-RSA2048 mchid=\"%s\",serial_no=\"%s\","
                + "nonce_str=\"%s\",timestamp=\"%d\",signature=\"%s\"",
                mchId, serialNo, nonceStr, timestamp, signature);
    }
    
    /**
     * 生成随机字符串
     * 
     * @param length 长度
     * @return 随机字符串
     */
    public static String generateNonceStr(int length) {
        char[] nonceChars = new char[length];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
    
    /**
     * 生成随机字符串（默认32位）
     * 
     * @return 随机字符串
     */
    public static String generateNonceStr() {
        return generateNonceStr(32);
    }
    
    /**
     * 加载私钥
     * 
     * @param privateKeyStr 私钥字符串（PEM格式）
     * @return 私钥对象
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        String privateKey = privateKeyStr
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
    
    /**
     * AES-GCM解密
     * 用于解密微信支付回调通知的敏感信息
     * 
     * @param associatedData 附加数据
     * @param nonce 随机串
     * @param ciphertext 密文
     * @param apiV3Key API v3密钥
     * @return 解密后的明文
     */
    public static String decryptAesGcm(String associatedData, String nonce, 
                                      String ciphertext, String apiV3Key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec key = new SecretKeySpec(apiV3Key.getBytes(StandardCharsets.UTF_8), "AES");
        GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));
        
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));
        
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(ciphertext));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    
    /**
     * HMAC-SHA256签名
     * 用于API v2版本的签名
     * 
     * @param data 待签名数据
     * @param key 密钥
     * @return 签名结果（十六进制字符串）
     */
    public static String hmacSha256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
    
    /**
     * MD5签名
     * 用于API v2版本的签名（不推荐，建议使用HMAC-SHA256）
     * 
     * @param data 待签名数据
     * @return 签名结果（十六进制字符串）
     */
    public static String md5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
}