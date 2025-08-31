package com.globemed.service;

import com.globemed.dao.StaffDAO;
import com.globemed.model.Staff;
import com.globemed.util.PasswordUtil;

import java.util.List;

public class StaffService {

    private final StaffDAO staffDAO;

    public StaffService(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    public void addStaff(Staff staff, String plainPassword) {
        // Hash the password before saving the new staff member
        staff.setPasswordHash(PasswordUtil.hashPassword(plainPassword));
        staffDAO.save(staff);
    }

    public void updateStaff(Staff staff, String newPlainPassword) {
        // Only update the password hash if a new password is provided
        if (newPlainPassword != null && !newPlainPassword.isEmpty()) {
            staff.setPasswordHash(PasswordUtil.hashPassword(newPlainPassword));
        } else {
            // If no new password, retain the old hash
            Staff existingStaff = staffDAO.findById(staff.getId());
            if (existingStaff != null) {
                staff.setPasswordHash(existingStaff.getPasswordHash());
            }
        }
        staffDAO.update(staff);
    }

    public void deleteStaff(int staffId) {
        Staff staff = staffDAO.findById(staffId);
        if (staff != null) {
            staffDAO.delete(staff);
        }
    }

    public Staff getStaffById(int staffId) {
        return staffDAO.findById(staffId);
    }

    public List<Staff> getAllStaff() {
        return staffDAO.findAll();
    }

    public List<Staff> searchStaff(String searchTerm) {
        return staffDAO.searchStaff(searchTerm);
    }
}