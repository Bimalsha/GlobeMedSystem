package com.globemed.dao;

import com.globemed.model.Role;

public class RoleDAO extends GenericDAO<Role, Integer> {
    public RoleDAO() {
        super(Role.class);
    }
}