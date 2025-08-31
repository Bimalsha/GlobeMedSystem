package com.globemed.dp.visitor;

import com.globemed.dao.AppointmentDAO;
import com.globemed.dao.BillDAO;
import com.globemed.model.Appointment;
import com.globemed.model.Bill;
import com.globemed.model.Patient;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ComprehensivePatientReportVisitor implements IReportVisitor {

    private final AppointmentDAO appointmentDAO;
    private final BillDAO billDAO;
    private final StringBuilder report = new StringBuilder();

    public ComprehensivePatientReportVisitor(AppointmentDAO appointmentDAO, BillDAO billDAO) {
        this.appointmentDAO = appointmentDAO;
        this.billDAO = billDAO;
    }

    @Override
    public void visit(Patient patient) {
        report.append("--- Comprehensive Medical Report ---\n\n");
        report.append("## PATIENT DEMOGRAPHICS ##\n");
        report.append(String.format("%-15s %s\n", "Patient ID:", patient.getId()));
        report.append(String.format("%-15s %s\n", "Full Name:", patient.getFullname()));
        report.append(String.format("%-15s %d\n", "Age:", patient.getAge()));
        report.append(String.format("%-15s %s\n", "Contact:", patient.getContact()));
        report.append(String.format("%-15s %s\n", "NIC:", patient.getNic()));
        report.append("\n");

        // Now, this visitor triggers the collection of related data
        List<Appointment> appointments = appointmentDAO.findByProperty("patient", patient, false);
        List<Bill> bills = billDAO.findByProperty("patient", patient, false);

        report.append("## APPOINTMENT HISTORY ##\n");
        if (appointments == null || appointments.isEmpty()) {
            report.append("No appointments found for this patient.\n");
        } else {
            for (Appointment appt : appointments) {
                visit(appt); // Visit each appointment to add it to the report
            }
        }
        report.append("\n");

        report.append("## BILLING HISTORY ##\n");
        if (bills == null || bills.isEmpty()) {
            report.append("No bills found for this patient.\n");
        } else {
            for (Bill bill : bills) {
                visit(bill); // Visit each bill to add it to the report
            }
        }
        report.append("\n--- End of Report ---\n");
    }

    @Override
    public void visit(Bill bill) {
        // This method adds a single bill's details to the report
        report.append(String.format("  - Bill #%d | Date: %s | Amount: LKR %.2f | Paid: %s\n",
                bill.getId(),
                bill.getBillingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                bill.getAmount(),
                bill.isPaid() ? "Yes" : "No"
        ));
    }

    @Override
    public void visit(Appointment appointment) {
        // This method adds a single appointment's details to the report
        report.append(String.format("  - Appt #%d | Date: %s | Doctor: %s | Status: %s\n",
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getDoctor().getFullname(),
                appointment.getStatus().getName()
        ));
    }

    @Override
    public String getReport() {
        return report.toString();
    }
}