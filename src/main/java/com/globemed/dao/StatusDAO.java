package com.globemed.dao;

import com.globemed.model.Status;

public class StatusDAO extends GenericDAO<Status, Integer> {
    public StatusDAO() {
        super(Status.class);
    }
}