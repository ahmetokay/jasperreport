package com.okay.model;

public enum EnumUserType {

    ADMIN(1, "Admin"), USER(2, "User");

    private int key;

    private String value;

    EnumUserType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}