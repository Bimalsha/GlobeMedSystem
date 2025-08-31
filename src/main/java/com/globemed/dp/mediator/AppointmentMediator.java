package com.globemed.dp.mediator;

import com.globemed.dao.*;
import com.globemed.model.*;
import com.globemed.service.AppointmentService;
import com.globemed.view.components.AppointmentTableModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Concrete Mediator. It centralizes all communication and logic
 * for the AppointmentSchedulingPanel. It knows about all the colleague
 * components and coordinates their interactions.
 */
public class AppointmentMediator implements IAppointmentMediator {

    // References to all "Colleague" UI components
    private JTextField txtPatientId;
    private JLabel lblPatientName;
    private JComboBox<Department> cmbDepartment;
    private JComboBox<Staff> cmbDoctor;
    private JComboBox<LocalTime> cmbTimeSlot;
    private JComboBox<String> cmbPaymentType;
    private JSpinner dateSpinner;
    private JTable appointmentTable;
    private JButton btnCancelAppointment;

    // Backend services and DAOs
    private PatientDAO patientDAO;
    private StaffDAO staffDAO;
    private DepartmentDAO departmentDAO;
    private AppointmentService appointmentService;

    private List<LocalTime> allTimeSlots;
    private static final double APPOINTMENT_PRICE = 5000.00; // Example price in LKR

    public AppointmentMediator() {
        // Initialize backend components
        this.patientDAO = new PatientDAO();
        this.staffDAO = new StaffDAO();
        this.departmentDAO = new DepartmentDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        AppointmentStatusDAO statusDAO = new AppointmentStatusDAO();
        this.appointmentService = new AppointmentService(appointmentDAO, statusDAO);
        generateAllTimeSlots();
    }

    //region Registration methods for colleagues
    public void registerPatientIdField(JTextField txtPatientId) { this.txtPatientId = txtPatientId; }
    public void registerPatientNameLabel(JLabel lblPatientName) { this.lblPatientName = lblPatientName; }
    public void registerDepartmentComboBox(JComboBox<Department> cmbDepartment) { this.cmbDepartment = cmbDepartment; }
    public void registerDoctorComboBox(JComboBox<Staff> cmbDoctor) { this.cmbDoctor = cmbDoctor; }
    public void registerTimeSlotComboBox(JComboBox<LocalTime> cmbTimeSlot) { this.cmbTimeSlot = cmbTimeSlot; }
    public void registerPaymentTypeComboBox(JComboBox<String> cmbPaymentType) { this.cmbPaymentType = cmbPaymentType; }
    public void registerDateSpinner(JSpinner dateSpinner) { this.dateSpinner = dateSpinner; }
    public void registerAppointmentTable(JTable appointmentTable) { this.appointmentTable = appointmentTable; }
    public void registerCancelButton(JButton btnCancelAppointment) { this.btnCancelAppointment = btnCancelAppointment; }
    //endregion

    @Override
    public void initialize() {
        List<Department> departments = departmentDAO.findAll();
        cmbDepartment.removeAllItems();
        if (departments != null) {
            departments.forEach(cmbDepartment::addItem);
        }
        refreshAppointmentTable();
        btnCancelAppointment.setEnabled(false);
    }

    @Override
    public void departmentChanged() {
        Department selectedDept = (Department) cmbDepartment.getSelectedItem();
        cmbDoctor.removeAllItems();

        if (selectedDept != null) {
            List<Staff> allStaff = staffDAO.findAll();
            List<Staff> doctors = allStaff.stream()
                    .filter(s -> "Doctor".equalsIgnoreCase(s.getRole().getName()) && s.getDepartment().getId() == selectedDept.getId())
                    .collect(Collectors.toList());
            doctors.forEach(cmbDoctor::addItem);
        }
        doctorOrDateChanged();
    }

    @Override
    public void doctorOrDateChanged() {
        Staff selectedDoctor = (Staff) cmbDoctor.getSelectedItem();
        Date selectedDate = (Date) dateSpinner.getValue();
        cmbTimeSlot.removeAllItems();

        if (selectedDoctor == null || selectedDate == null) return;

        LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<LocalTime> bookedSlots = appointmentService.getBookedSlots(selectedDoctor.getId(), localDate);

        allTimeSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .forEach(cmbTimeSlot::addItem);
    }

    @Override
    public void checkPatient() {
        String patientIdStr = txtPatientId.getText().trim();
        if (patientIdStr.isEmpty()) {
            lblPatientName.setText("Please enter a Patient ID.");
            lblPatientName.setForeground(Color.RED);
            return;
        }
        try {
            int patientId = Integer.parseInt(patientIdStr);
            Patient patient = patientDAO.findById(patientId);
            if (patient != null) {
                lblPatientName.setText(patient.getFullname());
                lblPatientName.setForeground(Color.BLUE);
            } else {
                lblPatientName.setText("Patient not found.");
                lblPatientName.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            lblPatientName.setText("Invalid ID format.");
            lblPatientName.setForeground(Color.RED);
        }
    }

    @Override
    public void bookAppointment() {
        // Validation logic centralized in the mediator
        Patient patient = null;
        try {
            patient = patientDAO.findById(Integer.parseInt(txtPatientId.getText().trim()));
        } catch(NumberFormatException e) { /* patient remains null */ }

        if (patient == null) {
            JOptionPane.showMessageDialog(null, "A valid patient must be fetched first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Staff doctor = (Staff) cmbDoctor.getSelectedItem();
        LocalTime timeSlot = (LocalTime) cmbTimeSlot.getSelectedItem();
        if (doctor == null || timeSlot == null) {
            JOptionPane.showMessageDialog(null, "Please select a doctor and an available time slot.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);
        newAppointment.setDepartment(doctor.getDepartment());
        newAppointment.setAppointmentDate(((Date) dateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        newAppointment.setAppointmentTime(timeSlot);
        newAppointment.setPrice(APPOINTMENT_PRICE);
        newAppointment.setPaymentType((String) cmbPaymentType.getSelectedItem());

        boolean success = appointmentService.scheduleAppointment(newAppointment);

        if (success) {
            JOptionPane.showMessageDialog(null, "Appointment booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAppointmentTable();
            doctorOrDateChanged(); // Refresh available slots
        } else {
            JOptionPane.showMessageDialog(null, "Failed to book appointment. The time slot may have just been taken.", "Booking Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void cancelAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an appointment to cancel.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int appointmentId = (int) appointmentTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel appointment #" + appointmentId + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = appointmentService.cancelAppointment(appointmentId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Appointment cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAppointmentTable();
                doctorOrDateChanged(); // A slot may have become free
            } else {
                JOptionPane.showMessageDialog(null, "Failed to cancel the appointment. It may have been completed already.", "Cancellation Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper methods
    private void generateAllTimeSlots() {
        allTimeSlots = new ArrayList<>();
        LocalTime time = LocalTime.of(9, 0);
        while (time.isBefore(LocalTime.of(17, 0))) {
            allTimeSlots.add(time);
            time = time.plusMinutes(30);
        }
    }

    private void refreshAppointmentTable() {
        if (appointmentTable.getModel() instanceof AppointmentTableModel) {
            ((AppointmentTableModel) appointmentTable.getModel()).setAppointments(appointmentService.getAllAppointments());
        }
    }
}