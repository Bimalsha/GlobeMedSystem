package com.globemed.dp.visitor;

import com.globemed.dao.*; // Import new DAOs
import com.globemed.model.*; // Import new Models
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ComprehensivePatientReportVisitor implements IReportVisitor {

    // Add references to new DAOs
    private final AppointmentDAO appointmentDAO;
    private final BillDAO billDAO;
    private final BloodReportDAO bloodReportDAO;
    private final UrineReportDAO urineReportDAO;
    private final XrayReportDAO xrayReportDAO;

    private final StringBuilder report = new StringBuilder();

    public ComprehensivePatientReportVisitor(AppointmentDAO appointmentDAO, BillDAO billDAO,
                                             BloodReportDAO bloodReportDAO, UrineReportDAO urineReportDAO,
                                             XrayReportDAO xrayReportDAO) {
        this.appointmentDAO = appointmentDAO;
        this.billDAO = billDAO;
        this.bloodReportDAO = bloodReportDAO;
        this.urineReportDAO = urineReportDAO;
        this.xrayReportDAO = xrayReportDAO;
    }

    @Override
    public void visit(Patient patient) {
        report.append("--- Comprehensive Medical Report ---\n\n");
        report.append("## PATIENT DEMOGRAPHICS ##\n");
        report.append(String.format("%-15s %s\n", "Patient ID:", patient.getId()));
        report.append(String.format("%-15s %s\n", "Full Name:", patient.getFullname()));
        // ... other demographic info ...
        report.append("\n");

        // --- Fetch and display all data related to the patient ---

        // Appointments
        List<Appointment> appointments = appointmentDAO.findByProperty("patient", patient, false);
        report.append("## APPOINTMENT HISTORY ##\n");
        if (appointments.isEmpty()) report.append("No appointments found.\n");
        else appointments.forEach(this::visit);
        report.append("\n");

        // Billing
        List<Bill> bills = billDAO.findByProperty("patient", patient, false);
        report.append("## BILLING HISTORY ##\n");
        if (bills.isEmpty()) report.append("No bills found.\n");
        else bills.forEach(this::visit);
        report.append("\n");

        // Blood Reports
        List<BloodReport> bloodReports = bloodReportDAO.findByProperty("patient", patient, false);
        report.append("## BLOOD TEST RESULTS ##\n");
        if (bloodReports.isEmpty()) report.append("No blood reports found.\n");
        else bloodReports.forEach(this::visit);
        report.append("\n");

        // Urine Reports
        List<UrineReport> urineReports = urineReportDAO.findByProperty("patient", patient, false);
        report.append("## URINE TEST RESULTS ##\n");
        if (urineReports.isEmpty()) report.append("No urine reports found.\n");
        else urineReports.forEach(this::visit);
        report.append("\n");

        // X-Ray Reports
        List<XrayReport> xrayReports = xrayReportDAO.findByProperty("patient", patient, false);
        report.append("## X-RAY IMAGING RESULTS ##\n");
        if (xrayReports.isEmpty()) report.append("No X-Ray reports found.\n");
        else xrayReports.forEach(this::visit);
        report.append("\n");

        report.append("--- End of Report ---\n");
    }

    // ... existing visit(Bill) and visit(Appointment) methods ...
    @Override public void visit(Bill bill) { /* ... same as before ... */ }
    @Override public void visit(Appointment appointment) { /* ... same as before ... */ }

    // --- Implement new visit methods to format each report ---
    @Override
    public void visit(BloodReport r) {
        report.append(String.format("  - Date: %s | HGB: %.1f | WBC: %d | RBC: %.2f | Platelets: %d\n",
                r.getReportDate(), r.getHemoglobin(), r.getWbcCount(), r.getRbcCount(), r.getPlatelets()));
    }

    @Override
    public void visit(UrineReport r) {
        report.append(String.format("  - Date: %s | Color: %s | pH: %.1f | Glucose: %s | Ketones: %s\n",
                r.getReportDate(), r.getColor(), r.getPh(), r.getGlucose(), r.getKetones()));
    }

    @Override
    public void visit(XrayReport r) {
        report.append(String.format("  - Date: %s | Body Part: %s\n", r.getReportDate(), r.getBodyPart()));
        report.append(String.format("    Findings: %s\n", r.getFindings()));
        report.append(String.format("    Impression: %s\n", r.getImpression()));
    }

    @Override
    public void visit(InsuranceClaim claim) {

    }

    @Override
    public String getReport() {
        return report.toString();
    }
}