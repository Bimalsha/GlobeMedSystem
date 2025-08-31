package com.globemed.view.custom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class IconTextField extends JPanel {
    private final JTextField textField;

    public IconTextField(ImageIcon icon) {
        setLayout(new BorderLayout(10, 0));
        setBackground(new Color(240, 244, 247));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JLabel iconLabel = new JLabel(icon);
        textField = new JTextField();
        textField.setBackground(new Color(240, 244, 247));
        textField.setBorder(null);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        add(iconLabel, BorderLayout.WEST);
        add(textField, BorderLayout.CENTER);
    }

    public String getText() {
        return textField.getText();
    }

    public JPasswordField asPasswordField() {
        JPasswordField passField = new JPasswordField();
        passField.setBackground(textField.getBackground());
        passField.setBorder(textField.getBorder());
        passField.setFont(textField.getFont());
        this.remove(textField);
        this.add(passField, BorderLayout.CENTER);
        return passField;
    }
}