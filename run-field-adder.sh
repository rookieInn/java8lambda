#!/bin/bash
# 数据库字段批量添加工具 - 启动脚本

# 检查参数
if [ $# -lt 1 ]; then
    echo "使用方法: ./run-field-adder.sh [gui|list|add|verify] [配置文件] [其他参数]"
    echo ""
    echo "命令说明:"
    echo "  gui              - 启动图形界面"
    echo "  list <配置文件>   - 列出所有表"
    echo "  add <配置文件>    - 添加字段到所有表"
    echo "  verify <配置文件> <字段名> - 验证字段是否添加成功"
    echo ""
    echo "示例:"
    echo "  ./run-field-adder.sh gui"
    echo "  ./run-field-adder.sh list src/main/resources/database.properties"
    echo "  ./run-field-adder.sh add src/main/resources/database.properties"
    echo "  ./run-field-adder.sh verify src/main/resources/database.properties created_time"
    exit 1
fi

# 编译项目（如果需要）
if [ ! -d "target/classes" ]; then
    echo "正在编译项目..."
    mvn clean compile
    if [ $? -ne 0 ]; then
        echo "编译失败，请检查代码"
        exit 1
    fi
fi

# 设置类路径
CLASSPATH="target/classes"
for jar in lib/*.jar; do
    CLASSPATH="$CLASSPATH:$jar"
done

# 根据参数执行相应操作
case "$1" in
    gui)
        echo "启动图形界面..."
        java -cp "$CLASSPATH" com.example.database.DatabaseFieldAdderGUI
        ;;
    list)
        if [ $# -lt 2 ]; then
            echo "错误：请指定配置文件"
            exit 1
        fi
        echo "列出所有表..."
        java -cp "$CLASSPATH" com.example.database.EnhancedDatabaseFieldAdder "$2" list
        ;;
    add)
        if [ $# -lt 2 ]; then
            echo "错误：请指定配置文件"
            exit 1
        fi
        echo "开始添加字段..."
        java -cp "$CLASSPATH" com.example.database.EnhancedDatabaseFieldAdder "$2" add
        ;;
    verify)
        if [ $# -lt 3 ]; then
            echo "错误：请指定配置文件和字段名"
            exit 1
        fi
        echo "验证字段..."
        java -cp "$CLASSPATH" com.example.database.EnhancedDatabaseFieldAdder "$2" verify "$3"
        ;;
    *)
        echo "未知命令: $1"
        exit 1
        ;;
esac