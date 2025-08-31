package com.globemed.view.panels;

import com.globemed.dao.*;
import com.globemed.dp.visitor.ComprehensivePatientReportVisitor;
import com.globemed.dp.visitor.FinancialSummaryVisitor;
import com.globemed.model.Patient;
import com.globemed.service.ReportGeneratorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Collections;

public class ReportsPanel extends JPanel {

    private JTextField txtPatientId;
    private JButton btnPatientReport;
    private JButton btnFinancialReport;
    private JTextArea reportArea;

    // DAOs and Service needed for reporting
    private PatientDAO patientDAO;
    private BillDAO billDAO;
    private ReportGeneratorService reportService;

    public ReportsPanel() {
        this.patientDAO = new PatientDAO();
        this.billDAO = new BillDAO();
        this.reportService = new ReportGeneratorService();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        setupLayout();
        addListeners();
    }

    private void initComponents() {
        txtPatientId = new JTextField(10);
        btnPatientReport = new JButton("Generate Comprehensive Patient Report");
        btnFinancialReport = new JButton("Generate System-Wide Financial Summary");
        reportArea = new JTextArea("Generated reports will appear here.");
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    private void setupLayout() {
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));

        // Patient Report Section
        JPanel patientReportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        patientReportPanel.setBorder(new TitledBorder("Patient-Specific Reports"));
        patientReportPanel.add(new JLabel("Enter Patient ID:"));
        patientReportPanel.add(txtPatientId);
        patientReportPanel.add(btnPatientReport);

        // System-wide Report Section
        JPanel systemReportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        systemReportPanel.setBorder(new TitledBorder("Aggregate System Reports"));
        systemReportPanel.add(btnFinancialReport);

        controlsPanel.add(patientReportPanel);
        controlsPanel.add(systemReportPanel);

        add(controlsPanel, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
    }

    private void addListeners() {
        btnPatientReport.addActionListener(e -> generatePatientReport());
        btnFinancialReport.addActionListener(e -> generateFinancialReport());
    }

    private void generatePatientReport() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText().trim());
            Patient patient = patientDAO.findById(patientId);
            if (patient == null) {
                JOptionPane.showMessageDialog(this, "Patient with ID " + patientId + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Create the specific visitor, providing it with all necessary DAOs
            ComprehensivePatientReportVisitor visitor = new ComprehensivePatientReportVisitor(
                    new AppointmentDAO(),
                    new BillDAO(),
                    new BloodReportDAO(),
                    new UrineReportDAO(),
                    new XrayReportDAO()
            );
            // Use the service to generate the report
            String report = reportService.generateReport(patient, visitor);
            reportArea.setText(report);
            reportArea.setCaretPosition(0); // Scroll to top

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Patient ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateFinancialReport() {
        // Create the visitor for this report
        FinancialSummaryVisitor visitor = new FinancialSummaryVisitor();
        // Use the service to generate the report on the list of all bills
        String report = reportService.generateReportForList(
                billDAO.findAll() != null ? billDAO.findAll() : Collections.emptyList(),
                visitor
        );
        reportArea.setText(report);
        reportArea.setCaretPosition(0); // Scroll to top
    }
}