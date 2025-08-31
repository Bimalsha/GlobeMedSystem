package com.globemed.view.panels;

import com.globemed.dao.*;
import com.globemed.dp.visitor.ComprehensivePatientReportVisitor;
import com.globemed.dp.visitor.DoctorWorkloadVisitor;
import com.globemed.dp.visitor.FinancialSummaryVisitor;
import com.globemed.dp.visitor.InsuranceClaimVisitor;
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
    private JButton btnInsuranceReport; // <-- NEW
    private JButton btnDoctorWorkloadReport; // <-- NEW
    private JTextArea reportArea;

    // DAOs and Service needed for reporting
    private PatientDAO patientDAO;
    private BillDAO billDAO;
    private AppointmentDAO appointmentDAO; // <-- NEW
    private ReportGeneratorService reportService;
    private InsuranceClaimDAO insuranceClaimDAO;

    public ReportsPanel() {
        this.patientDAO = new PatientDAO();
        this.billDAO = new BillDAO();
        this.appointmentDAO = new AppointmentDAO(); // <-- NEW
        this.reportService = new ReportGeneratorService();
        this.insuranceClaimDAO = new InsuranceClaimDAO();

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
        btnInsuranceReport = new JButton("Generate Insurance Claim Summary"); // <-- NEW
        btnDoctorWorkloadReport = new JButton("Generate Doctor Workload Report"); // <-- NEW
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
        JPanel systemReportPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // Use GridLayout for better alignment
        systemReportPanel.setBorder(new TitledBorder("Aggregate System Reports"));
        systemReportPanel.add(btnFinancialReport);
        systemReportPanel.add(btnInsuranceReport); // <-- NEW
        systemReportPanel.add(btnDoctorWorkloadReport); // <-- NEW

        controlsPanel.add(patientReportPanel);
        controlsPanel.add(systemReportPanel);

        add(controlsPanel, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
    }

    private void addListeners() {
        btnPatientReport.addActionListener(e -> generatePatientReport());
        btnFinancialReport.addActionListener(e -> generateFinancialReport());
        btnInsuranceReport.addActionListener(e -> generateInsuranceReport()); // <-- NEW
        btnDoctorWorkloadReport.addActionListener(e -> generateDoctorWorkloadReport()); // <-- NEW
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
            displayReport(report);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Patient ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateFinancialReport() {
        FinancialSummaryVisitor visitor = new FinancialSummaryVisitor();
        String report = reportService.generateReportForList(
                billDAO.findAll() != null ? billDAO.findAll() : Collections.emptyList(),
                visitor
        );
        displayReport(report);
    }

    private void generateInsuranceReport() {
        InsuranceClaimVisitor visitor = new InsuranceClaimVisitor();
        String report = reportService.generateReportForList(
                billDAO.findAll() != null ? billDAO.findAll() : Collections.emptyList(),
                visitor
        );
        displayReport(report);
    }

    private void generateDoctorWorkloadReport() {
        DoctorWorkloadVisitor visitor = new DoctorWorkloadVisitor();
        String report = reportService.generateReportForList(
                appointmentDAO.findAll() != null ? appointmentDAO.findAll() : Collections.emptyList(),
                visitor
        );
        displayReport(report);
    }

    private void displayReport(String report) {
        reportArea.setText(report);
        reportArea.setCaretPosition(0); // Scroll to the top of the report
    }
}