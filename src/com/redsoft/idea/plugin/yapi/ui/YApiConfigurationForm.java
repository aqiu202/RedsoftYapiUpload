package com.redsoft.idea.plugin.yapi.ui;

import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class YApiConfigurationForm {

    private JTextField urlField;
    private JTextField tokenField;
    private JCheckBox enableBasicScopeCheckBox;
    private JPanel panel;
    private JFormattedTextField projectIdField;

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

    public JCheckBox getEnableBasicScopeCheckBox() {
        return enableBasicScopeCheckBox;
    }

    private void createUIComponents() {
        this.projectIdField = new JFormattedTextField(new DecimalFormat("#0"));
        this.projectIdField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
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
    }
}
