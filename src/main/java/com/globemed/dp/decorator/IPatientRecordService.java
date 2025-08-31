package com.globemed.dp.decorator;

import com.globemed.model.Patient;
import java.util.List;

public interface IPatientRecordService {
    Patient getPatientById(int patientId);
    List<Patient> getAllPatients();
    List<Patient> searchPatients(String searchTerm);

    void addPatient(Patient patient);
    void updatePatient(Patient patient);
    void deletePatient(int patientId);
}