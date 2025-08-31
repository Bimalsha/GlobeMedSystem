package com.globemed;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.globemed.model.Staff;
import com.globemed.security.SecurityContext;
import com.globemed.view.LoginPanel;
import com.globemed.view.panels.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Main {
    private JFrame frame;
    private JTabbedPane mainTabbedPane;

    public static void main(String[] args) {
        // Set the modern look and feel
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaf look and feel.");
        }
        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        // Create the main application frame
        frame = new JFrame("GlobeMed Healthcare Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 800);
        frame.setLocationRelativeTo(null);

        // Using getResource() ensures it works inside a JAR file.
        URL iconURL = Main.class.getResource("/icons/icon.png");
        if (iconURL != null) {
            ImageIcon appIcon = new ImageIcon(iconURL);
            frame.setIconImage(appIcon.getImage());
        } else {
            // If the icon is not found, print an error message to the console.
            System.err.println("Application icon not found at /icons/icon.png");
        }
        // --- END: ADD ICON LOGIC ---
        // Start by showing the login panel
        showLoginPanel();
    }

    private void showLoginPanel() {
        frame.setTitle("GlobeMed - Login");
        // The LoginPanel takes a "callback" function (onLoginSuccess)
        // This function will be executed when login is successful.
        LoginPanel loginPanel = new LoginPanel(this::onLoginSuccess);
        frame.setContentPane(loginPanel);
        frame.pack(); // Resize frame to fit the login panel
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void onLoginSuccess(Staff loggedInUser) {
        // 1. Set the user in the security context
        SecurityContext.login(loggedInUser);

        // 2. Build the main application UI based on permissions
        buildMainUI();

        // 3. Display the main application
        frame.setTitle("GlobeMed Healthcare Management System - Logged in as: " + loggedInUser.getFullname());
        frame.setContentPane(mainTabbedPane);
        frame.setJMenuBar(createMenuBar());
        frame.setSize(1280, 800);
        frame.setLocationRelativeTo(null);
    }

    private void buildMainUI() {
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Dynamically add tabs based on the user's permissions
        if (SecurityContext.hasPermission("ACCESS_PATIENT_PANEL")) {
            addTab("Patient Management", new PatientManagementPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_STAFF_PANEL")) {
            addTab("Staff Management", new StaffManagementPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_APPOINTMENT_PANEL")) {
            addTab("Appointments", new AppointmentSchedulingPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_TEST_DATA_PANEL")) {
            addTab("Test Results Entry", new MedicalTestDataPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_BILLING_PANEL")) {
            addTab("Billing & Claims", new BillingPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_REPORTS_PANEL")) {
            addTab("System Reports", new ReportsPanel());
        }
    }

    private void addTab(String title, JComponent panel) {
        mainTabbedPane.addTab("<html><body style='padding: 5px 10px;'>" + title + "</body></html>", panel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");

        logoutItem.addActionListener(e -> {
            SecurityContext.logout();
            frame.setJMenuBar(null); // Remove the menu bar on logout
            showLoginPanel();
        });

        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        return menuBar;
    }
}