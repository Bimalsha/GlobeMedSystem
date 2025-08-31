package com.globemed.dao;

import com.globemed.model.InsuranceClaim;

public class InsuranceClaimDAO extends GenericDAO<InsuranceClaim, Integer> {
    public InsuranceClaimDAO() {
        super(InsuranceClaim.class);
    }
}