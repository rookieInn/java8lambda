package com.example.wechatpay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库表结构管理工具类
 * 提供添加字段、查询表信息等功能
 */
@Component
public class DatabaseSchemaUtils {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSchemaUtils.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseSchemaUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 获取数据库中所有表名
     */
    public List<String> getAllTableNames() {
        List<String> tableNames = new ArrayList<>();
        try {
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            String catalog = jdbcTemplate.getDataSource().getConnection().getCatalog();
            
            // 获取表信息
            ResultSet tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
            tables.close();
        } catch (SQLException e) {
            logger.error("获取表名失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取表名失败", e);
        }
        return tableNames;
    }

    /**
     * 检查表是否存在指定字段
     */
    public boolean columnExists(String tableName, String columnName) {
        try {
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            String catalog = jdbcTemplate.getDataSource().getConnection().getCatalog();
            
            ResultSet columns = metaData.getColumns(catalog, null, tableName, columnName);
            boolean exists = columns.next();
            columns.close();
            return exists;
        } catch (SQLException e) {
            logger.error("检查字段是否存在失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取表的所有列信息
     */
    public List<ColumnInfo> getTableColumns(String tableName) {
        List<ColumnInfo> columns = new ArrayList<>();
        try {
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            String catalog = jdbcTemplate.getDataSource().getConnection().getCatalog();
            
            ResultSet rs = metaData.getColumns(catalog, null, tableName, "%");
            while (rs.next()) {
                ColumnInfo column = new ColumnInfo();
                column.setColumnName(rs.getString("COLUMN_NAME"));
                column.setDataType(rs.getString("TYPE_NAME"));
                column.setColumnSize(rs.getInt("COLUMN_SIZE"));
                column.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                column.setDefaultValue(rs.getString("COLUMN_DEF"));
                columns.add(column);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("获取表列信息失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取表列信息失败", e);
        }
        return columns;
    }

    /**
     * 为单个表添加字段
     */
    public void addColumnToTable(String tableName, String columnName, String columnType, boolean nullable, String defaultValue) {
        // 检查字段是否已存在
        if (columnExists(tableName, columnName)) {
            logger.warn("表 {} 已存在字段 {}, 跳过添加", tableName, columnName);
            return;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(tableName)
           .append(" ADD COLUMN ").append(columnName)
           .append(" ").append(columnType);

        if (!nullable) {
            sql.append(" NOT NULL");
        }

        if (defaultValue != null && !defaultValue.trim().isEmpty()) {
            sql.append(" DEFAULT ").append(defaultValue);
        }

        try {
            jdbcTemplate.execute(sql.toString());
            logger.info("成功为表 {} 添加字段 {} {}", tableName, columnName, columnType);
        } catch (Exception e) {
            logger.error("为表 {} 添加字段 {} 失败: {}", tableName, columnName, e.getMessage(), e);
            throw new RuntimeException("添加字段失败", e);
        }
    }

    /**
     * 为所有表添加相同字段
     */
    public Map<String, Boolean> addColumnToAllTables(String columnName, String columnType, boolean nullable, String defaultValue) {
        List<String> tableNames = getAllTableNames();
        Map<String, Boolean> results = new HashMap<>();

        logger.info("开始为 {} 个表添加字段 {}", tableNames.size(), columnName);

        for (String tableName : tableNames) {
            try {
                addColumnToTable(tableName, columnName, columnType, nullable, defaultValue);
                results.put(tableName, true);
            } catch (Exception e) {
                logger.error("为表 {} 添加字段失败: {}", tableName, e.getMessage());
                results.put(tableName, false);
            }
        }

        long successCount = results.values().stream().mapToLong(success -> success ? 1 : 0).sum();
        logger.info("字段添加完成，成功: {}, 失败: {}", successCount, tableNames.size() - successCount);

        return results;
    }

    /**
     * 批量为指定表添加字段
     */
    public Map<String, Boolean> addColumnToTables(List<String> tableNames, String columnName, String columnType, boolean nullable, String defaultValue) {
        Map<String, Boolean> results = new HashMap<>();

        logger.info("开始为指定的 {} 个表添加字段 {}", tableNames.size(), columnName);

        for (String tableName : tableNames) {
            try {
                addColumnToTable(tableName, columnName, columnType, nullable, defaultValue);
                results.put(tableName, true);
            } catch (Exception e) {
                logger.error("为表 {} 添加字段失败: {}", tableName, e.getMessage());
                results.put(tableName, false);
            }
        }

        long successCount = results.values().stream().mapToLong(success -> success ? 1 : 0).sum();
        logger.info("字段添加完成，成功: {}, 失败: {}", successCount, tableNames.size() - successCount);

        return results;
    }

    /**
     * 删除表中的字段
     */
    public void dropColumnFromTable(String tableName, String columnName) {
        if (!columnExists(tableName, columnName)) {
            logger.warn("表 {} 不存在字段 {}, 跳过删除", tableName, columnName);
            return;
        }

        String sql = "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
        try {
            jdbcTemplate.execute(sql);
            logger.info("成功从表 {} 删除字段 {}", tableName, columnName);
        } catch (Exception e) {
            logger.error("从表 {} 删除字段 {} 失败: {}", tableName, columnName, e.getMessage(), e);
            throw new RuntimeException("删除字段失败", e);
        }
    }

    /**
     * 获取数据库类型
     */
    public String getDatabaseType() {
        try {
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            return metaData.getDatabaseProductName().toLowerCase();
        } catch (SQLException e) {
            logger.error("获取数据库类型失败: {}", e.getMessage(), e);
            return "unknown";
        }
    }

    /**
     * 列信息类
     */
    public static class ColumnInfo {
        private String columnName;
        private String dataType;
        private int columnSize;
        private boolean nullable;
        private String defaultValue;

        // Getters and Setters
        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public int getColumnSize() {
            return columnSize;
        }

        public void setColumnSize(int columnSize) {
            this.columnSize = columnSize;
        }

        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public String toString() {
            return "ColumnInfo{" +
                    "columnName='" + columnName + '\'' +
                    ", dataType='" + dataType + '\'' +
                    ", columnSize=" + columnSize +
                    ", nullable=" + nullable +
                    ", defaultValue='" + defaultValue + '\'' +
                    '}';
        }
    }
}