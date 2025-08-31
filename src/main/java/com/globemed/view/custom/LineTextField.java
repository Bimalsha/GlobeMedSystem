package com.globemed.view.custom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LineTextField extends JPanel {
    private final JTextField textField;
    private final JLabel iconLabel;
    private boolean hasFocus = false;
    private final Color defaultLineColor = new Color(220, 220, 220);
    private final Color focusLineColor = new Color(74, 144, 226);

    public LineTextField(String label, ImageIcon icon, boolean isPassword) {
        setLayout(new BorderLayout(10, 0));
        setOpaque(false); // Make panel transparent
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // Top Label
        JLabel topLabel = new JLabel(label);
        topLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        topLabel.setForeground(Color.GRAY);
        add(topLabel, BorderLayout.NORTH);

        // Icon
        iconLabel = new JLabel(icon);
        add(iconLabel, BorderLayout.WEST);

        // Text Field
        if (isPassword) {
            textField = new JPasswordField();
        } else {
            textField = new JTextField();
        }
        textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textField.setBorder(null); // No default border
        textField.setOpaque(false); // Transparent background

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                hasFocus = true;
                repaint(); // Repaint to change line color
            }
            @Override
            public void focusLost(FocusEvent e) {
                hasFocus = false;
                repaint();
            }
        });
        add(textField, BorderLayout.CENTER);
    }

    public String getText() {
        return textField.getText();
    }

    public char[] getPassword() {
        if (textField instanceof JPasswordField) {
            return ((JPasswordField) textField).getPassword();
        }
        return new char[0];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint the bottom line
        if (hasFocus) {
            g2d.setColor(focusLineColor);
            g2d.setStroke(new BasicStroke(2f)); // Thicker line on focus
        } else {
            g2d.setColor(defaultLineColor);
            g2d.setStroke(new BasicStroke(1f));
        }
        g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        g2d.dispose();
    }
}