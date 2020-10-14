package com.MaJnr.common;

import androidx.annotation.NonNull;

public class Patient {

    private int id;
    private String lastname;
    private String firstname;
    private String dateTest;

    // measures in integers
    private int measure_1;
    private int measure_2;
    private int measure_3;

    public Patient() {
    }

    // getters
    public int getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public int getMeasure_1() {
        return measure_1;
    }

    public int getMeasure_2() {
        return measure_2;
    }

    public int getMeasure_3() {
        return measure_3;
    }

    public String  getDateTest() {
        return dateTest;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setMeasure_1(int measure_1) {
        this.measure_1 = measure_1;
    }

    public void setMeasure_2(int measure_2) {
        this.measure_2 = measure_2;
    }

    public void setMeasure_3(int measure_3) {
        this.measure_3 = measure_3;
    }

    public void setDateTest(String  dateTest) {
        this.dateTest = dateTest;
    }

    @NonNull
    @Override
    public String toString() {
       return firstname + " " + lastname;
    }
}
