package com.redsoft.idea.plugin.yapi.ui;

import com.intellij.openapi.ui.ComboBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class YApiConfigurationForm {

    private JTextField urlField;
    private JTextField tokenField;
    private JCheckBox enableBasicScopeCheckBox;
    private JPanel panel;
    private JFormattedTextField projectIdField;
    private JComboBox<String> namingStrategyComboBox;

    public JPanel getPanel() {
        return panel;
    }

    public JTextField getUrlField() {
        return urlField;
    }

    public JFormattedTextField getProjectIdField() {
        return projectIdField;
    }

    public JTextField getTokenField() {
        return tokenField;
    }

    public JComboBox<String> getNamingStrategyComboBox() {
        return namingStrategyComboBox;
    }

    public JCheckBox getEnableBasicScopeCheckBox() {
        return enableBasicScopeCheckBox;
    }

    private void createUIComponents() {
        this.projectIdField = new JFormattedTextField(new DecimalFormat("#0"));
        this.projectIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                String old = projectIdField.getText();
                JFormattedTextField.AbstractFormatter formatter = projectIdField.getFormatter();
                if (!old.equals("")) {
                    if (formatter != null) {
                        String str = projectIdField.getText();
                        try {
                            long page = (Long) formatter.stringToValue(str);
                            projectIdField.setText(page + "");
                        } catch (ParseException pe) {
                            projectIdField.setText("1");//解析异常直接将文本框中值设置为1
                        }
                    }
                }
            }
        });
        this.namingStrategyComboBox = new ComboBox<>(
                new String[]{"None", "KebabCase", "SnakeCase", "LowerCase", "UpperCamelCase"});
    }
}
