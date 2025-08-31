package com.globemed.security.composite;

/**
 * The Component interface for the Composite pattern.
 * Declares the interface for both simple permissions (Leafs) and
 * groups of permissions (Composites).
 */
public abstract class PermissionComponent {

    public void add(PermissionComponent permissionComponent) {
        throw new UnsupportedOperationException();
    }

    public void remove(PermissionComponent permissionComponent) {
        throw new UnsupportedOperationException();
    }

    public PermissionComponent getChild(int i) {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    // The key method: checks if this component (or any of its children)
    // contains a permission with the given name.
    public abstract boolean hasPermission(String permissionName);
}