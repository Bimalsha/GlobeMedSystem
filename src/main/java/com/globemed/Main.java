package com.globemed;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.globemed.view.LoginFrame;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Apply the modern Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaf look and feel.");
        }

        // Launch the Login Frame on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}