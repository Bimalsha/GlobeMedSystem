package com.globemed.dao;

import com.globemed.config.HibernateUtil;
import com.globemed.model.Appointment;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentDAO extends GenericDAO<Appointment, Integer> {
    public AppointmentDAO() {
        super(Appointment.class);
    }

    /**
     * Finds all booked (i.e., not cancelled) appointments for a specific doctor on a given date.
     * @param doctorId The ID of the doctor.
     * @param date The date to check.
     * @return A list of booked appointment times.
     */
    public List<LocalTime> getBookedSlotsForDoctor(int doctorId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT a.appointmentTime FROM Appointment a " +
                    "WHERE a.doctor.id = :doctorId " +
                    "AND a.appointmentDate = :date " +
                    "AND a.status.name != 'Cancelled'";
            Query<LocalTime> query = session.createQuery(hql, LocalTime.class);
            query.setParameter("doctorId", doctorId);
            query.setParameter("date", date);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }
}