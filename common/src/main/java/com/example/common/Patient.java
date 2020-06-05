package com.example.common;

import androidx.annotation.NonNull;

import java.util.Date;

public class Patient {

    private int id;
    private String lastname;
    private String firstname;
    private String dateTest;

    // presuming measures are int and indexes real
    private int measure_1;
    private int measure_2;
    private int measure_3;
    private Double index_1;
    private Double index_2;

    // colors meaning :
    // - grey : no mesures made yet (by default)
    // - green : good health
    // - red : bad health
    private String hint_index_1;
    private String hint_index_2;

    public Patient() {
    }

    public Patient(int id, String lastname, String firstname, int measure_1, int measure_2, int measure_3, String dateTest) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.measure_1 = measure_1;
        this.measure_2 = measure_2;
        this.measure_3 = measure_3;
        this.dateTest = dateTest;
    }

    //getters
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

    public Double getIndex_1() {
        return index_1;
    }

    public Double getIndex_2() {
        return index_2;
    }

    public String getHint_index_1() {
        return hint_index_1;
    }

    public String getHint_index_2() {
        return hint_index_2;
    }

    public String  getDateTest() {
        return dateTest;
    }

    //setters
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

    public void setIndex_1(Double index_1) {
        this.index_1 = index_1;
    }

    public void setIndex_2(Double index_2) {
        this.index_2 = index_2;
    }

    public void setHint_index_1(String hint_index_1) {
        this.hint_index_1 = hint_index_1;
    }

    public void setHint_index_2(String hint_index_2) {
        this.hint_index_2 = hint_index_2;
    }

    public void setDateTest(String  dateTest) {
        this.dateTest = dateTest;
    }

    @NonNull
    @Override
    public String toString() {
       /* return "id: " + id + " nom: " + lastname + " prénom: " + firstname + " mesure 1: " + measure_1
                + " mesure 2: " + measure_2 + " mesure 3: " + measure_3 + " index 1: " + index_1
                + " index 2: " + index_2 + " couleur 1: " + hint_index_1 + " couleur 2: " + hint_index_2;*/
       return firstname + " " + lastname;
    }
}
