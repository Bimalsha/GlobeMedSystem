package com.globemed.security.composite;


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