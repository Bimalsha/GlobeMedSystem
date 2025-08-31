package com.globemed.security.composite;

/**
 * The Leaf class represents a single, indivisible permission.
 * It has no children.
 */
public class Permission extends PermissionComponent {

    private final String name;

    public Permission(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasPermission(String permissionName) {
        // A leaf has the permission if its name matches the one being checked.
        return this.name.equalsIgnoreCase(permissionName);
    }
}