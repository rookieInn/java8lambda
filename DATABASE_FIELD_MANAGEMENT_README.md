# 数据库字段管理解决方案

## 项目概述

这是一个基于Spring Boot的数据库字段管理解决方案，可以方便地为数据库中的表添加字段。支持为单个表、多个指定表或所有表批量添加字段。

## 功能特性

- ✅ **批量字段添加**：支持为所有表或指定表列表批量添加字段
- ✅ **灵活配置**：支持自定义字段类型、默认值、是否允许为空、字段注释
- ✅ **安全检查**：自动检查表是否存在、字段是否重复
- ✅ **事务支持**：所有操作都在事务中执行，确保数据一致性
- ✅ **REST API**：提供完整的REST API接口
- ✅ **错误处理**：详细的错误信息和操作结果反馈
- ✅ **常用类型**：内置常用数据库字段类型

## 项目结构

```
src/main/java/com/example/wechatpay/
├── config/
│   └── DatabaseConfig.java          # 数据库配置类
├── service/
│   ├── TableManagementService.java  # 表管理服务
│   └── ColumnManagementService.java # 字段管理服务
├── controller/
│   └── DatabaseManagementController.java # REST API控制器
├── example/
│   └── DatabaseManagementExample.java    # 使用示例
└── DatabaseManagementDemo.java           # 演示程序

src/test/java/com/example/wechatpay/
└── DatabaseManagementTest.java           # 测试类
```

## 快速开始

### 1. 配置数据库连接

修改 `src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
```

### 2. 运行项目

```bash
# 编译项目
mvn clean compile

# 运行演示程序
mvn spring-boot:run -Dspring-boot.run.main-class=com.example.wechatpay.DatabaseManagementDemo
```

### 3. 使用API接口

启动后可以通过以下API接口使用功能：

```bash
# 获取所有表名
curl http://localhost:8080/api/database/tables

# 为所有表添加创建时间字段
curl -X POST http://localhost:8080/api/database/tables/columns \
  -H "Content-Type: application/json" \
  -d '{
    "columnName": "create_time",
    "columnType": "DATETIME",
    "defaultValue": "CURRENT_TIMESTAMP",
    "nullable": false,
    "comment": "创建时间"
  }'
```

## 核心功能说明

### 1. 表管理服务 (TableManagementService)

- `getAllTableNames()`: 获取数据库中所有表名
- `getTableColumns(tableName)`: 获取指定表的字段信息
- `tableExists(tableName)`: 检查表是否存在
- `columnExists(tableName, columnName)`: 检查字段是否存在

### 2. 字段管理服务 (ColumnManagementService)

- `addColumnToTable()`: 为单个表添加字段
- `addColumnToAllTables()`: 为所有表添加字段
- `addColumnToSpecificTables()`: 为指定表列表添加字段
- `getCommonColumnTypes()`: 获取常用字段类型

### 3. REST API控制器 (DatabaseManagementController)

提供完整的REST API接口，支持：
- 获取表信息
- 获取字段信息
- 添加字段操作
- 获取常用字段类型

## 使用场景

### 1. 为所有表添加审计字段

```java
// 添加创建时间
columnManagementService.addColumnToAllTables(
    "create_time", "DATETIME", "CURRENT_TIMESTAMP", false, "创建时间"
);

// 添加更新时间
columnManagementService.addColumnToAllTables(
    "update_time", "DATETIME", "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", true, "更新时间"
);

// 添加删除标记
columnManagementService.addColumnToAllTables(
    "is_deleted", "TINYINT(1)", "0", false, "删除标记(0:未删除,1:已删除)"
);
```

### 2. 为指定表添加版本字段

```java
List<String> tableNames = Arrays.asList("user", "order", "product");
columnManagementService.addColumnToSpecificTables(
    tableNames, "version", "INT", "1", false, "版本号(用于乐观锁)"
);
```

### 3. 为单个表添加自定义字段

```java
columnManagementService.addColumnToTable(
    "user", "phone", "VARCHAR(20)", null, true, "手机号码"
);
```

## 支持的字段类型

| 类型 | 说明 | 示例 |
|------|------|------|
| VARCHAR(n) | 可变长度字符串 | VARCHAR(255) |
| TEXT | 长文本 | TEXT |
| INT | 整数 | INT |
| BIGINT | 长整数 | BIGINT |
| DECIMAL(p,s) | 精确小数 | DECIMAL(10,2) |
| DATETIME | 日期时间 | DATETIME |
| DATE | 日期 | DATE |
| TIME | 时间 | TIME |
| TIMESTAMP | 时间戳 | TIMESTAMP |
| BOOLEAN | 布尔值 | BOOLEAN |
| TINYINT(1) | 小整数 | TINYINT(1) |
| JSON | JSON数据 | JSON |

## 安全注意事项

1. **备份数据**：在执行字段添加操作前，建议先备份数据库
2. **权限检查**：确保数据库用户具有 ALTER TABLE 权限
3. **测试环境**：建议先在测试环境中验证功能
4. **字段重复**：系统会自动检查字段是否已存在，避免重复添加

## 扩展功能

可以基于现有代码扩展以下功能：

1. **字段删除**：添加删除字段的功能
2. **字段修改**：添加修改字段类型、长度等功能
3. **索引管理**：添加创建、删除索引的功能
4. **多数据库支持**：支持PostgreSQL、Oracle等其他数据库
5. **批量操作**：支持批量删除、修改字段
6. **字段重命名**：支持字段重命名功能

## 测试

运行测试类验证功能：

```bash
mvn test -Dtest=DatabaseManagementTest
```

## 依赖说明

项目使用的主要依赖：

- Spring Boot 2.7.12
- MySQL Connector 8.0.33
- HikariCP 5.0.1
- MyBatis Spring Boot Starter 2.3.1

## 许可证

本项目采用 MIT 许可证。

## 贡献

欢迎提交 Issue 和 Pull Request 来改进这个项目。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 GitHub Issue
- 发送邮件至项目维护者

---

**注意**：使用前请确保已正确配置数据库连接，并在测试环境中验证功能。