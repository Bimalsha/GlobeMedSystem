package com.globemed.model;

import com.globemed.dp.visitor.IVisitableElement;
import com.globemed.dp.visitor.IReportVisitor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "xray_report")
public class XrayReport implements IVisitableElement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;
    @Column(name = "body_part", nullable = false)
    private String bodyPart;
    @Lob private String findings;
    @Lob private String impression;

    // Getters and Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public String getBodyPart() { return bodyPart; }
    public void setBodyPart(String bodyPart) { this.bodyPart = bodyPart; }
    public String getFindings() { return findings; }
    public void setFindings(String findings) { this.findings = findings; }
    public String getImpression() { return impression; }
    public void setImpression(String impression) { this.impression = impression; }

    @Override
    public void accept(IReportVisitor visitor) {
        visitor.visit(this);
    }
}