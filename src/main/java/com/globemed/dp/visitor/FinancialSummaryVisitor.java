package com.globemed.dp.visitor;

import com.globemed.model.*;

import java.time.LocalDate;

public class FinancialSummaryVisitor implements IReportVisitor {
    private double totalRevenue = 0;
    private double totalOutstanding = 0;
    private int billCount = 0;

    @Override
    public void visit(Bill bill) {
        // This visitor only cares about bills
        billCount++;
        totalRevenue += bill.getAmount();
        if (!bill.isPaid()) {
            totalOutstanding += bill.getBalanceDue();
        }
    }

    // Other visit methods are ignored
    @Override public void visit(Patient patient) {}
    @Override public void visit(Appointment appointment) {}

    @Override public void visit(BloodReport report) {}
    @Override public void visit(UrineReport report) {}
    @Override public void visit(XrayReport report) {}
    @Override
    public String getReport() {
        StringBuilder report = new StringBuilder();
        report.append("--- Financial Summary Report ---\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        report.append(String.format("%-25s %d\n", "Total Bills Processed:", billCount));
        report.append(String.format("%-25s LKR %.2f\n", "Total Billed Revenue:", totalRevenue));
        report.append(String.format("%-25s LKR %.2f\n", "Total Outstanding Balance:", totalOutstanding));
        report.append("\n--- End of Report ---\n");
        return report.toString();
    }
}