package com.example.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 数据库字段添加工具类
 * 支持向数据库的所有表添加指定字段
 */
@Slf4j
public class DatabaseFieldAdder {
    
    private HikariDataSource dataSource;
    private String databaseType;
    private Properties config;
    
    /**
     * 构造函数，初始化数据库连接池
     * @param configFile 配置文件路径
     */
    public DatabaseFieldAdder(String configFile) throws IOException {
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
     * @return 表名列表
     */
    public List<String> getAllTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();
        String schema = config.getProperty("database.schema", null);
        
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, schema, "%", new String[]{"TABLE"});
            
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                // 过滤系统表
                if (!isSystemTable(tableName)) {
                    tableNames.add(tableName);
                }
            }
        }
        
        log.info("找到 {} 个用户表", tableNames.size());
        return tableNames;
    }
    
    /**
     * 判断是否为系统表
     */
    private boolean isSystemTable(String tableName) {
        String lowerTableName = tableName.toLowerCase();
        // 根据不同数据库类型过滤系统表
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
        
        try (ResultSet rs = metaData.getColumns(null, schema, tableName, columnName)) {
            return rs.next();
        }
    }
    
    /**
     * 为单个表添加字段
     */
    public void addFieldToTable(String tableName, String fieldName, String fieldType, 
                               String defaultValue, boolean notNull, String comment) throws SQLException {
        
        try (Connection conn = dataSource.getConnection()) {
            // 检查字段是否已存在
            if (columnExists(conn, tableName, fieldName)) {
                log.warn("表 {} 中已存在字段 {}, 跳过", tableName, fieldName);
                return;
            }
            
            // 构建ALTER TABLE语句
            String alterTableSql = buildAlterTableSql(tableName, fieldName, fieldType, 
                                                     defaultValue, notNull, comment);
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(alterTableSql);
                log.info("成功为表 {} 添加字段 {}", tableName, fieldName);
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
                sql.append("ALTER TABLE ").append(tableName)
                   .append(" ADD COLUMN ").append(fieldName).append(" ").append(fieldType);
                if (notNull) {
                    sql.append(" NOT NULL");
                }
                if (defaultValue != null && !defaultValue.isEmpty()) {
                    sql.append(" DEFAULT ").append(formatDefaultValue(defaultValue, fieldType));
                }
                if (comment != null && !comment.isEmpty()) {
                    sql.append(" COMMENT '").append(comment).append("'");
                }
                break;
                
            case "postgresql":
                sql.append("ALTER TABLE ").append(tableName)
                   .append(" ADD COLUMN ").append(fieldName).append(" ").append(fieldType);
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
                sql.append("ALTER TABLE ").append(tableName)
                   .append(" ADD ").append(fieldName).append(" ").append(fieldType);
                if (notNull) {
                    sql.append(" NOT NULL");
                }
                if (defaultValue != null && !defaultValue.isEmpty()) {
                    sql.append(" DEFAULT ").append(formatDefaultValue(defaultValue, fieldType));
                }
                break;
                
            default:
                // H2 和其他数据库
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
        
        // 判断是否是字符串类型
        if (upperFieldType.contains("CHAR") || upperFieldType.contains("TEXT") || 
            upperFieldType.contains("VARCHAR") || upperFieldType.contains("STRING")) {
            return "'" + defaultValue + "'";
        }
        
        // 判断是否是日期时间类型
        if (upperFieldType.contains("DATE") || upperFieldType.contains("TIME")) {
            if (defaultValue.equalsIgnoreCase("CURRENT_TIMESTAMP") || 
                defaultValue.equalsIgnoreCase("NOW()") ||
                defaultValue.equalsIgnoreCase("GETDATE()")) {
                return defaultValue;
            } else {
                return "'" + defaultValue + "'";
            }
        }
        
        // 布尔类型
        if (upperFieldType.contains("BOOL") || upperFieldType.contains("BOOLEAN")) {
            return defaultValue.toLowerCase();
        }
        
        // 数值类型直接返回
        return defaultValue;
    }
    
    /**
     * 为所有表添加字段
     */
    public void addFieldToAllTables(String fieldName, String fieldType, 
                                   String defaultValue, boolean notNull, String comment) {
        int successCount = 0;
        int failCount = 0;
        
        try {
            List<String> tableNames = getAllTableNames();
            
            if (tableNames.isEmpty()) {
                log.warn("未找到任何用户表");
                return;
            }
            
            log.info("开始为 {} 个表添加字段 {}", tableNames.size(), fieldName);
            
            for (String tableName : tableNames) {
                try {
                    addFieldToTable(tableName, fieldName, fieldType, defaultValue, notNull, comment);
                    successCount++;
                } catch (SQLException e) {
                    log.error("为表 {} 添加字段失败: {}", tableName, e.getMessage());
                    failCount++;
                }
            }
            
            log.info("字段添加完成. 成功: {}, 失败: {}", successCount, failCount);
            
        } catch (SQLException e) {
            log.error("获取表名列表失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 添加字段并添加注释（仅MySQL支持在ALTER TABLE中直接添加注释）
     */
    public void addFieldWithComment(String tableName, String fieldName, String fieldType,
                                   String defaultValue, boolean notNull, String comment) throws SQLException {
        // 先添加字段
        addFieldToTable(tableName, fieldName, fieldType, defaultValue, notNull, comment);
        
        // 对于PostgreSQL，需要单独添加注释
        if ("postgresql".equals(databaseType) && comment != null && !comment.isEmpty()) {
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                String commentSql = String.format("COMMENT ON COLUMN %s.%s IS '%s'", 
                                                tableName, fieldName, comment);
                stmt.execute(commentSql);
                log.info("为字段 {}.{} 添加注释成功", tableName, fieldName);
            }
        }
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
     * 主方法 - 使用示例
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("使用方法: java DatabaseFieldAdder <配置文件路径> <操作类型>");
            System.out.println("操作类型: ");
            System.out.println("  list - 列出所有表");
            System.out.println("  add - 为所有表添加字段");
            System.exit(1);
        }
        
        String configFile = args[0];
        String operation = args[1];
        
        try {
            DatabaseFieldAdder adder = new DatabaseFieldAdder(configFile);
            
            switch (operation.toLowerCase()) {
                case "list":
                    List<String> tables = adder.getAllTableNames();
                    System.out.println("找到以下表:");
                    tables.forEach(System.out::println);
                    break;
                    
                case "add":
                    // 从配置文件读取字段信息
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
                    
                    adder.addFieldToAllTables(fieldName, fieldType, defaultValue, notNull, comment);
                    break;
                    
                default:
                    System.err.println("未知操作: " + operation);
                    System.exit(1);
            }
            
            adder.close();
            
        } catch (Exception e) {
            log.error("程序执行失败", e);
            System.exit(1);
        }
    }
}