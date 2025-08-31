package com.globemed.dp.chain;

import com.globemed.model.InsuranceClaim;

public abstract class ClaimHandler {
    protected ClaimHandler nextHandler;

    public void setNextHandler(ClaimHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract boolean handleRequest(InsuranceClaim claim);

    protected boolean passToNext(InsuranceClaim claim) {
        if (nextHandler != null) {
            return nextHandler.handleRequest(claim);
        }
        return true; // End of chain, successfully processed
    }
}