package com.example.wechatpay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库字段管理服务类
 * 用于为数据库表添加字段
 * 
 * @author Generated
 * @date 2024
 */
@Service
public class ColumnManagementService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableManagementService tableManagementService;

    /**
     * 为单个表添加字段
     * 
     * @param tableName 表名
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param defaultValue 默认值（可选）
     * @param isNullable 是否允许为空
     * @param comment 字段注释（可选）
     * @return 操作结果
     */
    @Transactional
    public Map<String, Object> addColumnToTable(String tableName, String columnName, 
                                               String columnType, String defaultValue, 
                                               boolean isNullable, String comment) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查表是否存在
            if (!tableManagementService.tableExists(tableName)) {
                result.put("success", false);
                result.put("message", "表 " + tableName + " 不存在");
                return result;
            }

            // 检查字段是否已存在
            if (tableManagementService.columnExists(tableName, columnName)) {
                result.put("success", false);
                result.put("message", "表 " + tableName + " 中字段 " + columnName + " 已存在");
                return result;
            }

            // 构建ALTER TABLE语句
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN ");
            sql.append(columnName).append(" ").append(columnType);
            
            if (!isNullable) {
                sql.append(" NOT NULL");
            }
            
            if (defaultValue != null && !defaultValue.trim().isEmpty()) {
                sql.append(" DEFAULT ").append(defaultValue);
            }
            
            if (comment != null && !comment.trim().isEmpty()) {
                sql.append(" COMMENT '").append(comment).append("'");
            }

            // 执行SQL
            jdbcTemplate.execute(sql.toString());
            
            result.put("success", true);
            result.put("message", "成功为表 " + tableName + " 添加字段 " + columnName);
            result.put("sql", sql.toString());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加字段失败: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
        }
        
        return result;
    }

    /**
     * 为所有表添加相同字段
     * 
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param defaultValue 默认值（可选）
     * @param isNullable 是否允许为空
     * @param comment 字段注释（可选）
     * @return 操作结果列表
     */
    @Transactional
    public List<Map<String, Object>> addColumnToAllTables(String columnName, String columnType, 
                                                         String defaultValue, boolean isNullable, 
                                                         String comment) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try {
            // 获取所有表名
            List<String> tableNames = tableManagementService.getAllTableNames();
            
            if (tableNames.isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "数据库中没有找到任何表");
                results.add(result);
                return results;
            }

            // 为每个表添加字段
            for (String tableName : tableNames) {
                Map<String, Object> result = addColumnToTable(tableName, columnName, columnType, 
                                                            defaultValue, isNullable, comment);
                result.put("tableName", tableName);
                results.add(result);
            }
            
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "批量添加字段失败: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
            results.add(result);
        }
        
        return results;
    }

    /**
     * 为指定表列表添加字段
     * 
     * @param tableNames 表名列表
     * @param columnName 字段名
     * @param columnType 字段类型
     * @param defaultValue 默认值（可选）
     * @param isNullable 是否允许为空
     * @param comment 字段注释（可选）
     * @return 操作结果列表
     */
    @Transactional
    public List<Map<String, Object>> addColumnToSpecificTables(List<String> tableNames, 
                                                              String columnName, String columnType, 
                                                              String defaultValue, boolean isNullable, 
                                                              String comment) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        if (tableNames == null || tableNames.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "表名列表不能为空");
            results.add(result);
            return results;
        }

        // 为每个指定的表添加字段
        for (String tableName : tableNames) {
            Map<String, Object> result = addColumnToTable(tableName, columnName, columnType, 
                                                        defaultValue, isNullable, comment);
            result.put("tableName", tableName);
            results.add(result);
        }
        
        return results;
    }

    /**
     * 获取常用的字段类型
     * 
     * @return 字段类型列表
     */
    public List<String> getCommonColumnTypes() {
        List<String> types = new ArrayList<>();
        types.add("VARCHAR(255)");
        types.add("TEXT");
        types.add("INT");
        types.add("BIGINT");
        types.add("DECIMAL(10,2)");
        types.add("DATETIME");
        types.add("DATE");
        types.add("TIME");
        types.add("TIMESTAMP");
        types.add("BOOLEAN");
        types.add("TINYINT(1)");
        types.add("JSON");
        return types;
    }
}