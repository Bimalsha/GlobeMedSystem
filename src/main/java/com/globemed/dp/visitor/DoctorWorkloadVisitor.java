package com.globemed.dp.visitor;

import com.globemed.model.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DoctorWorkloadVisitor implements IReportVisitor {
    // Map where: Key = Doctor Name, Value = Appointment Count
    private final Map<String, Integer> appointmentCounts = new HashMap<>();

    @Override
    public void visit(Appointment appointment) {
        // We only care about appointments that are not cancelled
        if (!"Cancelled".equalsIgnoreCase(appointment.getStatus().getName())) {
            String doctorName = appointment.getDoctor().getFullname();
            appointmentCounts.put(doctorName, appointmentCounts.getOrDefault(doctorName, 0) + 1);
        }
    }

    // Unused visit methods
    @Override public void visit(Patient patient) {}
    @Override public void visit(Bill bill) {}
    @Override public void visit(BloodReport report) {}
    @Override public void visit(UrineReport report) {}
    @Override public void visit(XrayReport report) {}

    @Override
    public void visit(InsuranceClaim claim) {

    }

    @Override
    public String getReport() {
        StringBuilder report = new StringBuilder();
        report.append("--- Doctor Appointment Workload Report ---\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");

        if (appointmentCounts.isEmpty()) {
            report.append("No appointment data to analyze.\n");
        } else {
            report.append(String.format("%-30s | %s\n", "Doctor Name", "Appointment Count"));
            report.append("-----------------------------------------+------------------\n");
            appointmentCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) // Sort by highest count
                    .forEach(entry -> {
                        report.append(String.format("%-30s | %d\n", entry.getKey(), entry.getValue()));
                    });
        }

        report.append("\n--- End of Report ---\n");
        return report.toString();
    }
}