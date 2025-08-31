package com.globemed.dp.decorator;

import com.globemed.model.Patient;
import java.util.List;

public abstract class PatientRecordDecorator implements IPatientRecordService {
    protected IPatientRecordService decoratedService;

    public PatientRecordDecorator(IPatientRecordService decoratedService) {
        this.decoratedService = decoratedService;
    }

    @Override public Patient getPatientById(int patientId) { return decoratedService.getPatientById(patientId); }
    @Override public List<Patient> getAllPatients() { return decoratedService.getAllPatients(); }
    @Override public List<Patient> searchPatients(String searchTerm) { return decoratedService.searchPatients(searchTerm); }
    @Override public void addPatient(Patient patient) { decoratedService.addPatient(patient); }
    @Override public void updatePatient(Patient patient) { decoratedService.updatePatient(patient); }
    @Override public void deletePatient(int patientId) { decoratedService.deletePatient(patientId); }
}