package com.example.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 增强版数据库字段添加工具类
 * 支持事务管理、批量操作、回滚机制
 */
@Slf4j
public class EnhancedDatabaseFieldAdder {
    
    private HikariDataSource dataSource;
    private String databaseType;
    private Properties config;
    private List<String> executedTables = new ArrayList<>();
    private Map<String, String> failedTables = new HashMap<>();
    
    /**
     * 构造函数，初始化数据库连接池
     */
    public EnhancedDatabaseFieldAdder(String configFile) throws IOException {
        loadConfiguration(configFile);
        initializeDataSource();
        detectDatabaseType();
    }
    
    /**
     * 加载配置文件
     */
    private void loadConfiguration(String configFile) throws IOException {
        config = new Properties();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            config.load(fis);
        }
        log.info("配置文件加载成功: {}", configFile);
    }
    
    /**
     * 初始化数据源
     */
    private void initializeDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getProperty("database.url"));
        hikariConfig.setUsername(config.getProperty("database.username"));
        hikariConfig.setPassword(config.getProperty("database.password"));
        hikariConfig.setDriverClassName(config.getProperty("database.driver"));
        
        // 连接池配置
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setAutoCommit(false); // 关闭自动提交以支持事务
        
        dataSource = new HikariDataSource(hikariConfig);
        log.info("数据库连接池初始化成功");
    }
    
    /**
     * 检测数据库类型
     */
    private void detectDatabaseType() {
        String jdbcUrl = config.getProperty("database.url").toLowerCase();
        if (jdbcUrl.contains("mysql")) {
            databaseType = "mysql";
        } else if (jdbcUrl.contains("postgresql")) {
            databaseType = "postgresql";
        } else if (jdbcUrl.contains("oracle")) {
            databaseType = "oracle";
        } else if (jdbcUrl.contains("sqlserver")) {
            databaseType = "sqlserver";
        } else if (jdbcUrl.contains("h2")) {
            databaseType = "h2";
        } else {
            databaseType = "unknown";
        }
        log.info("检测到数据库类型: {}", databaseType);
    }
    
    /**
     * 获取所有表名
     */
    public List<String> getAllTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();
        String schema = config.getProperty("database.schema", null);
        
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String catalog = "oracle".equals(databaseType) ? schema : conn.getCatalog();
            
            ResultSet rs = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"});
            
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (!isSystemTable(tableName)) {
                    tableNames.add(tableName);
                }
            }
            
            conn.commit();
        }
        
        log.info("找到 {} 个用户表", tableNames.size());
        return tableNames;
    }
    
    /**
     * 判断是否为系统表
     */
    private boolean isSystemTable(String tableName) {
        String lowerTableName = tableName.toLowerCase();
        switch (databaseType) {
            case "mysql":
                return lowerTableName.startsWith("mysql") || 
                       lowerTableName.equals("information_schema") ||
                       lowerTableName.equals("performance_schema") ||
                       lowerTableName.equals("sys");
            case "postgresql":
                return lowerTableName.startsWith("pg_") || 
                       lowerTableName.equals("information_schema");
            case "oracle":
                return lowerTableName.startsWith("sys") || 
                       lowerTableName.startsWith("dba_") ||
                       lowerTableName.startsWith("user_") ||
                       lowerTableName.startsWith("all_");
            case "sqlserver":
                return lowerTableName.startsWith("sys") || 
                       lowerTableName.equals("information_schema");
            default:
                return false;
        }
    }
    
    /**
     * 检查表中是否已存在指定字段
     */
    private boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        String schema = config.getProperty("database.schema", null);
        DatabaseMetaData metaData = conn.getMetaData();
        
        try (ResultSet rs = metaData.getColumns(null, schema, tableName, columnName.toUpperCase())) {
            if (rs.next()) {
                return true;
            }
        }
        
        // 某些数据库区分大小写，再尝试小写
        try (ResultSet rs = metaData.getColumns(null, schema, tableName, columnName.toLowerCase())) {
            return rs.next();
        }
    }
    
    /**
     * 为所有表添加字段（带事务管理）
     */
    public void addFieldToAllTablesWithTransaction(String fieldName, String fieldType, 
                                                  String defaultValue, boolean notNull, String comment) {
        Connection conn = null;
        boolean useTransaction = Boolean.parseBoolean(config.getProperty("use.transaction", "true"));
        
        try {
            conn = dataSource.getConnection();
            if (useTransaction) {
                conn.setAutoCommit(false);
            }
            
            List<String> tableNames = getAllTableNames();
            
            if (tableNames.isEmpty()) {
                log.warn("未找到任何用户表");
                return;
            }
            
            log.info("开始为 {} 个表添加字段 {}, 事务模式: {}", 
                    tableNames.size(), fieldName, useTransaction ? "开启" : "关闭");
            
            // 执行字段添加
            for (String tableName : tableNames) {
                try {
                    addFieldToTableInTransaction(conn, tableName, fieldName, fieldType, 
                                               defaultValue, notNull, comment);
                    executedTables.add(tableName);
                } catch (SQLException e) {
                    String errorMsg = e.getMessage();
                    log.error("为表 {} 添加字段失败: {}", tableName, errorMsg);
                    failedTables.put(tableName, errorMsg);
                    
                    if (useTransaction) {
                        throw e; // 事务模式下，一个失败全部回滚
                    }
                }
            }
            
            if (useTransaction) {
                conn.commit();
                log.info("事务提交成功");
            }
            
            printSummary();
            
        } catch (SQLException e) {
            log.error("批量添加字段失败: {}", e.getMessage(), e);
            if (conn != null && useTransaction) {
                try {
                    conn.rollback();
                    log.info("事务已回滚");
                } catch (SQLException rollbackEx) {
                    log.error("事务回滚失败", rollbackEx);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("关闭连接失败", e);
                }
            }
        }
    }
    
    /**
     * 在事务中为单个表添加字段
     */
    private void addFieldToTableInTransaction(Connection conn, String tableName, String fieldName, 
                                            String fieldType, String defaultValue, boolean notNull, 
                                            String comment) throws SQLException {
        // 检查字段是否已存在
        if (columnExists(conn, tableName, fieldName)) {
            log.info("表 {} 中已存在字段 {}, 跳过", tableName, fieldName);
            return;
        }
        
        // 构建并执行ALTER TABLE语句
        String alterTableSql = buildAlterTableSql(tableName, fieldName, fieldType, 
                                                 defaultValue, notNull, comment);
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(alterTableSql);
            log.info("成功为表 {} 添加字段 {}", tableName, fieldName);
            
            // PostgreSQL需要单独添加注释
            if ("postgresql".equals(databaseType) && comment != null && !comment.isEmpty()) {
                String commentSql = String.format("COMMENT ON COLUMN %s.%s IS '%s'", 
                                                tableName, fieldName, comment);
                stmt.execute(commentSql);
            }
        }
    }
    
    /**
     * 构建ALTER TABLE SQL语句
     */
    private String buildAlterTableSql(String tableName, String fieldName, String fieldType,
                                     String defaultValue, boolean notNull, String comment) {
        StringBuilder sql = new StringBuilder();
        
        switch (databaseType) {
            case "mysql":
                sql.append("ALTER TABLE `").append(tableName).append("`")
                   .append(" ADD COLUMN `").append(fieldName).append("` ").append(fieldType);
                if (notNull) {
                    sql.append(" NOT NULL");
                }
                if (defaultValue != null && !defaultValue.isEmpty()) {
                    sql.append(" DEFAULT ").append(formatDefaultValue(defaultValue, fieldType));
                }
                if (comment != null && !comment.isEmpty()) {
                    sql.append(" COMMENT '").append(comment.replace("'", "''")).append("'");
                }
                break;
                
            case "postgresql":
                sql.append("ALTER TABLE \"").append(tableName).append("\"")
                   .append(" ADD COLUMN \"").append(fieldName).append("\" ").append(fieldType);
                if (notNull && defaultValue != null) {
                    sql.append(" NOT NULL DEFAULT ").append(formatDefaultValue(defaultValue, fieldType));
                } else if (notNull) {
                    sql.append(" NOT NULL");
                } else if (defaultValue != null) {
                    sql.append(" DEFAULT ").append(formatDefaultValue(defaultValue, fieldType));
                }
                break;
                
            case "oracle":
                sql.append("ALTER TABLE ").append(tableName)
                   .append(" ADD ").append(fieldName).append(" ").append(fieldType);
                if (defaultValue != null && !defaultValue.isEmpty()) {
                    sql.append(" DEFAULT ").append(formatDefaultValue(defaultValue, fieldType));
                }
                if (notNull) {
                    sql.append(" NOT NULL");
                }
                break;
                
            case "sqlserver":
                sql.append("ALTER TABLE [").append(tableName).append("]")
                   .append(" ADD [").append(fieldName).append("] ").append(fieldType);
                if (notNull) {
                    sql.append(" NOT NULL");
                }
                if (defaultValue != null && !defaultValue.isEmpty()) {
                    sql.append(" DEFAULT ").append(formatDefaultValue(defaultValue, fieldType));
                }
                break;
                
            default:
                sql.append("ALTER TABLE ").append(tableName)
                   .append(" ADD COLUMN ").append(fieldName).append(" ").append(fieldType);
                if (notNull) {
                    sql.append(" NOT NULL");
                }
                if (defaultValue != null && !defaultValue.isEmpty()) {
                    sql.append(" DEFAULT ").append(formatDefaultValue(defaultValue, fieldType));
                }
        }
        
        return sql.toString();
    }
    
    /**
     * 格式化默认值
     */
    private String formatDefaultValue(String defaultValue, String fieldType) {
        String upperFieldType = fieldType.toUpperCase();
        String upperDefaultValue = defaultValue.toUpperCase();
        
        // 特殊函数处理
        if (upperDefaultValue.equals("CURRENT_TIMESTAMP") || 
            upperDefaultValue.equals("NOW()") ||
            upperDefaultValue.equals("GETDATE()") ||
            upperDefaultValue.equals("SYSDATE") ||
            upperDefaultValue.equals("CURRENT_DATE")) {
            
            switch (databaseType) {
                case "mysql":
                    return "CURRENT_TIMESTAMP";
                case "postgresql":
                    return "CURRENT_TIMESTAMP";
                case "oracle":
                    return "SYSDATE";
                case "sqlserver":
                    return "GETDATE()";
                default:
                    return "CURRENT_TIMESTAMP";
            }
        }
        
        // 判断是否是字符串类型
        if (upperFieldType.contains("CHAR") || upperFieldType.contains("TEXT") || 
            upperFieldType.contains("VARCHAR") || upperFieldType.contains("STRING")) {
            return "'" + defaultValue.replace("'", "''") + "'";
        }
        
        // 判断是否是日期时间类型
        if (upperFieldType.contains("DATE") || upperFieldType.contains("TIME")) {
            if (!upperDefaultValue.contains("(") && !upperDefaultValue.equals("SYSDATE")) {
                return "'" + defaultValue + "'";
            }
            return defaultValue;
        }
        
        // 布尔类型
        if (upperFieldType.contains("BOOL") || upperFieldType.contains("BOOLEAN")) {
            if (defaultValue.equalsIgnoreCase("true")) {
                return databaseType.equals("mysql") ? "1" : "TRUE";
            } else if (defaultValue.equalsIgnoreCase("false")) {
                return databaseType.equals("mysql") ? "0" : "FALSE";
            }
            return defaultValue;
        }
        
        // 数值类型直接返回
        return defaultValue;
    }
    
    /**
     * 打印执行摘要
     */
    private void printSummary() {
        log.info("========== 执行摘要 ==========");
        log.info("成功添加字段的表数量: {}", executedTables.size());
        log.info("失败的表数量: {}", failedTables.size());
        
        if (!failedTables.isEmpty()) {
            log.info("失败的表详情:");
            failedTables.forEach((table, error) -> {
                log.info("  表名: {}, 错误: {}", table, error);
            });
        }
        
        // 生成报告文件
        generateReport();
    }
    
    /**
     * 生成执行报告
     */
    private void generateReport() {
        try {
            String reportFile = "field_addition_report_" + System.currentTimeMillis() + ".txt";
            try (java.io.PrintWriter writer = new java.io.PrintWriter(reportFile)) {
                writer.println("数据库字段添加报告");
                writer.println("执行时间: " + new java.util.Date());
                writer.println("数据库类型: " + databaseType);
                writer.println("数据库URL: " + config.getProperty("database.url"));
                writer.println();
                
                writer.println("成功的表 (" + executedTables.size() + "):");
                executedTables.forEach(table -> writer.println("  - " + table));
                
                writer.println();
                writer.println("失败的表 (" + failedTables.size() + "):");
                failedTables.forEach((table, error) -> {
                    writer.println("  - " + table);
                    writer.println("    错误: " + error);
                });
            }
            log.info("执行报告已生成: {}", reportFile);
        } catch (Exception e) {
            log.error("生成报告失败", e);
        }
    }
    
    /**
     * 验证字段是否成功添加
     */
    public void verifyFieldAddition(String fieldName) {
        log.info("开始验证字段添加结果...");
        int verifiedCount = 0;
        int notFoundCount = 0;
        
        try (Connection conn = dataSource.getConnection()) {
            for (String tableName : executedTables) {
                if (columnExists(conn, tableName, fieldName)) {
                    verifiedCount++;
                } else {
                    notFoundCount++;
                    log.warn("表 {} 中未找到字段 {}", tableName, fieldName);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            log.error("验证失败", e);
        }
        
        log.info("验证完成: {} 个表确认有该字段, {} 个表未找到该字段", 
                verifiedCount, notFoundCount);
    }
    
    /**
     * 关闭数据源
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            log.info("数据库连接池已关闭");
        }
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }
        
        String configFile = args[0];
        String operation = args[1];
        
        try {
            EnhancedDatabaseFieldAdder adder = new EnhancedDatabaseFieldAdder(configFile);
            
            switch (operation.toLowerCase()) {
                case "list":
                    listTables(adder);
                    break;
                    
                case "add":
                    addFieldToAllTables(adder, configFile);
                    break;
                    
                case "verify":
                    if (args.length < 3) {
                        System.err.println("请指定要验证的字段名");
                        System.exit(1);
                    }
                    adder.verifyFieldAddition(args[2]);
                    break;
                    
                default:
                    System.err.println("未知操作: " + operation);
                    printUsage();
                    System.exit(1);
            }
            
            adder.close();
            
        } catch (Exception e) {
            log.error("程序执行失败", e);
            System.exit(1);
        }
    }
    
    /**
     * 列出所有表
     */
    private static void listTables(EnhancedDatabaseFieldAdder adder) throws SQLException {
        List<String> tables = adder.getAllTableNames();
        System.out.println("找到以下表:");
        tables.forEach(System.out::println);
        System.out.println("总计: " + tables.size() + " 个表");
    }
    
    /**
     * 为所有表添加字段
     */
    private static void addFieldToAllTables(EnhancedDatabaseFieldAdder adder, String configFile) 
            throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);
        }
        
        String fieldName = props.getProperty("field.name");
        String fieldType = props.getProperty("field.type");
        String defaultValue = props.getProperty("field.default");
        boolean notNull = Boolean.parseBoolean(props.getProperty("field.notNull", "false"));
        String comment = props.getProperty("field.comment");
        
        if (fieldName == null || fieldType == null) {
            System.err.println("配置文件中必须包含 field.name 和 field.type");
            System.exit(1);
        }
        
        System.out.println("准备添加字段:");
        System.out.println("  字段名: " + fieldName);
        System.out.println("  类型: " + fieldType);
        System.out.println("  默认值: " + (defaultValue != null ? defaultValue : "无"));
        System.out.println("  非空: " + notNull);
        System.out.println("  注释: " + (comment != null ? comment : "无"));
        System.out.println();
        
        adder.addFieldToAllTablesWithTransaction(fieldName, fieldType, defaultValue, notNull, comment);
    }
    
    /**
     * 打印使用说明
     */
    private static void printUsage() {
        System.out.println("使用方法: java EnhancedDatabaseFieldAdder <配置文件路径> <操作类型> [参数]");
        System.out.println();
        System.out.println("操作类型:");
        System.out.println("  list   - 列出所有表");
        System.out.println("  add    - 为所有表添加字段（从配置文件读取字段信息）");
        System.out.println("  verify - 验证字段是否添加成功（需要指定字段名）");
        System.out.println();
        System.out.println("示例:");
        System.out.println("  java EnhancedDatabaseFieldAdder database.properties list");
        System.out.println("  java EnhancedDatabaseFieldAdder database.properties add");
        System.out.println("  java EnhancedDatabaseFieldAdder database.properties verify created_time");
    }
}