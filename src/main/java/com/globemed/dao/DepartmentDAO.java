package com.globemed.dao;

import com.globemed.model.Department;

public class DepartmentDAO extends GenericDAO<Department, Integer> {
    public DepartmentDAO() {
        super(Department.class);
    }
}