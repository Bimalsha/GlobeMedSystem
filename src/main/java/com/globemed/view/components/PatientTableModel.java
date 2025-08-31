package com.globemed.view.components;

import com.globemed.model.Patient;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PatientTableModel extends AbstractTableModel {
    private List<Patient> patients;
    private final String[] columnNames = {"ID", "Full Name", "Age", "Contact No.", "Address", "NIC", "Gender"};

    public PatientTableModel() {
        this.patients = new ArrayList<>();
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
        fireTableDataChanged();
    }

    public Patient getPatientAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < patients.size()) {
            return patients.get(rowIndex);
        }
        return null;
    }

    @Override public int getRowCount() { return patients.size(); }
    @Override public int getColumnCount() { return columnNames.length; }
    @Override public String getColumnName(int column) { return columnNames[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Patient patient = patients.get(rowIndex);
        switch (columnIndex) {
            case 0: return patient.getId();
            case 1: return patient.getFullname();
            case 2: return patient.getAge();
            case 3: return patient.getContact();
            case 4: return patient.getAddress();
            case 5: return patient.getNic();
            case 6: return patient.getGender() != null ? patient.getGender().getName() : "N/A";
            default: return null;
        }
    }
}