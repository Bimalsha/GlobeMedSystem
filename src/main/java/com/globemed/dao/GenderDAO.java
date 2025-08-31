package com.globemed.dao;

import com.globemed.model.Gender;

public class GenderDAO extends GenericDAO<Gender, Integer> {
    public GenderDAO() {
        super(Gender.class);
    }

    public Gender getGenderByName(String name) {
        return findOneByProperty("name", name);
    }
}