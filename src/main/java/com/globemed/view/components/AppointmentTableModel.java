package com.globemed.view.components;

import com.globemed.model.Appointment;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentTableModel extends AbstractTableModel {
    private List<Appointment> appointments;
    private final String[] columnNames = {"Appt ID", "Patient Name", "Doctor", "Department", "Date", "Time", "Status"};
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public AppointmentTableModel() {
        this.appointments = new ArrayList<>();
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = (appointments != null) ? appointments : new ArrayList<>();
        fireTableDataChanged();
    }

    @Override public int getRowCount() { return appointments.size(); }
    @Override public int getColumnCount() { return columnNames.length; }
    @Override public String getColumnName(int column) { return columnNames[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Appointment appt = appointments.get(rowIndex);
        switch (columnIndex) {
            case 0: return appt.getId();
            case 1: return appt.getPatient() != null ? appt.getPatient().getFullname() : "N/A";
            case 2: return appt.getDoctor() != null ? appt.getDoctor().getFullname() : "N/A";
            case 3: return appt.getDepartment() != null ? appt.getDepartment().getName() : "N/A";
            case 4: return appt.getAppointmentDate().format(DATE_FORMATTER);
            case 5: return appt.getAppointmentTime().format(TIME_FORMATTER);
            case 6: return appt.getStatus() != null ? appt.getStatus().getName() : "N/A";
            default: return null;
        }
    }
}