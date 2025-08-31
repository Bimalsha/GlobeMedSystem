package com.globemed.dao;

import com.globemed.model.Patient;

public class PatientDAO extends GenericDAO<Patient, Integer> {
    public PatientDAO() {
        super(Patient.class);
    }
}