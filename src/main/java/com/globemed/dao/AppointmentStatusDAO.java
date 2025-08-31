package com.globemed.dao;

import com.globemed.model.AppointmentStatus;

public class AppointmentStatusDAO extends GenericDAO<AppointmentStatus, Integer> {
    public AppointmentStatusDAO() {
        super(AppointmentStatus.class);
    }

    public AppointmentStatus getStatusByName(String name) {
        return findOneByProperty("name", name);
    }
}