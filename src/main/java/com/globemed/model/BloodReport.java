package com.globemed.model;

import com.globemed.dp.visitor.IVisitableElement;
import com.globemed.dp.visitor.IReportVisitor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "blood_report")
public class BloodReport implements IVisitableElement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;
    private Double hemoglobin;
    @Column(name = "wbc_count")
    private Integer wbcCount;
    @Column(name = "rbc_count")
    private Double rbcCount;
    private Integer platelets;

    // Getters and Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public Double getHemoglobin() { return hemoglobin; }
    public void setHemoglobin(Double hemoglobin) { this.hemoglobin = hemoglobin; }
    public Integer getWbcCount() { return wbcCount; }
    public void setWbcCount(Integer wbcCount) { this.wbcCount = wbcCount; }
    public Double getRbcCount() { return rbcCount; }
    public void setRbcCount(Double rbcCount) { this.rbcCount = rbcCount; }
    public Integer getPlatelets() { return platelets; }
    public void setPlatelets(Integer platelets) { this.platelets = platelets; }

    @Override
    public void accept(IReportVisitor visitor) {
        visitor.visit(this);
    }
}