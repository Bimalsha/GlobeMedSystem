package com.globemed.dp.mediator;

/**
 * The Mediator interface defines the contract for communication between
 * the colleague components of the appointment scheduling panel.
 */
public interface IAppointmentMediator {
    // Notifications from UI components (Colleagues)
    void departmentChanged();
    void doctorOrDateChanged();
    void checkPatient();

    // Actions requested by UI components
    void bookAppointment();
    void cancelAppointment();

    // Method for the Mediator to initialize its state
    void initialize();
}