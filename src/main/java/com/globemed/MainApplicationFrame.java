package com.globemed;

import com.globemed.model.Staff;
import com.globemed.security.SecurityContext;
import com.globemed.view.LoginFrame;
import com.globemed.view.panels.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainApplicationFrame extends JFrame {

    public MainApplicationFrame(Staff loggedInUser) {
        SecurityContext.login(loggedInUser);

        setTitle("GlobeMed - Logged in as: " + loggedInUser.getFullname());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);

        // Load the icon image from the resources folder.
        URL iconURL = getClass().getResource("/icons/icon.png");
        if (iconURL != null) {
            ImageIcon appIcon = new ImageIcon(iconURL);
            setIconImage(appIcon.getImage());
        } else {
            System.err.println("Application icon not found: /icons/icon.png");
        }
        // --- END: ADD ICON LOGIC ---
        // Build the main UI with tabs
        JTabbedPane mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        if (SecurityContext.hasPermission("ACCESS_PATIENT_PANEL")) {
            addTab(mainTabbedPane, "Patient Management", new PatientManagementPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_STAFF_PANEL")) {
            addTab(mainTabbedPane, "Staff Management", new StaffManagementPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_APPOINTMENT_PANEL")) {
            addTab(mainTabbedPane, "Appointments", new AppointmentSchedulingPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_TEST_DATA_PANEL")) {
            addTab(mainTabbedPane, "Test Results Entry", new MedicalTestDataPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_BILLING_PANEL")) {
            addTab(mainTabbedPane, "Billing & Claims", new BillingPanel());
        }
        if (SecurityContext.hasPermission("ACCESS_REPORTS_PANEL")) {
            addTab(mainTabbedPane, "System Reports", new ReportsPanel());
        }

        add(mainTabbedPane);
        setJMenuBar(createMenuBar());
    }

    private void addTab(JTabbedPane tabbedPane, String title, JComponent panel) {
        tabbedPane.addTab("<html><body style='padding: 5px 10px;'>" + title + "</body></html>", panel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");

        logoutItem.addActionListener(e -> {
            SecurityContext.logout();
            dispose(); // Close this frame
            new LoginFrame().setVisible(true); // Show the login frame again
        });

        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        return menuBar;
    }
}