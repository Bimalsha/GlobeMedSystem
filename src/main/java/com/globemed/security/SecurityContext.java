package com.globemed.security;

import com.globemed.model.Staff;
import com.globemed.security.composite.PermissionGroup;

public class SecurityContext {
    private static Staff currentUser;
    private static PermissionGroup currentUserPermissions;

    public static void login(Staff user) {
        System.out.println(">>> User '" + user.getFullname() + "' logged in with Role '" + user.getRole().getName() + "'.");
        currentUser = user;
        currentUserPermissions = com.globemed.security.PermissionManager.getPermissionsForRole(user.getRole().getName());
    }

    public static void logout() {
        if (currentUser != null) {
            System.out.println(">>> User '" + currentUser.getFullname() + "' logged out.");
        }
        currentUser = null;
        currentUserPermissions = null;
    }

    public static Staff getCurrentUser() {
        return currentUser;
    }

    public static boolean hasPermission(String permissionName) {
        if (currentUserPermissions == null) {
            return false;
        }
        return currentUserPermissions.hasPermission(permissionName);
    }
}