package com.mercury.pas.model.enums;

public enum PolicyTerm {
    SIX_MONTHS("6MON"),
    TWELVE_MONTHS("12MON");

    private final String value;

    PolicyTerm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

