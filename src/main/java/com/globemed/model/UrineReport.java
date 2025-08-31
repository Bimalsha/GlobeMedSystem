package com.globemed.model;

import com.globemed.dp.visitor.IVisitableElement;
import com.globemed.dp.visitor.IReportVisitor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "urine_report")
public class UrineReport implements IVisitableElement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;
    private String color;
    private Double ph;
    private String glucose;
    private String ketones;

    // Getters and Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Double getPh() { return ph; }
    public void setPh(Double ph) { this.ph = ph; }
    public String getGlucose() { return glucose; }
    public void setGlucose(String glucose) { this.glucose = glucose; }
    public String getKetones() { return ketones; }
    public void setKetones(String ketones) { this.ketones = ketones; }

    @Override
    public void accept(IReportVisitor visitor) {
        visitor.visit(this);
    }
}