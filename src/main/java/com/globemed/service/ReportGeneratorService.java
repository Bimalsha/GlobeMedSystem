package com.globemed.service;

import com.globemed.dp.visitor.IReportVisitor;
import com.globemed.dp.visitor.IVisitableElement;
import java.util.List;

public class ReportGeneratorService {

    /**
     * Generates a report by applying a visitor to a single root data element.
     */
    public String generateReport(IVisitableElement element, IReportVisitor visitor) {
        element.accept(visitor);
        return visitor.getReport();
    }

    /**
     * Generates an aggregate report by applying a visitor to a list of data elements.
     */
    public String generateReportForList(List<? extends IVisitableElement> elements, IReportVisitor visitor) {
        for (IVisitableElement element : elements) {
            element.accept(visitor);
        }
        return visitor.getReport();
    }
}