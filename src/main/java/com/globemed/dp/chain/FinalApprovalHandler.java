package com.globemed.dp.chain;

import com.globemed.model.InsuranceClaim;

public class FinalApprovalHandler extends ClaimHandler {
    @Override
    public boolean handleRequest(InsuranceClaim claim) {
        System.out.println("[Chain] FinalApprovalHandler is processing claim...");
        // If it gets this far, it's approved.
        claim.setClaimStatus("Approved");
        claim.setRemarks("Claim approved for payment.");
        claim.setPaid(true);
        return true; // Successfully processed
    }
}