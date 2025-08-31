package com.globemed.dao;

import com.globemed.model.InsuranceClaim;
// Add other necessary imports if your IDE suggests them
import com.globemed.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Collections;

public class InsuranceClaimDAO extends GenericDAO<InsuranceClaim, Integer> {
    public InsuranceClaimDAO() {
        super(InsuranceClaim.class);
    }

    /**
     * A specific method to find all claims for reporting purposes.
     * Could be extended to filter by date range, status etc.
     * @return A list of all InsuranceClaim objects.
     */
    public List<InsuranceClaim> findAllForReporting() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM InsuranceClaim";
            Query<InsuranceClaim> query = session.createQuery(hql, InsuranceClaim.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return empty list on error
        }
    }
}