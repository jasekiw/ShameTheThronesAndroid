package com.jasekiw.shamethethrones.providers.restroom;


public enum RestroomGender {
    MALE(0),
    FEMALE(1),
    BOTH(2);

    private final int value;

    private RestroomGender(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
