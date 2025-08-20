package com.extrigger.wechatpay;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付工具类
 * 提供签名生成、XML解析、随机字符串生成等工具方法
 */
public class WeChatPayUtils {
    
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "MD5";
    
    /**
     * 生成随机字符串
     */
    public static String generateNonceStr() {
        return generateNonceStr(32);
    }
    
    /**
     * 生成指定长度的随机字符串
     */
    public static String generateNonceStr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    /**
     * 生成时间戳
     */
    public static String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
    
    /**
     * 生成MD5签名
     */
    public static String generateSign(Map<String, String> data, String key) {
        String signStr = buildSignString(data, key);
        return md5(signStr);
    }
    
    /**
     * 构建签名字符串
     */
    public static String buildSignString(Map<String, String> data, String key) {
        // 过滤空值和sign参数
        Map<String, String> filteredData = filterEmptyValues(data);
        
        // 按参数名ASCII码从小到大排序
        List<String> keyList = new ArrayList<>(filteredData.keySet());
        Collections.sort(keyList);
        
        // 构建签名字符串
        StringBuilder sb = new StringBuilder();
        for (String keyName : keyList) {
            if (!"sign".equals(keyName)) {
                sb.append(keyName).append("=").append(filteredData.get(keyName)).append("&");
            }
        }
        
        // 加上商户密钥
        sb.append("key=").append(key);
        
        return sb.toString();
    }
    
    /**
     * 过滤空值
     */
    private static Map<String, String> filterEmptyValues(Map<String, String> data) {
        Map<String, String> filtered = new java.util.HashMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filtered.put(entry.getKey(), entry.getValue());
            }
        }
        return filtered;
    }
    
    /**
     * MD5加密
     */
    public static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(data.getBytes(CHARSET));
            return bytesToHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported encoding: " + CHARSET, e);
        }
    }
    
    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }
    
    /**
     * 验证签名
     */
    public static boolean verifySign(Map<String, String> data, String key, String sign) {
        String calculatedSign = generateSign(data, key);
        return calculatedSign.equals(sign);
    }
    
    /**
     * 将Map转换为XML字符串
     */
    public static String mapToXml(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            if (value != null) {
                sb.append("<").append(key).append(">");
                sb.append("<![CDATA[").append(value).append("]]>");
                sb.append("</").append(key).append(">");
            }
        }
        
        sb.append("</xml>");
        return sb.toString();
    }
    
    /**
     * 将XML字符串转换为Map
     */
    public static Map<String, String> xmlToMap(String xml) {
        // 这里简化实现，实际项目中建议使用DOM4J或JAXB等XML解析库
        Map<String, String> result = new java.util.HashMap<>();
        
        // 简单的XML解析实现
        xml = xml.replaceAll("<\\?xml[^>]*>", ""); // 移除XML声明
        xml = xml.replaceAll("<!\\[CDATA\\[|\\]\\]>", ""); // 移除CDATA标记
        
        String[] lines = xml.split(">");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("</") || line.startsWith("<xml")) {
                continue;
            }
            
            if (line.startsWith("<") && line.contains("</")) {
                String tagName = line.substring(1, line.indexOf("</"));
                String content = line.substring(line.indexOf("</") + 2);
                if (!tagName.isEmpty() && !content.isEmpty()) {
                    result.put(tagName, content);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 金额转换：元转分
     */
    public static int yuanToFen(double yuan) {
        return (int) Math.round(yuan * 100);
    }
    
    /**
     * 金额转换：分转元
     */
    public static double fenToYuan(int fen) {
        return fen / 100.0;
    }
    
    /**
     * 格式化金额显示
     */
    public static String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }
    
    /**
     * 生成商户订单号
     */
    public static String generateOutTradeNo() {
        return "ORDER" + System.currentTimeMillis() + generateNonceStr(6);
    }
    
    /**
     * 生成商户退款单号
     */
    public static String generateOutRefundNo() {
        return "REFUND" + System.currentTimeMillis() + generateNonceStr(6);
    }
}