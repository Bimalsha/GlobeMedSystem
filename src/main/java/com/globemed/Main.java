package com.globemed;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.globemed.view.panels.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            } catch (UnsupportedLookAndFeelException e) {
                System.err.println("Failed to initialize FlatLaf look and feel.");
            }

            JFrame frame = new JFrame("GlobeMed Healthcare Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 800);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

            // Create instances of all our management panels
            PatientManagementPanel patientPanel = new PatientManagementPanel();
            StaffManagementPanel staffPanel = new StaffManagementPanel();
            AppointmentSchedulingPanel appointmentPanel = new AppointmentSchedulingPanel();
            MedicalTestDataPanel testDataPanel = new MedicalTestDataPanel();
            BillingPanel billingPanel = new BillingPanel();
            ReportsPanel reportsPanel = new ReportsPanel();

            // Add the panels to the tabbed pane in a logical workflow order
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Patient Management</body></html>", patientPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Staff Management</body></html>", staffPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Appointment Scheduling</body></html>", appointmentPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Test Results Entry</body></html>", testDataPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Billing & Claims</body></html>", billingPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>System Reports</body></html>", reportsPanel);

            frame.getContentPane().add(tabbedPane);
            frame.setVisible(true);
        });
    }
}