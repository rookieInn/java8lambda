#!/bin/bash

# 数据库字段添加工具启动脚本

echo "=== 数据库表字段添加工具 ==="
echo "正在编译项目..."

# 编译项目
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "编译成功，启动字段添加工具..."
    echo "注意：请确保已在 application.yml 中正确配置数据库连接信息"
    echo ""
    
    # 运行命令行工具
    java -cp "target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" \
         com.example.wechatpay.util.DatabaseFieldAdder
else
    echo "编译失败，请检查代码是否有错误"
    exit 1
fi