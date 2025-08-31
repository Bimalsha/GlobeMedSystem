package com.globemed.dp.decorator;

import com.globemed.model.Patient;
import java.time.LocalDateTime;
import java.util.List;

public class AuditLoggingDecorator extends PatientRecordDecorator {

    private String currentUser = "SYSTEM_ADMIN"; // In real app, get from Security Context

    public AuditLoggingDecorator(IPatientRecordService decoratedService) { super(decoratedService); }

    private void log(String action, String details) {
        System.out.printf("[AUDIT] User '%s' action '%s' at %s. Details: %s\n",
                currentUser, action, LocalDateTime.now(), details);
    }

    @Override
    public Patient getPatientById(int patientId) {
        log("GET_PATIENT_BY_ID", "Patient ID: " + patientId);
        return super.getPatientById(patientId);
    }

    @Override
    public void addPatient(Patient patient) {
        log("ADD_PATIENT", "New Patient: " + patient.getFullname());
        super.addPatient(patient);
    }

    @Override
    public void updatePatient(Patient patient) {
        log("UPDATE_PATIENT", "Patient ID: " + patient.getId());
        super.updatePatient(patient);
    }

    @Override
    public void deletePatient(int patientId) {
        log("DELETE_PATIENT", "Patient ID: " + patientId);
        super.deletePatient(patientId);
    }
}