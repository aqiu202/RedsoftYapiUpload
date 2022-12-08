package com.github.aqiu202.ideayapi.config.ui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;

public class FieldEditor extends DialogWrapper {
    private JPanel contentPane;
    private JTextField fieldValTextField;

    public FieldEditor() {
        super(true);
        init();
        setModal(true);
        setTitle("字段设置");

//        contentPane.registerKeyboardAction(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                onCancel();
//            }
//        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public JTextField getFieldValTextField() {
        return fieldValTextField;
    }

    public String getText() {
        return this.fieldValTextField.getText();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.add(this.contentPane, BorderLayout.CENTER);
        return dialogPanel;
    }
}
