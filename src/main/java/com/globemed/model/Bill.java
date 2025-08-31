package com.globemed.model;

import com.globemed.dp.visitor.IVisitableElement;
import com.globemed.dp.visitor.IReportVisitor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
public class Bill implements IVisitableElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "balance_due", nullable = false)
    private double balanceDue;

    @Column(name = "billing_date", nullable = false)
    private LocalDateTime billingDate;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;

    @Column(name = "payment_method")
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private InsuranceClaim insuranceClaim;

    public Bill() {
        this.billingDate = LocalDateTime.now(); // Default to current time
        this.isPaid = false; // Default to not paid
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getBalanceDue() { return balanceDue; }
    public void setBalanceDue(double balanceDue) { this.balanceDue = balanceDue; }
    public LocalDateTime getBillingDate() { return billingDate; }
    public void setBillingDate(LocalDateTime billingDate) { this.billingDate = billingDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public InsuranceClaim getInsuranceClaim() {
        return insuranceClaim;
    }

    public void setInsuranceClaim(InsuranceClaim insuranceClaim) {
        this.insuranceClaim = insuranceClaim;
    }

    @Override
    public void accept(IReportVisitor visitor) {
        visitor.visit(this);
    }
}