package com.globemed.service;

import com.globemed.dao.PatientDAO;
import com.globemed.dp.decorator.*;
import com.globemed.model.Patient;
import java.util.List;

public class PatientService {

    private final IPatientRecordService patientRecordService;

    public PatientService(PatientDAO patientDAO) {
        // Compose the decorators: Base -> Encryption -> Auditing
        IPatientRecordService baseService = new BasePatientRecordService(patientDAO);
        IPatientRecordService encryptedService = new EncryptionDecorator(baseService);
        this.patientRecordService = new AuditLoggingDecorator(encryptedService);
    }

    public Patient getPatientById(int patientId) { return patientRecordService.getPatientById(patientId); }
    public List<Patient> getAllPatients() { return patientRecordService.getAllPatients(); }
    public List<Patient> searchPatients(String searchTerm) { return patientRecordService.searchPatients(searchTerm); }
    public void addPatient(Patient patient) { patientRecordService.addPatient(patient); }
    public void updatePatient(Patient patient) { patientRecordService.updatePatient(patient); }
    public void deletePatient(int patientId) { patientRecordService.deletePatient(patientId); }
}