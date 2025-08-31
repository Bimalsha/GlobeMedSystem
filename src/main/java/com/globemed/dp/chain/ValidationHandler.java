package com.globemed.dp.chain;

import com.globemed.model.InsuranceClaim;

public class ValidationHandler extends ClaimHandler {
    @Override
    public boolean handleRequest(InsuranceClaim claim) {
        System.out.println("[Chain] ValidationHandler is processing claim...");
        if (claim.getPolicyNumber() == null || claim.getPolicyNumber().trim().isEmpty()) {
            claim.setClaimStatus("Rejected");
            claim.setRemarks("Validation Failed: Policy number is missing.");
            return false;
        }
        if (claim.getClaimAmount() <= 0) {
            claim.setClaimStatus("Rejected");
            claim.setRemarks("Validation Failed: Claim amount must be positive.");
            return false;
        }
        claim.setClaimStatus("Validated");
        return passToNext(claim);
    }
}