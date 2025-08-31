package com.globemed.dp.visitor;
public interface IVisitableElement {
    void accept(IReportVisitor visitor);
}
