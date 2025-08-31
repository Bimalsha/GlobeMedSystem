package com.globemed.dp.decorator;

import com.globemed.model.Patient;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class EncryptionDecorator extends PatientRecordDecorator {

    public EncryptionDecorator(IPatientRecordService decoratedService) { super(decoratedService); }

    @Override
    public void addPatient(Patient patient) {
        System.out.println("[Security] Encrypting patient data before adding...");
        super.addPatient(encryptPatientData(patient));
    }

    @Override
    public void updatePatient(Patient patient) {
        System.out.println("[Security] Encrypting patient data before updating...");
        super.updatePatient(encryptPatientData(patient));
    }

    @Override
    public Patient getPatientById(int patientId) {
        Patient encryptedPatient = super.getPatientById(patientId);
        return (encryptedPatient != null) ? decryptPatientData(encryptedPatient) : null;
    }

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> encryptedPatients = super.getAllPatients();
        return encryptedPatients.stream().map(this::decryptPatientData).collect(Collectors.toList());
    }

    private Patient encryptPatientData(Patient original) {
        original.setAddress(Base64.getEncoder().encodeToString(original.getAddress().getBytes()));
        original.setNic(Base64.getEncoder().encodeToString(original.getNic().getBytes()));
        return original;
    }

    private Patient decryptPatientData(Patient encrypted) {
        try {
            encrypted.setAddress(new String(Base64.getDecoder().decode(encrypted.getAddress())));
            encrypted.setNic(new String(Base64.getDecoder().decode(encrypted.getNic())));
        } catch (Exception e) {
            System.err.println("Could not decode data for patient ID " + encrypted.getId() + ".");
        }
        return encrypted;
    }
}