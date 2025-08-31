package com.globemed.service;

import com.globemed.dao.BillDAO;
import com.globemed.dao.InsuranceClaimDAO;
import com.globemed.dp.chain.ClaimHandler;
import com.globemed.dp.chain.FinalApprovalHandler;
import com.globemed.dp.chain.ValidationHandler;
import com.globemed.model.Bill;
import com.globemed.model.InsuranceClaim;

public class BillingService {
    private final BillDAO billDAO;
    private final InsuranceClaimDAO claimDAO;
    private final ClaimHandler claimProcessingChain;

    public BillingService(BillDAO billDAO, InsuranceClaimDAO claimDAO) {
        this.billDAO = billDAO;
        this.claimDAO = claimDAO;
        this.claimProcessingChain = setupChain();
    }

    private ClaimHandler setupChain() {
        ClaimHandler validation = new ValidationHandler();
        ClaimHandler finalApproval = new FinalApprovalHandler();
        validation.setNextHandler(finalApproval); // Simple two-step chain
        return validation;
    }

    public void generateBill(Bill bill, InsuranceClaim claim) {
        if ("Insurance".equals(bill.getPaymentMethod())) {
            // Process as an insurance claim
            billDAO.save(bill); // Save bill to get an ID
            claim.setBill(bill);
            claimDAO.save(claim); // Save claim to get an ID

            boolean isApproved = claimProcessingChain.handleRequest(claim);

            if (isApproved) {
                bill.setBalanceDue(bill.getAmount() - claim.getClaimAmount());
                bill.setPaid(bill.getBalanceDue() <= 0);
            } else {
                bill.setBalanceDue(bill.getAmount());
                bill.setPaid(false);
            }
            claimDAO.update(claim);
            billDAO.update(bill);
        } else {
            // Process as a direct payment
            bill.setBalanceDue(0);
            bill.setPaid(true);
            billDAO.save(bill);
        }
    }
}