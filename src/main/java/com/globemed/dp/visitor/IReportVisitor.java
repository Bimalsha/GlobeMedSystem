package com.globemed.dp.visitor;

import com.globemed.model.Appointment; // <-- ADD THIS IMPORT
import com.globemed.model.Bill;
import com.globemed.model.Patient;

/**
 * The Visitor interface declares a set of visiting methods that correspond
 * to concrete element classes.
 */
public interface IReportVisitor {
    void visit(Patient patient);
    void visit(Bill bill);
    void visit(Appointment appointment); // <-- ADD THIS METHOD

    String getReport(); // A common method to retrieve the final report string
}