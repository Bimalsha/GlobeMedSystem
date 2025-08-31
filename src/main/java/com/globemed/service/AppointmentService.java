package com.globemed.service;

import com.globemed.dao.AppointmentDAO;
import com.globemed.dao.AppointmentStatusDAO;
import com.globemed.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final AppointmentStatusDAO statusDAO;

    public AppointmentService(AppointmentDAO appointmentDAO, AppointmentStatusDAO statusDAO) {
        this.appointmentDAO = appointmentDAO;
        this.statusDAO = statusDAO;
        // Ensure default statuses exist in the DB
        initStatuses();
    }

    private void initStatuses() {
        if (statusDAO.getStatusByName("Booked") == null) statusDAO.save(new com.globemed.model.AppointmentStatus("Booked"));
        if (statusDAO.getStatusByName("Cancelled") == null) statusDAO.save(new com.globemed.model.AppointmentStatus("Cancelled"));
        if (statusDAO.getStatusByName("Completed") == null) statusDAO.save(new com.globemed.model.AppointmentStatus("Completed"));
    }

    /**
     * The core logic for scheduling an appointment.
     * @param appointment The appointment object to be saved.
     * @return true if successful, false otherwise.
     */
    public boolean scheduleAppointment(Appointment appointment) {
        // Business Rule: Check for conflicts
        List<LocalTime> bookedSlots = appointmentDAO.getBookedSlotsForDoctor(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate()
        );
        if (bookedSlots.contains(appointment.getAppointmentTime())) {
            System.err.println("Conflict: Time slot is already booked.");
            return false;
        }

        // Business Rule: Set the initial status to "Booked"
        appointment.setStatus(statusDAO.getStatusByName("Booked"));
        appointmentDAO.save(appointment);
        return true;
    }

    /**
     * The core logic for canceling an appointment.
     * @param appointmentId The ID of the appointment to cancel.
     * @return true if successful, false otherwise.
     */
    public boolean cancelAppointment(int appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            System.err.println("Error: Appointment with ID " + appointmentId + " not found.");
            return false;
        }

        // Business Rule: Cannot cancel a "Completed" or already "Cancelled" appointment
        String currentStatus = appointment.getStatus().getName();
        if ("Completed".equalsIgnoreCase(currentStatus) || "Cancelled".equalsIgnoreCase(currentStatus)) {
            System.err.println("Error: Cannot cancel an appointment with status: " + currentStatus);
            return false;
        }

        appointment.setStatus(statusDAO.getStatusByName("Cancelled"));
        appointmentDAO.update(appointment);
        return true;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.findAll();
    }

    public List<LocalTime> getBookedSlots(int doctorId, LocalDate date) {
        return appointmentDAO.getBookedSlotsForDoctor(doctorId, date);
    }
}