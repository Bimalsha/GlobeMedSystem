package com.globemed.dp.decorator;

import com.globemed.dao.PatientDAO;
import com.globemed.model.Patient;
import java.util.List;

public class BasePatientRecordService implements IPatientRecordService {
    private final PatientDAO patientDAO;

    public BasePatientRecordService(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    @Override public Patient getPatientById(int patientId) { return patientDAO.findById(patientId); }
    @Override public List<Patient> getAllPatients() { return patientDAO.findAll(); }
    @Override public List<Patient> searchPatients(String searchTerm) { return patientDAO.findByProperty("fullname", searchTerm, true); }
    @Override public void addPatient(Patient patient) { patientDAO.save(patient); }
    @Override public void updatePatient(Patient patient) { patientDAO.update(patient); }
    @Override
    public void deletePatient(int patientId) {
        Patient patient = patientDAO.findById(patientId);
        if (patient != null) {
            patientDAO.delete(patient);
        }
    }
}