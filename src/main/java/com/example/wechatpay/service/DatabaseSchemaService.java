package com.example.wechatpay.service;

import com.example.wechatpay.util.DatabaseSchemaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 数据库表结构管理服务类
 * 提供批量添加字段、管理表结构等功能
 */
@Service
public class DatabaseSchemaService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSchemaService.class);

    @Autowired
    private DatabaseSchemaUtils databaseSchemaUtils;

    /**
     * 为所有表添加字段
     */
    public SchemaUpdateResult addFieldToAllTables(String fieldName, String fieldType, boolean nullable, String defaultValue) {
        logger.info("开始为所有表添加字段: {} {} nullable={} default={}", fieldName, fieldType, nullable, defaultValue);
        
        List<String> allTables = databaseSchemaUtils.getAllTableNames();
        Map<String, Boolean> results = databaseSchemaUtils.addColumnToAllTables(fieldName, fieldType, nullable, defaultValue);
        
        return new SchemaUpdateResult(fieldName, allTables.size(), results);
    }

    /**
     * 为指定表列表添加字段
     */
    public SchemaUpdateResult addFieldToSpecificTables(List<String> tableNames, String fieldName, String fieldType, boolean nullable, String defaultValue) {
        logger.info("开始为指定表添加字段: {} {} nullable={} default={}, 表数量: {}", fieldName, fieldType, nullable, defaultValue, tableNames.size());
        
        Map<String, Boolean> results = databaseSchemaUtils.addColumnToTables(tableNames, fieldName, fieldType, nullable, defaultValue);
        
        return new SchemaUpdateResult(fieldName, tableNames.size(), results);
    }

    /**
     * 为所有表添加创建时间字段
     */
    public SchemaUpdateResult addCreatedAtToAllTables() {
        String databaseType = databaseSchemaUtils.getDatabaseType();
        String columnType;
        String defaultValue;
        
        if (databaseType.contains("mysql")) {
            columnType = "TIMESTAMP";
            defaultValue = "CURRENT_TIMESTAMP";
        } else if (databaseType.contains("postgresql")) {
            columnType = "TIMESTAMP";
            defaultValue = "CURRENT_TIMESTAMP";
        } else {
            columnType = "TIMESTAMP";
            defaultValue = "CURRENT_TIMESTAMP";
        }
        
        return addFieldToAllTables("created_at", columnType, false, defaultValue);
    }

    /**
     * 为所有表添加更新时间字段
     */
    public SchemaUpdateResult addUpdatedAtToAllTables() {
        String databaseType = databaseSchemaUtils.getDatabaseType();
        String columnType;
        String defaultValue;
        
        if (databaseType.contains("mysql")) {
            columnType = "TIMESTAMP";
            defaultValue = "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP";
        } else if (databaseType.contains("postgresql")) {
            columnType = "TIMESTAMP";
            defaultValue = "CURRENT_TIMESTAMP";
        } else {
            columnType = "TIMESTAMP";
            defaultValue = "CURRENT_TIMESTAMP";
        }
        
        return addFieldToAllTables("updated_at", columnType, false, defaultValue);
    }

    /**
     * 为所有表添加版本字段（用于乐观锁）
     */
    public SchemaUpdateResult addVersionToAllTables() {
        return addFieldToAllTables("version", "INT", false, "0");
    }

    /**
     * 为所有表添加软删除字段
     */
    public SchemaUpdateResult addSoftDeleteToAllTables() {
        return addFieldToAllTables("deleted", "TINYINT(1)", false, "0");
    }

    /**
     * 批量添加常用字段（创建时间、更新时间、版本、软删除）
     */
    public Map<String, SchemaUpdateResult> addCommonFieldsToAllTables() {
        Map<String, SchemaUpdateResult> results = new HashMap<>();
        
        logger.info("开始为所有表批量添加常用字段");
        
        try {
            results.put("created_at", addCreatedAtToAllTables());
            results.put("updated_at", addUpdatedAtToAllTables());
            results.put("version", addVersionToAllTables());
            results.put("deleted", addSoftDeleteToAllTables());
            
            logger.info("批量添加常用字段完成");
        } catch (Exception e) {
            logger.error("批量添加常用字段失败: {}", e.getMessage(), e);
            throw e;
        }
        
        return results;
    }

    /**
     * 获取所有表的基本信息
     */
    public List<TableInfo> getAllTablesInfo() {
        List<String> tableNames = databaseSchemaUtils.getAllTableNames();
        List<TableInfo> tablesInfo = new ArrayList<>();
        
        for (String tableName : tableNames) {
            try {
                List<DatabaseSchemaUtils.ColumnInfo> columns = databaseSchemaUtils.getTableColumns(tableName);
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setColumnCount(columns.size());
                tableInfo.setColumns(columns);
                tablesInfo.add(tableInfo);
            } catch (Exception e) {
                logger.error("获取表 {} 信息失败: {}", tableName, e.getMessage());
            }
        }
        
        return tablesInfo;
    }

    /**
     * 表结构更新结果类
     */
    public static class SchemaUpdateResult {
        private String fieldName;
        private int totalTables;
        private int successCount;
        private int failureCount;
        private Map<String, Boolean> tableResults;
        private List<String> successTables;
        private List<String> failureTables;

        public SchemaUpdateResult(String fieldName, int totalTables, Map<String, Boolean> results) {
            this.fieldName = fieldName;
            this.totalTables = totalTables;
            this.tableResults = results;
            this.successTables = new ArrayList<>();
            this.failureTables = new ArrayList<>();
            
            for (Map.Entry<String, Boolean> entry : results.entrySet()) {
                if (entry.getValue()) {
                    successTables.add(entry.getKey());
                    successCount++;
                } else {
                    failureTables.add(entry.getKey());
                    failureCount++;
                }
            }
        }

        // Getters
        public String getFieldName() { return fieldName; }
        public int getTotalTables() { return totalTables; }
        public int getSuccessCount() { return successCount; }
        public int getFailureCount() { return failureCount; }
        public Map<String, Boolean> getTableResults() { return tableResults; }
        public List<String> getSuccessTables() { return successTables; }
        public List<String> getFailureTables() { return failureTables; }

        @Override
        public String toString() {
            return String.format("SchemaUpdateResult{fieldName='%s', totalTables=%d, successCount=%d, failureCount=%d}",
                    fieldName, totalTables, successCount, failureCount);
        }
    }

    /**
     * 表信息类
     */
    public static class TableInfo {
        private String tableName;
        private int columnCount;
        private List<DatabaseSchemaUtils.ColumnInfo> columns;

        // Getters and Setters
        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }
        public int getColumnCount() { return columnCount; }
        public void setColumnCount(int columnCount) { this.columnCount = columnCount; }
        public List<DatabaseSchemaUtils.ColumnInfo> getColumns() { return columns; }
        public void setColumns(List<DatabaseSchemaUtils.ColumnInfo> columns) { this.columns = columns; }

        @Override
        public String toString() {
            return String.format("TableInfo{tableName='%s', columnCount=%d}", tableName, columnCount);
        }
    }
}