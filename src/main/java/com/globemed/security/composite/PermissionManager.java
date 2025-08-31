package com.globemed.security;

import com.globemed.security.composite.Permission;
import com.globemed.security.composite.PermissionGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the creation and retrieval of permission structures for roles.
 * This class defines the application's Access Control List (ACL).
 */
public class PermissionManager {

    private static final Map<String, PermissionGroup> roles = new HashMap<>();

    static {
        // --- 1. Define all granular permissions for UI panels ---
        Permission accessPatientPanel = new Permission("ACCESS_PATIENT_PANEL");
        Permission accessStaffPanel = new Permission("ACCESS_STAFF_PANEL");
        Permission accessAppointmentPanel = new Permission("ACCESS_APPOINTMENT_PANEL");
        Permission accessTestDataPanel = new Permission("ACCESS_TEST_DATA_PANEL");
        Permission accessBillingPanel = new Permission("ACCESS_BILLING_PANEL");
        Permission accessReportsPanel = new Permission("ACCESS_REPORTS_PANEL");

        // --- 2. Create Roles and assign permissions ---

        // Role: Admin (has access to EVERYTHING)
        PermissionGroup adminRole = new PermissionGroup("Admin");
        adminRole.add(accessPatientPanel);
        adminRole.add(accessStaffPanel);
        adminRole.add(accessAppointmentPanel);
        adminRole.add(accessTestDataPanel);
        adminRole.add(accessBillingPanel);
        adminRole.add(accessReportsPanel);
        roles.put("Admin", adminRole);

        // Role: Doctor
        PermissionGroup doctorRole = new PermissionGroup("Doctor");
        doctorRole.add(accessAppointmentPanel);
        doctorRole.add(accessTestDataPanel);
        doctorRole.add(accessReportsPanel);
        roles.put("Doctor", doctorRole);

        // Role: Nurse
        PermissionGroup nurseRole = new PermissionGroup("Nurse");
        nurseRole.add(accessPatientPanel);
        nurseRole.add(accessAppointmentPanel);
        nurseRole.add(accessReportsPanel);
        roles.put("Nurse", nurseRole);

        // Role: Pharmacist
        PermissionGroup pharmacistRole = new PermissionGroup("Pharmacist");
        pharmacistRole.add(accessTestDataPanel);
        roles.put("Pharmacist", pharmacistRole);

        // Role: Receptionist
        PermissionGroup receptionistRole = new PermissionGroup("Receptionist");
        receptionistRole.add(accessPatientPanel);
        receptionistRole.add(accessAppointmentPanel);
        receptionistRole.add(accessBillingPanel);
        roles.put("Receptionist", receptionistRole);
    }

    /**
     * Retrieves the complete permission structure for a given role name.
     * @param roleName The name of the role (e.g., "Admin", "Doctor").
     * @return The PermissionGroup for that role, or null if not found.
     */
    public static PermissionGroup getPermissionsForRole(String roleName) {
        return roles.get(roleName);
    }
}