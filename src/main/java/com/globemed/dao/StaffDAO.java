package com.globemed.dao;

import com.globemed.config.HibernateUtil;
import com.globemed.model.Staff;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class StaffDAO extends GenericDAO<Staff, Integer> {
    public StaffDAO() {
        super(Staff.class);
    }

    /**
     * Searches for staff members where the search term matches their ID, name, or NIC.
     * @param searchTerm The term to search for.
     * @return A list of matching staff members.
     */
    public List<Staff> searchStaff(String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Staff s WHERE s.fullname LIKE :term OR s.nic LIKE :term OR s.email LIKE :term";

            // Try to parse as integer for ID search
            try {
                int id = Integer.parseInt(searchTerm);
                hql += " OR s.id = :id";
            } catch (NumberFormatException e) {
                // Not a number, so don't add ID search criteria
            }

            Query<Staff> query = session.createQuery(hql, Staff.class);
            query.setParameter("term", "%" + searchTerm + "%");

            try {
                int id = Integer.parseInt(searchTerm);
                query.setParameter("id", id);
            } catch (NumberFormatException e) {
                // Do nothing
            }

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}