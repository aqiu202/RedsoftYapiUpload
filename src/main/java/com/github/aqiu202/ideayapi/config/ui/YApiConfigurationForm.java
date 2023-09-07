package com.github.aqiu202.ideayapi.config.ui;

import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.StringJoiner;

/**
 * 配置面板
 */
public class YApiConfigurationForm {

    private JTextField urlField;
    private JTextField tokenField;
    private JCheckBox enableBasicScopeCheckBox;
    private JPanel panel;
    private JFormattedTextField projectIdField;
    private ComboBox<String> namingStrategyComboBox;
    private ComboBox<String> dataModeComboBox;
    private JCheckBox enableTypeDescCheckBox;
    private JCheckBox useMethodDefineAsRemarkCheckBox;
    private JCheckBox passPageUrlCheckBox;
    private JList<String> ignoredReqFields;
    private JPanel ignoredReqFieldsPanel;
    private JList<String> ignoredResFields;
    private JPanel ignoredResFieldsPanel;
    private DefaultListModel<String> ignoredReqFieldsModel;
    private DefaultListModel<String> ignoredResFieldsModel;

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

    public ComboBox<String> getNamingStrategyComboBox() {
        return namingStrategyComboBox;
    }

    public JCheckBox getEnableBasicScopeCheckBox() {
        return enableBasicScopeCheckBox;
    }

    public JCheckBox getEnableTypeDescCheckBox() {
        return enableTypeDescCheckBox;
    }

    public ComboBox<String> getDataModeComboBox() {
        return dataModeComboBox;
    }

    public JCheckBox getUseMethodDefineAsRemarkCheckBox() {
        return useMethodDefineAsRemarkCheckBox;
    }

    public JCheckBox getPassPageUrlCheckBox() {
        return passPageUrlCheckBox;
    }

    public String getIgnoredReqFields() {
        return this.readListModelValue(this.ignoredReqFieldsModel);
    }

    public String getIgnoredResFields() {
        return this.readListModelValue(this.ignoredResFieldsModel);
    }

    private String readListModelValue(DefaultListModel<String> listModel) {
        Enumeration<String> elements = listModel.elements();
        StringJoiner joiner = new StringJoiner(",");
        while (elements.hasMoreElements()) {
            String element = elements.nextElement();
            joiner.add(element);
        }
        return joiner.toString();
    }

    public void setIgnoredReqFields(java.util.List<String> fields) {
        this.ignoredReqFieldsModel.clear();
        for (String field : fields) {
            this.ignoredReqFieldsModel.addElement(field);
        }
    }

    public void setIgnoredResFields(java.util.List<String> fields) {
        this.ignoredResFieldsModel.clear();
        for (String field : fields) {
            this.ignoredResFieldsModel.addElement(field);
        }
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
        this.dataModeComboBox = new ComboBox<>(new String[]{"json-schema", "json5"});
        this.ignoredReqFieldsModel = new DefaultListModel<>();
        this.ignoredReqFields = this.createJList(this.ignoredReqFieldsModel);
        this.ignoredResFieldsModel = new DefaultListModel<>();
        this.ignoredResFields = this.createJList(this.ignoredResFieldsModel);
        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(MouseEvent e) {
                return true;
            }
        }.installOn(ignoredResFields);

    }

    private JBList<String> createJList(ListModel<String> dataModel) {
        JBList<String> jbList = new JBList<>(dataModel);
        jbList.setEmptyText("空");
        jbList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                final Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(String.valueOf(value));
                return comp;
            }
        });
//        jbList.setFixedCellWidth(400);
        jbList.setDragEnabled(true);
        jbList.setDropMode(DropMode.INSERT);
        return jbList;
    }

    public void init() {
        this.initPanel(this.ignoredReqFieldsPanel, this.ignoredReqFields, this.ignoredReqFieldsModel);
        this.initPanel(this.ignoredResFieldsPanel, this.ignoredResFields, this.ignoredResFieldsModel);
    }

    private void initPanel(JPanel jPanel, JList<String> jList, DefaultListModel<String> listModel) {
        jPanel.setEnabled(true);
        JPanel component = ToolbarDecorator.createDecorator(jList)
                .setPreferredSize(new Dimension(300, 200))
                .setToolbarPosition(ActionToolbarPosition.TOP)
                .setAddAction(button -> {
                    FieldEditor fieldEditor = new FieldEditor();
                    String text;
                    if (fieldEditor.showAndGet() && StringUtils.isNoneBlank(text = fieldEditor.getText())) {
                        if (!listModel.contains(text)) {
                            listModel.addElement(text);
                        }
                    }
                }).createPanel();
        GridConstraints constraints = new GridConstraints();
        constraints.setAnchor(GridConstraints.ANCHOR_NORTHWEST);
        jPanel.add(component, constraints);
    }

}
