package com.globemed;

// ... other imports
import com.globemed.view.panels.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // ... look and feel setup ...

            JFrame frame = new JFrame("GlobeMed Healthcare Management System");
            // ... frame setup ...

            JTabbedPane tabbedPane = new JTabbedPane();
            // ... font setup ...

            // Create instances of our management panels
            PatientManagementPanel patientPanel = new PatientManagementPanel();
            StaffManagementPanel staffPanel = new StaffManagementPanel();
            AppointmentSchedulingPanel appointmentPanel = new AppointmentSchedulingPanel();
            BillingPanel billingPanel = new BillingPanel();
            ReportsPanel reportsPanel = new ReportsPanel(); // <-- INSTANTIATE THE NEW PANEL

            // Add panels to tabs
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Patient Management</body></html>", patientPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Staff Management</body></html>", staffPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Appointment Scheduling</body></html>", appointmentPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>Billing & Claims</body></html>", billingPanel);
            tabbedPane.addTab("<html><body style='padding: 5px 10px;'>System Reports</body></html>", reportsPanel); // <-- ADD THE NEW TAB

            frame.getContentPane().add(tabbedPane);
            frame.setVisible(true);
        });
    }
}