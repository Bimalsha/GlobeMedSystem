package com.globemed.view.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private Color defaultBackgroundColor;
    private Color hoverBackgroundColor;

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 16)); // Slightly larger font
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Colors to match the Dribbble design
        defaultBackgroundColor = new Color(74, 144, 226);
        hoverBackgroundColor = new Color(90, 160, 240); // Lighter blue for hover
        setBackground(defaultBackgroundColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultBackgroundColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        // Use a more subtle rounding
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}