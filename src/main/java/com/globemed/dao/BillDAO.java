package com.globemed.dao;

import com.globemed.model.Bill;

public class BillDAO extends GenericDAO<Bill, Integer> {
    public BillDAO() {
        super(Bill.class);
    }
}