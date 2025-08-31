package com.globemed.security.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * The Composite class represents a group of permissions.
 * It can contain both simple Permissions (Leafs) and other PermissionGroups (Composites).
 */
public class PermissionGroup extends PermissionComponent {

    private final List<PermissionComponent> permissions = new ArrayList<>();
    private final String groupName;

    public PermissionGroup(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public void add(PermissionComponent permissionComponent) {
        permissions.add(permissionComponent);
    }

    @Override
    public void remove(PermissionComponent permissionComponent) {
        permissions.remove(permissionComponent);
    }

    @Override
    public PermissionComponent getChild(int i) {
        return permissions.get(i);
    }

    @Override
    public String getName() {
        return groupName;
    }

    @Override
    public boolean hasPermission(String permissionName) {
        // A group has a permission if any of its children have it.
        // It iterates through its children, calling their hasPermission() method.
        for (PermissionComponent component : permissions) {
            if (component.hasPermission(permissionName)) {
                return true;
            }
        }
        return false;
    }
}