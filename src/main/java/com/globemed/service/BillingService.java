package com.globemed.service;

import com.globemed.dao.BillDAO;
import com.globemed.dp.chain.ClaimHandler;
import com.globemed.dp.chain.FinalApprovalHandler;
import com.globemed.dp.chain.ValidationHandler;
import com.globemed.model.Bill;
import com.globemed.model.InsuranceClaim;

public class BillingService {
    private final BillDAO billDAO;
    // We no longer need the claimDAO here, as Hibernate will manage it via cascade.
    private final ClaimHandler claimProcessingChain;

    public BillingService(BillDAO billDAO) { // <-- REMOVED claimDAO from constructor
        this.billDAO = billDAO;
        this.claimProcessingChain = setupChain();
    }

    private ClaimHandler setupChain() {
        ClaimHandler validation = new ValidationHandler();
        ClaimHandler finalApproval = new FinalApprovalHandler();
        validation.setNextHandler(finalApproval);
        return validation;
    }

    /**
     * Corrected and simplified method to generate a bill.
     */
    public void generateBill(Bill bill, InsuranceClaim claim) {
        if ("Insurance".equals(bill.getPaymentMethod()) && claim != null) {
            // --- This is the new, safer logic ---
            // 1. Establish the link in both directions
            claim.setBill(bill);
            bill.setInsuranceClaim(claim);

            // 2. Process the claim with the Chain of Responsibility
            boolean isApproved = claimProcessingChain.handleRequest(claim);

            // 3. Set final bill status based on claim outcome
            if (isApproved) {
                bill.setBalanceDue(bill.getAmount() - claim.getClaimAmount());
                bill.setPaid(bill.getBalanceDue() <= 0);
            } else {
                bill.setBalanceDue(bill.getAmount());
                bill.setPaid(false);
            }
            // 4. Save the Bill. Hibernate will automatically save the associated InsuranceClaim
            // in the same transaction because of CascadeType.ALL.
            billDAO.save(bill);

        } else {
            // Process as a direct payment (no change here)
            bill.setBalanceDue(0);
            bill.setPaid(true);
            billDAO.save(bill);
        }
    }
}