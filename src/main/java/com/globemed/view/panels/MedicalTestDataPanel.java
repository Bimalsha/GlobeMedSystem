package com.globemed.view.panels;

import com.globemed.dao.*;
import com.globemed.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;

public class MedicalTestDataPanel extends JPanel {

    private PatientDAO patientDAO;
    private BloodReportDAO bloodReportDAO;
    private UrineReportDAO urineReportDAO;
    private XrayReportDAO xrayReportDAO;
    private Patient currentPatient = null;

    // Main components
    private JTextField txtPatientId;
    private JButton btnFetchPatient;
    private JLabel lblPatientInfo;
    private JTabbedPane testTabs;

    // Blood Report Tab Components
    private JTextField txtHgb, txtWbc, txtRbc, txtPlatelets;
    private JButton btnSaveBloodReport;

    // Urine Report Tab Components
    private JTextField txtUrineColor, txtPh, txtGlucose, txtKetones;
    private JButton btnSaveUrineReport;

    // X-Ray Report Tab Components
    private JTextField txtBodyPart;
    private JTextArea txtFindings, txtImpression;
    private JButton btnSaveXrayReport;

    public MedicalTestDataPanel() {
        this.patientDAO = new PatientDAO();
        this.bloodReportDAO = new BloodReportDAO();
        this.urineReportDAO = new UrineReportDAO();
        this.xrayReportDAO = new XrayReportDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        setupPatientSelectionPanel();
        setupTabbedPanel();
        addListeners();
    }

    private void setupPatientSelectionPanel() {
        JPanel patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        patientPanel.setBorder(new TitledBorder("1. Find Patient"));
        txtPatientId = new JTextField(10);
        btnFetchPatient = new JButton("Fetch Patient");
        lblPatientInfo = new JLabel("Please fetch a patient to begin data entry.");
        patientPanel.add(new JLabel("Patient ID:"));
        patientPanel.add(txtPatientId);
        patientPanel.add(btnFetchPatient);
        patientPanel.add(lblPatientInfo);
        add(patientPanel, BorderLayout.NORTH);
    }

    private void setupTabbedPanel() {
        testTabs = new JTabbedPane();
        testTabs.addTab("Blood Report", createBloodPanel());
        testTabs.addTab("Urine Report", createUrinePanel());
        testTabs.addTab("X-Ray Report", createXrayPanel());
        add(testTabs, BorderLayout.CENTER);
        setTabsEnabled(false); // Initially disable everything
    }

