package com.globemed.dp.visitor;

import com.globemed.model.*;

public interface IReportVisitor {
    void visit(Patient patient);
    void visit(Bill bill);
    void visit(Appointment appointment);

    // Add visit methods for the new report types
    void visit(BloodReport report);
    void visit(UrineReport report);
    void visit(XrayReport report);

    String getReport();
}