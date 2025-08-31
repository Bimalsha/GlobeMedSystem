package com.globemed.model;

import javax.persistence.*;

@Entity
@Table(name = "insurance_claim")
public class InsuranceClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "claim_amount", nullable = false)
    private double claimAmount;

    @Column(name = "claim_status", nullable = false)
    private String claimStatus;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;

    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @OneToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", nullable = false)
    private InsuranceProvider provider;

    public InsuranceClaim() {
        this.isPaid = false; // Default to not paid
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getClaimAmount() { return claimAmount; }
    public void setClaimAmount(double claimAmount) { this.claimAmount = claimAmount; }
    public String getClaimStatus() { return claimStatus; }
    public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
    public InsuranceProvider getProvider() { return provider; }
    public void setProvider(InsuranceProvider provider) { this.provider = provider; }
}