    //region Panel Creation Methods
    private JPanel createBloodPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); gbc.fill = GridBagConstraints.HORIZONTAL;

        txtHgb = new JTextField(10);
        txtWbc = new JTextField(10);
        txtRbc = new JTextField(10);
        txtPlatelets = new JTextField(10);
        btnSaveBloodReport = new JButton("Save Blood Report");

        gbc.gridx=0; gbc.gridy=0; panel.add(new JLabel("Hemoglobin (g/dL):"), gbc);
        gbc.gridx=1; panel.add(txtHgb, gbc);
        gbc.gridx=0; gbc.gridy=1; panel.add(new JLabel("WBC Count (cells/µL):"), gbc);
        gbc.gridx=1; panel.add(txtWbc, gbc);
        gbc.gridx=0; gbc.gridy=2; panel.add(new JLabel("RBC Count (million/µL):"), gbc);
        gbc.gridx=1; panel.add(txtRbc, gbc);
        gbc.gridx=0; gbc.gridy=3; panel.add(new JLabel("Platelets (per µL):"), gbc);
        gbc.gridx=1; panel.add(txtPlatelets, gbc);
        gbc.gridy=4; gbc.gridwidth=2; panel.add(btnSaveBloodReport, gbc);
        return panel;
    }

    private JPanel createUrinePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUrineColor = new JTextField(10);
        txtPh = new JTextField(10);
        txtGlucose = new JTextField(10);
        txtKetones = new JTextField(10);
        btnSaveUrineReport = new JButton("Save Urine Report");

        gbc.gridx=0; gbc.gridy=0; panel.add(new JLabel("Color:"), gbc);
        gbc.gridx=1; panel.add(txtUrineColor, gbc);
        gbc.gridx=0; gbc.gridy=1; panel.add(new JLabel("pH:"), gbc);
        gbc.gridx=1; panel.add(txtPh, gbc);
        gbc.gridx=0; gbc.gridy=2; panel.add(new JLabel("Glucose:"), gbc);
        gbc.gridx=1; panel.add(txtGlucose, gbc);
        gbc.gridx=0; gbc.gridy=3; panel.add(new JLabel("Ketones:"), gbc);
        gbc.gridx=1; panel.add(txtKetones, gbc);
        gbc.gridy=4; gbc.gridwidth=2; panel.add(btnSaveUrineReport, gbc);
        return panel;
    }

    private JPanel createXrayPanel() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        txtBodyPart = new JTextField();
        txtFindings = new JTextArea(5, 30);
        txtImpression = new JTextArea(3, 30);
        btnSaveXrayReport = new JButton("Save X-Ray Report");

        JPanel topPanel = new JPanel(new BorderLayout(5,0));
        topPanel.add(new JLabel("Body Part Examined:"), BorderLayout.WEST);
        topPanel.add(txtBodyPart, BorderLayout.CENTER);

        JPanel textAreasPanel = new JPanel();
        textAreasPanel.setLayout(new BoxLayout(textAreasPanel, BoxLayout.Y_AXIS));
        textAreasPanel.add(new JLabel("Findings:"));
        textAreasPanel.add(new JScrollPane(txtFindings));
        textAreasPanel.add(Box.createVerticalStrut(10));
        textAreasPanel.add(new JLabel("Impression:"));
        textAreasPanel.add(new JScrollPane(txtImpression));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(textAreasPanel, BorderLayout.CENTER);
        panel.add(btnSaveXrayReport, BorderLayout.SOUTH);
        return panel;
    }
    //endregion

    private void addListeners() {
        btnFetchPatient.addActionListener(e -> fetchPatient());
        btnSaveBloodReport.addActionListener(e -> saveBloodReport());
        btnSaveUrineReport.addActionListener(e -> saveUrineReport());
        btnSaveXrayReport.addActionListener(e -> saveXrayReport());
    }

    private void fetchPatient() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText().trim());
            currentPatient = patientDAO.findById(patientId);
            if (currentPatient != null) {
                lblPatientInfo.setText("Editing reports for: " + currentPatient.getFullname());
                lblPatientInfo.setForeground(Color.BLUE);
                setTabsEnabled(true);
            } else {
                lblPatientInfo.setText("Patient not found.");
                lblPatientInfo.setForeground(Color.RED);
                setTabsEnabled(false);
            }
        } catch (NumberFormatException e) {
            lblPatientInfo.setText("Invalid Patient ID.");
            lblPatientInfo.setForeground(Color.RED);
            currentPatient = null;
            setTabsEnabled(false);
        }
    }

    /**
     * Corrected method to enable/disable all components within the JTabbedPane
     * using a recursive helper method for robustness.
     */
    private void setTabsEnabled(boolean enabled) {
        testTabs.setEnabled(enabled);
        for (int i = 0; i < testTabs.getTabCount(); i++) {
            Component tabContent = testTabs.getComponentAt(i);
            if (tabContent instanceof Container) {
                setContainerAndChildrenEnabled((Container) tabContent, enabled);
            } else {
                tabContent.setEnabled(enabled);
            }
        }
    }

    /**
     * A recursive helper method that traverses a container and all its children
     * to set their enabled state.
     */
    private void setContainerAndChildrenEnabled(Container container, boolean enabled) {
        container.setEnabled(enabled);
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                setContainerAndChildrenEnabled((Container) component, enabled);
            }
            component.setEnabled(enabled);
        }
    }

    //region Save Methods
    private void saveBloodReport() {
        if (currentPatient == null) return;
        try {
            BloodReport report = new BloodReport();
            report.setPatient(currentPatient);
            report.setReportDate(LocalDate.now());
            report.setHemoglobin(Double.parseDouble(txtHgb.getText()));
            report.setWbcCount(Integer.parseInt(txtWbc.getText()));
            report.setRbcCount(Double.parseDouble(txtRbc.getText()));
            report.setPlatelets(Integer.parseInt(txtPlatelets.getText()));
            bloodReportDAO.save(report);
            JOptionPane.showMessageDialog(this, "Blood report saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid data. Please check all fields are numeric.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUrineReport() {
        if (currentPatient == null) return;
        try {
            UrineReport report = new UrineReport();
            report.setPatient(currentPatient);
            report.setReportDate(LocalDate.now());
            report.setColor(txtUrineColor.getText());
            report.setPh(Double.parseDouble(txtPh.getText()));
            report.setGlucose(txtGlucose.getText());
            report.setKetones(txtKetones.getText());
            urineReportDAO.save(report);
            JOptionPane.showMessageDialog(this, "Urine report saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid data. pH must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveXrayReport() {
        if (currentPatient == null) return;
        if (txtBodyPart.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Body Part cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        XrayReport report = new XrayReport();
        report.setPatient(currentPatient);
        report.setReportDate(LocalDate.now());
        report.setBodyPart(txtBodyPart.getText());
        report.setFindings(txtFindings.getText());
        report.setImpression(txtImpression.getText());
        xrayReportDAO.save(report);
        JOptionPane.showMessageDialog(this, "X-Ray report saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    //endregion
}