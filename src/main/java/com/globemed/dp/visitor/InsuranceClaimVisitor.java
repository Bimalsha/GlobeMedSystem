package com.globemed.dp.visitor;

import com.globemed.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsuranceClaimVisitor implements IReportVisitor {
    // Structure remains the same
    private final Map<String, Map<String, List<String>>> claimsByProvider = new HashMap<>();
    private double totalClaimedAmount = 0;
    private double totalApprovedAmount = 0;

    /**
     * This is now the primary method for this visitor.
     * It processes an InsuranceClaim object directly.
     */
    @Override
    public void visit(InsuranceClaim claim) {
        if (claim == null) return;

        String providerName = claim.getProvider().getName();
        String status = claim.getClaimStatus();

        // We can get the Bill and Patient from the claim object
        Bill bill = claim.getBill();
        Patient patient = (bill != null) ? bill.getPatient() : null;
        String patientName = (patient != null) ? patient.getFullname() : "N/A";

        String claimDetail = String.format("  - Bill #%d | Patient: %s | Claimed: LKR %.2f | Remarks: %s",
                (bill != null) ? bill.getId() : 0,
                patientName,
                claim.getClaimAmount(),
                claim.getRemarks()
        );

        // Update statistics
        totalClaimedAmount += claim.getClaimAmount();
        if ("Approved".equalsIgnoreCase(status)) {
            totalApprovedAmount += claim.getClaimAmount();
        }

        // Group the data for the report
        claimsByProvider.computeIfAbsent(providerName, k -> new HashMap<>())
                .computeIfAbsent(status, k -> new ArrayList<>())
                .add(claimDetail);
    }

    // Unused visit methods are now empty shells
    @Override public void visit(Patient patient) {}
    @Override public void visit(Bill bill) {} // We no longer start from the Bill
    @Override public void visit(Appointment appointment) {}
    @Override public void visit(BloodReport report) {}
    @Override public void visit(UrineReport report) {}
    @Override public void visit(XrayReport report) {}

    @Override
    public String getReport() {
        // The report generation logic itself remains exactly the same.
        StringBuilder report = new StringBuilder();
        report.append("--- Insurance Claim Summary Report ---\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");

        report.append("## Overall Statistics ##\n");
        report.append(String.format("%-25s LKR %.2f\n", "Total Amount Claimed:", totalClaimedAmount));
        report.append(String.format("%-25s LKR %.2f\n", "Total Amount Approved:", totalApprovedAmount));
        report.append("\n");

        report.append("## Claims Breakdown by Provider ##\n");
        if (claimsByProvider.isEmpty()) {
            report.append("No insurance claims found.\n");
        } else {
            claimsByProvider.forEach((provider, statusMap) -> {
                report.append("--------------------------------------\n");
                report.append("Provider: ").append(provider).append("\n");
                report.append("--------------------------------------\n");
                statusMap.forEach((status, details) -> {
                    report.append("Status: ").append(status.toUpperCase()).append("\n");
                    details.forEach(detail -> report.append(detail).append("\n"));
                });
                report.append("\n");
            });
        }
        report.append("--- End of Report ---\n");
        return report.toString();
    }
}