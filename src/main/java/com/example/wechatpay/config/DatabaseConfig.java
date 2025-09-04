package com.example.wechatpay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 数据库配置类
 * 
 * @author Generated
 * @date 2024
 */
@Configuration
public class DatabaseConfig {

    /**
     * 配置JdbcTemplate
     * 
     * @param dataSource 数据源
     * @return JdbcTemplate实例
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}