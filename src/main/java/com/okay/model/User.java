package com.okay.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String name;

    private String surname;

    private String email;

    private EnumUserType type;

    private List<Address> addressList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EnumUserType getType() {
        return type;
    }

    public void setType(EnumUserType type) {
        this.type = type;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}