package com.example.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.List;

/**
 * 数据库字段添加工具 - 图形界面版本
 */
public class DatabaseFieldAdderGUI extends JFrame {
    
    private JTextField urlField;
    private JTextField driverField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField schemaField;
    
    private JTextField fieldNameField;
    private JTextField fieldTypeField;
    private JTextField defaultValueField;
    private JCheckBox notNullCheckBox;
    private JTextField commentField;
    
    private JTextArea logArea;
    private JButton connectButton;
    private JButton listTablesButton;
    private JButton addFieldButton;
    private JButton saveConfigButton;
    private JButton loadConfigButton;
    
    private EnhancedDatabaseFieldAdder dbAdder;
    
    public DatabaseFieldAdderGUI() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("数据库字段批量添加工具");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 创建配置面板
        JPanel configPanel = createConfigPanel();
        mainPanel.add(configPanel, BorderLayout.NORTH);
        
        // 创建日志面板
        JPanel logPanel = createLogPanel();
        mainPanel.add(logPanel, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // 设置窗口大小和位置
        setSize(800, 700);
        setLocationRelativeTo(null);
    }
    
    private JPanel createConfigPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 数据库连接配置
        JPanel dbPanel = new JPanel(new GridBagLayout());
        dbPanel.setBorder(BorderFactory.createTitledBorder("数据库连接配置"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // URL
        gbc.gridx = 0;
        gbc.gridy = 0;
        dbPanel.add(new JLabel("JDBC URL:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        urlField = new JTextField("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC");
        dbPanel.add(urlField, gbc);
        
        // Driver
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        dbPanel.add(new JLabel("Driver:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        driverField = new JTextField("com.mysql.cj.jdbc.Driver");
        dbPanel.add(driverField, gbc);
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        dbPanel.add(new JLabel("用户名:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        usernameField = new JTextField("root");
        dbPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        dbPanel.add(new JLabel("密码:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField();
        dbPanel.add(passwordField, gbc);
        
        // Schema
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        dbPanel.add(new JLabel("Schema(可选):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        schemaField = new JTextField();
        dbPanel.add(schemaField, gbc);
        
        panel.add(dbPanel);
        
        // 字段配置
        JPanel fieldPanel = new JPanel(new GridBagLayout());
        fieldPanel.setBorder(BorderFactory.createTitledBorder("字段配置"));
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Field Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldPanel.add(new JLabel("字段名:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        fieldNameField = new JTextField("created_time");
        fieldPanel.add(fieldNameField, gbc);
        
        // Field Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        fieldPanel.add(new JLabel("字段类型:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        fieldTypeField = new JTextField("DATETIME");
        fieldPanel.add(fieldTypeField, gbc);
        
        // Default Value
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        fieldPanel.add(new JLabel("默认值:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        defaultValueField = new JTextField("CURRENT_TIMESTAMP");
        fieldPanel.add(defaultValueField, gbc);
        
        // Not Null
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        fieldPanel.add(new JLabel("非空:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        notNullCheckBox = new JCheckBox();
        fieldPanel.add(notNullCheckBox, gbc);
        
        // Comment
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        fieldPanel.add(new JLabel("注释:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        commentField = new JTextField("创建时间");
        fieldPanel.add(commentField, gbc);
        
        panel.add(fieldPanel);
        
        return panel;
    }
    
    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("执行日志"));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        loadConfigButton = new JButton("加载配置");
        loadConfigButton.addActionListener(this::loadConfiguration);
        panel.add(loadConfigButton);
        
        saveConfigButton = new JButton("保存配置");
        saveConfigButton.addActionListener(this::saveConfiguration);
        panel.add(saveConfigButton);
        
        connectButton = new JButton("测试连接");
        connectButton.addActionListener(this::testConnection);
        panel.add(connectButton);
        
        listTablesButton = new JButton("列出所有表");
        listTablesButton.addActionListener(this::listTables);
        panel.add(listTablesButton);
        
        addFieldButton = new JButton("开始添加字段");
        addFieldButton.addActionListener(this::addField);
        panel.add(addFieldButton);
        
        JButton clearButton = new JButton("清空日志");
        clearButton.addActionListener(e -> logArea.setText(""));
        panel.add(clearButton);
        
        return panel;
    }
    
    private void loadConfiguration(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择配置文件");
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile())) {
                    props.load(fis);
                }
                
                urlField.setText(props.getProperty("database.url", ""));
                driverField.setText(props.getProperty("database.driver", ""));
                usernameField.setText(props.getProperty("database.username", ""));
                passwordField.setText(props.getProperty("database.password", ""));
                schemaField.setText(props.getProperty("database.schema", ""));
                
                fieldNameField.setText(props.getProperty("field.name", ""));
                fieldTypeField.setText(props.getProperty("field.type", ""));
                defaultValueField.setText(props.getProperty("field.default", ""));
                notNullCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("field.notNull", "false")));
                commentField.setText(props.getProperty("field.comment", ""));
                
                log("配置文件加载成功: " + fileChooser.getSelectedFile().getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "加载配置文件失败: " + ex.getMessage(), 
                                            "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void saveConfiguration(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存配置文件");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Properties props = new Properties();
                props.setProperty("database.url", urlField.getText());
                props.setProperty("database.driver", driverField.getText());
                props.setProperty("database.username", usernameField.getText());
                props.setProperty("database.password", new String(passwordField.getPassword()));
                props.setProperty("database.schema", schemaField.getText());
                
                props.setProperty("field.name", fieldNameField.getText());
                props.setProperty("field.type", fieldTypeField.getText());
                props.setProperty("field.default", defaultValueField.getText());
                props.setProperty("field.notNull", String.valueOf(notNullCheckBox.isSelected()));
                props.setProperty("field.comment", commentField.getText());
                
                try (FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile())) {
                    props.store(fos, "Database Field Adder Configuration");
                }
                
                log("配置文件保存成功: " + fileChooser.getSelectedFile().getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "保存配置文件失败: " + ex.getMessage(), 
                                            "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void testConnection(ActionEvent e) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                log("正在测试数据库连接...");
                createDbAdder();
                return true;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    log("数据库连接成功！");
                    JOptionPane.showMessageDialog(DatabaseFieldAdderGUI.this, 
                                                "数据库连接成功！", "成功", 
                                                JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    log("数据库连接失败: " + ex.getCause().getMessage());
                    JOptionPane.showMessageDialog(DatabaseFieldAdderGUI.this, 
                                                "数据库连接失败: " + ex.getCause().getMessage(), 
                                                "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void listTables(ActionEvent e) {
        SwingWorker<List<String>, Void> worker = new SwingWorker<List<String>, Void>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                log("正在获取表列表...");
                createDbAdder();
                return dbAdder.getAllTableNames();
            }
            
            @Override
            protected void done() {
                try {
                    List<String> tables = get();
                    log("找到 " + tables.size() + " 个表:");
                    for (String table : tables) {
                        log("  - " + table);
                    }
                } catch (Exception ex) {
                    log("获取表列表失败: " + ex.getCause().getMessage());
                    JOptionPane.showMessageDialog(DatabaseFieldAdderGUI.this, 
                                                "获取表列表失败: " + ex.getCause().getMessage(), 
                                                "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void addField(ActionEvent e) {
        // 验证输入
        if (fieldNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入字段名", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (fieldTypeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入字段类型", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
                                                  "确定要为所有表添加字段吗？\n" +
                                                  "字段名: " + fieldNameField.getText() + "\n" +
                                                  "类型: " + fieldTypeField.getText(), 
                                                  "确认", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    log("开始添加字段...");
                    createDbAdder();
                    dbAdder.addFieldToAllTablesWithTransaction(
                        fieldNameField.getText(),
                        fieldTypeField.getText(),
                        defaultValueField.getText(),
                        notNullCheckBox.isSelected(),
                        commentField.getText()
                    );
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        get();
                        log("字段添加完成！");
                        JOptionPane.showMessageDialog(DatabaseFieldAdderGUI.this, 
                                                    "字段添加完成！", "成功", 
                                                    JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        log("字段添加失败: " + ex.getCause().getMessage());
                        JOptionPane.showMessageDialog(DatabaseFieldAdderGUI.this, 
                                                    "字段添加失败: " + ex.getCause().getMessage(), 
                                                    "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void createDbAdder() throws IOException {
        if (dbAdder != null) {
            dbAdder.close();
        }
        
        // 创建临时配置文件
        Properties props = new Properties();
        props.setProperty("database.url", urlField.getText());
        props.setProperty("database.driver", driverField.getText());
        props.setProperty("database.username", usernameField.getText());
        props.setProperty("database.password", new String(passwordField.getPassword()));
        props.setProperty("database.schema", schemaField.getText());
        props.setProperty("use.transaction", "true");
        
        String tempFile = "temp_config_" + System.currentTimeMillis() + ".properties";
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            props.store(fos, "Temporary Configuration");
        }
        
        dbAdder = new EnhancedDatabaseFieldAdder(tempFile);
        
        // 删除临时文件
        new java.io.File(tempFile).delete();
    }
    
    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()) + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            DatabaseFieldAdderGUI gui = new DatabaseFieldAdderGUI();
            gui.setVisible(true);
        });
    }
}