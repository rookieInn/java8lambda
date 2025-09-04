package com.example.wechatpay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据库表管理服务类
 * 用于获取数据库中的所有表名和表信息
 * 
 * @author Generated
 * @date 2024
 */
@Service
public class TableManagementService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取数据库中所有表名
     * 
     * @return 表名列表
     */
    public List<String> getAllTableNames() {
        String sql = "SHOW TABLES";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        
        return result.stream()
                .map(row -> {
                    // 获取第一个列的值（表名）
                    Object tableName = row.values().iterator().next();
                    return tableName.toString();
                })
                .toList();
    }

    /**
     * 获取指定表的字段信息
     * 
     * @param tableName 表名
     * @return 字段信息列表
     */
    public List<Map<String, Object>> getTableColumns(String tableName) {
        String sql = "DESCRIBE " + tableName;
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 检查表是否存在
     * 
     * @param tableName 表名
     * @return 是否存在
     */
    public boolean tableExists(String tableName) {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
        return count != null && count > 0;
    }

    /**
     * 检查字段是否已存在
     * 
     * @param tableName 表名
     * @param columnName 字段名
     * @return 是否存在
     */
    public boolean columnExists(String tableName, String columnName) {
        String sql = "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = ? AND column_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName);
        return count != null && count > 0;
    }
}